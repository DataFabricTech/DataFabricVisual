<template>
  <category-graph />
  <!--  <network-diagram />-->
  <!-- 모델 리스트 -->
  <div class="visual-model-list" v-if="showGraphModelListMenu">
    <div class="visual-model-list-head">
      <div class="breadcrumb">
        <ul class="breadcrumb-list">
          <li class="breadcrumb-item">
            <span class="breadcrumb-link">1depth</span>
          </li>
          <li class="breadcrumb-item is-breadcrumb-selected">
            <span class="breadcrumb-link">데이터 모델</span>
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
        >총 <em class="primary">{{ graphModelList.length }}개</em></strong
      >
      <div
        class="menu menu-data menu-lg"
        v-if="graphModelList && graphModelList.length !== 0"
      >
        <ul class="menu-list">
          <li class="menu-item" v-for="menu in graphModelList" :key="menu">
            <a
              href="javascript:void(0);"
              class="menu-button"
              @click="modelNmClick(menu)"
            >
              <div :class="menu.serviceIcon"></div>
              <span class="menu-text">{{ menu.modelNm }}</span>
              <span class="menu-subtext">
                ({{
                  menu.owner === null
                    ? "소유자 없음"
                    : (menu.owner.displayName ?? menu.owner.name)
                }})</span
              >
            </a>
            <div class="menu-button-group">
              <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
              <button
                class="button button-neutral-ghost button-sm"
                @click="setCheckedBookmark(menu)"
              >
                <span class="hidden-text">북마크</span>
                <svg-icon
                  class="svg-icon"
                  :class="menu.checkBookmark ? 'secondary' : ''"
                  :name="menu.checkedBookmark ? 'tag-fill' : 'tag'"
                ></svg-icon>
              </button>
            </div>
          </li>
        </ul>
      </div>
      <!-- 결과 없을 시 no-result 표시 -->
      <div class="no-result" v-else>
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
import NetworkDiagram from "./graph/network-diagram.vue";
import { useSearchCommonStore } from "~/store/search/common";
import { useDataModelSearchStore } from "~/store/datamodel-creation/search";
import { useRouter } from "nuxt/app";

const router = useRouter();
const searchCommonStore = useSearchCommonStore();
const dataModelSearchStore = useDataModelSearchStore();
const {} = searchCommonStore;
const { graphModelList, filteredIdAndTagIdData, showGraphModelListMenu } =
  storeToRefs(searchCommonStore);

const { getSearchList } = dataModelSearchStore;

const closeModelList = () => {
  showGraphModelListMenu.value = false;
};

// TODO: [개발] 데이터 생성의 추가 api 로 작업 변경할 것.
const setCheckedBookmark = (menu: any) => {
  // const node = menu.target;
  console.log("item?", menu);
  menu.checkedBookmark = !menu.checkedBookmark;

  console.log("graphModelList?", graphModelList.value);
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
  filteredIdAndTagIdData.value = [];
  graphModelList.value = [];
  showGraphModelListMenu.value = false;
});
</script>

<style lang="scss" scoped></style>
