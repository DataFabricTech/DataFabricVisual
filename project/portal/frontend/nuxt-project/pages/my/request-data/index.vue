<template>
        <div class="contents">
          <div>
            <div class="inner">
              <div class="board-list-wrap">
                <div class="mu-heading">
                  <!-- board-list-search -->
                  <Border-list-search :searchObj.sync="searchObj" @searchMethod="getDataRequestList" :searchList="searchList"/>

                  <div class="mu-item-group">
                  <page-size
                    v-model:pageSize="pagingObj.limit"
                    @searchMethod="getDataRequestList"
                  />
                  </div>
                </div>
                <div class="board-list">
                  <table class="mu-grid">
                    <colgroup>
                      <col style="width:60px">
                      <col style="width:auto">
                      <col style="width:120px" span="4">
                    </colgroup>
                    <thead>
                    <tr>
                      <th scope="col">번호</th>
                      <th scope="col">제목</th>
                      <th scope="col">첨부파일</th>
                      <th scope="col">신청일</th>
                      <th scope="col">완료일</th>
                      <th scope="col">진행상태</th>
                    </tr>
                    </thead>
                    <tbody>
                      <tr v-for="(item, index) in dataRequestList" :key="index">
                        <td>{{ item.rownum }}</td>
                        <td class="tl">
                          <NuxtLink :to="'/data/request/detail?id=' + item.id">{{ item.title }}</NuxtLink>
                          <i v-if="item.new_yn" class="mu-icon-svg new">새로운글</i>
                        </td><!-- 왼쪽정렬시 .tl 추가 -->
                        <td><i v-if="item.file_cnt>0" class="mu-icon-svg file">첨부파일 있음</i></td>
                        <td>{{ $yyyyMMdd(item.create_date) }}</td>
                        <td>{{ $yyyyMMdd(item.finish_date) }}</td>
                        <td>
                          <em :class="getProcessStyle(item.process_status)">{{ getProcessStatus(item.process_status) }}</em>
                        </td>
                      </tr>
                      <tr v-if="dataRequestList.length === 0">
                        <td colspan="6">
                          <p class="nodata">데이터 요청 내역이 존재하지 않습니다.</p>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
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
        <!-- //contents -->
</template>

<script setup lang="ts">
import { ref, computed, watchEffect, onMounted } from 'vue'
import { useUserStore } from '~/store/user';
import PageSize from "@/pages/admin/manager/system/user/page-size";
import Pagination from "@functional/pagination/pagination";
import DatePicker from "@functional/datepicker/date-picker";
import BorderListSearch from "@functional/border-list-search/border-list-search.vue";

definePageMeta({
  name: "MyRequestData",
  layout: 'user',
})

const { $ofetch } = useNuxtApp();
const userStore = useUserStore();
const getUserInfo = computed(() => userStore.getUser)
const getTabInfo = computed(() => userStore.getTab)

const pagingObj = ref({
  total: 0,       // 전체 데이터 건수
  limit: 10,      // 한 페이지에 보여줄 데이터 건수
  block: 10,      // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
})

const dataRequestList = ref([])
const searchWord = ref("")
const searchType = ref("all")
const checkedList = ref([]) // [데이터 정의 : Array]
const allChecked = ref(false)
const searchObj = ref({
  searchType: "all",// 검색 조건 유형
  searchWord: "",   // 검색어
})

const searchList = ref([
  {
    value : "title",
    text : "제목"
  }
])

onMounted(() => {
  getDataRequestList()
})

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

const getDataRequestList = async (offset = 1) => {
  try {
    const param = {
      offset,
      limit: pagingObj.value.limit,
      searchWord: searchObj.value.searchWord,
    };
    const result = await $ofetch("/api/myCatalog/myDataRequestList", {
        method: 'POST',
        body: JSON.stringify(param),
        headers: {
            'Content-Type': 'application/json'
        }
    });
    if (result.data.list) {
      pagingObj.value = {
        ...pagingObj.value,
        total: result.data.total,
        currentPage: offset,
      };
      dataRequestList.value = result.data.list;
    }
    checkedList.value = [];
  } catch (error) {
    console.log(error);
  }
}

const selected = (e) => {
  const selectedItem = dataRequestList.value.find(v => v.id == e.target.value);
  if(selectedItem.selected) {
    checkedList.value.push(e.target.value);
  } else {
    checkedList.value.splice(checkedList.value.findIndex(v => v == e.target.value), 1);
  }

  for (let i in dataRequestList.value) {
    if(! dataRequestList.value[i].selected) {
      allChecked.value = false;
      return;
    } else {
      allChecked.value = true;
    }
  }
}

const onClickPage = (page) => {
  getDataRequestList(page);
}
</script>
