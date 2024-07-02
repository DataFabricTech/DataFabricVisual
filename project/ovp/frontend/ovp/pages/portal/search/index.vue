<template>
  <div class="wrap">
    <Header />
    <main id="main">
      <h2 class="hidden-text">데이터 목록</h2>
      <Sidebar></Sidebar>
      <section id="section">
        <!--  상단 필터 & 초기화-->
        <div class="filters">
          <filter-item :data="filters" @reset-filters="resetFilters" />
        </div>
        <div class="section-contents">
          <top-bar></top-bar>
          <div class="l-split">
            <div class="data-page">
              <div class="data-list">
                <resource-box-list
                  v-if="viewType === 'listView'"
                  :data-list="searchResult.data"
                  :use-list-checkbox="false"
                  :show-owner="true"
                  :show-category="true"
                  :is-box-selected-style="isBoxSelectedStyle"
                  @previewClick="previewClick"
                  @modelNmClick="modelNmClick"
                />
                <custom-knowledge-graph v-if="viewType === 'graphView'" />
              </div>
            </div>
            <preview
              v-if="viewType === 'listView'"
              :isShowPreview="isShowPreview"
              :preview-data="previewData"
              @change="getPreviewCloseStatus"
            ></preview>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import Header from "@/layouts/header.vue";
import Sidebar from "@/layouts/sidebar.vue";
import TopBar from "./top-bar.vue";
import { useSearchCommonStore } from "@/store/search/common";
import { onMounted } from "vue";
import { storeToRefs } from "pinia";

const searchCommonStore = useSearchCommonStore();
const { getSearchCommonData, getPreviewData } = searchCommonStore;
const {
  filters,
  searchResult,
  previewData,
  viewType,
  isShowPreview,
  isBoxSelectedStyle,
  searchResultLength,
} = storeToRefs(searchCommonStore);

onMounted(async () => {
  // 목록정보 loading
  await getSearchCommonData();
  searchResultLength.value = searchResult.value.data.length;
});

const getPreviewCloseStatus = (option: boolean) => {
  isShowPreview.value = option;
  isBoxSelectedStyle.value = false;
};

const previewClick = async (id: string | number) => {
  console.log(`previewClick : ${id}`);
  await getPreviewData();
  isShowPreview.value = true;
  isBoxSelectedStyle.value = true;
};

const modelNmClick = (id: string | number) => {
  console.log(`modelNmClick : ${id}`);
};

const resetFilters = () => {
  console.log("resetFilters");
};
</script>

<style lang="scss" scoped></style>
