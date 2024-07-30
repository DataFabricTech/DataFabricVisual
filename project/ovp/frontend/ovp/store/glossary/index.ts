import { defineStore } from "pinia";
import { reactive, ref } from "vue";
import type { Activity, Glossary, Term } from "~/type/glossary";
import type { JsonPatchOperation, Tag } from "~/type/common";

export const useGlossaryStore = defineStore("glossary", () => {
  const { $api } = useNuxtApp();

  const glossaries = reactive<Glossary[]>([]);
  const glossary = reactive<Glossary>(<Glossary>{});

  const terms = reactive<Term[]>([]);

  const activities = reactive<Activity[]>([]);

  const tags = reactive<Tag[]>([]);
  const menuSearchTagsData = reactive<Tag[]>([]);
  const tab = ref("term");
  const currentComponent = ref("glossary");

  const editNameMode = ref(false);
  const editDesMode = ref(false);
  const editTagMode = ref(false);

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
   * 용어 리스트
   * @param term
   */
  async function getGlossaryTerms(term: string): Promise<void> {
    const res = await $api(`/api/glossary/terms?term=${term}`);
    terms.splice(0, terms.length, ...res.data);
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
  function changeEditNameMode(): void {
    editNameMode.value = !editNameMode.value;
  }
  function changeEditDesMode(): void {
    editDesMode.value = !editDesMode.value;
  }
  function changeEditTagMode(): void {
    editTagMode.value = !editTagMode.value;
  }
  function disableEditModes(): void {
    editNameMode.value = false;
    editDesMode.value = false;
    editTagMode.value = false;
  }

  /**
   * 용어 선택
   * @param param
   */
  async function changeCurrentGlossary(param: Glossary): Promise<void> {
    Object.assign(glossary, param);
    changeTab("term");
    await getGlossaryTerms(param.name);
    await getGlossaryActivities(`<%23E::glossary::${param.name}>`);
    disableEditModes();
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
    activities,
    tab,
    tags,
    menuSearchTagsData,
    currentComponent,
    editNameMode,
    editDesMode,
    editTagMode,
    getGlossaries,
    editGlossary,
    deleteGlossary,
    getGlossaryTerms,
    getGlossaryActivities,
    changeTab,
    openEditTermComponent,
    changeEditNameMode,
    changeEditDesMode,
    changeEditTagMode,
    changeCurrentGlossary,
    getAllTags,
  };
});
