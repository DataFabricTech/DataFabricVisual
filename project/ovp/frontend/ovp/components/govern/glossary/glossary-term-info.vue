<template>
  <div class="work-page" v-if="store.currentComponent === 'term'">
    <div class="l-top-bar">
      <div class="editable-group" v-if="!store.editTermMode.name">
        <span class="editable-group-title">{{ store.term.name }}</span>
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
              updateTerm({
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
      <div class="v-group gap-2">
        <div class="font-semibold text-neutral-700">설명</div>
        <div
          class="editable-group"
          v-if="Object.keys(term).length > 0 && !store.editTermMode.des"
        >
          <span class="editable-group-desc">{{
            term && term.description ? term.description : "-"
          }}</span>
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
                updateTerm({
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
      <!-- //수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="h-group gap-2">
        <div class="font-semibold text-neutral-700 w-16">태그</div>
        <div class="editable-group" v-if="!store.editTermMode.tag">
          <div
            class="tag tag-primary tag-sm"
            v-if="term && term.tags && term.tags.length > 0"
            v-for="tag in term.tags"
          >
            <span class="tag-text">{{ tag.displayName }}</span>
          </div>
          <div class="text-neutral-700" v-else>
            <span>-</span>
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
        <div class="editable-group" v-if="store.editTermMode.tag">
          <menu-search-tag
            :data="menuSearchTagsData"
            :selected-items="term.tags"
            label-key="displayName"
            value-key="tagFQN"
            :is-multi="true"
            title="값을 선택하세요"
            @multiple-change="changeTag"
            @cancel="changeEditTermMode('tag')"
            @close="changeEditTermMode('tag')"
          >
          </menu-search-tag>
        </div>
      </div>
      <div class="h-group gap-2">
        <div class="font-semibold text-neutral-700 w-16">동의어</div>
        <div class="editable-group" v-if="!store.editTermMode.synonyms">
          <span
            class="editable-group-desc"
            v-if="!term.synonyms || term.synonyms.length === 0"
            >-</span
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
              @click="cancelSynonyms"
            >
              취소
            </button>
            <button
              class="button button-primary-lighter"
              type="button"
              @click="changeSynonyms"
            >
              완료
            </button>
          </div>
        </div>
      </div>
      <!-- //수정 버튼 클릭시 아래 내용으로 전환됩니다 -->

      <div class="h-group gap-2">
        <div class="font-semibold text-neutral-700 w-16">관련용어</div>
        <div class="editable-group" v-if="!store.editTermMode.relatedTerms">
          <span
            class="editable-group-desc"
            v-if="!term.relatedTerms || term.relatedTerms.length === 0"
            >-</span
          >
          <!-- 관련용어 있을 경우-->
          <div
            class="tag tag-primary tag-sm"
            v-if="term.relatedTerms && term.relatedTerms.length > 0"
            v-for="relatedTerm in term.relatedTerms"
          >
            <span class="tag-text">{{ relatedTerm.label }}</span>
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
        <div class="editable-group" v-if="store.editTermMode.relatedTerms">
          <menu-search-tag
            :data="filteredRelatedTerms"
            :selected-items="term.relatedTerms"
            label-key="label"
            value-key="id"
            :is-multi="true"
            title="값을 선택하세요"
            @multiple-change="changeRelatedTerms"
            @cancel="changeEditTermMode('relatedTerms')"
            @close="changeEditTermMode('relatedTerms')"
          >
          </menu-search-tag>
        </div>
      </div>
      <data-model></data-model>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useGlossaryStore } from "~/store/glossary";
import menuSearchTag from "@extends/menu-seach/tag/menu-search-tag.vue";
import { onMounted, reactive, watch } from "vue";
import type { JsonPatchOperation, Tag } from "~/type/common";
import type { MenuSearchItemImpl } from "@extends/menu-seach/MenuSearchComposition";
import type { Term } from "~/type/glossary";
const {
  term,
  terms,
  tags,
  editTermMode,
  menuSearchTagsData,
  menuSearchRelatedTermsData,
  getTerms,
  editTerm,
  deleteTerm,
  changeCurrentTerm,
  getGlossaries,
  openEditTermComponent,
  disableEditTermModes,
  changeEditTermMode,
  createTagOperation,
  createRelatedTermOperation,
  createSynonymsOperation,
} = useGlossaryStore();
const store = useGlossaryStore();

const editData = reactive({
  name: "",
  description: "",
  synonyms: "",
});

watch(
  () => store.term,
  (newTerm) => {
    editData.name = newTerm.name;
    editData.description = newTerm.description;
    if (newTerm.synonyms) {
      editData.synonyms = newTerm.synonyms.join(",");
    }
  },
  { deep: true },
);

onMounted(() => {
  syncEditDataWithTerm();
});

const filteredRelatedTerms = computed(() => {
  return menuSearchRelatedTermsData.filter(
    (item: object) => item.id !== term.id,
  );
});

function syncEditDataWithTerm(): void {
  editData.name = store.term.name;
  editData.description = store.term.description;
  if (store.term.synonyms) {
    editData.synonyms = store.term.synonyms.join(",");
  }
}

async function refreshTerm(): Promise<void> {
  const ID = term.id;
  await getTerms(store.glossary.name);
  const termData: Term = terms.find((term: Term) => term.id === ID);
  changeCurrentTerm(termData);
  disableEditTermModes();
}

async function updateTerm(op: JsonPatchOperation): Promise<void> {
  await editTerm(term.id, [op]);
  await refreshTerm();
}

async function onDeleteTerm(): Promise<void> {
  await deleteTerm(term.id);
  await getGlossaries();
  openEditTermComponent("glossary");
}

function cancel(property: keyof typeof editTermMode): void {
  changeEditTermMode(property);
  syncEditDataWithTerm();
}

function cancelSynonyms(): void {
  editData.synonyms = term.synonyms;
  changeEditTermMode("synonyms");
}

async function changeSynonyms() {
  const arr = editData.synonyms.split(",");
  const operations: JsonPatchOperation[] = createSynonymsOperation(
    arr,
    term.synonyms,
  );

  await editTerm(term.id, operations);
  await refreshTerm();
}

async function changeTag(items: MenuSearchItemImpl[]): Promise<void> {
  const selectedItems: string[] = items.map(
    (item: MenuSearchItemImpl) => item.tagFQN as string,
  );
  const matchTags: Tag[] = tags.filter((tag) =>
    selectedItems.includes(tag.tagFQN),
  );
  const operations: JsonPatchOperation[] = createTagOperation(
    selectedItems,
    matchTags,
    term,
  );
  await editTerm(term.id, operations);
  await refreshTerm();
}

async function changeRelatedTerms(items: MenuSearchItemImpl[]): Promise<void> {
  const selectedItems: string[] = items.map(
    (item: MenuSearchItemImpl) => item.id as string,
  );
  const matchRelatedTerms: object[] = terms.filter((term: object) =>
    selectedItems.includes(term.id),
  );
  matchRelatedTerms.forEach((term: object) => {
    term.type = "glossaryTerm";
    term.deleted = false;
    delete term.tags;
    delete term.synonyms;
    delete term.relatedTerms;
  });
  const operations: JsonPatchOperation[] = createRelatedTermOperation(
    selectedItems,
    matchRelatedTerms,
  );
  await editTerm(term.id, operations);
  await refreshTerm();
}
</script>
