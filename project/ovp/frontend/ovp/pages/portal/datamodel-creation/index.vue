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
  <splitter
    class="section-contents p-0 bg-white"
    v-model="horizontalSplitter"
    :limits="[30, 70]"
    unit="%"
    horizontal
  >
    <template #before>
      <splitter v-model="verticalSplitter" :limits="[14, 70]" unit="%">
        <template #before>
          <selected-model
            :modelList="selectedModelList"
            :dataModelFilter="selectedDataModelFilters"
            :modelListCnt="selectedModelListCnt"
            @change="addModelInstance.open()"
            @delete="deleteDataModel"
            @item-click="onClickDataModelItem"
            @bookmark-change="updateMainSelectedModelBookmark"
          ></selected-model>
        </template>
        <template #after>
          <execute-query></execute-query>
        </template>
      </splitter>
    </template>
    <template #after>
      <bottom-tab></bottom-tab>
    </template>
  </splitter>
</template>

<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useModal } from "vue-final-modal";

import Splitter from "@extends/splitter/Splitter.vue";
import selectedModel from "@/components/datamodel-creation/selected-model.vue";
import executeQuery from "@/components/datamodel-creation/execute-query.vue";
import addModel from "@/components/datamodel-creation/modal/add.vue";
import saveModel from "@/components/datamodel-creation/modal/save.vue";
import { useCreationStore } from "@/store/datamodel-creation";
import { useDataModelSearchStore } from "@/store/datamodel-creation/search";
import { useDataModelSaveStore } from "@/store/datamodel-creation/save";
import { ref } from "vue";

const horizontalSplitter = ref(50); // 상단과 하단을 50%씩 분할
const verticalSplitter = ref(20); // 상단을 좌우 50%씩 분할

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
const { deleteDataModel, onClickDataModelItem, resetQuery, showProfile } =
  creationStore;

// 탐색 > 데이터 모델 조회 Store
const dataModelSearchStore = useDataModelSearchStore();
const { selectedModelList, selectedModelListCnt, selectedDataModelFilters } =
  storeToRefs(dataModelSearchStore);
const { updateMainSelectedModelBookmark, getFilters } = dataModelSearchStore;

const dataModelSaveStore = useDataModelSaveStore();
const { getCategoryList, getTagList } = dataModelSaveStore;

onMounted(() => {
  // 데이터 초기화
  resetQuery();
  // 선택된 데이터 모델 초기화
  selectedModelList.value = [];
  // 선택한 데이터 모델 값 삭제
  isItemClicked.value = false;
});

await getCategoryList();
await getTagList();
// 필터내 항목 값 호출
await getFilters();
</script>
