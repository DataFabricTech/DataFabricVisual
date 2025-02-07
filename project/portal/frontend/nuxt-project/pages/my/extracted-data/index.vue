<template>
  <div class="contents">
    <div>
      <div class="inner">
        <select-tab
          :tabs="tabs"
          :activeTab="currentExtractTab"
          @onSelectTab="onSelectCurrentTab"
        />
        <div class="board-list-wrap">
          <div class="mu-heading">
            <!-- board-list-search -->
            <Border-list-search :searchObj="searchObj" @searchMethod="getMyExtractedDataList" :searchList="searchList"/>

            <!-- //board-list-search -->
            <div class="mu-item-group">
              <!-- mu-selectbox / 10개씩 보기(Default) -->
              <page-size
                v-model:pageSize="pagingObj.limit"
                @searchMethod="getMyExtractedDataList"
              />
            </div>
          </div>
          <!-- 추출 데이터 -->
          <div class="board-list" v-if="currentExtractTab == 0">
            <table class="mu-grid">
              <colgroup>
                <col style="width: 60px" />
                <col style="width: auto" />
                <col style="width: 90px" />
                <col style="width: 180px" />
              </colgroup>
              <thead>
              <tr>
                <th scope="col">번호</th>
                <th scope="col">데이터명</th>
                <th scope="col">추출방법</th>
                <th scope="col">추출 일시</th>
              </tr>
              </thead>
              <tbody>

              <tr v-for="(item, index) in postList" :key="index">
                  <td>{{ item.rownum }}</td>
                  <td class="tl">
                    <!-- <a v-on:click="onClickDataName(item)">{{item.name}}</a>왼쪽정렬시 .tl 추가 -->
                    <a @click="onClickDataName(item)">
                      {{ item.description && item.description.length > 0 ? `${item.description} (${item.meta_name})` : item.meta_name }}
                    </a>
                  </td><!-- 왼쪽정렬시 .tl 추가 -->
                  <td>{{ getDownloadType(item.download_type) }}</td>
                  <td>{{ $yyyyMMddHHmmSS(item.download_date) }}</td>
              </tr>
              <tr v-if="postList.length === 0">
                <td colspan="4">
                  <p class="nodata">추출 데이터가 존재하지 않습니다.</p>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <!-- 대용량 추출 -->
          <div class="board-list" v-if="currentExtractTab == 1">
            <table class="mu-grid">
              <colgroup>
                <col style="width: 60px" />
                <col style="width: auto" />
                <col style="width: 90px" />
                <col style="width: 180px" />
                <col style="width: 180px" />
                <col style="width: 180px" />
                <col style="width: 110px" />
                <col style="width: 110px" />
              </colgroup>
              <thead>
              <tr>
                <th scope="col">번호</th>
                <th scope="col">데이터명</th>
                <th scope="col">추출방법</th>
                <th scope="col">추출일시</th>
                <th scope="col">완료일시</th>
                <th scope="col">만료일</th>
                <th scope="col">파일 다운로드</th>
                <th scope="col">상태</th>
              </tr>
              </thead>
              <tbody>

              <tr v-for="(item, index) in postList" :key="index">
                  <td>{{ item.rownum }}</td>
                  <td class="tl">
                    <!-- <a v-on:click="onClickDataName(item)">{{item.name}}</a>왼쪽정렬시 .tl 추가 -->
                    <a @click="onClickDataName(item)">
                      {{ item.description && item.description.length > 0 ? `${item.description} (${item.meta_name})` : item.meta_name }}
                    </a>
                  </td><!-- 왼쪽정렬시 .tl 추가 -->
                  <td>{{ getDownloadType(item.download_type) }}</td>
                  <td>{{ $yyyyMMddHHmmSS(item.download_date) }}</td>
                  <td>{{ $yyyyMMddHHmmSS(item.end_time) }}</td>
                  <td>{{ $yyyyMMddHHmmSS(item.download_limit_time) }}</td>
                  <td v-if="item.status && item.status == 2">
                    <button type="button" class="mu-btn mu-btn-sm mu-icon-svg download" @click="downloadBigFile(item)">
                      <span class="blind">다운로드</span>
                    </button>
                  </td>
                  <td v-else>-</td>
                  <td><em :class="getStatusStyle(item.status)" >{{ getStatus(item.status) }}</em></td>

              </tr>
              <tr v-if="postList.length === 0">
                <td colspan="8">
                  <p class="nodata">추출 데이터가 존재하지 않습니다.</p>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <!-- mu-pagination -->
        </div>
          <pagination :paging="pagingObj" @onClickPage="onClickPage" />
          <!-- //mu-pagination -->
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import PageSize from "@/pages/admin/manager/system/user/page-size";
import Pagination from "@functional/pagination/pagination";
import selectTab from "@/components/functional/selectTab/select-tab.vue";
import BorderListSearch from "@functional/border-list-search/border-list-search.vue";
import { defaultAlert, errorAlert } from '@/components/functional/alert/alert-default';

