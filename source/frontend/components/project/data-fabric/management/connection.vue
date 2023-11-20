<template>
  <!--  데이터 상세-->
  <section class="l-split-section">
    <div class="page-subject">
      <h3 class="page-title">
        <span class="hidden-text">선택된 데이터 정보</span>
      </h3>
      <div class="h-group gap-2">
        <baseButton class="button-normal">
          <svg-icon class="svg-icon" name="pen"></svg-icon>
          <span class="button-text">수정</span>
        </baseButton>
        <baseButton class="button-normal">
          <span class="button-text">권한 설정</span>
        </baseButton>
        <baseButton class="button-danger">
          <span class="button-text">삭제</span>
        </baseButton>
      </div>
    </div>
    <article class="page-article">
      <!-- TODO: (개발) Card 컴포넌트 추후 개발 -->
      <Card
        class="w-full"
        :model="connection.card"
        :show-info-complex="true"
        :show-statistics="false"
        :show-preview-btn="false"
        :showConnectInfo="true"
      ></Card>
    </article>
    <article class="page-article">
<!--      <div class="anchor">-->
<!--        <a class="anchor-link" title="이동" href="#status-info">상태 정보</a>-->
<!--        <a class="anchor-link" title="이동" href="#default-info">기본 정보</a>-->
<!--        <a class="anchor-link" title="이동" href="#collect-info">수집 정보</a>-->
<!--      </div>-->
      <div class="v-group gap-9 p-6 w-full">
        <div class="page-group">
          <h4 class="page-subtitle" id="default-info">기본 정보</h4>
          <div class="table-col w-full">
            <ag-grid-vue
              style="width: 100%; height: 740px"
              class="ag-theme-alpine"
              :columnDefs="connection.defaultGrid.colDefs"
              :rowData="connection.defaultGrid.rows"
              :gridOptions="gridOptions"
              @grid-ready="onGridReady"
            >
            </ag-grid-vue>
          </div>
        </div>
        <!--        <div class="page-group">-->
        <!--          <h4 class="page-subtitle" id="collect-info">수집 정보</h4>-->
        <!--          <div class="w-full bg-gray-200 h-[500px]"></div>-->
        <!--        </div>-->
      </div>
    </article>
  </section>
</template>

<script lang="ts" setup>
import { AgGridVue } from "ag-grid-vue3";
import StorageSample from "~/store/data-fabric/management/storage-sample.json";

const sample = StorageSample["connection-overview"];
const gridOptions = {
  headerHeight: 56,
  rowHeight: 56
};
const connection: {
  defaultGrid: Object;
  card: Object;
} = reactive({
  defaultGrid: {},
  card: {}
});

connection.defaultGrid = sample.default;
connection.card = {
  id: sample.id,
  name: sample.name,
  description: sample.description,
  tags: sample.tags,
  storageInfo: {
    storageType: sample.storageType
  },
  domain: sample.domain,
  lastModifiedAt: sample.lastModifiedAt,
  createdBy: sample.createdBy,
  status: sample["connection-status"]
};
function onGridReady(params) {
  params.api.sizeColumnsToFit();
}
</script>
