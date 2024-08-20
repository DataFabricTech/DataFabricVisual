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
import type { ServiceAddModalProps } from "~/components/admin/service/modal/service-add-modal/ServiceAddModalProps";
import { ServiceAddModalComposition } from "~/components/admin/service/modal/service-add-modal/ServiceAddModalComposition";

const props = withDefaults(defineProps<ServiceAddModalProps>(), {});
const { selectedServiceObj } = ServiceAddModalComposition(props);
</script>

<style scoped></style>
