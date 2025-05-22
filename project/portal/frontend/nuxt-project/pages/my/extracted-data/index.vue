<template>
  <div class="contents">
    <div>
      <div class="inner">
        <select-tab :tabs="tabs" :activeTab="currentExtractTab" @onSelectTab="onSelectCurrentTab" />
        <div class="board-list-wrap">
          <div class="mu-heading">
            <!-- board-list-search -->
            <border-list-search :searchObj="searchObj" @searchMethod="getMyExtractedDataList"
              :searchList="searchList" />

            <!-- //board-list-search -->
            <div class="mu-item-group">
              <div v-if="currentExtractTab == 1" class="mu-selectbox w120">
                <select name="searchType3" :value="currentStatus" @change="onChangeStatus">
                  <option v-for="(item, index) in filterStatusList" :key="index" :value="item.name">
                    {{ item.description }}
                  </option>
                </select>
              </div>
              <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getMyExtractedDataList" />
            </div>
          </div>
          <!-- 추출 데이터 -->
          <div class="board-list" v-if="currentExtractTab == 0">
            <table class="mu-grid">
              <colgroup>
                <col style="width: 60px" />
                <col style="width: 250px" />
                <col style="width: 250px" />
                <col style="width: 150px" />
                <col style="width: 60px" />
                <col style="width: 180px" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">번호</th>
                  <th scope="col">데이터 설명</th>
                  <th scope="col">데이터 명</th>
                  <th scope="col">시스템 명</th>
                  <th scope="col">추출방법</th>
                  <th scope="col">추출 일시</th>
                </tr>
              </thead>
              <tbody>

                <tr v-for="(item, index) in postList" :key="index">
                  <td :title="item.rownum ? item.rownum : '-'">{{ item.rownum }}</td>
                  <td :title="item.description ? item.description : '-'"><a @click="onClickDataName(item)">{{ item.description ? item.description : '-' }}</a></td>
                  <td :title="item.meta_name ? item.meta_name : '-'">{{ item.meta_name ? item.meta_name : '-' }}</td>
                  <td :title="item.system_name ? item.system_name : '-'">{{ item.system_name ? item.system_name : '-' }}</td>
                  <td :title="getDownloadType(item.download_type)">{{ getDownloadType(item.download_type) }}</td>
                  <td :title="$yyyyMMddHHmmSS(item.download_date)">{{ $yyyyMMddHHmmSS(item.download_date) }}</td>
                </tr>
                <tr v-if="postList.length === 0">
                  <td colspan="6">
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
                <col style="width: 200px" />
                <col style="width: 200px" />
                <col style="width: 150px" />
                <col style="width: 90px" />
                <col style="width: 110px" />
                <col style="width: 110px" />
                <col style="width: 110px" />
                <col style="width: 110px" />
                <col style="width: 110px" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">번호</th>
                  <th scope="col">데이터 설명</th>
                  <th scope="col">데이터 명</th>
                  <th scope="col">시스템 명</th>
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
                  <td :title="item.rownum ? item.rownum : '-'">{{ item.rownum }}</td>
                  <td :title="item.description ? item.description : '-'"><a @click="onClickDataName(item)" :title="item.description ? item.description : '-'">{{ item.description ? item.description : '-' }}</a></td>
                  <td :title="item.meta_name ? item.meta_name : '-'">{{ item.meta_name ? item.meta_name : '-' }}</td>
                  <td :title="item.system_name ? item.system_name : '-'">{{ item.system_name ? item.system_name : '-' }}</td>
                  <td :title="getDownloadType(item.download_type)">{{ getDownloadType(item.download_type) }}</td>
                  <td :title="$yyyyMMddHHmmSS(item.download_date)">{{ $yyyyMMddHHmmSS(item.download_date) }}</td>
                  <td :title="$yyyyMMddHHmmSS(item.end_time)">{{ $yyyyMMddHHmmSS(item.end_time) }}</td>
                  <td :title="$yyyyMMddHHmmSS(item.download_limit_time)">{{ $yyyyMMddHHmmSS(item.download_limit_time) }}</td>
                  <td title="다운로드" v-if="item.status && item.status == 2">
                    <button type="button" class="mu-btn mu-btn-sm mu-icon-svg download" @click="downloadBigFile(item)">
                      <span class="blind">다운로드</span>
                    </button>
                  </td>
                  <td v-else>-</td>
                  <td :title="getStatus(item.status)"><em :class="getStatusStyle(item.status)">{{ getStatus(item.status) }}</em></td>

                </tr>
                <tr v-if="postList.length === 0">
                  <td colspan="10">
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
import { defaultAlert, errorAlert } from '@/components/functional/alert/alert-default';
import selectTab from "@/components/functional/selectTab/select-tab.vue";
import PageSize from "@/pages/admin/manager/system/user/page-size";
import BorderListSearch from "@functional/border-list-search/border-list-search.vue";
import Pagination from "@functional/pagination/pagination";
import { useRouter } from 'vue-router';

definePageMeta({
  name: "MyExtractedData",
  layout: 'user',
})

const { $ofetch } = useNuxtApp();

const tabs = ref([
  { idx: 0, key: "데이터 추출 이력" },
  { idx: 1, key: "대용량 데이터 추출" }
])

// 대용량 데이터 추출 필터 항목
const filterStatusList = ref([
  {
    name: "",
    description: "전체"
  },
  {
    name: 0,
    description: "대기"
  },
  {
    name: 1,
    description: "진행"
  },
  {
    name: 2,
    description: "성공"
  },
  {
    name: 3,
    description: "실패"
  },
])
const currentStatus = ref("");

const currentExtractTab = ref(0)
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
const searchList = ref([
  {
    value: "description",
    text: "데이터 설명"
  },
  {
    value: "meta_name",
    text: "데이터 명"
  },
  {
    value: "system_name",
    text: "시스템 명"
  },
])

const router = useRouter();

const getMyExtractedDataList = async (offset = 1) => {
  const param = {
    select_type: currentExtractTab.value,
    offset,
    limit: pagingObj.value.limit,
    searchType: searchObj.value.searchType,
    searchWord: searchObj.value.searchWord,
    searchStatus: currentStatus.value,
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
      console.log(postList.value)
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

const onChangeStatus = (e) => {
  currentStatus.value = e.target.value
  getMyExtractedDataList();
}

</script>
