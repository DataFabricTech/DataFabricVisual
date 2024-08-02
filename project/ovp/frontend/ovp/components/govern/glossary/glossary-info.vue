<template>
  <div class="work-page" v-if="store.currentComponent === 'glossary'">
    <div class="l-top-bar">
      <div class="editable-group" v-if="!store.editGlossaryMode.name">
        <span class="editable-group-title">{{ store.glossary.name }}</span>
        <button
          class="button button-neutral-ghost button-sm"
          type="button"
          @click="changeEditGlossaryMode('name')"
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
          class="text-input w-4/5"
          v-model="editData.name"
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
      <!-- TODO alert 개발 후 얼럿 적용 -->
      <button
        class="button button-error-lighter"
        @click="deleteGlossary(glossary.id)"
      >
        삭제
      </button>
    </div>
    <div class="work-contents">
      <!-- 결과 없을 시 no-result 표시  -->
      <div class="no-result" v-if="Object.keys(glossary).length === 0">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">등록된 정보가 없습니다.</p>
        </div>
      </div>
      <!-- // 결과 없을 시 no-result 표시  -->

      <div
        class="editable-group"
        v-if="Object.keys(glossary).length > 0 && !store.editGlossaryMode.des"
      >
        <span class="editable-group-desc">{{ glossary.description }}</span>
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
      <!-- // 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="editable-group" v-if="!store.editGlossaryMode.tag">
        <div class="tag tag-primary tag-sm" v-for="tag in glossary.tags">
          <span class="tag-text">{{ tag.label }}</span>
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
      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <!-- TODO: 개발 - 태그 수정 -->
      <div class="editable-group" v-if="store.editGlossaryMode.tag">
        <menu-search-tag
          :data="menuSearchTagsData"
          :selected-items="glossary.tags"
          label-key="label"
          value-key="data"
          :is-multi="true"
          title="값을 선택하세요"
          @multiple-change="changeTag"
          @cancel="changeEditGlossaryMode('tag')"
          @close="changeEditGlossaryMode('tag')"
        ></menu-search-tag>
      </div>
      <!-- // 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->

      <div>
        <div class="tab tab-line">
          <ul class="tab-list">
            <li :class="getTabItemClassName('term')" @click="changeTab('term')">
              <button class="tab-button">
                <p class="tab-button-text">용어</p>
              </button>
            </li>
            <li
              :class="getTabItemClassName('activity')"
              @click="changeTab('activity')"
            >
              <button class="tab-button">
                <p class="tab-button-text">활동사항</p>
                <span
                  class="tab-button-count"
                  v-if="store.activities.length > 0"
                  >{{ store.activities.length }}</span
                >
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
import type { JsonPatchOperation } from "~/type/common";
import { reactive, watch, onMounted } from "vue";
import type { MenuSearchItemImpl } from "@extends/menu-seach/MenuSearchComposition";

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
  getAllTags,
} = useGlossaryStore();
const store = useGlossaryStore();

const editData = reactive({
  name: "",
  description: "",
});

watch(
  () => store.glossary,
  (newGlossary) => {
    editData.name = newGlossary.name;
    editData.description = newGlossary.description;
  },
  { deep: true },
);

onMounted(() => {
  syncEditDataWithGlossary();
  getAllTags();
});

function syncEditDataWithGlossary() {
  editData.name = store.glossary.name;
  editData.description = store.glossary.description;
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

async function changeTag(items: MenuSearchItemImpl[]) {
  // const selectedItems = items.map((v) => v.data);
  // const value = tags.filter((v) => selectedItems.includes(v.name));
  // const diff = glossary.tags.filter((v) => !selectedItems.includes(v.name));
  // console.log(diff);
  // if (items.length < glossary.tags.length) {
  // }
  //
  // // {
  // //   op: 'replace',
  // //       path: '/displayName',
  // //     value: editData.displayName,
  // // }
  // //await editGlossary(glossary.id);
  // console.log(value);
  console.log(items);
}
</script>
