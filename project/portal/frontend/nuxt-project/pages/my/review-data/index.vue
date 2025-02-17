<template>
  <div class="contents">
    <div>
      <div class="inner">
        <div class="board-list-wrap">
          <div class="mu-heading">
            <!-- board-list-search -->
            <Border-list-search :searchObj="searchObj" @searchMethod="getPost" :searchList="searchList" />
            <div class="mu-item-group">
              <!-- mu-selectbox / 10개씩 보기(Default) -->
              <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getPost" />
            </div>
          </div>
          <div class="board-list">
            <table class="mu-grid">
              <colgroup>
                <col style="width: 60px" />
                <col style="width: 250px" />
                <col style="width: 250px" />
                <col style="width: 150px" />
                <col style="width: 150px" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">번호</th>
                  <th scope="col">데이터 설명</th>
                  <th scope="col">데이터 명</th>
                  <th scope="col">시스템 명</th>
                  <th scope="col">찜한 날짜</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in postList" :key="index">
                  <td>{{ item.rownum }}</td>
                  <td class="tl">
                    <a @click="onClickDataName(item)">
                      {{ item.description ? item.description : '-' }}
                    </a>
                  </td>
                  <td>{{ item.meta_name ? item.meta_name : '-' }}</td>
                  <td>{{ item.system_name ? item.system_name : '-' }}</td>
                  <td>{{ formatDate(item.review_date)}}</td>
                </tr>
                <tr v-if="postList.length === 0">
                  <td colspan="5">
                    <p class="nodata">리뷰 데이터가 존재하지 않습니다.</p>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <!-- mu-pagination -->
          <pagination :paging="pagingObj" @onClickPage="onClickPage" />
          <!-- //mu-pagination -->
        </div>
      </div>
    </div>
    <!-- //layout -->
  </div>
</template>

<script setup lang="ts">
import {ref, onMounted, computed} from "vue";
import PageSize from "@/pages/admin/manager/system/user/page-size";
import Pagination from "@functional/pagination/pagination";
import DatePicker from "@functional/datepicker/date-picker";
import BorderListSearch from "@functional/border-list-search/border-list-search.vue";
import { useRouter, useRoute } from 'vue-router';

definePageMeta({
  name: "MyReviewData",
  layout: 'user',
})

interface PagingObj {
  total: number;
  limit: number;
  block: number;
  currentPage: number;
}
interface SearchObj {
  searchType: string;
  searchWord: string;
}
interface Post {
  rownum: number;
  meta_name: string;
  description: string;
  system_name: string;
  user_nm: string;
  review_date: string;
  guid: string;
}

const { $ofetch } = useNuxtApp();
const router = useRouter();
const route = useRoute();

const pagingObj = ref<PagingObj>({
  total: 0,
  limit: 10,
  block: 10,
  currentPage: 1,
});
const searchObj = ref<SearchObj>({
  searchType: "all",// 검색 조건 유형
  searchWord: "",   // 검색어
})
const searchList = ref([
  {
    value : "description",
    text : "데이터 설명"
  },
  {
    value : "meta_name",
    text : "데이터 명"
  },
  {
    value : "system_name",
    text : "시스템 명"
  },
])

const postList = ref<Post[]>([]);
const guid = computed(() => route.query.guid);

const formatDate = (dateStr: string): string => {
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  const hours = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');
  const seconds = date.getSeconds().toString().padStart(2, '0');

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  // return `${year}-${month}-${day}`;
};

const postApiData = async (urlStr: string, param: any) => {
  try {
    const response = await $ofetch(urlStr, {
      method: 'POST',
      body: JSON.stringify(param),
    });
    return response.data;
  } catch (error) {
    console.error('Error posting data:', error);
    throw error;
  }
};

const getPost = async (offset: number = 1) => {
  const param = {
    offset,
    limit: pagingObj.value.limit,
    searchType: searchObj.value.searchType,
    searchWord: searchObj.value.searchWord,
  };

  try {
    const result = await postApiData('/api/myCatalog/myReviewDataList', param);
    if (result.list) {

      pagingObj.value = {
        ...pagingObj.value,
        total: result.total,
        currentPage: offset
      }
      postList.value = result.list;
    }
  } catch (error) {
    console.error('Error fetching posts:', error);
  }
};

const onClickPage = (page: any) =>{
  getPost(page);
}

const onClickDataName = async (item: { meta_id: string; }) => {
  await router.push({
    path: "/search/detail",
    query: { guid: item.meta_id }
  });
}

onMounted(() => {
  getPost();
});
</script>
