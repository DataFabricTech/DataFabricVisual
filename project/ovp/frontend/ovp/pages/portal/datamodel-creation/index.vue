<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h4 class="title">데이터 모델 생성</h4>
      <button class="button button-primary w-20" @click="saveDataModel(true)">
        저장
      </button>
    </div>
  </div>
  <div class="section-contents p-0 bg-white">
    <div class="l-split">
      <selected-model
        :modelList="modelList"
        :dataModelFilter="dataModelFilter"
        :modelListCnt="modelListCnt"
        @change="addDataModel"
        @delete="deleteDataModel"
        @item-click="onClickDataModelItem"
        @bookmark-change="changeBookmark"
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
      ></sample>
      <result
        :querySuccess="querySuccess"
        :executeResult="executeResult"
        :isFirstExecute="isFirstExecute"
        :executeResultErrMsg="executeResultErrMsg"
      ></result>
    </div>
  </div>
  <save-model v-if="isShowSaveModel" @change="saveDataModel"></save-model>
  <add-model
    v-if="isShowAddModel"
    :data-model-list="modelList"
    :selected-model-list="selectedModelList"
    :my-model-list="myModelList"
    :filter="dataModelFilter"
    @change="addDataModel"
  ></add-model>
</template>

<script setup lang="ts">
import selectedModel from "@/components/datamodel-creation/selected-model.vue";
import executeQuery from "@/components/datamodel-creation/execute-query.vue";
import sample from "@/components/datamodel-creation/sample.vue";
import result from "@/components/datamodel-creation/result.vue";
import addModel from "@/components/datamodel-creation/modal/add.vue";
import saveModel from "@/components/datamodel-creation/modal/save.vue";
import { useCreationStore } from "~/store/datamodel-creation/index";

const isShowSaveModel = ref(false);
const isShowAddModel = ref(false);

const saveDataModel = (param: boolean) => {
  isShowSaveModel.value = param;
};

const addDataModel = (param: boolean) => {
  isShowAddModel.value = param;
};

const creationStore = useCreationStore();
const {
  modelList,
  modelListCnt,
  dataModelFilter,
  selectedModelList,
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
  myModelList
} = storeToRefs(creationStore);
const {
  setDataModelFilter,
  setDataModelList,
  deleteDataModel,
  changeBookmark,
  onClickDataModelItem,
  runQuery,
  resetQuery,
  editQueryText,
  setSelectedModelList,
  setMyModelList,
} = creationStore;

// 데이터 목록, 필터 목록, 선택 필터 초기화
setDataModelFilter();
setDataModelList();
setMyModelList();
setSelectedModelList();
</script>

<style scoped></style>
