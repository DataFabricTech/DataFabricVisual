<template>
  <div class="work-page" v-if="store.currentComponent === 'glossary'">
    <div class="l-top-bar">
      <div class="editable-group" v-if="!store.editGlossaryMode.name">
        <span class="editable-group-title">{{ store.glossary.name }}</span>
        <button
          class="button button-neutral-ghost button-sm"
          type="button"
          @click="changeEditGlossaryMode('name')"
          v-if="!isGlossaryNull()"
        >
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="editable-group" v-if="store.editGlossaryMode.name">
        <label class="hidden-text" for="title-modify"
          >카테고리 이름 수정
        </label>
        <input
          id="title-modify"
          class="text-input"
          v-model="editData.name"
          maxlength="20"
        />
        <div class="h-group gap-1">
          <button
            class="button button-neutral-stroke"
            type="button"
            @click="cancel('name')"
          >
            취소
          </button>
          <button
            class="button button-primary-lighter"
            type="button"
            @click="
              updateGlossary(glossary.id, {
                op: 'replace',
                path: '/name',
                value: editData.name,
              })
            "
          >
            완료
          </button>
        </div>
      </div>
      <!-- // 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <button
        class="button button-error-lighter"
        v-if="!isGlossaryNull()"
        @click="removeGlossary"
      >
        삭제
      </button>
    </div>
    <div
      :id="store.tab === 'term' ? 'termList' : 'activitiesList'"
      class="work-contents gap-5"
    >
      <!-- 결과 없을 시 no-result 표시  -->
      <div class="no-result" v-if="Object.keys(glossary).length === 0">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">데이터가 없습니다.</p>
        </div>
      </div>
      <!-- // 결과 없을 시 no-result 표시  -->
      <div class="v-group gap-2" v-if="!isGlossaryNull()">
        <div class="font-semibold text-neutral-700">설명</div>
        <div
          class="editable-group"
          v-if="Object.keys(glossary).length > 0 && !store.editGlossaryMode.des"
        >
          <span class="editable-group-desc">{{
            glossary && glossary.description ? glossary.description : "-"
          }}</span>
          <button
            class="button button-neutral-ghost button-sm"
            type="button"
            @click="changeEditGlossaryMode('des')"
          >
            <span class="hidden-text">수정</span>
            <svg-icon class="button-icon" name="pen"></svg-icon>
          </button>
        </div>

        <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
        <div class="editable-group" v-if="store.editGlossaryMode.des">
          <label class="hidden-text" for="description-modify"
            >카테고리 설명 수정
          </label>
          <textarea
            id="description-modify"
            class="textarea"
            width="300px"
            v-model="editData.description"
          ></textarea>
          <div class="h-group gap-1">
            <button
              class="button button-neutral-stroke"
              type="button"
              @click="cancel('des')"
            >
              취소
            </button>
            <button
              class="button button-primary-lighter"
              type="button"
              @click="
                updateGlossary(glossary.id, {
                  op: 'replace',
                  path: '/description',
                  value: editData.description,
                })
              "
            >
              완료
            </button>
          </div>
        </div>
      </div>
      <!-- // 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="h-group gap-2" v-if="!isGlossaryNull()">
        <div class="font-semibold text-neutral-700 w-16">태그</div>
        <div class="editable-group" v-if="!store.editGlossaryMode.tag">
          <div
            class="tag tag-primary tag-sm"
            v-if="glossary && glossary.tags && glossary.tags.length > 0"
            v-for="tag in glossary.tags"
          >
            <span class="tag-text">{{ tag.displayName }}</span>
          </div>
          <div class="text-neutral-700" v-else>
            <span>-</span>
          </div>
          <button
            class="button button-neutral-ghost button-sm"
            type="button"
            @click="changeEditGlossaryMode('tag')"
          >
            <span class="hidden-text">수정</span>
            <svg-icon class="button-icon" name="pen"></svg-icon>
          </button>
        </div>
        <div
          class="editable-group editable-group-unusual"
          v-if="store.editGlossaryMode.tag"
        >
          <menu-search-tag
            :data="menuSearchTagsData"
            :selected-items="glossary.tags"
            selected-label-key="displayName"
            label-key="tagFQN"
            value-key="tagFQN"
            :is-multi="true"
            title="값을 선택하세요"
            @multiple-change="changeTag"
            @cancel="changeEditGlossaryMode('tag')"
            @close="changeEditGlossaryMode('tag')"
          ></menu-search-tag>
        </div>
      </div>

      <div v-if="!isGlossaryNull()">
        <div class="tab tab-line">
          <ul class="tab-list">
            <li
              :class="getTabItemClassName('term')"
              @click="clickedTab('term')"
            >
              <button class="tab-button">
                <p class="tab-button-text">용어</p>
              </button>
            </li>
            <li
              :class="getTabItemClassName('activity')"
              @click="clickedTab('activity')"
            >
              <button class="tab-button">
                <p class="tab-button-text">활동사항</p>
                <span class="tab-button-count">{{
                  store.activitiesCount
                }}</span>
              </button>
            </li>
          </ul>
        </div>
        <glossary-table v-if="store.tab === 'term'"></glossary-table>
        <glossary-activities
          v-if="store.tab === 'activity'"
        ></glossary-activities>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useGlossaryStore } from "~/store/glossary";
