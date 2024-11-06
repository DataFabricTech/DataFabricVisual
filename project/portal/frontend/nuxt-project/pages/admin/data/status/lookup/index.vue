<template>
  <div>
    <div class="inner">
      <!-- admin-dashboard -->
      <div class="admin-dashboard">
          <!-- mu-tab-box dashboard-tab -->
          <div class="mu-tab-box dashboard-tab">
              <ul class="mu-tab">
                  <li v-bind:class="{ active: searchType=='login' }" @click="onChangeTab('login')">
                      <a href="#none">
                          <span>오늘의<br />접속자 수</span>
                          <strong><b>{{ today_total.login_today_total }}</b> 명</strong>
                      </a>
                  </li>
                  <li v-bind:class="{ active: searchType=='meta' }" @click="onChangeTab('meta')">
                      <a href="#none">
                          <span>오늘의<br />메타 데이터 조회</span>
                          <strong><b>{{ today_total.meta_today_total }}</b> 건</strong>
                      </a>
                  </li>
                  <li v-bind:class="{ active: searchType=='file' }" @click="onChangeTab('file')">
                      <a href="#none">
                          <span>오늘의<br />File 추출 데이터</span>
                          <strong><b>{{ today_total.file_today_total }}</b> 건</strong>
                      </a>
                  </li>
                  <li v-bind:class="{ active: searchType=='api' }" @click="onChangeTab('api')">
                      <a href="#none">
                          <span>오늘의<br />API 추출 데이터</span>
                          <strong><b>{{ today_total.api_today_total }}</b> 건</strong>
                      </a>
                  </li>
                  <li v-bind:class="{ active: searchType=='request' }" @click="onChangeTab('request')">
                      <a href="#none">
                          <span>오늘의<br />데이터 요청</span>
                          <strong><b>{{ today_total.data_request_today_total }}</b> 건</strong>
                      </a>
                  </li>
              </ul>
          </div>
          <!-- //mu-tab-box dashboard-tab -->
          <!-- dashboard-container -->
          <div class="dashboard-container">
              <div class="mu-heading">
                  <h4>{{ tooltipString[searchType] }} 수({{ searchType == "login" ? "명" : "건" }})</h4>
              </div>
              <!-- mu-search-group -->
              <div class="mu-search-group simple-type">
                  <div class="mu-search-item">
                    <div class="mu-item-group">
                      <div class="mu-radio" v-for="(item, index) in radioList" :key="index">
                        <input type="radio" :id="index" v-model="searchDateType" :value="item.value" />
                        <label :for="index"><span>{{ item.text }}</span></label>
                      </div>
                    </div>
                    <div class="mu-item-group item-ml-auto">
                        <!-- period-set : 기간 -->
                        <div class="period-set">
                            <date-picker v-model:date="startDate" :disabledMaxDate="endDate" :type="datetimeType"  :clearable="false" placeholder="시작일" :confirm="false"></date-picker>
                            <div v-if="searchDateType=='hour'" class="mu-selectbox w90">
                                <select v-model="startTime">
                                  <option v-for="item in timeObj" :key="item" :value="item">{{item}}</option>
                                </select>
                            </div>
                            <span>~</span>
                            <date-picker v-model:date="endDate" :disabledMinDate="startDate" :type="datetimeType" :clearable="false" placeholder="종료일" :confirm="false"></date-picker>
                            <div v-if="searchDateType=='hour'" class="mu-selectbox w90">
                                <select v-model="endTime">
                                  <option v-for="item in timeObj" :key="item" :value="item">{{item}}</option>
                                </select>
                            </div>
                        </div>
                        <button type="button" class="mu-btn mu-btn-icon-only" title="초기화" @click="onClickClearButton">
                            <i class="mu-icon-svg refresh"></i>
                            <span>초기화</span>
                        </button>
                    </div>
                  </div>
                  <div class="mu-search-btn">
                      <button type="button" class="mu-btn mu-btn-primary" @click="getChartDataList"><i class="mu-icon-svg search"></i><span>검색</span></button>
                  </div>
              </div>
              <!-- //mu-search-group -->
              <!-- dashboard-chart -->
              <div class="dashboard-chart">
                  <!-- <div style="height: 400px; border: 1px dashed red; background-color: #fafafa">로그 데이터 기반 그래프 출력</div> -->
                  <div class="svg-container" id="svg-container">
                    <highcharts :constructorType="'chart'" :options="chartOptions" ref="chart" />
                  </div>
              </div>
              <!-- //dashboard-chart -->
              <!-- mu-formbox -->
              <table class="mu-formbox">
                  <caption>
                      데이터 활용 현황 정보 통계
                  </caption>
                  <colgroup>
                      <col style="width:210px" />
                      <col style="width: auto" />
                      <col style="width:210px" />
                      <col style="width: auto" />
                      <col style="width:210px" />
                      <col style="width: auto" />
                  </colgroup>
                  <tbody>
                      <tr>
                          <th scope="row">총 {{ tooltipString[searchType] }} 수 ({{ searchType == "login" ? "명" : "건" }})</th>
                          <td>{{ totalInfo.total }}</td>
                          <th scope="row">평균 {{ tooltipString[searchType] }} 수 ({{ searchType == "login" ? "명" : "건" }})</th>
                          <td>{{ totalInfo.avg }}</td>
                          <th scope="row">오늘 {{ tooltipString[searchType] }} 수 ({{ searchType == "login" ? "명" : "건" }})</th>
                          <td>{{ totalInfo.today }}</td>
                      </tr>
                      <tr>
                          <th scope="row">어제 {{ tooltipString[searchType] }} 수 ({{ searchType == "login" ? "명" : "건" }})</th>
                          <td>{{ totalInfo.yesterday }}</td>
                          <th scope="row">이번달 {{ tooltipString[searchType] }} 수 ({{ searchType == "login" ? "명" : "건" }})</th>
                          <td>{{ totalInfo.month }}</td>
                          <th scope="row">지난달 {{ tooltipString[searchType] }} 수 ({{ searchType == "login" ? "명" : "건" }})</th>
                          <td>{{ totalInfo.prev_month }}</td>
                      </tr>
                  </tbody>
              </table>
              <!-- //mu-formbox -->
          </div>
          <!-- //dashboard-container -->
      </div>
      <!-- //admin-dashboard -->
    </div>
  </div>
