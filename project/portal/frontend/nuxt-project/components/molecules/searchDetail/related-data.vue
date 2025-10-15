<template>
  <div :class="props.isAdmin ? 'data-box-wrap' : ''"
    :style="props.isAdmin ? 'padding-top: 5px; padding-bottom: 35px' : ''">
    <div class="detail-section" :class="showData ? 'open' : 'close'">
      <div class="mu-heading">
        <h4>연관 데이터</h4>
        <div class="mu-item-group">
          <button type="button" class="mu-btn mu-btn-bg-non btn-section-toggle" title="접기" @click="toggleShowData()"
            style="margin-left: 10px">
            <span>{{ toggleShowDataText }}</span>
          </button>
        </div>
      </div>
      <div class="detail-box" v-if="relatedDataList && relatedDataList.length > 0">
        <div class="mu-row">
          <div class="mu-col mu-col-7">
            <ul class="connection-list">
              <li v-for="(item, index) in relatedDataList" :key="index">
                <div class="datatype">
                  <em class="datatype-label">{{ item.classifications[0] && item.classifications[0].c_description ? item.classifications[0].c_description : '카테고리 미지정' }}</em>
                </div>
                <a v-on:click="onClickDataName(item)">
                  <span class="datatype-desc" v-if="item.description">{{ item.description }} &nbsp;&nbsp;&nbsp;</span>
                  <span>{{ item.meta_name }}</span>
                </a>
              </li>
            </ul>
          </div>
          <div class="mu-col mu-col-5">
            <highcharts :constructorType="'chart'" :options="pieChartOptions" ref="pieChart"/>
          </div>
        </div>
      </div>
      <div class="data-result h300" v-else>
        <p class="nodata">연관 데이터가 존재하지 않습니다.</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { watch } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "~/store/user";

const userStore = useUserStore();
const router = useRouter();

const emit = defineEmits(["onClickData"]);
const props = defineProps(["relatedDataList", "entity", "isAdmin"]);

const showData = ref(true);
const toggleShowDataText = ref("접기");

const pieChart = ref(null)

// 연관데이터 링크 클릭 후 메타데이터 재조회 시 차트를 다시 그리기 위해 추가, 초기에는 차트 DOM이 생성되기 전 호출됨
watch(() => props.relatedDataList, (newVal) => {
  drawChart(newVal)
})

// 차트 DOM이 초기에 생성되었을 때 차트에 데이터를 설정하기 위해 호출
watch(() => pieChart.value, () => {
  drawChart(props.relatedDataList)
})

// 파이 차트 설정
const pieChartOptions = ref({
  chart: {
      type: 'variablepie'
  },
  title: {
      text: ''
  },
  tooltip: {
      headerFormat: '',
      pointFormat: '{point.name}: <b>{point.y}</b> visits<br>Size: <b>{point.z}</b>'
  },
  series: [{
      minPointSize: 10,
      innerSize: '10%',
      zMin: 0,
      name: 'countries',
      borderRadius: 5,
      data: [{
          name: 'Spain', // 이름
          y: 505992, //  수치값
          z: 95 // 크기값
      }, {
          name: 'France',
          y: 551695,
          z: 118
      }, {
          name: 'Poland',
          y: 312679,
          z: 131
      }, {
          name: 'Czech Republic',
          y: 78865,
          z: 136
      }, {
          name: 'Italy',
          y: 301336,
          z: 198
      }, {
          name: 'Switzerland',
          y: 41284,
          z: 224
      }, {
          name: 'Germany',
          y: 357114,
          z: 238
      }],
      colors: [
          '#4caefe',
          '#3dc3e8',
          '#2dd9db',
          '#1feeaf',
          '#0ff3a0',
          '#00e887',
          '#23e274'
      ]
  }]
})

const toggleShowData = () => {
  showData.value = !showData.value;
  if (showData.value) {
    toggleShowDataText.value = "접기";
  } else {
    toggleShowDataText.value = "펼치기";
  }
};

const onClickDataName = (item) => {
  router.push({ query: { guid: item.meta_id } }).then(() => {
    // 페이지를 강제로 새로고침
    userStore.setGuid(item.meta_id);
    emit("onClickData", item.meta_id);
    window.scrollTo(0, 0);
  });
}

const drawChart = (raw) => {
  const chart = pieChart.value?.chart
  if (!chart) return

  // 1️⃣ 기존 시리즈 제거
  deleteSeries(chart)

  // 2️⃣ variablepie용 데이터 생성
  const data = createSeries(raw)

  // 3️⃣ 새 시리즈 추가
  chart.addSeries({
    type: 'variablepie',
    name: 'Similarity',
    minPointSize: 10,
    innerSize: '20%',
    zMin: 0,
    borderRadius: 5,
    dataLabels: {
      enabled: false,
      format: '{point.name}', // description 표시
      style: { fontSize: '12px' }
    },
    tooltip: {
      headerFormat: '',
      pointFormat: `
        <b>{point.name}</b><br/>
        {point.meta_name}
      `
    },
    data
  }, true) // redraw = true
}

const createSeries = (raw) => {
  console.log(raw)
  return raw.map(row => {
    // similarity가 없거나 null/NaN이면 기본값 0.1로 설정
    const similarity = (typeof row.similarity === 'number' && !isNaN(row.similarity))
      ? row.similarity
      : 0.1

    return {
      name: row.description || 'Unknown',
      y: similarity * 100,   // 각도 비율
      z: similarity,         // 크기 반영
      meta_name: row.meta_name || 'N/A'
    }
  })
}



const deleteSeries = (chart) => {
  while (chart.series.length > 0) {
    chart.series[0].remove(false)  // false = redraw 안 함 (성능 최적화)
  }
}

</script>
