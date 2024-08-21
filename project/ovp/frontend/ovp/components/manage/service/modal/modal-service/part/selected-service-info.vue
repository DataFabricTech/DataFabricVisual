<template>
  <div class="service-form-summary">
    <label for="" class="form-label"> 선택된 서비스 타입 </label>
    <div class="form-detail">
      <img :src="selectedServiceObj.imgUrl" :alt="selectedServiceObj.label" />
      <p class="form-detail-text">({{ selectedServiceObj.label }})</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { ModalServiceProps } from "~/components/manage/service/modal/modal-service/ModalServiceProps";
import { ModalServiceComposition } from "~/components/manage/service/modal/modal-service/ModalServiceComposition";

const props = withDefaults(defineProps<ModalServiceProps>(), {});
const { selectedServiceObj } = ModalServiceComposition(props);
</script>

<style scoped></style>
