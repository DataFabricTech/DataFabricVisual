<template>
  <div class="work-page" v-if="store.currentComponent === 'term'">
    <div class="l-top-bar">
      <div class="editable-group" v-if="!store.editTermMode.name">
        <span class="editable-group-title">{{ store.term.displayName }}</span>
        <button
          class="button button-neutral-ghost button-sm"
          type="button"
          @click="changeEditTermMode('name')"
        >
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="editable-group" v-if="store.editTermMode.name">
        <label class="hidden-text" for="title-modify">용어 수정</label>
        <input
          id="title-modify"
          class="text-input w-4/5"
          v-model="editData.displayName"
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
              updateTerm(term.id, {
                op: 'replace',
                path: '/displayName',
                value: editData.displayName,
              })
            "
          >
            완료
          </button>
        </div>
      </div>
      <!-- TODO alert 개발 후 진행 -->
      <button class="button button-error-lighter" @click="onDeleteTerm">
        삭제
      </button>
    </div>
    <div class="work-contents">
      <!-- 결과 없을 시 no-result 표시  -->
      <div class="no-result" v-if="Object.keys(term).length === 0">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">등록된 정보가 없습니다.</p>
        </div>
      </div>
      <div
        class="editable-group"
        v-if="Object.keys(term).length > 0 && !store.editTermMode.des"
      >
        <span class="editable-group-desc">{{ term.description }}</span>
        <button
          class="button button-neutral-ghost button-sm"
          type="button"
          @click="changeEditTermMode('des')"
        >
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>

      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="editable-group" v-if="store.editTermMode.des">
        <label class="hidden-text" for="description-modify"
          >용어 설명 수정
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
              updateTerm(term.id, {
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
      <!-- //수정 버튼 클릭시 아래 내용으로 전환됩니다 -->

      <div class="editable-group" v-if="!store.editTermMode.tag">
        <div class="tag tag-primary tag-sm" v-for="tag in term.tags">
          <span class="tag-text">{{ tag.label }}</span>
        </div>
        <button
          class="button button-neutral-ghost button-sm"
          type="button"
          @click="changeEditTermMode('tag')"
        >
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>

      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <!-- TODO: 개발 - 태그 수정 -->
      <div class="editable-group" v-if="store.editTermMode.tag">
        <menu-search-tag
          :data="menuSearchTagsData"
          :selected-items="term.tags"
          label-key="label"
          value-key="data"
          :is-multi="true"
          title="값을 선택하세요"
          @multiple-change="changeTag"
          @cancel="changeEditTermMode('tag')"
          @close="changeEditTermMode('tag')"
        >
        </menu-search-tag>
      </div>
      <!-- //수정 버튼 클릭시 아래 내용으로 전환됩니다 -->

      <div class="editable-group" v-if="!store.editTermMode.synonyms">
        <span
          class="editable-group-desc"
          v-if="!term.synonyms || term.synonyms.length === 0"
          >동의어 없음</span
        >
        <!-- 동의어 있을 경우-->
        <div
          class="tag tag-primary tag-sm"
          v-if="term.synonyms.length > 0"
          v-for="synonym in term.synonyms"
        >
          <span class="tag-text">{{ synonym }}</span>
        </div>
        <button
          class="button button-neutral-ghost button-sm"
          type="button"
          @click="changeEditTermMode('synonyms')"
        >
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="editable-group" v-if="store.editTermMode.synonyms">
        <label class="hidden-text" for="synonym-modify"
          >동의어 설명 수정
        </label>
        <input
          id="synonym-modify"
          class="text-input w-4/5"
          v-model="editData.synonyms"
        />
        <div class="h-group gap-1">
          <button
            class="button button-neutral-stroke"
            type="button"
            @click="changeEditTermMode('synonyms')"
          >
            취소
          </button>
          <button class="button button-primary-lighter" type="button">
            완료
          </button>
        </div>
      </div>
      <!-- //수정 버튼 클릭시 아래 내용으로 전환됩니다 -->

      <div class="editable-group" v-if="!store.editTermMode.relatedTerms">
        <span
          class="editable-group-desc"
          v-if="!term.relatedTerms || term.relatedTerms.length === 0"
          >관련용어 없음</span
        >
        <!-- 관련용어 있을 경우-->
        <div
          class="tag tag-primary tag-sm"
          v-if="term.relatedTerms && term.relatedTerms.length > 0"
          v-for="relatedTerm in term.relatedTerms"
        >
          <span class="tag-text">{{ relatedTerm.name }}</span>
        </div>
        <button
          class="button button-neutral-ghost button-sm"
          type="button"
          @click="changeEditTermMode('relatedTerms')"
        >
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <!-- TODO: 개발 - 태그 수정 -->
      <div class="editable-group" v-if="store.editTermMode.relatedTerms">
        <menu-search-tag
          :data="menuSearchTagsData"
          label-key="label"
          value-key="data"
          :is-multi="true"
          title="값을 선택하세요"
          @multiple-change="changeTag"
          @cancel="changeEditTermMode('tag')"
          @close="changeEditTermMode('tag')"
        >
        </menu-search-tag>
      </div>
      <!-- //수정 버튼 클릭시 아래 내용으로 전환됩니다 -->

      <!-- TODO: 개발 -->
      <data-model></data-model>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useGlossaryStore } from "~/store/glossary";
import menuSearchTag from "@extends/menu-seach/tag/menu-search-tag.vue";
import { onMounted, reactive, watch } from "vue";
import type { JsonPatchOperation } from "~/type/common";
const {
  term,
  editTermMode,
  menuSearchTagsData,
  getAllTags,
  editTerm,
  deleteTerm,
  getGlossaries,
  openEditTermComponent,
  changeEditTermMode,
} = useGlossaryStore();
const store = useGlossaryStore();

const editData = reactive({
  displayName: "",
  description: "",
  synonyms: "",
});

watch(
  () => store.term,
  (newTerm) => {
    editData.displayName = newTerm.displayName;
    editData.description = newTerm.description;
    if (newTerm.synonyms) {
      editData.synonyms = newTerm.synonyms.join(",");
    }
  },
  { deep: true },
);

onMounted(() => {
  syncEditDataWithTerm();
  getAllTags();
});

function syncEditDataWithTerm() {
  editData.displayName = store.term.displayName;
  editData.description = store.term.description;
  if (store.term.synonyms) {
    editData.synonyms = store.term.synonyms.join(",");
  }
}

async function updateTerm(id: string, op: JsonPatchOperation): Promise<void> {
  await editTerm(id, [op]);
}

async function onDeleteTerm() {
  await deleteTerm(term.id);
  await getGlossaries();
  openEditTermComponent("glossary");
}

function cancel(property: keyof typeof editTermMode): void {
  changeEditTermMode(property);
  syncEditDataWithTerm();
}
</script>
