<template>
  <div class="section-contents bg-white">
    <detail-default />
    <resource-box
      class="resource-box-full"
      :data-obj="dataModel"
      :use-fir-model-nm="true"
      :use-data-nm-link="false"
      :show-owner="true"
      :show-category="true"
      :editable="true"
      :user-list="userList"
      owner-key="id"
      :category-key="FILTER_KEYS.CATEGORY"
      @editIconClick="editIconClick"
      @editDone="editDone"
    />
    <Tab
      class="mt-8"
      :data="filteredTabs"
      :label-key="'label'"
      :value-key="'value'"
      :current-item="initTabValue"
      current-item-type="value"
      @change="changeTab"
    >
      <template #[currentTab]>
        <component :is="currentComponent" />
      </template>
    </Tab>
  </div>
</template>

<script setup lang="ts">
import _ from "lodash";

import { ref, shallowRef } from "vue";
import { storeToRefs } from "pinia";

import { useDataModelDetailStore } from "@/store/search/detail/index";

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
import { useLineageStore } from "@/store/lineage/lineageStore";

import { FILTER_KEYS } from "@/store/search/common";

const route = useRoute();

const dataModelDetailStore = useDataModelDetailStore();
const lineageStore = useLineageStore();

const { userList, dataModel } = storeToRefs(dataModelDetailStore);

const {
  getDataModelFqn,
  getDataModelType,
  setDataModelId,
  setDataModelFqn,
  setDataModelType,
  getUserFilter,
  getDataModel,
  getDefaultInfo,
  getSchema,
  getSampleData,
  getProfile,
  getQuery,
  changeDataModel,
} = dataModelDetailStore;

const { getLineageData } = lineageStore;

// tabfilter 객체 정의 (예시로 tables 값을 포함)
const tabfilter = ref({ tables: true });

// computed 속성으로 filteredTabs 정의
const filteredTabs = computed(() => {
  if (tabfilter.value.tables) {
    // tables가 true이면 모든 탭 옵션을 반환
    return tabOptions;
  } else {
    // tables가 false이면 기본정보, 데이터 리니지, Knowledge graph, 추천 데이터 모델 탭만 반환
    const includedValues = ["default", 6, 7, 8];
    return tabOptions.filter((option) => includedValues.includes(option.value));
  }
});

const tabOptions = [
  { label: "기본정보", value: "default", component: DefaultInfo },
  { label: "스키마", value: "schema", component: Schema },
  { label: "샘플데이터", value: "sample", component: Sample },
  { label: "프로파일링", value: "profile", component: Profiling },
  { label: "쿼리", value: "query", component: Query },
  { label: "데이터 리니지", value: "lineage", component: Lineage },
  { label: "Knowledge graph", value: 7, component: KnowledgeGraph },
  { label: "추천 데이터 모델", value: 8, component: RecommendModel },
];

const initTabValue: Ref<any> = ref("default");
const currentTab: Ref<any> = ref("default");
const currentComponent: Component = shallowRef(DefaultInfo);

async function changeTab(tab: number | string) {
  currentTab.value = tab;
  switch (tab) {
    case "default":
      await getDefaultInfo();
      break;
    case "schema":
      await getSchema();
    case "sample":
      await getSampleData();
      break;
    case "profile":
      await getProfile();
      break;
    case "query":
      await getQuery();
      break;
    case "lineage":
      await getLineageData(getDataModelType(), getDataModelFqn());
      break;
  }
  currentComponent.value = _.find(tabOptions, ["value", tab])?.component;
}

const editIconClick = (key: string) => {
  if (key === "category") {
    console.log("key = ", key);
  } else {
    getUserFilter();
  }
};

const editDone = (data: object) => {
  // TODO: [개발] 변경 데이터 저장한는 API 호출 필요
  changeDataModel(data)
    .then(() => {
      getDataModel();
    })
    .catch((error) => {
      console.error(error);
    });
};

setDataModelId(route.query.id);
setDataModelFqn(route.query.fqn);
setDataModelType(route.query.type);
await getDataModel();
await getDefaultInfo();
</script>

<style lang="scss" scoped></style>
