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
        @change="openAddModal"
        @delete="deleteDataModel"
      ></selected-model>
      <excute-quary></excute-quary>
    </div>
    <div class="l-split">
      <sample></sample>
      <result></result>
    </div>
  </div>
  <save-model v-if="isShowSaveModel" @change="saveDataModel"></save-model>
  <add-model :modal-id="$constants.DATAMODEL_CREATION.ADD.MODAL_ID"></add-model>
</template>

<script setup lang="ts">
import selectedModel from "@/components/datamodel-creation/selected-model.vue";
import excuteQuary from "@/components/datamodel-creation/excute-quary.vue";
import sample from "@/components/datamodel-creation/sample.vue";
import result from "@/components/datamodel-creation/result.vue";
import addModel from "@/components/datamodel-creation/modal/add.vue";
import saveModel from "@/components/datamodel-creation/modal/save.vue";
import { useNuxtApp } from "nuxt/app";
import $constants from "~/utils/constant";

const { $vfm } = useNuxtApp();
const isShowSaveModel = ref(false);

const saveDataModel = (param: boolean) => {
  isShowSaveModel.value = param;
};

const openAddModal = () => {
  $vfm.open($constants.DATAMODEL_CREATION.ADD.MODAL_ID);
};
</script>

<style scoped></style>
