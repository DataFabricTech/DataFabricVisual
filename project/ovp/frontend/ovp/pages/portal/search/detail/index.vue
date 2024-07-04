<template>
  <resource-box
    :data-obj="resourceBoxObj"
    class="resource-box-full"
    :use-fir-model-nm="true"
    :show-owner="true"
    :show-category="true"
    :use-data-nm-link="false"
    :editable="true"
  />
  <detail-default />
  <Tab
    :data="filteredTabs"
    :label-key="'label'"
    :value-key="'value'"
    :current-item="currentTab"
    current-item-type="value"
    @change="changeTab"
  />
  <!-- 동적 컴포넌트 사용 -->
  <component :is="currentComponent" />
</template>

<script setup lang="ts">
import _ from "lodash";
import { ref, shallowRef } from "vue";
import Tab from "@extends/tab/Tab.vue";
import ResourceBox from "@/components/common/resource-box/resource-box.vue";
import DetailDefault from "@/components/search/detail-default.vue";
import DefaultInfo from "@/components/search/detail-tab/default-info.vue";
import Schema from "@/components/search/detail-tab/schema.vue";
import Sample from "@/components/search/detail-tab/sample.vue";
import Profiling from "@/components/search/detail-tab/profiling.vue";
import Query from "@/components/search/detail-tab/query.vue";
import Lineage from "@/components/search/detail-tab/lineage.vue";
import KnowledgeGraph from "@/components/search/detail-tab/knowledge-graph.vue";
import RecommendModel from "@/components/search/detail-tab/recommend-model.vue";

// tabfilter 객체 정의 (예시로 tables 값을 포함)
const tabfilter = ref({ tables: true });

// computed 속성으로 filteredTabs 정의
const filteredTabs = computed(() => {
  if (tabfilter.value.tables) {
    // tables가 true이면 모든 탭 옵션을 반환
    return tabOptions;
  } else {
    // tables가 false이면 기본정보, 데이터 리니지, Knowledge graph, 추천 데이터 모델 탭만 반환
    const includedValues = [1, 6, 7, 8];
    return tabOptions.filter((option) => includedValues.includes(option.value));
  }
});

const tabOptions = [
  { label: "기본정보", value: 1, component: DefaultInfo },
  { label: "스키마", value: 2, component: Schema },
  { label: "샘플데이터", value: 3, component: Sample },
  { label: "프로파일링", value: 4, component: Profiling },
  { label: "쿼리", value: 5, component: Query },
  { label: "데이터 리니지", value: 6, component: Lineage },
  { label: "Knowledge graph", value: 7, component: KnowledgeGraph },
  { label: "추천 데이터 모델", value: 8, component: RecommendModel },
];

const currentTab = ref(1);
const currentComponent: Component = shallowRef(DefaultInfo);

let resourceBoxObj: any = {
  id: "1",
  serviceIcon: "http://www.mobigen.com/media/img/common/mobigen_logo.svg",
  depth: ["1depth", "2depth", "3depth", "데이터모델"],
  firModelNm: "최초 데이터모델 명",
  modelNm: "Model Name",
  modelDesc:
    "데이터 모델 설명에 대한 영역입니다. 데이터 모델 설명에 대한 영역입니다. 데이터 모델 설명에 대한 영역입니다. ",
  owner: "장소라",
  category: "카테고리",
};

function changeTab(item: number | string) {
  currentComponent.value = _.find(tabOptions, ["value", item])?.component;
}
</script>

<style lang="scss" scoped></style>
