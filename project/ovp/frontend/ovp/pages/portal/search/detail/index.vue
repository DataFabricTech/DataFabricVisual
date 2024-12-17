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
      :user="user"
      :category-list="categoryList"
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
import { useNuxtApp } from "nuxt/app";
import _ from "lodash";

import { type Ref, ref, shallowRef } from "vue";
import { storeToRefs } from "pinia";
import { useRouter } from "vue-router";
const { $alert } = useNuxtApp();

import { useDataModelDetailStore } from "@/store/search/detail/index";
import { useLineageStore } from "@/store/search/detail/lineage/index";
import { useSearchCommonStore } from "@/store/search/common";
import { useUserStore } from "@/store/user/userStore";

import Tab from "@extends/tab/Tab.vue";
import ResourceBox from "@/components/common/resource-box/resource-box.vue";
import DetailDefault from "@/components/search/detail-default.vue";
import DefaultInfo from "@/components/search/detail-tab/default-info.vue";
import Schema from "@/components/search/detail-tab/schema.vue";
import Sample from "@/components/search/detail-tab/sample.vue";
import Profiling from "@/components/search/detail-tab/profiling.vue";
import Lineage from "@/components/search/detail-tab/lineage.vue";
import KnowledgeGraph from "@/components/search/detail-tab/knowledge-graph.vue";
import Query from "@/components/search/detail-tab/query.vue";
import RecommendModel from "@/components/search/detail-tab/recommend-model.vue";

import { FILTER_KEYS } from "@/store/search/common";

const route = useRoute();
const router = useRouter();

const dataModelDetailStore = useDataModelDetailStore();
const lineageStore = useLineageStore();
const searchCommonStore = useSearchCommonStore();
const userStore = useUserStore();

const {
  dataModelType,
  userList,
  categoryList,
  dataModel,
  defaultInfo,
  sampleList,
} = storeToRefs(dataModelDetailStore);

const {
  getUserList,
  getCategoryList,
  getCategoryAllList,
  getDataModelFqn,
  setDataModelId,
  setDataModelFqn,
  setDataModelType,
  setContainerMetaInfo,
  setExceptExtSampleData,
  getDataModel,
  getDefaultInfo,
  getSchema,
  getSampleData,
  getProfile,
  getQuery,
  changeDataModel,
  getRecommendDataModels,
} = dataModelDetailStore;

const { getLineageData } = lineageStore;

const { getFilters } = searchCommonStore;

const { user } = storeToRefs(userStore);

// computed 속성으로 filteredTabs 정의
const filteredTabs = computed(() => {
  const exceptExtList = ["hwp", "hwpx", "doc", "docx"];
  let includedValues = [];

  if (route.query.type !== "storage") {
    // tables가 true이면 모든 탭 옵션을 반환
    return tabOptions;
  } else {
    if (_.includes(exceptExtList, defaultInfo.value.modelInfo.model.ext)) {
      includedValues = [
        "default",
        "sample",
        "lineage",
        "knowledge",
        "recommended",
      ];
    } else {
      return tabOptions;
    }
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
  {
    label: "연관데이터모델 시각화",
    value: "knowledge",
    component: KnowledgeGraph,
  },
  {
    label: "추천 데이터 모델",
    value: "recommended",
    component: RecommendModel,
  },
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
      break;
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
      await getLineageData(dataModelType.value, getDataModelFqn());
      // await getFilters();
      break;
    case "knowledge":
      // $alert("개발중입니다.", "info");
      break;
    case "recommended":
      const url = await checkSampleDataValid();
      await getRecommendDataModels(url);
      break;
  }
  currentComponent.value = _.find(tabOptions, ["value", tab])?.component;
}

const checkSampleDataValid = async () => {
  await getSampleData();
  return sampleList.value.length > 0 ? "embedding" : "clustering";
};

const editIconClick = (key: string) => {
  if (key === "category") {
    getCategoryList();
    getCategoryAllList();
  } else {
    getUserList();
  }
};

const editDone = (data: any) => {
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
setContainerMetaInfo([]);
setExceptExtSampleData("");
getDataModel().then(async (data) => {
  if (data.result === 0) {
    // TODO: 에러페이지 추가
    // router.push("/portal/error");
  }

  await getDefaultInfo();
});
</script>

<style lang="scss" scoped></style>
