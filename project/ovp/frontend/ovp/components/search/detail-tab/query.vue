<template>
  <div v-if="queryList.length !== 0" class="data-detail">
    <div id="dataList" class="data-list">
      <div class="query" v-for="(query, index) in queryList" :key="index">
        <div class="query-date">{{ query.queryDate }}</div>
        <div class="code-box">{{ query.query }}</div>
      </div>
      <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
      <Loading
        id="loader"
        :use-loader-overlay="true"
        class="loader-lg is-loader-inner"
        style="display: none"
      ></Loading>
    </div>
  </div>
  <div v-else class="no-result">
    <div class="notification">
      <svg-icon class="notification-icon" name="info"></svg-icon>
      <p class="notification-detail">작성된 내용이 없습니다.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import Loading from "@base/loading/Loading.vue";
import { useIntersectionObserver } from "~/composables/intersectionObserverHelper";
import { useDataModelDetailStore } from "@/store/search/detail/index";

const dataModelDetailStore = useDataModelDetailStore();

const { queryList } = storeToRefs(dataModelDetailStore);
const { addQuery } = dataModelDetailStore;

onMounted(() => {
  setScrollOptions(0);
});

const { scrollTrigger, setScrollOptions } = useIntersectionObserver({
  callback: addQuery,
});
</script>

<style lang="scss" scoped></style>
