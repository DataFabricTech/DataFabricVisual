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
      <selected-model @change="addDataModel"></selected-model>
      <excute-quary></excute-quary>
    </div>
    <div class="l-split">
      <sample></sample>
      <result></result>
    </div>
  </div>
  <save-model v-if="isShowSaveModel" @change="saveDataModel"></save-model>
  <add-model v-if="isShowAddModel" @change="addDataModel"></add-model>
</template>

<script setup lang="ts">
import selectedModel from "@/components/datamodel-creation/selected-model.vue";
import excuteQuary from "@/components/datamodel-creation/excute-quary.vue";
import sample from "@/components/datamodel-creation/sample.vue";
import result from "@/components/datamodel-creation/result.vue";
import addModel from "@/components/datamodel-creation/modal/add.vue";
import saveModel from "@/components/datamodel-creation/modal/save.vue";

const isShowSaveModel = ref(false);
const isShowAddModel = ref(false);

const saveDataModel = (param: boolean) => {
  isShowSaveModel.value = param;
};

const addDataModel = (param: boolean) => {
  isShowAddModel.value = param;
};
</script>

<style scoped></style>
