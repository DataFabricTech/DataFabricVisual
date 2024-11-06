<template>
  <div class="contents">
    <div>
      <div class="inner">
        <div class="board-list-wrap">
          <div class="mu-heading">
            <!-- board-list-search -->
            <faq-border-list-search :searchObj.sync="searchObj" @searchMethod="getPost" />

            <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getPost" />
          </div>
          <!-- mu-accordion : 아코디언타입 FAQ리스트 타 질문 클릭 시, 확장된 본 답변 영역이 축소되며 선택된 질문에 대한 답변 영역이 확장됨 / 확장 가능한 답변 영역 총 1개 -->
          <ul class="mu-accordion">
            <li class="mu-accordion-item" v-for="(item, index) in postList" :key="index">
              <div
                v-bind:class="[item.showAnswer ? 'active' : '', 'mu-accordion-head']"
                @click="onClickShowAnswerButton(index)"
              >
                <!-- <a href="#"> -->
                  <div class="question">
                    <span class="ico-question">질문</span>
                    <em class="data-label">{{ item.category }}</em>
                    <p class="inner-cms-share" v-html="item.post_content"></p>
                  </div>
                <!-- </a> -->
              </div>
              <div v-if="item.showAnswer" class="mu-accordion-body">
                <div class="answer">
                  <span class="ico-answer">답변</span>
                  <p class="inner-cms-share" v-html="item.admin_reply"></p>
                </div>
              </div>
            </li>
            <li v-if="postList.length === 0">
              <p class="nodata">FAQ가 존재하지 않습니다.</p>
            </li>
          </ul>

          <pagination :paging="pagingObj" @onClickPage="onClickPage" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import PageSize from "@/pages/admin/manager/system/user/page-size";
import { useUserStore } from "@/store/user";
import Pagination from "@functional/pagination/pagination";
import FaqBorderListSearch from "@organisms/post/faq-border-list-search";
import { useNuxtApp, useRoute } from "nuxt/app";
import { onMounted, ref } from "vue";

definePageMeta({
  name: "SupportHelp",
  layout: "user",
});

const { $ofetch } = useNuxtApp();

const route = useRoute();
const userStore = useUserStore()

interface PagingObj {
  total: number;
  limit: number;
  block: number;
  currentPage: number;
}

interface Post {
  admin_reply: string;
  admin_reply_status: boolean;
  category: string;
  file_cnt: number;
  id: number;
  is_open: boolean;
  new_yn: boolean;
  ord_no: number;
  post_content: string;
  post_type: string;
  rownum: number;
  title: string;
  upload_date: string;
  user_nm: string;
  view_count: number;
  writer: string;
  selected?: boolean;
  showAnswer?: boolean;
}

interface SearchObj {
  searchType: string;
  searchWord: string;
}

onMounted(async () => {
  await getPost(1);

  // 메인 화면에서 특정 FAQ 클릭하여 진입한 경우
  if (userStore.getFaqId) {
    const matchedIndex = postList.value.findIndex((el) => el.id === userStore.getFaqId);
    if (matchedIndex > -1) {
      onClickShowAnswerButton(matchedIndex);
    }
  }
  userStore.setFaqId("") // 초기화
});

const pagingObj = ref<PagingObj>({
  total: 0, // 전체 데이터 건수
  limit: 10, // 한 페이지에 보여줄 데이터 건수
  block: 10, // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
});

const postList = ref<Array<Post>>([]);
const searchObj = ref<SearchObj>({
  searchType: "all", // 검색 조건 유형
  searchWord: "", // 검색어
});
// FAQ 목록 조회
const getPost = async (offset: number = 1): Promise<void> => {
  try {
    let { data: result } = await $ofetch(
      `/api/post/list?postType=faq&pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}`
    );

    pagingObj.value = {
      ...pagingObj.value,
      total: result.total,
      currentPage: offset,
    };
    postList.value = result.list;
  } catch (error) {
    console.log(error);
  }
};

// 답변보기 버튼 클릭 이벤트
const onClickShowAnswerButton = (index: number): void => {
  postList.value[index].showAnswer = !postList.value[index].showAnswer;

  for (var i = 0; i < postList.value.length; i++) {
    if (i == index) continue;
    postList.value[i].showAnswer = false;
  }
};

// 페이지 번호 클릭 이벤트 핸들러
const onClickPage = (page: number): void => {
  getPost(page);
};
</script>
