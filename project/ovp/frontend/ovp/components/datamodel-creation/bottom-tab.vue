<template>
  <div class="w-full h-full">
    <tab
      class="tab-line"
      :data="tabOptions"
      label-key="label"
      value-key="value"
      current-item-type="value"
      :current-item="initTabValue"
      :hided-list="hidedList"
      @change="onChangeTab"
    >
      <template #schema>
        <schema></schema>
      </template>
      <template #sample>
        <sample></sample>
      </template>
      <template #profile>
        <profiling></profiling>
      </template>
      <template #result>
        <result
          :querySuccess="querySuccess"
          :executeResult="executeResult"
          :isFirstExecute="isFirstExecute"
          :executeResultErrMsg="executeResultErrMsg"
        ></result>
      </template>
      <template #knowledge>
        <knowledge-graph></knowledge-graph>
      </template>
    </tab>
  </div>
</template>

<script setup lang="ts">
import { ref, type Ref, shallowRef, nextTick } from "vue";
import { storeToRefs } from "pinia";
import _ from "lodash";

import Tab from "@extends/tab/Tab.vue";
import Schema from "@/components/datamodel-creation/tab/schema.vue";
import Sample from "@/components/datamodel-creation/tab/sample.vue";
import Profiling from "@/components/datamodel-creation/tab/profiling.vue";
import Result from "~/components/datamodel-creation/result.vue";
import KnowledgeGraph from "@/components/datamodel-creation/tab/knowledge-graph.vue";

import { useCreationStore } from "@/store/datamodel-creation";
import { useDataModelDetailStore } from "@/store/search/detail/index";

const creationStore = useCreationStore();
const {
  isRunQuery,
  isFirstExecute,
  querySuccess,
  executeResult,
  executeResultErrMsg,
  selectedDataModel,
} = storeToRefs(creationStore);

const dataModelDetailStore = useDataModelDetailStore();
const {
  getDefaultInfo,
  getSchema,
  getSampleData,
  getProfile,
  setDataModelId,
  setDataModelFqn,
  setDataModelType,
  setExceptExtSampleData,
  initTabData,
} = dataModelDetailStore;

const initTabValue: Ref<any> = ref("schema");
const hidedList: Ref<string[] | number[]> = ref([]);

const tabOptions = [
  { label: "스키마", value: "schema" },
  { label: "샘플데이터", value: "sample" },
  { label: "프로파일링", value: "profile" },
  { label: "실행 결과", value: "result" },
  {
    label: "연관데이터모델 시각화",
    value: "knowledge",
    component: KnowledgeGraph,
  },
];
watchEffect(async () => {
  if (isRunQuery.value) {
    initTabValue.value = "";
    await nextTick();
    initTabValue.value = "result";
  }
});

watch(selectedDataModel, async (newVale) => {
  console.log(newVale);
  const dataModel = selectedDataModel.value;
  initTabValue.value = "";
  isRunQuery.value = false;
  await nextTick();

  if (!dataModel.id) {
    initTabData();
    return;
  }

  if (dataModel.type === "storage") {
    hidedList.value = ["schema", "profile"];
    initTabValue.value = "sample";
    onChangeTab("sample");
  } else {
    hidedList.value = [];
    initTabValue.value = "schema";
    onChangeTab("schema");
  }
});

async function onChangeTab(tab: number | string) {
  initTabData();
  isRunQuery.value = false;

  const dataModel = selectedDataModel.value;
  if (!dataModel.id) {
    return;
  }
  setDataModelId(dataModel.id);
  setDataModelFqn(dataModel.fqn);
  setDataModelType(dataModel.type);
  setExceptExtSampleData("");
  await getDefaultInfo();

  switch (tab) {
    case "schema":
      await getSchema();
      break;
    case "sample":
      await getSampleData();
      break;
    case "profile":
      await getProfile();
      break;
    case "result":
      console.log("result");
      break;
    case "knowledge":
      break;
  }
}
</script>

<style scoped></style>
