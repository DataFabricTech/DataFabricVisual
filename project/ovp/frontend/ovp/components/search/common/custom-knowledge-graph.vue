<template>
  <category-graph />
  <!--  <network-diagram />-->
  <!-- 모델 리스트 -->
  <div class="visual-model-list" v-show="showGraphModelListMenu">
    <div class="visual-model-list-head">
      <div class="breadcrumb">
        <ul class="breadcrumb-list">
          <li
            class="breadcrumb-item"
            v-for="(item, index) in graphCategoryPath"
            :key="index"
          >
            <span class="breadcrumb-link">{{ item }}</span>
          </li>
        </ul>
      </div>
      <button
        class="search-input-action-button button button-neutral-ghost button-sm"
        type="button"
        @click="closeModelList"
      >
        <span class="hidden-text">지우기</span>
        <svg-icon class="button-icon" name="close"></svg-icon>
      </button>
    </div>
    <div class="p-3 h-full">
      <strong
        >총 <em class="primary">{{ filteredSearchList.length }}개</em></strong
      >
      <div
        class="menu menu-data menu-lg"
        v-show="filteredSearchList && filteredSearchList.length !== 0"
        style="position: relative"
      >
        <ul class="menu-list">
          <li class="menu-item" v-for="menu in filteredSearchList" :key="menu">
            <a
              href="javascript:void(0);"
              class="menu-button"
              @click="modelNmClick(menu)"
            >
              <div :class="menu.serviceIcon"></div>
              <span class="menu-text">{{
                menu.displayName ?? menu.modelNm
              }}</span>
              <span class="menu-subtext">
                ({{
                  menu.owner === null
                    ? "소유자 없음"
                    : (menu.owner.displayName ?? menu.owner.name)
                }})
              </span>
            </a>
            <div class="menu-button-group">
              <button
                class="button button-neutral-ghost button-sm"
                @click="updateBookmarkList(menu)"
              >
                <span class="hidden-text">북마크</span>
                <svg-icon
                  class="svg-icon"
                  :class="menu.isFollow ? 'secondary' : ''"
                  :name="menu.isFollow ? 'tag-fill' : 'tag'"
                ></svg-icon>
              </button>
            </div>
          </li>
        </ul>
      </div>
      <!-- 결과 없을 시 no-result 표시 -->
      <div class="no-result" v-show="filteredSearchList.length === 0">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">등록된 정보가 없습니다.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import CategoryGraph from "./graph/category-graph.vue";
import { useSearchCommonStore } from "~/store/search/common";
import { useRouter } from "nuxt/app";

const router = useRouter();
const searchCommonStore = useSearchCommonStore();
const { updateBookmarkList } = searchCommonStore;
const {
  showGraphModelListMenu,
  graphModelList,
  graphCategoryPath,
  filteredSearchList,
} = storeToRefs(searchCommonStore);

const closeModelList = () => {
  showGraphModelListMenu.value = false;
};
const modelNmClick = (data: object) => {
  const { id, fqn, type } = data as { id: string; fqn: string; type: string };
  router.push({
    path: "/portal/search/detail",
    query: {
      type: type,
      id: id,
      fqn: fqn,
    },
  });
};

onBeforeMount(() => {
  graphModelList.value = [];
  showGraphModelListMenu.value = false;
});
</script>

<style lang="scss" scoped></style>
