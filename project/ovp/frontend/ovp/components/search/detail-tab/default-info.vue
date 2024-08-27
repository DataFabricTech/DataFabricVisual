<template>
  <div class="data-detail">
    <table>
      <colgroup>
        <col style="width: 20%" />
        <col />
      </colgroup>
      <tr v-if="dataModelType === 'table'">
        <th>유형</th>
        <td>테이블</td>
      </tr>
      <tr v-else-if="dataModelType === 'model'">
        <th>유형</th>
        <td>융합모델</td>
      </tr>
      <template v-else-if="dataModelType === 'storage'">
        <tr>
          <th>유형</th>
          <td>스토리지</td>
        </tr>
        <tr>
          <th>확장자</th>
          <td>{{ defaultInfo.modelInfo.model.ext.toUpperCase() }}</td>
        </tr>
      </template>
      <tr>
        <th>태그</th>
        <td class="py-0">
          <div class="editable-group" v-if="!editTagsMode">
            <div class="tag tag-primary tag-sm" v-for="tag in mdoelTagList">
              <span class="tag-text">{{ tag.displayName }}</span>
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
            <menu-search-tag
              :data="tagList"
              :selected-items="mdoelTagList"
              label-key="displayName"
              value-key="tagFQN"
              :is-multi="true"
              title="값을 선택하세요"
              @multiple-change="changeTags"
              @cancel="editTags"
              @close="editTagsMode = false"
            ></menu-search-tag>
          </div>
        </td>
      </tr>
      <tr>
        <th>용어</th>
        <td class="py-0">
          <div class="editable-group" v-if="!editTermsMode">
            <div class="tag tag-primary tag-sm" v-for="term in dataModel.terms">
              <span class="tag-text">{{ term.displayName }}</span>
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
            <menu-search-tag
              :data="glossaryList"
              :selected-items="dataModel.terms"
              label-key="displayName"
              value-key="fullyQualifiedName"
              :is-multi="true"
              title="값을 선택하세요"
              @multiple-change="changeTags"
              @cancel="editTags"
              @close="editTags"
            ></menu-search-tag>
          </div>
        </td>
      </tr>
    </table>
  </div>
</template>

<script setup lang="ts">
import _ from "lodash";

import { useDataModelDetailStore } from "@/store/search/detail/index";
import { useSearchCommonStore } from "@/store/search/common";

import type { MenuSearchItemImpl } from "@extends/menu-seach/MenuSearchComposition";
import menuSearchTag from "@extends/menu-seach/tag/menu-search-tag.vue";

import type { ComputedRef } from "vue";

const dataModelDetailStore = useDataModelDetailStore();
const searchCommonStore = useSearchCommonStore();

const { getTagList, getGlossaryList } = dataModelDetailStore;
const { dataModelType, tagList, glossaryList, dataModel, defaultInfo } =
  storeToRefs(dataModelDetailStore);
const { filters } = storeToRefs(searchCommonStore);

const editTagsMode = ref(false);
const editTermsMode = ref(false);

const mdoelTagList: ComputedRef<any[]> = computed(() => {
  return _.filter(dataModel.value.tags, (tag) => {
    return !_.includes(tag.tagFQN, "ovp_category");
  });
});

const editTags = async () => {
  await getTagList();
  editTagsMode.value = !editTagsMode.value;

  if (editTagsMode.value) {
    await getTagList();
  }
};

const editTerms = async () => {
  editTermsMode.value = !editTermsMode.value;

  if (editTermsMode.value) {
    await getGlossaryList();
  }
};

const changeTags = (val: MenuSearchItemImpl[]) => {
  // TODO: 태그 변경 처리
  editTagsMode.value = false;
};
const changeTerms = (val: MenuSearchItemImpl[]) => {
  // TODO: 용어 변경 처리
  editTermsMode.value;
};
</script>

<style lang="scss" scoped></style>
