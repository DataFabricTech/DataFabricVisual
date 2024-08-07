import { defineStore } from "pinia";
import { reactive, ref } from "vue";
import type { Activity, Glossary, Term } from "~/type/glossary";
import type { JsonPatchOperation, PreviewData, Tag } from "~/type/common";
import type { DataModel } from "~/components/common/resource-box/resource-box-common-props";
import $constants from "~/utils/constant";

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
  const menuSearchTagsData = reactive<Tag[]>([]);
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
  async function getTerms(term: string): Promise<void> {
    const res = await $api(`/api/glossary/terms?term=${term}`);
    terms.splice(0, terms.length, ...res.data);
  }

  async function editTerm(
    id: string,
    body: [JsonPatchOperation],
  ): Promise<void> {
    const res = await $api(`/api/glossary/terms/${id}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json-patch+json",
      },
      body: body,
    });
    changeCurrentTerm(res.data);
    disableEditTermModes();
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
   */
  const createTagOperation = (
    selectedItems: string[],
    matchTags: Tag[],
  ): JsonPatchOperation[] => {
    const newTag = selectedItems;
    const oldTag = glossary.tags.map((tag) => tag.tagFQN);
    const maxLength = Math.max(newTag.length, oldTag.length);

    const operations: JsonPatchOperation[] = [];

    // eslint-disable-next-line id-length
    for (let i: number = 0; i < maxLength; i++) {
      const newVal = newTag[i] ?? null;
      const oldVal = oldTag[i] ?? null;

      if (newVal === null && oldVal !== null) {
        operations.push({
          op: "remove",
          path: `/tags/${i}`,
        });
      } else if (newVal !== null && oldVal === null) {
        const newTag = matchTags.find((tag) => tag.tagFQN === newVal);
        if (newTag) {
          operations.push({
            op: "add",
            path: `/tags/${i}`,
            value: matchTags[i],
          });
        }
      } else if (newVal !== oldVal) {
        const tag: Tag | undefined = matchTags.find(
          (tag: Tag) => tag.tagFQN === newVal,
        );
        if (tag) {
          $constants.PATCH_OPERATION.PATH_LIST.forEach((path: string) => {
            operations.push({
              op: "replace",
              path: `/tags/${i}/${path}`,
              value: tag[path],
            });
          });
        }
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
    currentComponent,

    editGlossaryMode,
    editTermMode,

    getGlossaries,
    editGlossary,
    deleteGlossary,

    getTerms,
    editTerm,
    deleteTerm,
    updateTerm,

    getGlossaryActivities,

    getDataModels,
    getDataModel,

    changeTab,
    openEditTermComponent,

    changeEditGlossaryMode,
    changeEditTermMode,

    changeCurrentTerm,
    changeCurrentGlossary,

    getAllTags,
    createTagOperation,
  };
});
