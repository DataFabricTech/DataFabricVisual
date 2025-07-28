<template>
  <div>
    <main id="main" class="main-sub">
      <!-- sub page : main-sub class 추가 -->
      <div class="main-contents">
        <h2 class="blind">System Monitoring</h2>
        <!-- content-floating-wrap가 있는 페이지에만 .floating-contents 추가 -->
        <!-- contents -->
        <div class="contents">
          <div class="mypage-wrap">
            <div class="mu-row gap-10">
              <div class="mu-col mu-col-4">
                <div class="mu-heading">
                  <h4>시스템 타입별 메타데이터 수</h4>
                </div>
                <div class="chart-wrap">
                  <div class="chart-box">
                    <highcharts :constructorType="'chart'" :options="pieChartOptions" ref="chart"  class="chart" />
                  </div>
                </div>
              </div>
              <div class="mu-col mu-col-4">
                <div class="mu-heading">
                  <h4>메타 타입별 메타데이터 수</h4>
                </div>
                <div class="chart-wrap">
                  <div class="chart-box">
                    <highcharts :constructorType="'chart'" :options="pieChart2Options" ref="chart"  class="chart" />
                  </div>
                </div>
              </div>
              <div class="mu-col mu-col-4">
                <div class="mu-heading">
                  <h4>시스템별 응답시간</h4>
                </div>
                <div class="scroll-box">
                  <div
                    v-for="(item, index) in systemResponseData"
                    :key="index"
                    class="response-item"
                  >
                    <span class="service-name">{{ item.system_name }}</span>
                    <span class="divider-line"></span>
                    <span class="response-time">{{ getResponseTime(item.response_time) }} sec</span>
                  </div>
                </div>
              </div>
            </div>
            <div class="mu-row gap-20">
              <div class="mu-col mu-col">
                <div class="mu-heading" style="display: flex; justify-content: space-between;">
                  <h4>시스템별 CPU 사용량</h4>
                  <div class="mu-selectbox w120">
                    <select name="searchType2" :value="currentUsageCpuYear" @change="onChangeUsageCpuYear">
                      <option v-for="(item, index) in years" :key="index" :value="item">
                        {{ item }}년
                      </option>
                    </select>
                  </div>
                </div>
                <div class="chart-wrap">
                  <div class="chart-box">
                    <highcharts :constructorType="'chart'" :options="lineChartOptions" ref="chart"  class="chart" />
                  </div>
                </div>
              </div>
              <div class="mu-col mu-col">
                <div class="mu-heading" style="display: flex; justify-content: space-between;">
                  <h4>시스템별 Memory 사용량</h4>
                  <div class="mu-selectbox w120">
                    <select name="searchType2" :value="currentUsageMemoryYear" @change="onChangeUsageMemoryYear">
                      <option v-for="(item, index) in years" :key="index" :value="item">
                        {{ item }}년
                      </option>
                    </select>
                  </div>
                </div>
                <div class="chart-wrap">
                  <div class="chart-box">
                    <highcharts :constructorType="'chart'" :options="lineChart2Options" ref="chart"  class="chart" />
                  </div>
                </div>
              </div>
            </div>
            <div class="mu-row gap-20">
              <div class="mu-col mu-col">
                <div class="mu-heading">
                  <h4>시스템별 Query (성공/실패) 수</h4>
                </div>
                <div class="chart-wrap">
                  <div class="chart-box">
                    <highcharts :constructorType="'chart'" :options="columnChartOptions" ref="chart"  class="chart" />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- //contents -->
      </div>
    </main>
  </div>

</template>
<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";
import { ref } from "vue";

