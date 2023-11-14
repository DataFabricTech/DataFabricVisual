<template>
  <section class="l-split-section">
    <div class="page-subject">
      <h3 class="page-title">
        <span class="hidden-text">선택된 데이터 정보</span>
      </h3>
      <BaseSwitch>
        <template v-slot:switch-text>데이터 모델 생성</template>
        <template v-slot:hidden-text>데이터 모델 생성</template>
      </BaseSwitch>
    </div>
    <article class="page-article">
      <Card
        class="w-full"
        :model="tableInfoData"
        :show-info-complex="true"
        :show-statistics="false"
        :show-preview-btn="false"
        :showConnectInfo="true"
      ></Card>
    </article>
    <article class="page-article">
      <div class="anchor">
        <!-- TODO: 1차 개발에서 제외. 추후 개발-->
        <!--        <a class="anchor-link" title="이동" href="#overview">overview</a>-->
        <!--        <a class="anchor-link" title="이동" href="#data">data</a>-->
        <!--        <a class="anchor-link" title="이동" href="#query">query</a>-->
      </div>
      <div class="v-group gap-12 p-6 w-full">
        <div class="v-group gap-9 w-full">
          <h3 class="page-title" id="overview">Overview</h3>
          <div class="page-group">
            <h4 class="page-subtitle">Summary</h4>
            <div class="h-group gap-8 w-full">
              <dl class="data-status" v-for="item in storage.currStorage.systemMeta" :key="item">
                <dt class="data-status-label">
                  <svg-icon name="data" class="svg-icon" />
                  <span class="data-status-name">{{ item.key }}</span>
                </dt>
                <dd class="data-status-num">{{ item.value }}</dd>
              </dl>
            </div>
          </div>
          <div class="page-group">
            <h4 class="page-subtitle">생성된 데이터 모델</h4>
            <div class="card-container">
              <ul class="card-simple-list">
                <Card
                  class="w-full"
                  :model="createdData"
                  :show-info-complex="true"
                  :show-preview-btn="false"
                  :showConnectInfo="true"
                ></Card>
              </ul>
            </div>
          </div>
          <div class="page-group" v-if="storage.currStorage.dataStructure">
            <h4 class="page-subtitle">Structure</h4>
            <div class="table-col w-full">
              <ag-grid-vue
                style="width: 100%; height: 430px"
                class="ag-theme-alpine"
                :columnDefs="storage.currStorage.dataStructure?.colDefs"
                :rowData="storage.currStorage.dataStructure?.rows"
                :gridOptions="gridOptions"
                @grid-ready="onGridReady"
              >
              </ag-grid-vue>
            </div>
          </div>
        </div>
        <!--        <div class="v-group gap-9 w-full">-->
        <!--          <h4 class="page-title" id="data">data</h4>-->
        <!--          <div class="page-group">-->
        <!--            <div class="w-full bg-gray-200 h-[500px]"></div>-->
        <!--          </div>-->
        <!--        </div>-->
        <!--        <div class="v-group gap-9 w-full">-->
        <!--          <h4 class="page-title" id="query">query</h4>-->
        <!--          <div class="page-group">-->
        <!--            <div class="w-full bg-gray-200 h-[500px]"></div>-->
        <!--          </div>-->
        <!--        </div>-->
      </div>
    </article>
  </section>
</template>

<script lang="ts" setup>
import StorageSample from "~/store/data-fabric/management/storage-sample.json";
import { AgGridVue } from "ag-grid-vue3";
const gridOptions = {
  headerHeight: 56,
  rowHeight: 56
};

const route = useRoute();
const queryId = route.query.id;

const storage: {
  list: Array<any>;
  currStorage: Object;
} = reactive({
  list: [],
  currStorage: {}
});
initData();
function initData() {
  storage.list = StorageSample.tables;
  storage.currStorage = _find(storage.list, { id: queryId });
  if (!storage.currStorage) {
    alert("존재하지 않는 페이지입니다.");
  }
}
function onGridReady(params) {
  params.api.sizeColumnsToFit();
}
const createdData = computed(() => {
  return {
    name: storage.currStorage.name,
    description: storage.currStorage.description,
    lastModifiedAt: storage.currStorage.lastModifiedAt,
    domain: storage.currStorage.dataFormat,
    createdBy: storage.currStorage.createdBy,
    storageInfo: {
      storageType: storage.currStorage.dataLocation[0].type
    },
    status: storage.currStorage.status,
    tags: storage.currStorage.tage,
    statistics: storage.currStorage.statistics
  };
});
const tableInfoData = computed(() => {
  const data = storage.currStorage.dataLocation[0];
  return {
    name: data.tableName,
    description: `서초 데이터 > ${data.databaseName}`,
    createdBy: data.createdBy,
    lastModifiedAt: data.lastModifiedAt,
    domain: storage.currStorage.dataFormat,
    storageInfo: {
      storageType: data.type
    },
    status: storage.currStorage.status
  };
});
</script>
