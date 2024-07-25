<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">분류</h3>
    </div>
  </div>
  <div class="section-contents p-0 bg-white">
    <div class="l-split">
      <classification-list :isLoaded="isLoaded"></classification-list>
      <classification-list-modify></classification-list-modify>
    </div>
  </div>
</template>

<script setup lang="ts">
import ClassificationList from "@/components/classification/classification-list/classificationList.vue";
import ClassificationListModify from "@/components/classification/classification-list/classificationList-modify.vue";
import {classificationStore} from "@/store/classification";
import {storeToRefs} from "pinia";
import {onMounted, ref} from "vue";

const useClassificationStore = classificationStore();
const { classificationList } = storeToRefs(useClassificationStore);
const { getClassificationList } = useClassificationStore;
const isLoaded = ref(false);

onMounted(async () => {
  await getClassificationList(); // API 호출
  isLoaded.value = true; // 데이터 로딩 완료
});
</script>

<style lang="scss" scoped></style>