import menuSearchTag from "@extends/menu-seach/tag/menu-search-tag.vue";
import type { JsonPatchOperation, Tag } from "~/type/common";
import { reactive, watch, onMounted, type Ref } from "vue";
import type { MenuSearchItemImpl } from "@extends/menu-seach/MenuSearchComposition";
import { useNuxtApp } from "nuxt/app";

const { $alert, $confirm } = useNuxtApp();
const {
  glossary,
  tags,
  editGlossaryMode,
  menuSearchTagsData,
  getGlossaries,
  editGlossary,
  deleteGlossary,
  changeTab,
  changeEditGlossaryMode,
  createTagOperation,
  getTerms,
  resetTerms,
  getGlossaryActivities,
  resetGlossaryActivities,
} = useGlossaryStore();
const store = useGlossaryStore();

const editData = reactive({
  name: "",
  description: "",
});

watch(
  () => store.glossary,
  async (newGlossary) => {
    editData.name = newGlossary.name;
    editData.description = newGlossary.description;
    await resetTerms();
    await getTerms();
    await resetGlossaryActivities();
    await getGlossaryActivities();
  },
  { deep: true },
);

onMounted(() => {
  syncEditDataWithGlossary();
});

const isGlossaryNull = (): boolean => {
  return Object.keys(store.glossary).length === 0;
};

function syncEditDataWithGlossary(): void {
  editData.name = store.glossary.name;
  editData.description = store.glossary.description;
}

async function removeGlossary() {
  if (await $confirm("데이터모델을 삭제 하시겠습니까?")) {
    await deleteGlossary(glossary.id);
    await getGlossaries()
      .then(() => {
        $alert("삭제되었습니다.", "info");
      })
      .catch((error) => {
        console.error("삭제 중 오류 발생: ", error);
      });
  }
}

async function updateGlossary(
  id: string,
  op: JsonPatchOperation,
): Promise<void> {
  await editGlossary(id, [op]);
  await getGlossaries();
}

function cancel(property: keyof typeof editGlossaryMode): void {
  changeEditGlossaryMode(property);
  syncEditDataWithGlossary();
}

const getTabItemClassName = (param: string): string => {
  return store.tab === param ? "tab-item is-tab-item-selected" : "tab-item";
};

const selectedItems: Ref<string[]> = ref([]);

async function changeTag(items: MenuSearchItemImpl[]): Promise<void> {
  selectedItems.value = items.map((item: MenuSearchItemImpl) => item.tagFQN);
  const matchTags: Tag[] = tags.filter((tag) =>
    selectedItems.value.includes(tag.tagFQN),
  );
  const operations: JsonPatchOperation[] = createTagOperation(
    selectedItems.value,
    matchTags,
    glossary,
  );
  await editGlossary(glossary.id, operations);
  await getGlossaries();
}

const clickedTab = (tabKey: string) => {
  changeTab(tabKey);
  if (tabKey === "term") {
    resetTerms();
    getTerms();
  } else {
    resetGlossaryActivities();
    getGlossaryActivities();
  }
};
</script>
