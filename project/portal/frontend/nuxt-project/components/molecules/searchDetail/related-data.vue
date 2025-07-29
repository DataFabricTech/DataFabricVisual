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
      <div class="detail-box" v-if="classifications && classifications.length > 0">
        <div class="mu-row">
          <div class="mu-col mu-col-7">
            <ul class="connection-list">
              <li v-for="(item, index) in classifications" :key="index">
                <div class="datatype">
                  <em class="datatype-label">{{ item.classifications[0] ? item.classifications[0].c_description : '-' }}</em>
                </div>
                <a v-on:click="onClickDataName(item)">
                  <span class="datatype-desc" v-if="item.description">{{ item.description }} &nbsp;&nbsp;&nbsp;</span>
                  <span>{{ item.meta_name }}</span>
                </a>
              </li>
            </ul>
          </div>
          <div class="mu-col mu-col-5">
            <highcharts :constructorType="'chart'" :options="networkChartOptions" ref="networkChart"/>
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
import { useRouter } from "vue-router";
import { useUserStore } from "~/store/user";

const userStore = useUserStore();
const router = useRouter();

const emit = defineEmits(["onClickData"]);
const props = defineProps(["classifications", "entity", "isAdmin"]);

const showData = ref(true);
const toggleShowDataText = ref("접기");

watch(() => props.classifications, (newVal) => {
  const nodes = [];
  const links = [];

  const rootId = props.entity?.meta_id || "root_id";
  const rootName = props.entity?.meta_name || "root_name";
  const rootDesc = props.entity?.description || "Root";

  // 루트 노드
  nodes[rootId] = {
    id: rootId,
    name: rootDesc,
    meta_name: rootName,
    color: '#f9eae5',
    marker: { radius: 40 },
  }

  const raw = newVal || []

  raw.forEach(row => {
    const toId = row.meta_id
    const toName = row.meta_name
    const toDesc = row.description

    // 노드가 없으면 생성
    if (!nodes[toId]) {
      nodes[toId] = {
        id: toId,
        name: toDesc,
        meta_name: toName,
        color: '#e8f1f1',
        marker: { radius: 24 }
      }
    }

    // 부모-자식 링크 만들기
    links.push({ from: rootId, to: toId })

  });

  // 노드 배열화
  const nodeArray = Object.values(nodes);
  networkChartOptions.value.series[0].data = links;
  networkChartOptions.value.series[0].nodes = nodeArray;
})

// 네트워크 차트 설정
const networkChartOptions = ref({
  chart: {
    type: 'networkgraph',
    height: '300px',
    width: "500",
    backgroundColor: '#fafafa',
    borderWidth: 1,
    borderColor: '#ddd',},
  title: { text: '' },
  plotOptions: {
    networkgraph: {
      keys: ['from', 'to'],          // 4. data는 [[from,to], ...]
      layoutAlgorithm: {
        enableSimulation: true,
        linkLength: 50,
        friction: -0.9
      },
      dataLabels: {
        enabled: true,          // 라벨 표시
        linkFormat: '',         // 링크 라벨 숨김
        style: {
          fontSize: '0.8em',
          fontWeight: 'normal',
          textOutline: '2px contrast'
        },
        formatter() { return this.point.name || this.point.id }
      }
    }
  },
  tooltip: {
    useHTML: true,         // 원하는 HTML 구성 가능
    backgroundColor: 'rgba(255,255,255,0.9)',
    borderRadius: 4,
    shadow: false,
    // 🔍 노드·링크 구분
    formatter() {
      // 링크(간선)일 때: this.from / this.to 존재
      if (this.from && this.to) {
        return `<b>${this.from}</b> → <b>${this.to}</b>`
      }
      // 노드일 때: this.point
      const p = this.point
      return `
        <b>${p.options.name}</b><br/>
        <span>${p.options.meta_name}</span>
      `
    },
    style: {
      fontSize: '12px',
      fontFamily: 'NanumSquare',
      textOutline: 'none',   // 글자 테두리 제거
      color: '#222'
    }
  },
  series: [{
    accessibility: {
      enabled: false
    },
    dataLabels: {
      enabled: true,
      linkFormat: '',
      style: {
        fontSize: '14px',
        fontWeight: 'normal',
      }
    },
    id: 'lang-tree',
    data: [],
    nodes: []
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

</script>
