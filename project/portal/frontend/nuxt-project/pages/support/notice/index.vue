<template>
  <div class="contents">
    <div>
      <div class="inner">
        <div class="board-list-wrap">
          <div class="mu-heading">
            <!-- board-list-search -->
            <notice-border-list-search
              :searchObj.sync="searchObj"
              @searchMethod="getPost"
            />

            <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getPost" />
          </div>

          <div class="board-list">
            <table class="mu-grid">
              <colgroup>
                <col style="width: 60px" />
                <col style="width: 70px" />
                <col style="width: 90px" />
                <col style="width: auto" />
                <col style="width: 100px" />
                <col style="width: 120px" />
                <col style="width: 120px" />
                <col style="width: 80px" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">No.</th>
                  <th scope="col">구분</th>
                  <th scope="col">메인롤링</th>
                  <th scope="col">제목</th>
                  <th scope="col">첨부파일</th>
                  <th scope="col">등록자</th>
                  <th scope="col">등록일</th>
                  <th scope="col">조회수</th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="{
                    rownum,
                    category,
                    is_open,
                    id,
                    title,
                    new_yn,
                    file_cnt,
                    user_nm,
                    upload_date,
                    index,
                    view_count
                  } in postList"
                  :key="index"
                >
                  <td>{{ rownum }}</td>
                  <td>
                    <em class="data-label">{{ category }}</em>
                  </td>
                  <td>
                    {{ getIsRolling(is_open) }}
                  </td>
                  <td class="tl">
                    <NuxtLink :to="'/support/notice/detail?id=' + id">{{
                      title
                    }}</NuxtLink
                    ><i v-if="new_yn" class="mu-icon-svg new">새로운글</i>
                  </td>
                  <!-- 왼쪽정렬시 .tl 추가 -->
                  <td>
                    <i v-if="file_cnt > 0" class="mu-icon-svg file">첨부파일 있음</i>
                  </td>
                  <td>{{user_nm}}</td>
                  <td>{{upload_date }}</td>
                  <td>{{view_count}}</td>
                </tr>
                <tr v-if="postList.length === 0">
                  <td colspan="7">
                    <p class="nodata">공지사항이 존재하지 않습니다.</p>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <pagination :paging="pagingObj" @onClickPage="onClickPage" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import PageSize from "@/pages/admin/manager/system/user/page-size";
import Pagination from "@functional/pagination/pagination";
import NoticeBorderListSearch from "@organisms/post/notice-border-list-search";
import { useNuxtApp, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";

definePageMeta({
  name: "SupportNotice",
  layout: "user",
});

const { $ofetch } = useNuxtApp();

interface PagingObj {
  total: number;
  limit: number;
  block: number;
  currentPage: number;
}

interface Post {
  admin_reply_status: boolean;
  category: string;
  file_cnt: number;
  id: number;
  is_open: boolean;
  new_yn: boolean;
  post_content: string;
  post_type: string;
  rownum: number;
  title: string;
  upload_date: string;
  user_nm: string;
  view_count: number;
  writer: string;
  selected?: boolean;
}

interface SearchObj {
  searchType: string;
  searchWord: string;
}

onMounted(() => {
  getPost(1);
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

// 공지사항 목록 조회
const getPost = async (offset: number = 1): Promise<void> => {
  try {
    let { data: result } = await $ofetch(
      `/api/post/list?postType=notice&pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}`
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

// 페이지 번호 클릭 이벤트 핸들러
const onClickPage = (page: number): void => {
  getPost(page);
};

// 메인 롤링 여부 표기
const getIsRolling = (is_open) => {
  if (is_open) return "사용"
  else return "미사용"
}
</script>
