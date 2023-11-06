<template>
  <section class="l-split-section">
    <div class="page-subject">
      <h3 class="page-title">Overview</h3>
<!--      <BaseButton class="button-tertiary">-->
<!--        <span class="button-text">데이터 모델 생성</span>-->
<!--      </BaseButton>-->
    </div>
    <article class="page-article">
      <!-- TODO: (개발) notification 받은 후 개발 -->
      <BaseNotification theme="warning" use-close>
        <strong class="notification-title"> 연결정보가 변동되어 데이터모델 정보 를 다운로드 받을 수 없습니다. </strong>
        <a href="#" class="notification-link"> 바로가기 </a>
      </BaseNotification>
    </article>
    <article class="page-article">
      <h4 class="page-subtitle">요약 정보</h4>
      <div class="h-group w-full gap-[16px]">
        <!-- 데이터 저장소 유형 요약 -->
        <div class="chartbox">
          <h4 class="mr-auto">데이터 저장소 유형 요약</h4>
          <highcharts style="width: 100%; height: 100%" :options="chart.storageTypeCount"> </highcharts>
        </div>
        <!-- 연결정보 상태 요약 -->
        <div class="chartbox">
          <h4 class="mr-auto">연결정보 상태 요약</h4>
          <highcharts style="width: 100%; height: 100%" :options="chart.storageStatusCount"> </highcharts>
        </div>
        <!--  연결정보 응답시간 -->
        <div class="chartbox">
          <h4 class="mr-auto">연결정보 응답시간</h4>
          <!-- TODO: (개발) 연결정보 개발 -->
          <div class="connection-info">
            <dl>
              <dt>연결정보 C</dt>
              <hr>
              <dd>24.10 sec</dd>
            </dl>
            <dl>
              <dt>연결정보 C</dt>
              <hr>
              <dd>24.10 sec</dd>
            </dl>
            <dl>
              <dt>연결정보 C</dt>
              <hr>
              <dd>24.10 sec</dd>
            </dl>
            <dl>
              <dt>연결정보 C</dt>
              <hr>
              <dd>24.10 sec</dd>
            </dl>
            <dl>
              <dt>연결정보 C</dt>
              <hr>
              <dd>24.10 sec</dd>
            </dl>
          </div>
        </div>
      </div>
      <div class="h-group w-full gap-[16px]">
        <!-- 데이터 모델 조회 순위 -->
        <div class="chartbox">
          <h4 class="mr-auto">데이터 모델 조회 순위</h4>
          <highcharts style="width: 100%; height: 100%" :options="chart.storageStatistics"> </highcharts>
        </div>
        <!-- 데이터 모델 등록 현황-->
        <div class="chartbox">
          <h4 class="mr-auto">데이터 모델 등록 현황</h4>
          <highcharts style="width: 100%; height: 100%" :options="chart.storageDataCount"> </highcharts>
        </div>
      </div>
    </article>
    <article class="page-article">
      <h4 class="page-subtitle">최근 등록/변경된 연결정보</h4>
      <div class="table-col w-full">
        <ag-grid-vue
          style="width: 100%; height: 230px"
          class="ag-theme-alpine"
          :gridOptions="DEFAULT_GRID_OPTION"
          :columnDefs="grid.event.colDefs"
          :rowData="grid.event.rowData"
          @grid-ready="onEventGridReady"
        >
        </ag-grid-vue>
      </div>
    </article>
    <article class="page-article">
      <h4 class="page-subtitle">히스토리</h4>
      <div class="table-col w-full">
        <ag-grid-vue
          style="width: 100%; height: 230px"
          class="ag-theme-alpine"
          :gridOptions="DEFAULT_GRID_OPTION"
          :columnDefs="grid.history.colDefs"
          :rowData="grid.history.rowData"
          @grid-ready="onHistoryGridReady"
        >
        </ag-grid-vue>
      </div>
    </article>
  </section>
</template>
<script lang="ts" setup>
import { AgGridVue } from "ag-grid-vue3";
import { useStorageStore } from "~/store/data-fabric/storage/storage";
import type { Overview } from "~/components/project/data-fabric/overview/storage-overview";
import type { Grid } from "~/@types/global";

const { getOverview, getStorageEvent } = useStorageStore();

const DEFAULT_BAR_CHART_OPTION = {
  chart: {
    type: "column",
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
      align:"high",
      margin: 0
    },
  },
  legend: {
    enabled: true
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
const DEFAULT_GRID_OPTION = {
  // headerHeight: 56,
};

const storage: {
  overview: Overview;
  events: Array<Object>
} = reactive({
  overview: {
    storageTypeCount: {
      series: []
    },
    storageStatusCount: {
      series: []
    },
    storageStatistics: {
      categories: [],
      series: []
    },
    storageDataCount: {
      categories: [],
      series: []
    },
    history: {
      colDefs: [],
      rowData: []
    },
    event: {
      colDefs: [],
      rowData: []
    }
  },
  events: []
});
storage.overview = getOverview();
storage.events = getStorageEvent();

const chart: {
  storageTypeCount: Object;
  storageStatusCount: Object;
  storageStatistics: Object;
  storageDataCount: Object;
} = reactive({
  storageTypeCount: {},
  storageStatusCount: {},
  storageStatistics: {},
  storageDataCount: {}
});
setChartData();

function setChartData() {
  chart.storageTypeCount = _merge(
    {
      series: storage.overview.storageTypeCount.series
    },
    DEFAULT_PIE_CHART_OPTION
  );

  chart.storageStatusCount = _merge(
    {
      series: storage.overview.storageStatusCount.series
    },
    DEFAULT_PIE_CHART_OPTION
  );

  chart.storageStatistics = _merge(
    {
      xAxis: {
        categories: storage.overview.storageStatistics.categories
      },
      yAxis: {
        title: {
          text: "(계)"
        }
      },
      series: storage.overview.storageStatistics.series
    },
    DEFAULT_BAR_CHART_OPTION
  );
  chart.storageDataCount = _merge(
    {
      xAxis: {
        categories: storage.overview.storageDataCount.categories,
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
      series: storage.overview.storageDataCount.series
    },
    DEFAULT_BAR_CHART_OPTION
  );
}

const grid: {
  history: Grid;
  event: Grid;
  historyGridApi: any;
  eventGridApi: any;
} = reactive({
  history: {
    rowData: [],
    colDefs: []
  },
  event: {
    rowData: [],
    colDefs: []
  },
  historyGridApi: null,
  eventGridApi: null
});
setGridData();
function setGridData() {
  grid.history = storage.overview.history;
  grid.event = storage.overview.event;
}
function onHistoryGridReady(params: any) {
  grid.historyGridApi = params.api;
  grid.historyGridApi.sizeColumnsToFit();
}
function onEventGridReady(params: any) {
  grid.eventGridApi = params.api;
  grid.eventGridApi.sizeColumnsToFit();
}
</script>