</template>

<script setup lang="ts">
import DatePicker  from "@functional/datepicker/date-picker";
import moment from "moment";
import { errorAlert } from "@functional/alert/alert-default";
import {onMounted, watch, reactive, ref} from "vue";

definePageMeta({
  name: "AdminDataLookup",
  layout: 'admin',
})

onMounted(() => {
  getTodayTotal();
  getChartDataList();
});

const { $ofetch } = useNuxtApp();

// --- data start ---
interface TooltipString {
  [key: string]: string;
}

interface TodayTotal {
  login_today_total: number;
  meta_today_total: number;
  file_today_total: number;
  api_today_total: number;
  data_request_today_total: number;
}

interface TotalInfo {
  total: number;
  today: number;
  avg: number;
  yesterday: number;
  month: number;
  prev_month: number;
}

interface ChartOptions {
  credits: { enabled: boolean };
  chart: { type: string };
  title: { text: string };
  xAxis: {
    reversed: boolean;
    type: string;
    labels: { format: string };
    maxPadding: number;
    showLastLabel: boolean;
  };
  yAxis: {
    labels: { format: string };
    lineWidth: number;
    softMax: number;
  };
  legend: { enabled: boolean };
  tooltip: { headerFormat: string; pointFormatter: () => string };
  plotOptions: { spline: { marker: { enable: boolean } } };
  series: Array<{ name: string; data: Array<[string, number]> }>;
}

const radioList = ref([
  { value: 'day', text: '일별' },
  { value: 'month', text: '월별' },
  { value: 'year', text: '년별' },
  { value: 'hour', text: '시간대별' },
]);

let searchType = ref<string>('login'); // login(로그인), meta(메타데이터), file(파일추출), api(API추출), request(데이터 요청)
const searchDateType = ref('day'); // year(년도별), month(월별), day(일별), hour(시간대별)
const datetimeType = ref('date'); // 달력유형
const timeObj = ref([
  '00:00', '01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00',
  '08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00',
  '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00'
]); // 시간콤보

