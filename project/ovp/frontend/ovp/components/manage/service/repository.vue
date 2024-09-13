<template>
  <div class="service-page">
    <template v-if="DBServiceListData.length > 0">
      <agGrid
        class="ag-theme-alpine ag-theme-quartz"
        :column-width-list="[20, 100, 150]"
        :columnDefs="columnDefs"
        :rowData="DBServiceListData"
        :useRowCheckBox="false"
        :setColumnFit="true"
        :useColumnResize="true"
      ></agGrid>
      <!-- TODO: [퍼블] 저장소 탭 부분에만 loading 처리 가능 여부 확인 -->
      <Loading
        v-show="isDoneRepoAPI"
        id="repoLoader"
        :use-loader-overlay="true"
        class="is-loader-inner loader-content"
      ></Loading>
    </template>
    <!-- 결과 없을 시 no-result 표시 -->
    <div class="no-result h-auto" v-else>
      <div class="notification">
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">데이터 리스트가 없습니다.</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useServiceStore } from "@/store/manage/service";
import agGrid from "@extends/grid/Grid.vue";
import LinkDetailComponent from "./linkDetailComponent.vue";
import Loading from "@base/loading/Loading.vue";
const serviceStore = useServiceStore();

const { DBServiceListData, isDoneRepoAPI } = storeToRefs(serviceStore);

interface DBServiceListData {
  owner: string | undefined;
  fqn: string;
  name: string;
  id: string;
  type: string;
  desc: string | undefined;
}

const columnDefs = ref([
  { field: "type", headerName: "구분" },
  {
    field: "name",
    headerName: "이름",
    cellRenderer: LinkDetailComponent,
  },
  { field: "desc", headerName: "설명" },
  // { field: "owner", headerName: "소유자" },
]);
</script>

<style scoped></style>
