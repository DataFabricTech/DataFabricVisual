<template>
  <div class="mu-col mu-col-4">
    <div class="mu-heading">
      <h4>데이터 요청 내역</h4>
      <div class="mu-item-group">
        <button type="button" title="더보기" class="mu-btn mu-btn-round mu-btn-icon-only"
          @click="goToDataRequest()"><i class="mu-icon-svg add"></i>
        </button>
      </div>
    </div>
    <ul class="basic-list">
      <li v-for="item in dataList">
        <a @click="goToMyRequestDataDetail(item.id)">{{ item.title }}</a>
        <span class="date">{{ $yyyyMMddHHmmSS(item.create_date) }}</span>
      </li>
      <div v-if="dataList.length == 0">
        <p class="nodata">데이터 요청 내역이 없습니다.</p>
      </div>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { computed, ComputedRef, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter()

const props = defineProps(["dataList_s"]);

interface MyDataRequest {
  id: number
	writer: string
	request_type: string
	title: string
	data_name: string
	contents: string
	request_start?: string
	request_end?: string
	benefit_content: string
	create_date: string
	update_date: string
	process_status: number
	finish_date: string
}

const dataList: ComputedRef<Array<MyDataRequest>> = computed(() => (props.dataList_s.list ? props.dataList_s.list : []));
const total: ComputedRef<number> = computed(() => (props.dataList_s.total ? props.dataList_s.total : 0));

const formatDate = (value: string | number | Date): string => {
  const date = new Date(value);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");

  return `${year}-${month}-${day}`;
  // return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

const goToDataRequest = () => {
  router.push('/my/request-data')
}

const goToMyRequestDataDetail = (id) => {
  router.push(`/data/request/detail?id=${id}`)
}
</script>