const tooltipString = ref<TooltipString>({
  login: '접속자',
  meta: '메타 데이터 조회',
  file: 'File 추출 데이터',
  api: 'API 추출 데이터',
  request: '데이터 요청',
});

const startDate = ref(moment().subtract(30, 'd').format('YYYY-MM-DD')); // 시작년도, 월, 일
const endDate = ref(moment().format('YYYY-MM-DD')); // 종료년도, 월, 일
const startTime = ref(moment().subtract(24, 'h').format('HH:00')); // 시작시간
const endTime = ref(moment().add(1, 'h').format('HH:00')); // 종료시간

let today_total = ref<TodayTotal>({
  login_today_total: 0,
  meta_today_total: 0,
  file_today_total: 0,
  api_today_total: 0,
  data_request_today_total: 0,
});

let chartDataList = ref<Array<any>>([]); // 차트 데이터

let totalInfo = ref<TotalInfo>({
  total: 0,
  today: 0,
  avg: 0,
  yesterday: 0,
  month: 0,
  prev_month: 0,
});

const chartOptions = ref<ChartOptions>({
  credits: { enabled: false },
  chart: {
    type: 'spline',
  },
  title: {
    text: ''
  },
  xAxis: {
    reversed: false,
    type: 'category',
    labels: {
      format: '{value}'
    },
    maxPadding: 0.05,
    showLastLabel: true
  },
  yAxis: {
    labels: {
      format: '{value}'
    },
    lineWidth: 2,
    softMax: 10,
  },
  legend: {
    enabled: false
  },
  tooltip: {
    headerFormat: '<b style="font-size: 12px;">{point.key}</b><br/><strong style="font-size: 16px;">{point.y}</strong><br/>',
    pointFormatter: function() {
      // Tooltip 포맷터 로직
    }
  },
  plotOptions: {
    spline: {
      marker: {
        enable: false
      }
    }
  },
  series: [{
    name: '접속자/당일/전일/명',
    data: []
  }],
});

// --- data end ---

watch(searchDateType, (newValue, oldValue) => {
  if (newValue === "day") { // 일별
    datetimeType.value = "date";
    startDate.value = moment().subtract(30, "d").format("YYYY-MM-DD");
    endDate.value = moment().format("YYYY-MM-DD");
  } else if (newValue === "month") { // 월별
    datetimeType.value = "month";
    startDate.value = moment().subtract(12, "M").format("YYYY-MM");
    endDate.value = moment().format("YYYY-MM");
  } else if (newValue === "year") { // 년별
    datetimeType.value = "year";
    startDate.value = moment().subtract(5, "y").format("YYYY");
    endDate.value = moment().format("YYYY");
  } else if (newValue === "hour") { // 시간대별
    datetimeType.value = "datetime"; // Modified part: Set appropriate value for datetimeType
    startDate.value = moment().subtract(24, "h").format("YYYY-MM-DD");
    startTime.value = moment().subtract(24, "h").format("HH:00");
    endDate.value = moment().format("YYYY-MM-DD");
    endTime.value = moment().add(1, "h").format("HH:00");
  }
  fnSetTooltipText();
  getChartDataList();
})

// --- method ---

// 상단 통계데이터 조회
async function getTodayTotal() {
  try {
    const result = await $ofetch<{ data: { today_total: number; gubun: string }[] }>(`/api/datastat/total`);
    today_total.value = result.data.reduce((target: TodayTotal, item: { today_total: number; gubun: string }) => {
      target[item.gubun as keyof TodayTotal] = item.today_total;
      return target;
    }, { ...today_total.value });
  } catch (error) {
    console.log(error);
  }
}


// 차트 데이터 조회
interface resultChartData {
  dd: string;
  cnt: number;
}

interface resultTotalInfo {
  gubun: string;
  cnt: number;
}

