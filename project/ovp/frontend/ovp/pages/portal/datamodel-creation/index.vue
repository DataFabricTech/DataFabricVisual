<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h4 class="title">데이터 모델 생성</h4>
      <button
        class="button button-primary w-20"
        @click="saveModelInstance.open"
      >
        저장
      </button>
    </div>
  </div>
  <div class="section-contents p-0 bg-white">
    <div class="l-split">
      <selected-model
        :modelList="selectedModelList"
        :dataModelFilter="filters"
        :modelListCnt="selectedModelListCnt"
        @change="addModelInstance.open"
        @delete="deleteDataModel"
        @item-click="onClickDataModelItem"
        @bookmark-change="onClickBookmark"
      ></selected-model>
      <execute-query
        :query="query"
        @execute="runQuery"
        @reset="resetQuery"
        @edit="editQueryText"
      ></execute-query>
    </div>
    <div class="l-split">
      <sample
        :dataModelName="dataModelName"
        :dataModelOwner="dataModelOwner"
        :sampleDataList="sampleDataList"
        :isItemClicked="isItemClicked"
        :isColumnSelected="isColumnSelected"
        :dataProfileList="dataProfileList"
        :columnOptions="columnOptions"
        @profile-show="showProfile"
      ></sample>
      <result
        :querySuccess="querySuccess"
        :executeResult="executeResult"
        :isFirstExecute="isFirstExecute"
        :executeResultErrMsg="executeResultErrMsg"
      ></result>
    </div>
  </div>
</template>

<script setup lang="ts">
import selectedModel from "@/components/datamodel-creation/selected-model.vue";
import executeQuery from "@/components/datamodel-creation/execute-query.vue";
import sample from "@/components/datamodel-creation/sample.vue";
import result from "@/components/datamodel-creation/result.vue";
import addModel from "@/components/datamodel-creation/modal/add.vue";
import saveModel from "@/components/datamodel-creation/modal/save.vue";
import { useCreationStore } from "~/store/datamodel-creation/index";
import { useDataModelSearchStore } from "~/store/datamodel-creation/search";
import { storeToRefs } from "pinia";
import { useModal } from "vue-final-modal";

const isShowSaveModel = ref(false);

const addModelInstance = useModal({
  component: addModel,
  attrs: {
    onClose() {
      addModelInstance.close();
    },
  },
});

const saveModelInstance = useModal({
  component: saveModel,
  attrs: {
    onClose() {
      saveModelInstance.close();
    },
  },
});

const creationStore = useCreationStore();
const {
  query,
  isFirstExecute,
  querySuccess,
  executeResult,
  executeResultErrMsg,
  isItemClicked,
  isColumnSelected,
  dataModelName,
  dataModelOwner,
  sampleDataList,
  columnOptions,
  dataProfileList,
} = storeToRefs(creationStore);
const {
  deleteDataModel,
  onClickDataModelItem,
  runQuery,
  resetQuery,
  editQueryText,
  showProfile,
} = creationStore;

// 탐색 > 데이터 모델 조회 Store
const dataModelSearchStore = useDataModelSearchStore();
const { filters, selectedModelList, selectedModelListCnt } =
  storeToRefs(dataModelSearchStore);
const { onClickBookmark } = dataModelSearchStore;

onMounted(() => {
  alert("개발중 입니다.");
});
</script>

<style scoped></style>
