import { defineStore } from "pinia";
import { reactive, ref } from "vue";
import type { Activity, Glossary, Term } from "~/type/glossary";
import type { JsonPatchOperation, PreviewData, Tag } from "~/type/common";
import type { DataModel } from "~/components/common/resource-box/resource-box-common-props";
import $constants from "~/utils/constant";
import type { Service } from "~/type/service";
import { usePagingStore } from "~/store/common/paging";

export const useGlossaryStore = defineStore("glossary", () => {
  const pagingStore = usePagingStore();
  const { setFrom, updateIntersectionHandler } = pagingStore;
  const { from } = storeToRefs(pagingStore);

  const { $api } = useNuxtApp();

  const glossaries = reactive<Glossary[]>([]);
  const glossariesAfter = ref();

  const glossary = reactive<Glossary>(<Glossary>{});

  const terms = reactive<Term[]>([]);
  const termsAfter = ref(null);
  const termsBefore = ref(null);

  const term = reactive<Term>(<Term>{});

  const activities = reactive<Activity[]>([]);
  const activityAfter = ref(null);
  const activityBefore = ref(null);
  const activitiesCount = ref(0);

  const dataModels = reactive<DataModel[]>([]);
  const dataModel = reactive<PreviewData>(<PreviewData>{});

  const tags = reactive<Tag[]>([]);
  const menuSearchTagsData = reactive<object[]>([]);
  const menuSearchRelatedTermsData = reactive<object[]>([]);

  const tab = ref("term");
  const currentComponent = ref("glossary");

  const editGlossaryMode = reactive({
    name: false,
    des: false,
    tag: false,
  });
  const editTermMode = reactive({
    name: false,
    des: false,
    tag: false,
    synonyms: false,
    relatedTerms: false,
  });

  /**
   * Glossary CRUD
   */
  async function createGlossary(body: object): Promise<void> {
    const res = await $api(`/api/glossary`, {
      method: "POST",
      body: body,
    });
    if (res.data === null) {
      throw new Error(res.errorMessage);
    }
  }

  async function getGlossaries(): Promise<void> {
    const param =
      // eslint-disable-next-line eqeqeq
      glossariesAfter.value != null ? `?after=${glossariesAfter.value}` : "";
    const res = await $api(`/api/glossary/list${param}`, {
      showLoader: false,
    });
    const glossariesData: Glossary[] = res.data.data;
    glossaries.splice(0, glossaries.length, ...glossariesData);

    if (res.data.paging.after) {
      glossariesAfter.value = res.data.paging.after;
    }
    const glossaryData: Glossary =
      Object.keys(glossary).length === 0 ? glossariesData[0] : glossary;
    await changeCurrentGlossary(glossaryData);
  }

  async function editGlossary(
    id: string,
    body: JsonPatchOperation[],
  ): Promise<void> {
    const res = await $api(`/api/glossary/${id}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json-patch+json",
      },
      body: body,
    });
    Object.assign(glossary, res.data);
    disableEditModes();
  }

  async function deleteGlossary(id: string): Promise<void> {
    await $api(`/api/glossary/${id}`, {
      method: "DELETE",
    });
    Object.keys(glossary).forEach((key) => {
      delete glossary[key];
    });
  }

  /**
   * 용어 Crud
   */
  async function createTerm(body: object): Promise<void> {
    const res = await $api(`/api/glossary/terms`, {
      method: "POST",
      body: body,
    });
    if (res.data === null) {
      throw new Error(res.errorMessage);
    }
  }

  function resetTerms(): void {
    termsAfter.value = null;
    termsBefore.value = null;
    terms.splice(0);
  }

  async function getTerms(): Promise<void> {
    // 마지막 데이터까지 조회 완료한 경우, 추가적인 호출 중단
    if (termsAfter.value === null && termsBefore.value !== null) {
      return;
    }

    menuSearchRelatedTermsData.length = 0;
    const param: string =
      // eslint-disable-next-line eqeqeq
      termsAfter.value != null
        ? `term=${glossary.name}&after=${termsAfter.value}`
        : `term=${glossary.name}`;
    const res = await $api(`/api/glossary/terms?${param}`, {
      showLoader: false,
    });

    if (res.data.terms !== null) {
      terms.splice(0, terms.length, ...res.data.terms);
    }

    if (res.data.paging) {
      termsAfter.value = res.data.paging.after;
      termsBefore.value = res.data.paging.before;
    }
    terms.forEach((term) => {
      menuSearchRelatedTermsData.push({ label: term.name, id: term.id });
    });
  }

  async function editTerm(
    id: string,
    body: JsonPatchOperation[],
  ): Promise<void> {
    await $api(`/api/glossary/terms/${id}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json-patch+json",
      },
      body: body,
    });
  }

  async function updateTerm(id: string, body: object[]): Promise<void> {
    await $api(`/api/glossary/terms/${id}/assets/remove`, {
      method: "PUT",
      body: body,
    });
  }

  async function deleteTerm(id: string): Promise<void> {
    const res = await $api(`/api/glossary/terms/${id}`, {
      method: "DELETE",
    });
    if (res.data === null) {
      throw new Error(res.errorMessage);
    }
  }

  function changeCurrentTerm(param: Term): void {
    Object.assign(term, param);
  }

  /**
   * 데이터 모델 초기화
   */
  function resetGlossaryActivities(): void {
    activityAfter.value = null;
    activityBefore.value = null;
    activities.splice(0);
  }

  /**
   * 용어 활동 사항
   */
  async function getGlossaryActivities(): Promise<void> {
    // 마지막 데이터까지 조회 완료한 경우, 추가적인 호출 중단
    if (activityAfter.value === null && activityBefore.value !== null) {
      return;
    }

    const param: string =
      // eslint-disable-next-line eqeqeq
      activityAfter.value != null
        ? `entityLink=<%23E::glossary::${encodeURIComponent(glossary.name)}>&after=${activityAfter.value}`
        : `entityLink=<%23E::glossary::${encodeURIComponent(glossary.name)}>`;
    const res = await $api(`/api/glossary/activities?${param}`, {
      showLoader: false,
    });

    if (res.data !== null) {
      activities.push(...res.data.activities);
    }
    if (res.data.paging) {
      activityAfter.value = res.data.paging.after;
      activityBefore.value = res.data.paging.before;
      activitiesCount.value = res.data.paging.total;
    }
  }

  /**
   * 용어 활동 사항 개수
   * @param entityLink
   */
  async function getGlossaryActivitiesCount(entityLink: string): Promise<void> {
    const res = await $api(
      `/api/glossary/activities/count?entityLink=${entityLink}`,
    );
    if (res.data !== null) {
      activitiesCount.value = res.data;
    }
  }

  /**
   * 데이터 모델 초기화
   */
  function resetDataModels(): void {
    setFrom(0);
    updateIntersectionHandler(0);
    dataModels.splice(0);
  }

  /**
   * 데이터 모델 리스트
   * @param name
   * @param search
   */
  async function getDataModels(name: string, search?: string): Promise<void> {
    const res = await $api(
      `/api/glossary/data-models?search=${search}&name=${name}&from=${from.value}`,
    );
    if (res.data !== null) {
      dataModels.push(...res.data);
    }
  }

  /**
   * 데이터 모델 상세
   * @param fqn
   */
  async function getDataModel(fqn: string): Promise<void> {
    const res = await $api(`/api/glossary/data-model?fqn=${fqn}`);
    if (res.data !== null) {
      Object.assign(dataModel, res.data);
    }
  }

  function changeTab(param: string): void {
    tab.value = param;
  }
  function openEditTermComponent(component: string): void {
    currentComponent.value = component;
  }

  /**
   * 수정 Input show-hide 처리
   */
  function changeEditGlossaryMode(
    property: keyof typeof editGlossaryMode,
  ): void {
    editGlossaryMode[property] = !editGlossaryMode[property];
  }
  function disableEditModes(): void {
    editGlossaryMode.name = false;
    editGlossaryMode.des = false;
    editGlossaryMode.tag = false;
  }
  function changeEditTermMode(property: keyof typeof editTermMode): void {
    editTermMode[property] = !editTermMode[property];
  }
  function disableEditTermModes(): void {
    editTermMode.name = false;
    editTermMode.des = false;
    editTermMode.tag = false;
    editTermMode.synonyms = false;
    editTermMode.relatedTerms = false;
  }

  /**
   * 용어 선택
   * @param param
   */
  async function changeCurrentGlossary(param: Glossary): Promise<void> {
    Object.assign(glossary, param);
    changeTab("term");
    disableEditModes();
    disableEditTermModes();
    openEditTermComponent("glossary");
  }

  /**
   * 태그 호출
   */
  async function getAllTags(): Promise<void> {
    const res = await $api(`/api/glossary/all-tags`);
    tags.splice(0, tags.length, ...res.data);

    // 배열 초기화
    menuSearchTagsData.length = 0;
    tags.forEach((tag) => {
      menuSearchTagsData.push({ displayName: tag.name, tagFQN: tag.tagFQN });
    });
  }

  /**
   * Tag 수정 operation 생성
   * @param selectedItems
   * @param matchTags
   * @param entity
   */
  const createTagOperation = (
    selectedItems: string[],
    matchTags: Tag[],
    entity: Term | Glossary | Service,
  ): JsonPatchOperation[] => {
    const newData = selectedItems;
    const oldData = entity.tags.map((tag) => tag.tagFQN);
    const operations: JsonPatchOperation[] = [];
    const maxLength = Math.max(newData.length, oldData.length);

    const processTagChange = (index: number) => {
      const newValue = newData[index] ?? null;
      const oldValue = oldData[index] ?? null;

      if (newValue === null && oldValue !== null) {
        operations.push({ op: "remove", path: `/tags/${index}` });
      } else if (newValue !== null && oldValue === null) {
        const matchingTag = matchTags.find((tag) => tag.tagFQN === newValue);
        if (matchingTag) {
          operations.push({
            op: "add",
            path: `/tags/${index}`,
            value: matchingTag,
          });
        }
      } else if (newValue !== oldValue) {
        const matchingTag = matchTags.find((tag) => tag.tagFQN === newValue);
        if (matchingTag) {
          $constants.PATCH_OPERATION.PATH_LIST.TAGS.forEach((path) => {
            operations.push({
              op: "replace",
              path: `/tags/${index}/${path}`,
              value: matchingTag[path],
            });
          });
        }
      }
    };

    if (newData.length >= oldData.length) {
      for (let i = 0; i < maxLength; i++) {
        processTagChange(i);
      }
    } else {
      for (let i = maxLength - 1; i >= 0; i--) {
        processTagChange(i);
      }
    }

    return operations;
  };

  /**
   * 연관 용어 수정 operation 생성
   * @param selectedItems
   * @param matchTags
   */
  const createRelatedTermOperation = (
    selectedItems: string[],
    matchTags: object[],
  ): JsonPatchOperation[] => {
    const newData = selectedItems;
    const oldData = term.relatedTerms.map((term) => term.id);
    const operations: JsonPatchOperation[] = [];
    const maxLength = Math.max(newData.length, oldData.length);

    const processTagChange = (index: number) => {
      const newValue = newData[index] ?? null;
      const oldValue = oldData[index] ?? null;

      if (newValue === null && oldValue !== null) {
        operations.push({ op: "remove", path: `/relatedTerms/${index}` });
      } else if (newValue !== null && oldValue === null) {
        const matchingTag = matchTags.find((tag) => tag.id === newValue);
        if (matchingTag) {
          operations.push({
            op: "add",
            path: `/relatedTerms/${index}`,
            value: matchingTag,
          });
        }
      } else if (newValue !== oldValue) {
        const matchingTag = matchTags.find((tag) => tag.id === newValue);
        if (matchingTag) {
          $constants.PATCH_OPERATION.PATH_LIST.RELATED_TERMS.forEach((path) => {
            operations.push({
              op: "replace",
              path: `/relatedTerms/${index}/${path}`,
              value: matchingTag[path],
            });
          });
        }
      }
    };

    if (newData.length >= oldData.length) {
      for (let i = 0; i < maxLength; i++) {
        processTagChange(i);
      }
    } else {
      for (let i = maxLength - 1; i >= 0; i--) {
        processTagChange(i);
      }
    }

    return operations;
  };

  /**
   * 동의어 수정 operation 생성
   * @param editedItems
   * @param matchSynonyms
   */
  const createSynonymsOperation = (
    editedItems: string[],
    matchSynonyms: string[],
  ) => {
    const operations: JsonPatchOperation[] = [];
    const maxLength: number = Math.max(
      editedItems.length,
      matchSynonyms.length,
    );

    const processTagChange = (index: number) => {
      const newData = editedItems[index] ?? null;
      const oldData = matchSynonyms[index] ?? null;

      if (newData === null && oldData !== null) {
        operations.push({ op: "remove", path: `/synonyms/${index}` });
      } else if (newData !== null && oldData === null) {
        operations.push({
          op: "add",
          path: `/synonyms/${index}`,
          value: editedItems[index],
        });
      } else if (newData !== oldData) {
        operations.push({
          op: "replace",
          path: `/synonyms/${index}`,
          value: editedItems[index],
        });
      }
    };

    if (editedItems.length >= matchSynonyms.length) {
      for (let i = 0; i < maxLength; i++) {
        processTagChange(i);
      }
    } else {
      for (let i = maxLength - 1; i >= 0; i--) {
        processTagChange(i);
      }
    }

    return operations;
  };

  return {
    glossaries,
    glossariesAfter,
    glossary,

    terms,
    termsAfter,
    term,

    activities,
    activityAfter,
    activitiesCount,

    dataModels,
    dataModel,

    tab,
    tags,
    menuSearchTagsData,
    menuSearchRelatedTermsData,

    currentComponent,

    editGlossaryMode,
    editTermMode,

    createGlossary,
    getGlossaries,
    editGlossary,
    deleteGlossary,

    createTerm,
    getTerms,
    resetTerms,
    editTerm,
    deleteTerm,
    updateTerm,

    resetGlossaryActivities,
    getGlossaryActivities,
    getGlossaryActivitiesCount,

    getDataModels,
    getDataModel,
    resetDataModels,

    changeTab,
    openEditTermComponent,
    disableEditTermModes,

    changeEditGlossaryMode,
    changeEditTermMode,

    changeCurrentTerm,
    changeCurrentGlossary,

    getAllTags,
    createTagOperation,
    createRelatedTermOperation,
    createSynonymsOperation,
  };
});