definePageMeta({
  name: "MyExtractedData",
  layout: 'user',
})

const { $ofetch } = useNuxtApp();

const tabs = ref([
  {idx: 0, key: "데이터 추출 이력"},
  {idx: 1, key: "대용량 데이터 추출"}
])
const currentExtractTab = ref(0)

const searchList = ref([])

const pagingObj = ref({
  total: 0,
  limit: 10,
  block: 10,
  currentPage: 1,
});
const postList = ref([]);
const searchObj = ref({
  searchType: "all",
  searchWord: "",
  changedTab: false,
});
const router = useRouter();

const getMyExtractedDataList = async (offset = 1) => {
  const param = {
    select_type: currentExtractTab.value,
    offset,
    limit: pagingObj.value.limit,
    searchWord: searchObj.value.searchWord,
  };

  await $ofetch("/api/myCatalog/myExtractedDataList", {
      method: 'POST',
      body: JSON.stringify(param),
      headers: {
          'Content-Type': 'application/json'
      }
  }).then(async (res) => {
    if (res && res.data && res.data.list) {
      pagingObj.value = {
        ...pagingObj.value,
        total: res.data.total,
        currentPage: offset,
      };
      postList.value = res.data.list;
    } else {
      initListData()
      await errorAlert("조회에 실패하였습니다.")
    }
  }).catch(async (err) => {
    initListData()
    await errorAlert("조회에 실패하였습니다.")
  });
};

const onClickDataName = (item) => {
  if (item.meta_id) {
    router.push({
      path: "/search/detail",
      query: { guid: item.meta_id },
    });
  }
};

const onClickPage = (page) => {
  getMyExtractedDataList(page);
};

const getDownloadType = (dt) => {
  if (dt == 0) return 'FILE'
  if (dt == 1) return 'API'
  if (dt == 2) return 'BIG'
  return dt
}

const getStatusStyle = (status) => {
  let className = 'step-process'
  if (status == 0) return 'request ' + className // 대기
  if (status == 1) return 'processing ' + className // 진행
  if (status == 2) return 'finish ' + className // 성공
  if (status == 3) return 'err ' + className // 실패
  else return ' ' + className
}

const getStatus = (st) => {
  if (st == 0) return '대기'
  if (st == 1) return '진행'
  if (st == 2) return '성공'
  if (st == 3) return '실패'
  return '-'
}

// 대용량 데이터 다운로드
const downloadBigFile = async (item) => {
  // 1. 만료되었는지 확인
  try {
    const now = new Date().getTime()
    const limit_time = new Date(item.download_limit_time).getTime()

    // 다운로드 만료일을 넘긴 경우
    if (now > limit_time) {
      defaultAlert("다운로드 기한이 만료되었습니다.")
      return
    }

  } catch (err) {
    errorAlert("다운로드에 실패하였습니다.")
    return
  }

  // 2. 다운로드 URL 유무 확인
  if (item.share_url) {
    try {
      const a = document.createElement("a");
      a.href = item.share_url;
      a.download = item.minio_object_name
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    } catch (error) {
      await errorAlert("다운로드에 실패하였습니다.")
      return
    }
  }
}

onMounted(() => {
  getMyExtractedDataList();
});

// 추출 데이터 / 대용량 추출 탭
const onSelectCurrentTab = (curTabObj) => {
  if (curTabObj) {
    currentExtractTab.value = curTabObj.idx
  } else {
    currentExtractTab.value = 0
  }

  initSearchOption()
  getMyExtractedDataList()
}

const initSearchOption = () => {
  searchObj.value = {
    ...searchObj.value,
    searchType: "all",
    searchWord: "",
  }

  pagingObj.value = {
    ...pagingObj.value,
    total: 0,
    limit: 10,
    block: 10,
    currentPage: 1,
  };
}

const initListData = () => {
  postList.value = []
  pagingObj.value = {
    total: 0,
    limit: 10,
    block: 10,
    currentPage: 1,
  }
}
</script>