definePageMeta({
  name: "AdminDataSystemMonitoring",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();

const colorList = ref([
  "#FF4C4C", // 빨강 (Red)
  "#FF914D", // 주황 (Orange)
  "#FFD93B", // 노랑 (Yellow)
  "#6BD96B", // 연두/그린 (Green)
  "#4DA6FF", // 파랑 (Blue)
  "#5C6BC0", // 인디고 (Indigo)
  "#9D6BFF", // 보라 (Violet)
  "#C06BDB", // 라벤더/보랏빛
  "#FF6BB5", // 핑크계열
  "#999999"  // 중립 회색 (보조 색)
]);

const currentYear = new Date().getFullYear()
const startYear = 2000
const years = Array.from({ length: currentYear - startYear + 1 }, (_, i) => startYear + i).reverse();

const currentUsageCpuYear = ref(currentYear)
const currentUsageMemoryYear = ref(currentYear)

const systemTypeCountChartData = ref([])
const metaTypeCountChartData = ref([])
const systemResponseData = ref([])
const systemUsageCpuChartData = ref([])
const systemUsageMemoryChartData = ref([])
const systemQuerySuccessFailedChartData = ref([])

const pieChartOptions = ref({
  chart: {
    plotBackgroundColor: null,
    plotBorderWidth: null,
    plotShadow: false,
    type: 'pie',
    backgroundColor: 'transparent',
  },
  title: {
    text: '<span>데이터 현황</span> ',
    x: 0,
    y: 17,
    align: 'center',
    verticalAlign: 'middle',
    floating: true,
    useHTML: true,
    style: {
      lineHeight: '1',
      textAlign: 'center',
    }
  },
  credits: { enabled: false },
  legend: { enabled: false },
  plotOptions: {
    pie: {
      events: { click: e => onClickPieChart(e) },
      allowPointSelect: true,
      showInLegend: true,
      cursor: 'pointer',
      size: '250px',
      innerSize: '50%',
      borderColor: 'transparent',
      borderWidth: 0,
      dataLabels: {
        enabled: true,
        formatter: function() {
          return `  <b>${this.point.name}</b><br/>
          <span>${ObjectUtils.numberFormatter(this.point.y)} 개</span>`;
        },
        distance: 15,
        style: {
          fontFamily: 'NanumSquare',
          fontSize: '14px',
          fontWeight: '400',
          color: '#5f6874',
          textShadow: 'none',
          textOverflow: 'none',
          overflow: 'visible',
        },
      },
      states: {
        hover: {
          enabled: false,
          halo: false
        }
      },
    }
  },
  series: [{
    name: '',
    colorByPoint: true,
    data: [],
  }],
  navigation: { buttonOptions: { enabled: false } },
  tooltip: {
    backgroundColor: 'rgba(255,255,255,0.8)',
    shadow: false,
    borderRadius: 0,
    shared: false,
    useHTML: true,
    formatter: function() {
      return `<b>${this.point.name}</b>
          <br />${ObjectUtils.numberFormatter(this.point.y)}개`;
    },
    style: {
      fontFamily: 'NanumSquare',
      fontSize: '14px',
      color: '#222',
      textOverflow: 'none',
    }
  }
})

const pieChart2Options = ref({
  chart: {
    plotBackgroundColor: null,
    plotBorderWidth: null,
    plotShadow: false,
    type: 'pie',
    backgroundColor: 'transparent',
  },
  title: {
    text: '<span>데이터 현황</span> ',
    x: 0,
    y: 17,
    align: 'center',
    verticalAlign: 'middle',
    floating: true,
    useHTML: true,
    style: {
      lineHeight: '1',
      textAlign: 'center',
    }
  },
  credits: { enabled: false },
  legend: { enabled: false },
  plotOptions: {
    pie: {
      events: { click: e => onClickPieChart(e) },
      allowPointSelect: true,
      showInLegend: true,
      cursor: 'pointer',
      size: '250px',
      innerSize: '50%',
      borderColor: 'transparent',
      borderWidth: 0,
      dataLabels: {
        enabled: true,
        formatter: function() {
          return `  <b>${this.point.name}</b><br/>
          <span>${ObjectUtils.numberFormatter(this.point.y)}개</span>`;
        },
        distance: 15,
        style: {
          fontFamily: 'NanumSquare',
          fontSize: '14px',
          fontWeight: '400',
          color: '#5f6874',
          textShadow: 'none',
          textOverflow: 'none',
          overflow: 'visible',
        },
      },
      states: {
        hover: {
          enabled: false,
          halo: false
        }
      },
    }
  },
  series: [{
    name: '',
    colorByPoint: true,
    data: [],
  }],
  navigation: { buttonOptions: { enabled: false } },
  tooltip: {
    backgroundColor: 'rgba(255,255,255,0.8)',
    shadow: false,
    borderRadius: 0,
    shared: false,
    useHTML: true,
    formatter: function() {
      return `<b>${this.point.name}</b>
          <br />${ObjectUtils.numberFormatter(this.point.y)}개`;
    },
    style: {
      fontFamily: 'NanumSquare',
      fontSize: '14px',
      color: '#222',
      textOverflow: 'none',
    }
  }
})

const lineChartOptions = ref({
  chart: {
    type: 'line'
  },
  title: {
    text: ''
  },
  subtitle: {
    text: ''
  },
  xAxis: {
    categories: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월']
  },
  yAxis: {
    title: {
      text: ''
    },
    tickInterval: 1000
  },
  plotOptions: {
    line: {
      dataLabels: {
        enabled: true
      },
      enableMouseTracking: true
    }
  },
  series: [
    {
      name: 'A 시스템',
      data: [10, 15, 20, 30, 25, 35, 40, 38, 32, 28, 22, 18]
    },
    {
      name: 'B 시스템',
      data: [5, 10, 15, 22, 27, 33, 37, 36, 31, 25, 20, 15]
    }
  ],
  tooltip: {
    backgroundColor: 'rgba(255,255,255,0.8)',
    shadow: false,
    borderRadius: 0,
    shared: false,
    useHTML: true,
    formatter: function() {
      return `<b>${this.point.category}</b>
          <br />${ObjectUtils.numberFormatter(this.point.y)}`;
    },
    style: {
      fontFamily: 'NanumSquare',
      fontSize: '14px',
      color: '#222',
      textOverflow: 'none',
    }
  }
})


const lineChart2Options = ref({
  chart: {
    type: 'line'
  },
  title: {
    text: ''
  },
  subtitle: {
    text: ''
  },
  xAxis: {
    categories: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월']
  },
  yAxis: {
    title: {
      text: ''
    },
    tickInterval: 1000
  },
  plotOptions: {
    line: {
      dataLabels: {
        enabled: true
      },
      enableMouseTracking: true
    }
  },
  series: [
    {
      name: 'Reggane',
      data: [16.0, 18.2, 23.1, 27.9, 32.2, 36.4, 39.8, 38.4, 35.5, 29.2, 22.0, 17.8]
    }
  ],
  tooltip: {
    backgroundColor: 'rgba(255,255,255,0.8)',
    shadow: false,
    borderRadius: 0,
    shared: false,
    useHTML: true,
    formatter: function() {
      return `<b>${this.point.category}</b>
          <br />${ObjectUtils.numberFormatter(this.point.y)}`;
    },
    style: {
      fontFamily: 'NanumSquare',
      fontSize: '14px',
      color: '#222',
      textOverflow: 'none',
    }
  }
})

const columnChartOptions = ref({
  chart: {
    type: 'column'
  },
  title: {
    text: ''
  },
  xAxis: {
    title: {
      text: '',
    },
    categories: [],
    tickInterval: 1
  },
  yAxis: {
    title: {
      text: ''
    },
    min: 0,
    tickInterval: 100000,
  },
  plotOptions: {
    line: {
      dataLabels: {
        enabled: true
      },
      enableMouseTracking: true
    }
  },
  series: [
    {
      name: 'Jane',
      data: [1, 0, 4]
    }
  ],
  tooltip: {
    backgroundColor: 'rgba(255,255,255,0.8)',
    shadow: false,
    borderRadius: 0,
    shared: false,
    useHTML: true,
    formatter: function() {
      return `<b>${this.point.category}</b>
          <br />${ObjectUtils.numberFormatter(this.point.y)}개`;
    },
    style: {
      fontFamily: 'NanumSquare',
      fontSize: '14px',
      color: '#222',
      textOverflow: 'none',
    }
  }
})

onMounted(() => {
  getChartData()
})

// Chart Clieck Event
const onClickPieChart = (e) => {
  e.stopPropagation();
  const target = ObjectUtils.getPropertyValue(e, 'point/options', {});
}

// 아이콘 영역 렌더에 사용하는 데이터 값 설정
const initColor = async (list) => {

  // 각각 색상 저장
  for (let i in list) {
    let mod = i % colorList.value.length;
    list[i].color = colorList.value[mod];
  }
};

// 라인 차트에 맞게 데이터 변환
const getUsageCpuSeries = (rawData) => {
  // 1. x축에 사용할 월
  const months = Array.from({ length: 12 }, (_, i) => {
    const month = (i + 1).toString().padStart(2, '0')
    return `2025-${month}`
  })

  // 2. 시스템별 데이터 정리
  const systemsMap = {}
  rawData.forEach(row => {
    const key = `${row.system_name}`
    if (!systemsMap[key]) {
      systemsMap[key] = {}
    }
    systemsMap[key][row.month] = row.total_cpu_usage
  })

  // 3. series 구성 (모든 월 포함, 없으면 0)
  const series = Object.entries(systemsMap).map(([name, usageMap]) => ({
    name,
    type: 'line',
    data: months.map(month => usageMap[month] ?? 0)
  }))

  return series
};

// 라인 차트에 맞게 데이터 변환
const getUsageMemorySeries = (rawData) => {
  // 1. x축에 사용할 월
  const months = Array.from({ length: 12 }, (_, i) => {
    const month = (i + 1).toString().padStart(2, '0')
    return `2025-${month}`
  })

  // 2. 시스템별 데이터 정리
  const systemsMap = {}
  rawData.forEach(row => {
    const key = `${row.system_name}`
    if (!systemsMap[key]) {
      systemsMap[key] = {}
    }
    systemsMap[key][row.month] = row.total_memory_usage
  })

  // 3. series 구성 (모든 월 포함, 없으면 0)
  const series = Object.entries(systemsMap).map(([name, usageMap]) => ({
    name,
    type: 'line',
    data: months.map(month => usageMap[month] ?? 0)
  }))

  return series
};

// 막대 차트에 맞게 데이터 변환
const getQuerySuccessFailedSeries = (raw) => {
  const querySuccessFailedCategories = raw ? raw.map(row => row.system_name) : [];
  const successData = raw ? raw.map(row => row.query_success_count) : [];
  const failedData = raw ? raw.map(row => row.query_fail_count) : [];

  const querySuccessFailedSeries = [
    {
      name: "성공",
      data: successData,
    },
    {
      name: "실패",
      data: failedData
    }
  ]

  return { querySuccessFailedCategories, querySuccessFailedSeries }
}

const getChartData = async () => {
  const charData = []

  await $ofetch(`/api/monitoring/chartData`).then((res) => {
    if (res && res.data) {
      // 시스템 타입별 메타데이터 수
      systemTypeCountChartData.value = res.data.systemTypeCountChartData.map((el) => { return { name: el.system_type, y: el.count }})
      initColor(systemTypeCountChartData.value)
      pieChartOptions.value.series[0].data = systemTypeCountChartData.value;

      // 메타 타입별 메타데이터 수
      metaTypeCountChartData.value = res.data.metaTypeCountChartData.map((el) => { return { name: el.meta_type, y: el.count }})
      initColor(metaTypeCountChartData.value)
      pieChart2Options.value.series[0].data = metaTypeCountChartData.value;

      systemResponseData.value = res.data.systemResponseData // 시스템별 응답시간
      setSystemUsageCpuChartData(res.data.systemUsageCpuChartData) // 시스템별 CPU 사용량
      setSystemUsageMemoryChartData(res.data.systemUsageMemoryChartData) // 시스템별 Memory 사용량
      setSystemQuerySuccessFailedChartData(res.data.systemQuerySuccessFailedChartData) // 시스템별 Query (성공/실패) 수
    }
  })
}

// 시스템별 CPU 사용량 차트 데이터 설정
const setSystemUsageCpuChartData = (data) => {
  systemUsageCpuChartData.value = data
  lineChartOptions.value.yAxis.title.text = 'CPU 사용량 (sec)' // y축 설정
  lineChartOptions.value.yAxis.tickInterval = 1000 // y축 간격
  lineChartOptions.value.xAxis.categories = ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'] // x축 설정
  lineChartOptions.value.series = getUsageCpuSeries(systemUsageCpuChartData.value)
}

// 시스템별 Memory 사용량 차트 데이터 설정
const setSystemUsageMemoryChartData = (data) => {
  systemUsageMemoryChartData.value = data
  lineChart2Options.value.yAxis.title.text = 'Memory 사용량 (sec)' // y축 설정
  lineChart2Options.value.yAxis.tickInterval = 1000 // y축 간격
  lineChart2Options.value.xAxis.categories = ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'] // x축 설정
  lineChart2Options.value.series = getUsageMemorySeries(systemUsageMemoryChartData.value)
}

// 시스템별 Query (성공/실패) 수 차트 데이터 설정
const setSystemQuerySuccessFailedChartData = (data) => {
  systemQuerySuccessFailedChartData.value = data
  columnChartOptions.value.yAxis.title.text = '쿼리 성공/실패 카운트' // y축 설정
  columnChartOptions.value.yAxis.tickInterval = 1000 // y축 간격

  const { querySuccessFailedCategories, querySuccessFailedSeries }= getQuerySuccessFailedSeries(systemQuerySuccessFailedChartData.value)
  columnChartOptions.value.xAxis.categories = querySuccessFailedCategories // x축 설정
  columnChartOptions.value.series = querySuccessFailedSeries
  columnChartOptions.value.colors = ['#28a745', '#dc3545'] // 초록, 빨강
}

const getResponseTime = (ms_time) => {
  if (ms_time) {
    const seconds = (ms_time / 1000000).toFixed(2);  // "123.46" (문자열)
    return seconds
  } else {
    if (ms_time == 0) return 0
    else return '-'
  }
}

const onChangeUsageCpuYear = async (e) => {
  currentUsageCpuYear.value = e.target.value
  await $ofetch(`/api/monitoring/usageCpuChartData?year=${currentUsageCpuYear.value}`).then((res) => {
    if (res && res.data) {
      systemUsageCpuChartData.value = res.data.systemUsageCpuChartData
      lineChartOptions.value.series = getUsageCpuSeries(systemUsageCpuChartData.value)

    }
  })
}

const onChangeUsageMemoryYear = async (e) => {
  currentUsageMemoryYear.value = e.target.value
  await $ofetch(`/api/monitoring/usageMemoryChartData?year=${currentUsageMemoryYear.value}`).then((res) => {
    if (res && res.data) {
      systemUsageMemoryChartData.value = res.data.systemUsageMemoryChartData
      lineChart2Options.value.series = getUsageMemorySeries(systemUsageMemoryChartData.value)
    }
  })
}

</script>
<style scoped>
.scroll-box {
  max-height: 400px;        /* 스크롤 높이 제한 */
  overflow-y: auto;         /* 세로 스크롤 활성화 */
  border: 1px solid #eee;
  padding: 0px 10px;
}

.response-item {
  display: flex;
  align-items: center;
  margin: 8px 0;
  padding: 0 4px;
}

.service-name {
  flex: 0 0 130px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.divider-line {
  flex: 1 1 auto;
  height: 1px;
  background-color: #e0e0e0;
  margin: 0 8px;
  min-width: 20px; /* 줄어들 수 있는 최소 길이 */
}

.response-time {
  flex: 0 0 auto;
  white-space: nowrap; /* 줄바꿈 방지 */
  text-align: right;
  color: #666;
}

.empty-message {
  color: #999;
  font-style: italic;
  margin: 10px 0;
}

</style>
