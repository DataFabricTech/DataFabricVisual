<template>
  <div class="mu-col mu-col-4">
    <div class="mu-heading">
      <h4>리뷰 데이터</h4>
      <div class="mu-item-group">
        <button type="button" title="더보기" class="mu-btn mu-btn-round mu-btn-icon-only"
          @click="goToReviewData()"><i class="mu-icon-svg add"></i>
        </button>
      </div>
    </div>
    <div class="mydata-count-box">
      <dl class="data-count">
        <dt>전체</dt>
        <dd>
          <strong>{{ total }}</strong
          >
        </dd>
      </dl>
    </div>
    <!-- 전체 리뷰/추천 데이터 개수 출력 -->
    <ul class="basic-list">
      <li v-for="(item, index) in dataList" :key="index">
        <a :title="item.description ? item.description : item.meta_name" v-on:click="onClickDataName(item)">{{ item.description ? item.description : item.meta_name }}</a>
        <span class="date" @click="onClickDataName(item)">{{ $yyyyMMddHHmmSS(item.review_date) }}</span>
      </li>
      <div v-if="dataList.length == 0">
        <p class="nodata">리뷰 데이터가 없습니다.</p>
      </div>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "nuxt/app";
import { computed, ComputedRef } from 'vue';

const router = useRouter();
const props = defineProps(["dataList_s"]);

interface MyReviewData {
  meta_id: string;
  meta_name: string;
  qualified_name: string;
  review: string;
  review_date: string;
  review_id: number;
  rownum: number;
  meta_type: string;
  user_id: string;
  user_name: string;
}

const dataList: ComputedRef<Array<MyReviewData>> = computed(() => (props.dataList_s.list ? props.dataList_s.list : []));
const total: ComputedRef<number> = computed(() => (props.dataList_s.total ? props.dataList_s.total : 0));

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

const goToReviewData = () => {
  router.push('/my/review-data')
}

const onClickDataName = (item: any) => {
  if (item.meta_id) {
    router.push(`/search/detail?guid=${item.meta_id}`);
  }
};
</script>

<style scoped></style>
