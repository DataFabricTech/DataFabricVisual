<template>
  <div class="data-detail">
    <table>
      <colgroup>
        <col style="width: 20%" />
        <col />
      </colgroup>
      <tr>
        <th>유형</th>
        <td>{{ defaultInfo.modelInfo.model.tableType }}</td>
      </tr>
      <tr>
        <th>Columns</th>
        <td>{{ defaultInfo.modelInfo.columns.length }}</td>
      </tr>
      <tr>
        <th>Rows</th>
        <td>{{ modelInfo.totalCount }}</td>
      </tr>
      <tr>
        <th>태그</th>
        <td class="py-0">
          <div class="editable-group" v-if="!editTagsMode">
            <div class="tag tag-primary tag-sm" v-for="tag in defaultInfo.tags">
              <span class="tag-text">{{ tag.name }}</span>
            </div>
            <button
              class="button button-neutral-ghost button-sm"
              type="button"
              @click="editTags"
            >
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
          <div class="editable-group" v-if="editTagsMode">
            <div class="select select-clean">
              <button class="select-button" @click.stop="editTags">
                <div
                  class="tag tag-primary tag-sm"
                  v-for="tag in modelInfo.tags"
                >
                  <span class="tag-text">{{ tag.label }}</span>
                  <button
                    class="tag-delete-button"
                    @click.stop="deleteTag(tag)"
                  >
                    <span class="hidden-text">삭제</span>
                    <svg-icon class="svg-icon" name="close"></svg-icon>
                  </button>
                </div>
                <svg-icon
                  class="svg-icon select-indicator"
                  name="chevron-down-medium"
                ></svg-icon>
              </button>
              <menu-search
                :data="tags"
                :selected-items="modelInfo.tags"
                :is-multi="true"
                @multiple-change="changeTags"
                @cancel="editTags"
              ></menu-search>
            </div>
          </div>
        </td>
      </tr>
      <tr>
        <th>용어</th>
        <td class="py-0">
          <div class="editable-group" v-if="!editTermsMode">
            <div
              class="tag tag-primary tag-sm"
              v-for="term in defaultInfo.glossaries"
            >
              <span class="tag-text">{{ term.name }}</span>
            </div>
            <button
              class="button button-neutral-ghost button-sm"
              type="button"
              @click="editTerms"
            >
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
          <div class="editable-group" v-if="editTermsMode">
            <div class="select select-clean">
              <button class="select-button" @click.stop="editTerms">
                <div
                  class="tag tag-primary tag-sm"
                  v-for="term in modelInfo.terms"
                >
                  <span class="tag-text">{{ term.label }}</span>
                  <button
                    class="tag-delete-button"
                    @click.stop="deleteTerm(term)"
                  >
                    <span class="hidden-text">삭제</span>
                    <svg-icon class="svg-icon" name="close"></svg-icon>
                  </button>
                </div>
                <svg-icon
                  class="svg-icon select-indicator"
                  name="chevron-down-medium"
                ></svg-icon>
              </button>
              <menu-search
                :data="terms"
                :selected-items="modelInfo.terms"
                :is-multi="true"
                @multiple-change="changeTerms"
                @cancel="editTerms"
              ></menu-search>
            </div>
          </div>
        </td>
      </tr>
    </table>
  </div>
</template>

<script setup lang="ts">
import { useDataModelDetailStore } from "@/store/search/detail/index";
import type { MenuSearchItemImpl } from "@extends/menu-seach/MenuSearchComposition";

import menuSearch from "@extends/menu-seach/menu-search.vue";

const dataModelDetailStore = useDataModelDetailStore();

const { defaultInfo } = storeToRefs(dataModelDetailStore);

const modelInfo = reactive({
  type: "Regular",
  columnsLength: 7,
  totalCount: 10,
  tags: [
    { label: "tag 03", value: "tag 03" },
    { label: "tag 04", value: "tag 04" },
  ],
  terms: [
    { label: "term 01", value: "term 01" },
    { label: "term 04", value: "term 04" },
    { label: "term 05", value: "term 05" },
  ],
});
const editTagsMode = ref(false);
const editTermsMode = ref(false);
const editTags = () => {
  editTagsMode.value = !editTagsMode.value;
};

const editTerms = () => {
  editTermsMode.value = !editTermsMode.value;
};
const deleteTag = (tag: object) => {
  const index = modelInfo.tags.indexOf(tag);
  if (index !== -1) {
    modelInfo.tags.splice(index, 1);
  }
};
const deleteTerm = (term: object) => {
  const index = modelInfo.terms.indexOf(term);
  if (index !== -1) {
    modelInfo.terms.splice(index, 1);
  }
};
const changeTags = (val: MenuSearchItemImpl[]) => {
  modelInfo.tags = [...val];
  editTags();
};
const changeTerms = (val: MenuSearchItemImpl[]) => {
  modelInfo.terms = [...val];
  editTerms();
};
const tags = [
  { label: "tag 01", value: "tag 01" },
  { label: "tag 02", value: "tag 02" },
  { label: "tag 03", value: "tag 03" },
  { label: "tag 04", value: "tag 04" },
  { label: "tag 05", value: "tag 05" },
];
const terms = [
  { label: "term 01", value: "term 01" },
  { label: "term 02", value: "term 02" },
  { label: "term 03", value: "term 03" },
  { label: "term 04", value: "term 04" },
  { label: "term 05", value: "term 05" },
];
</script>

<style lang="scss" scoped></style>
