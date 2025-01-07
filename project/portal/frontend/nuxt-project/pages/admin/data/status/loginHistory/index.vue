<template>
  <div>
    <div class="inner">
      <div class="board-list-wrap">
        <div class="mu-heading">
          <!-- board-list-search -->
          <history-board-list-search
            :searchObj.sync="searchObj"
            :startDate="startDate"
            :endDate="endDate"
            :historyType="historyType"
            @updateStartDate="updateStartDate"
            @updateEndDate="updateEndDate"
            @searchMethod="getHistoryList"
            @onClickClearButton="onClickClearButton"
            @onClickCsvDownload="onClickCsvDownload"
          />

          <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getHistoryList" />
        </div>

        <div class="board-list">
          <table class="mu-grid">
            <colgroup>
              <col style="width: 6%" />
              <col style="width: 6%" />
              <col style="width: 6%" />
              <col style="width: 10%" />
              <col style="width: 10%" />
              <col style="width: 10%" />
              <col style="width: 10%" />
              <!-- <col style="width: 80px" /> -->
            </colgroup>
            <thead>
              <tr>
                <th scope="col">No.</th>
                <th scope="col">사용자ID</th>
                <th scope="col">사용자명</th>
                <th scope="col">조직ID</th>
                <th scope="col">조직명</th>
                <th scope="col">사용자IP</th>
                <th scope="col">로그인 시간</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in historyList" :key="index">
                <td>{{ item.rownum }}</td>
                <td>{{ item.user_id ? item.user_id : "-" }}</td>
                <td>{{ item.user_name ? item.user_name : "-" }}</td>
                <td>{{ item.dept_id ? item.dept_id : "-" }}</td>
                <td>{{ item.dept_name ? item.dept_name : "-" }}</td>
                <td>{{ item.user_ip_addr }}</td>
                <td>{{ yyyyMMddHHmmSS(item.login_date) }}</td>
              </tr>
              <tr v-if="historyList.length === 0">
                <td colspan="7">
                  <p class="nodata">이력이 존재하지 않습니다.</p>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <pagination :paging="pagingObj" @onClickPage="onClickPage" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import PageSize from "@/pages/admin/manager/user/page-size";
import Pagination from "@functional/pagination/pagination";
import moment from "moment";
import { errorAlert } from "@functional/alert/alert-default";
import {onMounted, watch, reactive, ref} from "vue";
import HistoryBoardListSearch from "@organisms/history/history-board-list-search.vue";

definePageMeta({
  name: "AdminLoginHistory",
  layout: 'admin',
})

onMounted(() => {
  getHistoryList();
});

const { $ofetch } = useNuxtApp();

const pagingObj = ref({
  total: 0, // 전체 데이터 건수
  limit: 10, // 한 페이지에 보여줄 데이터 건수
  block: 10, // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
});

const historyType = ref("login")
const historyList = ref([]);
const searchObj = ref({
  searchTypeList: [
  {
    "name": "all",
    "description": "전체"
  },
  {
    "name": "user_id",
    "description": "사용자ID"
  },
  {
    "name": "user_name",
    "description": "사용자명"
  },
  {
    "name": "dept_id",
    "description": "조직ID"
  },
  {
    "name": "dept_name",
    "description": "조직명"
  }],
  searchType: "all",
  searchWord: "",
});

const startDate = ref(moment().subtract(30, 'd').format('YYYY-MM-DD')); // 시작년도, 월, 일
const endDate = ref(moment().format('YYYY-MM-DD')); // 종료년도, 월, 일

// 이력 목록 조회
const getHistoryList = async (offset: number = 1): Promise<void> => {
  try {
    await $ofetch(`/api/datastat/history?pageSize=${pagingObj.value.limit}&pnum=${offset}&historyType=${historyType.value}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}&startDate=${startDate.value}&endDate=${endDate.value}`)
    .then((res) => {
      if (res && !res.errorMessage) {
        historyList.value = res.data.list
        pagingObj.value = {
          ...pagingObj.value,
          currentPage: offset,
          total: res.data.total
        }
      }
    })
  } catch (error) {
    console.log(error);
  }
};

// 페이지 번호 클릭
const onClickPage = (page: number): void => {
  getHistoryList(page);
};

const yyyyMMddHHmmSS = (value) => {
  let date = new Date(value);
  let year = date.getFullYear();
  let month = String(date.getMonth() + 1).padStart(2, "0");
  let day = String(date.getDate()).padStart(2, "0");
  let hours = String(date.getHours()).padStart(2, "0");
  let min = String(date.getMinutes()).padStart(2, "0");
  let sec = String(date.getSeconds()).padStart(2, "0");
  if (value == null || value == undefined) {
    return "";
  }
  return year + "-" + month + "-" + day + " " + hours + ":" + min + ":" + sec;
};

// 달력 입력값 초기화 버튼
const onClickClearButton = () => {
  getHistoryList();
}

// 시작날짜 업데이트
const updateStartDate = (dt) => {
  startDate.value = dt
}

// 종료날짜 업데이트
const updateEndDate = (dt) => {
  endDate.value = dt
}

// CSV 다운로드
const onClickCsvDownload = async (historyType) => {
  const params = {
    historyType: historyType,
    searchType: searchObj.value.searchType,
    searchWord: searchObj.value.searchWord,
    startDate: startDate.value,
    endDate: endDate.value,
  }

  const res = await $ofetch(`/api/datastat/downloadHistory`, {
    method: 'POST',
    body: params,
    responseType: 'blob'
  })

  // 성공시 {}
  if (res && res.errorMessage && res.errorMessage != "") {
      errorAlert("CSV 다운로드에 실패하였습니다.")
      return;
    }

    if (res.size && res.size > 0) {
      await new Response(res).blob().then((data) => {
        const url = URL.createObjectURL(data);
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `${historyType}History` + ".csv");
        link.style.cssText = "display:none";
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      })
    }
}

</script>

<style lang="scss">
</style>
