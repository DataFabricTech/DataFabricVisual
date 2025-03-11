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
            <request-border-list-search :searchObj.sync="searchObj" @searchMethod="getDataRequestList" />
            <div class="mu-item-group">
              <div class="mu-selectbox w120">
                <select name="searchType3" :value="searchObj.searchStatus" @change="onChangeProcessStatus">
                  <option v-for="(item, index) in processStatusfilterList" :key="index" :value="item.value">
                    {{ item.desc }}
                  </option>
                </select>
              </div>
              <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getDataRequestList" />
            </div>

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
                <tr v-for="(item, index) in dataRequestList" :key="index">
                  <td>{{ item.rownum }}</td>
                  <td class="tl">
                    <a v-on:click="onClickDetail(item.id, item.writer)">{{
                      item.title
                      }}</a>
                    <i v-if="item.new_yn" class="mu-icon-svg new">새로운글</i>
                  </td>
                  <td>
                    <i v-if="item.file_cnt > 0" class="mu-icon-svg file">첨부파일 있음</i>
                    <i v-if="item.file_cnt === 0">-</i>
                  </td>
                  <td>{{ item.user_nm ? item.user_nm : "-" }}</td>
                  <td>{{ $yyyyMMdd(item.create_date) }}</td>
                  <td>{{ $yyyyMMdd(item.finish_date) }}</td>
                  <td>
                    <em :class="getProcessStyle(item.process_status)">{{ getProcessStatus(item.process_status)
                      }}</em><!-- 신청apply – 검토중review – 진행중ing – 제공supply/미제공unsupply -->
                  </td>
                </tr>
                <tr v-if="dataRequestList.length == 0">
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
import RequestContentFloatingWrap from "@organisms/dataRequest/request-content-floating-wrap";
import RequestBorderListSearch from "@organisms/dataRequest/request-border-list-search";
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

interface DataRequest {
  id: number
  writer: string
  request_type: number
  title: string
  data_name: string
  contents: string
  request_start: string
  request_end: string
  benefit_content: string
  create_date: string
  update_date: string
  process_status: number
  finish_date: string
}

interface SearchObj {
  searchType: string;
  searchWord: string;
  searchStatus: string;
}

interface FilterOption {
  value: number | string; // 두번째 필터 조건
  desc: string; // 검색어
}

onMounted(() => getDataRequestList());

// 페이징 처리 제어
const pagingObj = ref<PagingObj>({
  total: 0, // 전체 데이터 건수
  limit: 10, // 한 페이지에 보여줄 데이터 건수
  block: 10, // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
});

const dataRequestList = ref<Array<DataRequest>>([]);
const checkedList = ref<Array<number>>([]);
const searchObj = ref<SearchObj>({
  searchType: "all", // 검색 조건 유형
  searchWord: "", // 검색어
  searchStatus: "", // 진행상태 필터값값
});

// 데이터 요청 진행상태 필터목록
const processStatusfilterList = ref<Array<FilterOption>>([
  {
    value: "",
    desc: "전체"
  },
  {
    value: 0,
    desc: "신청"
  },
  {
    value: 1,
    desc: "검토중"
  },
  {
    value: 2,
    desc: "진행중"
  },
  {
    value: 3,
    desc: "제공"
  },
  {
    value: 4,
    desc: "미제공"
  },
])

const getProcessStyle = (status) => {
  let className = 'step-process'
  if (status == 0) return 'apply ' + className
  if (status == 1) return 'review ' + className
  if (status == 2) return 'ing ' + className
  if (status == 3) return 'supply ' + className
  if (status == 4) return 'unsupply ' + className
  else return ' ' + className
}

const getProcessStatus = (status) => {
  if (status == 0) return '신청'
  if (status == 1) return '검토중'
  if (status == 2) return '진행중'
  if (status == 3) return '제공'
  if (status == 4) return '미제공'
  if (status == 5) return '-'
}

// 데이터요청 목록 조회
const getDataRequestList = async (offset: number = 1): Promise<void> => {
  try {
    let { data: result } = await $ofetch(
      `/api/dataRequest/list?pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}&searchStatus=${searchObj.value.searchStatus}`
    );
    pagingObj.value = {
      ...pagingObj.value,
      total: result.total,
      currentPage: offset,
    };
    dataRequestList.value = result.list;
    checkedList.value = [];
  } catch (error) {
    console.log(error);
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
  getDataRequestList(page);
};

// 진행상태 필터 변경 이벤트
const onChangeProcessStatus = (e) => {
  searchObj.value.searchStatus = e.target.value
  getDataRequestList();
}
</script>
