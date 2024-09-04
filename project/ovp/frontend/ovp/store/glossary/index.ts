import { defineStore } from "pinia";
import { reactive, ref } from "vue";
import type { Activity, Glossary, Term } from "~/type/glossary";
import type { JsonPatchOperation, PreviewData, Tag } from "~/type/common";
import type { DataModel } from "~/components/common/resource-box/resource-box-common-props";
import $constants from "~/utils/constant";
import type { Service } from "~/type/service";

export const useGlossaryStore = defineStore("glossary", () => {
  const { $api } = useNuxtApp();

  const glossaries = reactive<Glossary[]>([]);
  const glossary = reactive<Glossary>(<Glossary>{});

  const terms = reactive<Term[]>([]);
  const term = reactive<Term>(<Term>{});

  const activities = reactive<Activity[]>([]);

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
  async function createGlossary(body: object) {
    await $api(`/api/glossary`, {
      method: "POST",
      body: body,
    });
  }

  async function getGlossaries(): Promise<void> {
    const res = await $api(`/api/glossary/list`);
    const glossariesData: Glossary[] = res.data;
    glossaries.splice(0, glossaries.length, ...glossariesData);

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
    await getGlossaries();
  }

  /**
   * 용어 Crud
   */
  async function createTerm(body: object): Promise<void> {
    await $api(`/api/glossary/terms`, {
      method: "POST",
      body: body,
    });
  }

  async function getTerms(term: string): Promise<void> {
    menuSearchRelatedTermsData.length = 0;
    const res = await $api(`/api/glossary/terms?term=${term}`);
    terms.splice(0, terms.length, ...res.data);
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

  async function updateTerm(id: string, body: object[]) {
    await $api(`/api/glossary/terms/${id}/assets/remove`, {
      method: "PUT",
      body: body,
    });
  }

  async function deleteTerm(id: string) {
    await $api(`/api/glossary/terms/${id}`, {
      method: "DELETE",
    });
  }

  function changeCurrentTerm(param: Term) {
    Object.assign(term, param);
  }

  /**
   * 용어 활동 사항
   * @param entityLink
   */
  async function getGlossaryActivities(entityLink: string): Promise<void> {
    const res = await $api(`/api/glossary/activities?entityLink=${entityLink}`);
    if (res.data !== null) {
      activities.splice(0, activities.length, ...res.data);
    }
  }

  /**
   * 데이터 모델 리스트
   * @param name
   * @param search
   */
  async function getDataModels(name: string, search?: string): Promise<void> {
    // TODO : [개발] queryParam (q) 에 검색어 제외 AND ( ) 이런 내용이 들어가면 안됩니다. backend 에서 처리할수 있게 수정해주세요.
    // myPageStore.ts getSearchListQuery() function 보고 참조해서 수정해주세요.
    // eslint-disable-next-line id-length
    const q: string = search
      ? `*${search}* AND (tags.tagFQN:"${name}")`
      : `** AND (tags.tagFQN:"${name}")`;
    const res = await $api(`/api/glossary/data-models?q=${q}`);
    if (res.data !== null) {
      dataModels.splice(0, dataModels.length, ...res.data);
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
    await getTerms(param.name);
    await getGlossaryActivities(`<%23E::glossary::${param.name}>`);
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
    tags.forEach((tag) => {
      menuSearchTagsData.push({ label: tag.name, tagFQN: tag.tagFQN });
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
    glossary,

    terms,
    term,

    activities,

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
    editTerm,
    deleteTerm,
    updateTerm,

    getGlossaryActivities,

    getDataModels,
    getDataModel,

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
