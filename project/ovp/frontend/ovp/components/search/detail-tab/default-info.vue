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
          <td>{{ defaultInfo.modelInfo.model.ext?.toUpperCase() }}</td>
        </tr>
      </template>
      <tr>
        <th>태그</th>
        <td class="py-0">
          <div
            class="editable-group"
            v-show="!user.admin && dataModel.owner?.id !== user.id"
          >
            <template v-if="mdoelTagList.length > 0">
              <div v-for="tag in mdoelTagList">
                <span>{{ tag.displayName }}</span>
              </div>
            </template>
            <template v-else>
              <div>
                <span>-</span>
              </div>
            </template>
          </div>
          <div
            class="editable-group"
            v-if="!editTagsMode"
            v-show="dataModel.owner?.id === user.id || user.admin"
          >
            <template v-if="mdoelTagList.length > 0">
              <div class="tag tag-primary tag-sm" v-for="tag in mdoelTagList">
                <span class="tag-text">{{ tag.displayName }}</span>
              </div>
            </template>
            <template v-else>
              <div>
                <span>-</span>
              </div>
            </template>
            <button
              class="button button-neutral-ghost button-sm"
              type="button"
              @click="editTags"
            >
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
          <div
            class="editable-group editable-group-unusual"
            v-if="editTagsMode"
          >
            <menu-search-tag
              :data="tagList"
              :selected-items="mdoelTagList"
              selected-label-key="displayName"
              label-key="tagFQN"
              value-key="tagFQN"
              :is-multi="true"
              title="값을 선택하세요"
              @multiple-change="changeTags"
              @cancel="closeTags"
              @close="closeTags"
            ></menu-search-tag>
          </div>
        </td>
      </tr>
      <tr>
        <th>용어</th>
        <td class="py-0">
          <div
            class="editable-group"
            v-show="!user.admin && dataModel.owner?.id !== user.id"
          >
            <template v-if="dataModel.terms?.length > 0">
              <div
                class="tag tag-primary tag-sm"
                v-for="term in dataModel.terms"
              >
                <span class="tag-text">{{ term.displayName }}</span>
              </div>
            </template>
            <template v-else>
              <div>
                <span>-</span>
              </div>
            </template>
          </div>
          <div
            class="editable-group"
            v-if="!editTermsMode"
            v-show="dataModel.owner?.id === user.id || user.admin"
          >
            <template v-if="dataModel.terms?.length > 0">
              <div
                class="tag tag-primary tag-sm"
                v-for="term in dataModel.terms"
              >
                <span class="tag-text">{{ term.displayName }}</span>
              </div>
            </template>
            <template v-else>
              <div>
                <span>-</span>
              </div>
            </template>
            <button
              class="button button-neutral-ghost button-sm"
              type="button"
              @click="editTerms"
            >
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
          <div
            class="editable-group editable-group-unusual"
            v-if="editTermsMode"
          >
            <menu-search-tag
              :data="termList"
              :selected-items="dataModel.terms"
              selected-label-key="displayName"
              label-key="tagFQN"
              value-key="tagFQN"
              :is-multi="true"
              title="값을 선택하세요"
              @multiple-change="changeTerms"
              @cancel="closeTerms"
              @close="closeTerms"
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
import { useUserStore } from "@/store/user/userStore";

import type { MenuSearchItemImpl } from "@extends/menu-seach/MenuSearchComposition";
import menuSearchTag from "@extends/menu-seach/tag/menu-search-tag.vue";

import type { ComputedRef } from "vue";

const dataModelDetailStore = useDataModelDetailStore();
const userStore = useUserStore();

const { getTagList, getGlossaryList, getDataModel, changeTag } =
  dataModelDetailStore;
const { dataModelType, tagList, termList, dataModel, defaultInfo } =
  storeToRefs(dataModelDetailStore);

const { user } = storeToRefs(userStore);

const editTagsMode = ref(false);
const editTermsMode = ref(false);

const mdoelTagList: ComputedRef<any[]> = computed(() => {
  return _.filter(dataModel.value.tags, (tag) => {
    return !_.includes(tag.tagFQN, "ovp_category");
  });
});

const editTags = async () => {
  editTagsMode.value = !editTagsMode.value;

  if (editTagsMode.value) {
    await getTagList();
  }
};
const closeTags = () => {
  editTagsMode.value = false;
};

const editTerms = async () => {
  editTermsMode.value = !editTermsMode.value;

  if (editTermsMode.value) {
    await getGlossaryList();
  }
};

const closeTerms = () => {
  editTermsMode.value = false;
};

const changeTags = (value: MenuSearchItemImpl[]) => {
  const data: any = _.map(value, "tagFQN");
  changeTag("Classification", false, data)
    .then(() => {
      editTagsMode.value = false;
      getDataModel();
    })
    .catch((error) => {
      console.error(error);
    });
};

const changeTerms = (value: MenuSearchItemImpl[]) => {
  const data: any = _.map(value, "tagFQN");

  changeTag("Glossary", false, data)
    .then(() => {
      editTermsMode.value = false;
      getDataModel();
    })
    .catch((error) => {
      console.error(error);
    });
};
</script>

<style lang="scss" scoped></style>
