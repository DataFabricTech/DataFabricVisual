<template>
        <div class="contents">
          <div>
            <div class="inner">
              <div class="board-list-wrap">
                <div class="mu-heading">
                  <!-- board-list-search -->
                  <Border-list-search-review :searchObj="searchObj" @searchMethod="getPost" :searchList="searchList"/>

                  <div class="mu-item-group">
                    <!-- mu-selectbox / 10개씩 보기(Default) -->
                    <page-size
                      v-model:pageSize="pagingObj.limit"
                      @searchMethod="getPost"
                    />
                  </div>
                </div>
                <ul class="data-box-list">
                  <li class="data-box" v-for="(item, index) in postList" :key="index">
                    <div class="inner-data no">{{ item.rownum }}</div>
                    <div class="inner-data">
                      <div class="title-group">
                        <a @click="onClickDataName(item)">
                          <strong class="data-title">{{ item.description ? `${item.description} (${item.meta_name})` : item.meta_name }}</strong>
                          <p class="data-comment">{{ item.review }}</p>
                        </a>
                      </div>
                      <div class="data-info">
                        <dl>
                          <dt class="blind">리뷰 등록일</dt>
                          <dd>{{ formatDate(item.review_date) }}</dd>
                        </dl>
                      </div>
                    </div>
                  </li>
                  <li v-if="postList.length === 0">
                    <p class="nodata">리뷰 데이터가 존재하지 않습니다.</p>
                  </li>
                </ul>
                <!-- mu-pagination -->
                <pagination
                  :paging="pagingObj"
                  @onClickPage="onClickPage"
                />
                <!-- //mu-pagination -->
              </div>
            </div>
          </div>
        </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from 'vue-router';
import PageSize from "@/pages/admin/manager/system/user/page-size";
import Pagination from "@functional/pagination/pagination";
// import DatePicker from "@functional/datepicker/date-picker";
import BorderListSearchReview from "@functional/border-list-search/border-list-search-review.vue";

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
  name: string;
  description: string;
  review: string;
  user_nm: string;
  review_date: string;
  guid: string;
}

const { $ofetch } = useNuxtApp();
const router = useRouter()

const pagingObj = ref<PagingObj>({
  total: 0,
  limit: 10,
  block: 10,
  currentPage: 1,
});
const postList = ref<Post[]>([]);
const searchWord = ref("");
const searchType = ref("all");
const checkedList = ref([]); // [데이터 정의 : Array]
const allChecked = ref(false);

const searchObj = ref<SearchObj>({
  searchType: "all", // 검색 조건 유형
  searchWord: "",    // 검색어
});
const searchList = ref([
  {
    value: "name",
    text: "데이터명",
  },
  {
    value: "review",
    text: "리뷰내용",
  },
]);

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
    offset: offset,
    limit: pagingObj.value.limit,
    searchWord: searchObj.value.searchWord,
    searchType: searchObj.value.searchType,
  };

  try {
    const result = await postApiData("/api/myCatalog/myReviewDataList", param);
    if (result.list) {
      pagingObj.value = {
        ...pagingObj.value,
        total: result.total,
        currentPage: offset,
      };
      postList.value = result.list;
    }
  } catch (error) {
    console.error('Error fetching posts:', error);
  }
};

const onClickPage = (page: number) => {
  getPost(page);
};

const onClickDataName = async (item: { meta_id: string; }) => {
  await router.push({
    path: "/search/detail",
    query: { guid: item.meta_id }
  });
}

const formatDate = (value: string | number | Date): string => {
  const date = new Date(value);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

onMounted(() => {
  getPost();
});
</script>
