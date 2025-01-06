<template>
  <div class="mu-col mu-col-4">
    <div class="mu-heading">
      <h4>최근 본 데이터</h4>
    </div>
    <ul class="basic-list">
      <li v-for="item in dataList">
        <a v-on:click="onClickDataName(item)">{{ item.description ? item.description : item.name }}</a>
        <span class="date" @click="onClickDataName(item)">{{
          formatDate(item.search_date)
        }}</span>
      </li>
      <div v-if="dataList.length == 0">
        <p class="nodata">조회 데이터가 없습니다.</p>
      </div>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "nuxt/app";
import { computed, ComputedRef } from "vue";

const router = useRouter();
const props = defineProps(["dataList_s"]);

interface MySearchData {
  description: string;
  guid: string;
  meta_name: string;
  qualified_name: string;
  search_date: string;
  search_meta_id: number;
  type_name: string;
  user_id: string;
}

const dataList: ComputedRef<Array<MySearchData>> = computed(() => (props.dataList_s ? props.dataList_s : []));

const formatDate = (value: string | number | Date): string => {
  const date = new Date(value);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");

  // return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  return `${year}-${month}-${day}`;
};

const onClickDataName = (item: MySearchData): void => {
  if (item.guid) {
    router.push(`/search/detail?guid=${item.guid}`);
  }
};
</script>

<style scoped></style>
