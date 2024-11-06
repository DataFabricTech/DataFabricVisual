<template>
  <div class="contents">
    <div>
      <!-- content-floating-wrap -->
      <request-content-floating-wrap />


      <div class="inner">
        <div class="board-list-wrap">
          <div class="board-list-toolbar">
            <h4>데이터 요청</h4>
            <div class="mu-item-group">
              <button type="button" class="mu-btn mu-btn-lg mu-btn-primary" @click="onClickRegisterButton">
                <i class="mu-icon-svg apply"></i>
                <span>데이터 요청하기</span>
              </button>
            </div>
          </div>

          <div class="mu-heading">
            <!-- board-list-search -->
            <request-border-list-search
              :searchObj.sync="searchObj"
              @searchMethod="getPost"
            />

            <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getPost" />
          </div>
          <div class="board-list">
            <table class="mu-grid">
              <colgroup>
                <col style="width: 60px" />
                <col style="width: auto" />
                <col style="width: 120px" span="5" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">번호</th>
                  <th scope="col">제목</th>
                  <th scope="col">첨부파일</th>
                  <th scope="col">신청자</th>
                  <th scope="col">신청일</th>
                  <th scope="col">완료일</th>
                  <th scope="col">진행상태</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in postList" :key="index">
                  <td>{{ item.rownum }}</td>
                  <td class="tl">
                    <a v-on:click="onClickDetail(item.id, item.writer)">{{
                      item.title
                    }}</a>
                    <i v-if="item.new_yn" class="mu-icon-svg new">새로운글</i>
                  </td>
                  <!-- 왼쪽정렬시 .tl 추가 -->
                  <td>
                    <i v-if="item.file_cnt > 0" class="mu-icon-svg file">첨부파일 있음</i>
                    <i v-if="item.file_cnt === 0" >-</i>
                  </td>
                  <td>{{ item.user_nm }}</td>
                  <td>{{ item.upload_date }}</td>
                  <td v-if="item.finish_date">{{ item.finish_date }}</td>
                  <td v-else="item.finish_date">-</td>
                  <td>
                    <em
                      v-bind:class="[
                        item.process_status == '신청'
                          ? 'apply'
                          : item.process_status == '검토중'
                          ? 'review'
                          : item.process_status == '진행중'
                          ? 'ing'
                          : item.process_status == '제공'
                          ? 'supply'
                          : item.process_status == '미제공'
                          ? 'unsupply'
                          : '',
                        'step-process',
                      ]"
                      >{{ item.process_status }}</em
                    ><!-- 신청apply – 검토중review – 진행중ing – 제공supply/미제공unsupply -->
                  </td>
                </tr>
                <tr v-if="postList.length == 0">
                  <td colspan="7">
                    <p class="nodata">데이터 요청 내역이 존재하지 않습니다.</p>
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
  <!-- //contents -->
</template>

<script setup lang="ts">
import RequestContentFloatingWrap from "@organisms/post/request-content-floating-wrap";
import RequestBorderListSearch from "@organisms/post/request-border-list-search";
import PageSize from "@/pages/admin/manager/system/user/page-size";
import Pagination from "@functional/pagination/pagination";
import { defaultAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";
import { useUserStore } from "@/store/user";

definePageMeta({
  name: "DataRequest",
  layout: "user",
});

const { $ofetch } = useNuxtApp();
const router = useRouter();
const userStore = useUserStore();

interface PagingObj {
  total: number;
  limit: number;
  block: number;
  currentPage: number;
}

interface Post {
  admin_reply: string;
  admin_reply_status: boolean;
  benefit_content: string;
  data_name: string;
  file_cnt: number;
  id: number;
  is_open: boolean;
  new_yn: boolean;
  post_content: string;
  post_type: string;
  process_status: string;
  request_end: string;
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

onMounted(() => getPost());

// 페이징 처리 제어
const pagingObj = ref<PagingObj>({
  total: 0, // 전체 데이터 건수
  limit: 10, // 한 페이지에 보여줄 데이터 건수
  block: 10, // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
});

const postList = ref<Array<Post>>([]);
const checkedList = ref<Array<number>>([]);
const allChecked = ref<boolean>(false);
const searchObj = ref<SearchObj>({
  searchType: "all", // 검색 조건 유형
  searchWord: "", // 검색어
});

// 데이터요청 목록 조회
const getPost = async (offset: number = 1): Promise<void> => {
  try {
    let { data: result } = await $ofetch(
      `/api/post/list?postType=data-request&pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}`
    );
    pagingObj.value = {
      ...pagingObj.value,
      total: result.total,
      currentPage: offset,
    };
    postList.value = result.list;
    checkedList.value = [];
  } catch (error) {
    console.log(error);
  }
};

// 전체선택 체크박스 클릭 이벤트
const checkedAll = (checked: boolean): void => {
  allChecked.value = checked;
  for (let i in postList.value) {
    postList.value[i].selected = allChecked.value;
    if (allChecked.value) {
      if (checkedList.value.findIndex((v) => v == postList.value[i].id) >= 0) continue;
      checkedList.value.push(postList.value[i].id);
    } else {
      checkedList.value = [];
    }
  }
};

// 체크박스 클릭 이벤트
const selected = (e: any): void => {
  const selectedItem = postList.value.find((v) => v.id == e.target.value);
  if (selectedItem.selected) {
    checkedList.value.push(e.target.value);
  } else {
    checkedList.value.splice(
      checkedList.value.findIndex((v) => v == e.target.value),
      1
    );
  }

  for (let i in postList.value) {
    if (!postList.value[i].selected) {
      allChecked.value = false;
      return;
    } else {
      allChecked.value = true;
    }
  }
};

const onClickDetail = (id: number, writer: string): void => {
  if (userStore.getUser.user_id == writer) router.push(`/data/request/detail?id=${id}`);
  else defaultAlert("상세 요청내용은 작성자만 볼 수 있습니다.");
};

// 신청하기 버튼 클릭 이벤트
const onClickRegisterButton = (): void => {
  router.push("/data/request/register");
};

// 페이지 번호 클릭 이벤트 핸들러
const onClickPage = (page: number): void => {
  getPost(page);
};
</script>
