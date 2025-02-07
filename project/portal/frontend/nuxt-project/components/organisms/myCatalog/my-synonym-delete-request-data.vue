<template>
  <div class="mu-col mu-col-4">
    <div class="mu-heading">
      <h4>동의어 삭제 요청 내역</h4>
      <div class="mu-item-group">
        <button type="button" title="더보기" class="mu-btn mu-btn-round mu-btn-icon-only"
          @click="goToDeleteSynonymRequest()"><i class="mu-icon-svg add"></i>
        </button>
      </div>
    </div>
    <ul class="basic-list" style="text-align:center;">
      <li v-for="item in dataList">
        <div class="synonym-keyword tags-group" v-on:click="onClickDataName(item)">
          <div class="tag"><span class="tag-label">{{ item.term }}</span></div>
        </div>
        <span class="date" @click="onClickDataName(item)">{{ $yyyyMMddHHmmSS(item.req_date) }}</span>
      </li>
      <div v-if="dataList.length == 0">
        <p class="nodata">동의어 삭제 요청 내역이 없습니다.</p>
      </div>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "nuxt/app";
import { computed, ComputedRef } from "vue";

const router = useRouter();
const props = defineProps(["dataList_s"]);

interface MySynonymDeleteRequestData {
  id: number;
  term: string;
  type: number;
  request: string;
  response: string;
  status: number;
  req_date: string;
  req_user: string;
  res_date: string;
  res_user: string;
}

const dataList: ComputedRef<Array<MySynonymDeleteRequestData>> = computed(() => (props.dataList_s ? props.dataList_s : []));

const onClickDataName = (item: any) => {
  if (item.guid) {
    router.push("/search/detail?guid=" + item.guid);
  }
};

const goToDeleteSynonymRequest = () => {
  router.push('/my/request-delete-synonym')
}
</script>
