<template>
  <section class="l-split-section">
    <div class="page-subject">
      <h3 class="page-title">Overview</h3>
      <!--      <BaseButton class="button-tertiary">-->
      <!--        <span class="button-text">데이터 모델 생성</span>-->
      <!--      </BaseButton>-->
    </div>
    <article class="page-article">
      <notification :messages="storageEvent"></notification>
    </article>
    <article class="page-article">
      <h4 class="page-subtitle">요약 정보</h4>
      <div class="h-group w-full gap-[16px]">
        <!-- 데이터 저장소 유형 요약 -->
        <div class="chartbox">
          <h4 class="mr-auto">데이터 저장소 유형 요약</h4>
          <highcharts style="width: 100%; height: 100%" :options="storageTypeCount"> </highcharts>
        </div>
        <!-- 연결정보 상태 요약 -->
        <div class="chartbox">
          <h4 class="mr-auto">연결정보 상태 요약</h4>
          <highcharts style="width: 100%; height: 100%" :options="storageStatusCount"> </highcharts>
        </div>
        <!--  연결정보 응답시간 -->
        <div class="chartbox">
          <h4 class="mr-auto">연결정보 응답시간</h4>
          <div class="connection-info">
            <dl v-for="(item, key) in storageResponseTime" :key="key">
              <dt>{{ item.name }}</dt>
              <hr />
              <dd>{{ item.responseTime }} sec</dd>
            </dl>
          </div>
        </div>
      </div>
      <div class="h-group w-full gap-[16px]">
        <!-- 데이터 모델 조회 순위 -->
        <div class="chartbox">
          <h4 class="mr-auto">데이터 모델 조회 순위</h4>
          <highcharts style="width: 100%; height: 100%" :options="storageStatistics"> </highcharts>
        </div>
        <!-- 데이터 모델 등록 현황-->
        <div class="chartbox">
          <h4 class="mr-auto">데이터 모델 등록 현황</h4>
          <highcharts style="width: 100%; height: 100%" :options="storageDataCount"> </highcharts>
        </div>
      </div>
    </article>
    <article class="page-article">
      <h4 class="page-subtitle">최근 등록/변경된 연결정보</h4>
      <div class="table-col w-full">
        <!--        TODO: [개발] ag-grid에 스타일 옵션 추가 필요-->
        <ag-grid-vue
          style="width: 100%; height: 230px"
          class="ag-theme-alpine"
          :columnDefs="storage.overview.event.colDefs"
          :rowData="storage.overview.event.rowData"
          @grid-ready="onEventGridReady"
          :gridOptions="gridOptions"
        >
        </ag-grid-vue>
      </div>
    </article>
    <article class="page-article">
      <h4 class="page-subtitle">히스토리</h4>
      <div class="table-col w-full">
        <!--        TODO: [개발] ag-grid에 스타일 옵션 추가 필요-->
        <ag-grid-vue
          style="width: 100%; height: 230px"
          class="ag-theme-alpine"
          :columnDefs="storage.overview.history.colDefs"
          :rowData="storage.overview.history.rowData"
          @grid-ready="onHistoryGridReady"
          :gridOptions="gridOptions"
        >
        </ag-grid-vue>
      </div>
    </article>
  </section>
</template>
<script lang="ts" setup>
import { AgGridVue } from "ag-grid-vue3";
import { useOverviewStore } from "~/store/data-fabric/management/storage";
import { storeToRefs } from "pinia";

const store = useOverviewStore();
const { getOverview, getStorageEvent } = store;
const { storage, storageEvent } = storeToRefs(store);

const gridOptions = {
  headerHeight: 56,
  rowHeight: 56
};

const DEFAULT_BAR_CHART_OPTION = {
  chart: {
    type: "column"
  },
  title: {
    text: null
  },
  yAxis: {
    min: 0,
    title: {
      rotation: 0,
      align: "low"
    }
  },
  xAxis: {
    title: {
      text: "연결정보",
      align: "high",
      margin: 0
    }
  },
  tooltip: {
    enable: true
  },
  credits: {
    enabled: false
  }
};
const DEFAULT_PIE_CHART_OPTION = {
  chart: {
    type: "pie"
  },
  title: {
    text: null
  },
  legend: {
    enabled: true
  },
  tooltip: {
    enable: true
  },
  plotOptions: {
    pie: {
      innerSize: "80%",
      showInLegend: true
    }
  },
  credits: {
    enabled: false
  }
};

// NOTE: Grid-Option이 공유 될 시 하나의 그리드로 인식 됨
const DEFAULT_GRID_OPTION = {
  // headerHeight: 56,
};

onMounted(() => {
  getOverview();
  getStorageEvent();
});
const storageTypeCount = computed(() => {
  return _merge(
    {
      series: storage.value.overview.storageTypeCount.series
    },
    DEFAULT_PIE_CHART_OPTION
  );
});
const storageStatusCount = computed(() => {
  return _merge(
    {
      series: storage.value.overview.storageStatusCount.series
    },
    DEFAULT_PIE_CHART_OPTION
  );
});

const storageStatistics = computed(() => {
  return _merge(
    {
      xAxis: {
        categories: storage.value.overview.storageStatistics.categories
      },
      yAxis: {
        title: {
          text: "(계)"
        }
      },
      legend: {
        enabled: false
      },
      series: storage.value.overview.storageStatistics.series
    },
    DEFAULT_BAR_CHART_OPTION
  );
});

const storageDataCount = computed(() => {
  return _merge(
    {
      xAxis: {
        categories: storage.value.overview.storageDataCount.categories
      },
      yAxis: {
        title: {
          text: "(건)"
        }
      },
      plotOptions: {
        column: {
          stacking: "normal"
        }
      },
      legend: {
        enabled: true
      },
      series: storage.value.overview.storageDataCount.series
    },
    DEFAULT_BAR_CHART_OPTION
  );
});

const storageResponseTime = computed(() => {
  return storage.value.overview.storageResponseTime;
});

const grid: {
  historyGridApi: any;
  eventGridApi: any;
} = reactive({
  historyGridApi: null,
  eventGridApi: null
});

function onHistoryGridReady(params: any) {
  grid.historyGridApi = params.api;
  grid.historyGridApi.sizeColumnsToFit();
}

function onEventGridReady(params: any) {
  grid.eventGridApi = params.api;
  grid.eventGridApi.sizeColumnsToFit();
}
</script>
