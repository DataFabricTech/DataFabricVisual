import { defineStore } from "pinia";
import { reactive, ref } from "vue";
import type { Activity, Glossary, Term } from "~/type/glossary";
import type { JsonPatchOperation, Tag } from "~/type/common";

export const useGlossaryStore = defineStore("glossary", () => {
  const { $api } = useNuxtApp();

  const glossaries = reactive<Glossary[]>([]);
  const glossary = reactive<Glossary>(<Glossary>{});

  const terms = reactive<Term[]>([]);
  const term = reactive<Term>(<Term>{});

  const activities = reactive<Activity[]>([]);

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
    body: [JsonPatchOperation],
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
    tags.splice(0, tags.length, ...res.data.allData);
    menuSearchTagsData.splice(
      0,
      menuSearchTagsData.length,
      ...res.data.menuSearchData,
    );
  }

  return {
    glossaries,
    glossary,

    terms,
    term,

    activities,

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

    getGlossaryActivities,

    changeTab,
    openEditTermComponent,

    changeEditGlossaryMode,
    changeEditTermMode,

    changeCurrentTerm,
    changeCurrentGlossary,

    getAllTags,
  };
});