interface ResultData {
  chartDataList: resultChartData[];
  totalInfo: resultTotalInfo[];
}
interface Result {
  data: ResultData;
}
const getChartDataList = async (): Promise<void> => {
  try {
    let result: Result;
    if (searchDateType.value === "hour") {
      result = await $ofetch(`/api/datastat/chart?searchType=${searchType.value}&searchDateType=${searchDateType.value}&startDate=${startDate.value} ${startTime.value}&endDate=${endDate.value} ${endTime.value}`);
    } else {
      result = await $ofetch(`/api/datastat/chart?searchType=${searchType.value}&searchDateType=${searchDateType.value}&startDate=${startDate.value}&endDate=${endDate.value}`);
    }

    const chartDataList = result.data.chartDataList;

    let avg_total = 0;
    chartOptions.value.series[0].data = chartDataList.map((el) => {
      avg_total += Number(el.cnt);
      return [el.dd, Number(el.cnt)];
    });

    totalInfo.value = result.data.totalInfo.reduce(
      (target, key) => {
        switch (key.gubun) {
          case 'total':
            target.total = key.cnt;
            break;
          case 'today':
            target.today = key.cnt;
            break;
          case 'avg':
            target.avg = key.cnt;
            break;
          case 'yesterday':
            target.yesterday = key.cnt;
            break;
          case 'month':
            target.month = key.cnt;
            break;
          case 'prev_month':
            target.prev_month = key.cnt;
            break;
        }
        return target;
      },
      { total: 0, today: 0, avg: 0, yesterday: 0, month: 0, prev_month: 0 }
    );

    totalInfo.value.avg = Math.round((avg_total / chartOptions.value.series[0].data.length) * 10) / 10;
  } catch (error) {
    console.log(error);
  }
};

// 탭 클릭 이벤트 : 접속자, 메타데이터, File추출, API추출, 데이터요청
const onChangeTab = (param: string) =>{
  searchType.value = param;
  fnSetTooltipText();
  getChartDataList();
}

// 달력 입력값 초기화 버튼
const onClickClearButton = () => {
  let value = searchDateType.value;
  if(value == "day") { // 일별
    datetimeType.value = "date";//date |datetime|year|month|time|week
    startDate.value = moment().subtract(30, "d").format("YYYY-MM-DD");
    endDate.value = moment().format("YYYY-MM-DD");
  } else if(value == "month") { // 월별
    datetimeType.value = "month";
    startDate.value = moment().subtract(12, "M").format("YYYY-MM");
    endDate.value = moment().format("YYYY-MM");
  } else if(value == "year") { // 년별
    datetimeType.value = "year";
    startDate.value = moment().subtract(12, "y").format("YYYY");
    endDate.value = moment().format("YYYY");
  } else if(value == "hour") { // 시간대별
    datetimeType.value = "date";
    startDate.value = moment().subtract(24, "h").format("YYYY-MM-DD");
    startTime.value = moment().subtract(24, "h").format("HH:00");
    endDate.value = moment().format("YYYY-MM-DD");
    endTime.value = moment().add(1, "h").format("HH:00");
  }
  fnSetTooltipText();
  getChartDataList();
}

// 툴팁에서 보여줄 텍스트 정보 셋팅
const fnSetTooltipText = () => {
  let strTooltipText = "";
  if(searchDateType.value == "day") {
    if(searchType.value == "login") strTooltipText = '/당일/전일/명'; // login(로그인), meta(메타데이터), file(파일추출), api(API추출), request(데이터 요청)
    else strTooltipText = '/당일/전일/건';
  } else if(searchDateType.value == "month") {
    if(searchType.value == "login") strTooltipText = '/당월/전월/명';
    else strTooltipText = '/당월/전월/건';
  } else if(searchDateType.value == "year") {
    if(searchType.value == "login") strTooltipText = '/해당년도/전년도/명';
    else strTooltipText = '/해당년도/전년도/건';
  } else if(searchDateType.value == "hour") {
    if(searchType.value == "login") strTooltipText = '/해당시간/한시간전/명';
    else strTooltipText = '/해당시간/한시간전/건';
  }
  chartOptions.value.series[0].name = tooltipString[searchType.value] + strTooltipText;
}
</script>

<style lang="scss">
  @import "index";
</style>
