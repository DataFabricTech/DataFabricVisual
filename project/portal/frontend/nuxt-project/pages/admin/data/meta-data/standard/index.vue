<template>
  <div class="contents">
    <div>
      <div class="inner">
        <select-tab :tabs="tabs" :activeTab="currentTab" @onSelectTab="onSelectCurrentTab" />
        <div class="board-list-wrap">
          <div class="mu-heading">
            <!-- board-list-search -->
            <standard-border-list-search :searchObj="searchObj" @searchMethod="getMetaDataStandardList"
              :searchList="searchList" />
              <!-- <button @click="downloadSampleFile" type="button" class="mu-btn mu-btn" style="margin-left: 5px;">
                <span>엑셀 샘플</span>
              </button> -->
              <!-- <button @click="downloadSampleFile" type="button" class="mu-btn mu-btn" style="margin-left: 5px;">
                <span>엑셀 업로드</span>
              </button>
              <button @click="downloadSampleFile" type="button" class="mu-btn mu-btn" style="margin-left: 5px;">
                <span>엑셀 데이터</span>
              </button> -->
            <!-- //board-list-search -->
            <div class="mu-item-group">
              <div class="mu-selectbox w120" v-if="currentTab == 1">
                <select name="searchType4" :value="currentFormCheckedOnly" @change="onChangeFormCheckedOnly">
                  <option v-for="(item, index) in filterFormCheckedOnlyList" :key="index" :value="item.name">
                    {{ item.description }}
                  </option>
                </select>
              </div>
              <div class="mu-selectbox w120">
                <select name="searchType3" :value="currentUsed" @change="onChangeStatus">
                  <option v-for="(item, index) in filterStatusList" :key="index" :value="item.name">
                    {{ item.description }}
                  </option>
                </select>
              </div>
              <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getMetaDataStandardList" />
            </div>
          </div>
          <div v-if="selectType == 'term'" class="board-list">
            <table class="mu-grid">
              <colgroup>
                <col style="width: 15%" />
                <col style="width: 20%" />
                <col style="width: 15%" />
                <col style="width: 15%" />
                <col style="width: 15%" />
                <col style="width: 15%" />
                <col style="width: 10%" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">용어명</th>
                  <th scope="col">영문약어명</th>
                  <th scope="col">도메인명</th>
                  <th scope="col">출처</th>
                  <th scope="col">등록일자</th>
                  <th scope="col">사용여부</th>
                  <th scope="col">관리</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in dataList" :key="index">
                  <td>
                    <a @click="goToDetail(item, selectType)" :title="item.name ? item.name : '-'">
                      {{ item.name ? item.name : '-' }}
                    </a>
                  </td>
                  <td>{{ item.english_sign ? item.english_sign : '-' }}</td>
                  <td :title="item.domain ? item.domain : '-'">{{ item.domain ? item.domain : '-' }}</td>
                  <td :title="item.final_version_date ? item.final_version_date : '-'">{{ item.final_version_date ? item.final_version_date : '-' }}</td>
                  <td>{{ $yyyyMMdd(item.created_date) }}</td>
                  <td>{{ getUsed(item.used) }}</td>
                  <td>
                    <button v-if="item.used && item.used == 'Y'" type="button" class="mu-btn mu-btn-sm" @click="deleteSoft(item.id, 'term')"><i
                      class="mu-icon-svg del"></i><span>삭제</span>
                    </button>
                  </td>
                </tr>
                <tr v-if="dataList.length === 0">
                  <td colspan="7">
                    <p class="nodata">데이터가 존재하지 않습니다.</p>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div v-if="selectType == 'word'" class="board-list">
            <table class="mu-grid">
              <colgroup>
                <col style="width: 15%" />
                <col style="width: 15%" />
                <col style="width: 15%" />
                <col style="width: 20%" />
                <col style="width: 15%" />
                <col style="width: 15%" />
                <col style="width: 10%" />
                <col style="width: 10%" />
                <col style="width: 10%" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">단어명</th>
                  <th scope="col">영문약어명</th>
                  <th scope="col">단어영문명</th>
                  <th scope="col">형식단어여부</th>
                  <th scope="col">도메인분류어</th>
                  <th scope="col">출처</th>
                  <th scope="col">등록일자</th>
                  <th scope="col">사용여부</th>
                  <th scope="col">관리</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in dataList" :key="index">
                  <td>
                    <a @click="goToDetail(item, selectType)" :title="item.name ? item.name : '-'">
                      {{ item.name ? item.name : '-' }}
                    </a>
                  </td>
                  <td> {{ item.english_sign ? item.english_sign : '-' }} </td>
                  <td>{{ item.word_eng ? item.word_eng : '-' }}</td>
                  <td :title="item.form_checked ? item.form_checked : '-'">{{ item.form_checked ? item.form_checked : '-' }}</td>
                  <td :title="item.domain_classified_name ? item.domain_classified_name : '-'">{{ item.domain_classified_name ? item.domain_classified_name : '-' }}</td>
                  <td>{{ item.final_version_date }}</td>
                  <td>{{ $yyyyMMdd(item.created_date) }}</td>
                  <td>{{ getUsed(item.used) }}</td>
                  <td>
                    <button type="button" class="mu-btn mu-btn-sm" @click="deleteSoft(item.id, 'word')"><i
                      class="mu-icon-svg del"></i><span>삭제</span>
                    </button>
                  </td>
                </tr>
                <tr v-if="dataList.length === 0">
                  <td colspan="9">
                    <p class="nodata">데이터가 존재하지 않습니다.</p>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div v-if="selectType == 'domain'" class="board-list">
            <table class="mu-grid">
              <colgroup>
                <col style="width: 15%" />
                <col style="width: 15%" />
                <col style="width: 15%" />
                <col style="width: 20%" />
                <col style="width: 15%" />
                <col style="width: 15%" />
                <col style="width: 10%" />
                <col style="width: 10%" />
                <col style="width: 10%" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">도메인명</th>
                  <th scope="col">데이터 타입</th>
                  <th scope="col">데이터 길이</th>
                  <th scope="col">도메인 그룹명</th>
                  <th scope="col">도메인 분류명</th>
                  <th scope="col">출처</th>
                  <th scope="col">등록일자</th>
                  <th scope="col">사용여부</th>
                  <th scope="col">관리</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in dataList" :key="index">
                  <td>
                    <a @click="goToDetail(item, selectType)" :title="item.name ? item.name : '-'">
                      {{ item.name ? item.name : '-' }}
                    </a>
                  </td>
                  <td> {{ item.data_type ? item.data_type : '-' }} </td>
                  <td>{{ item.data_size ? item.data_size : '-' }}</td>
                  <td :title="item.group_name ? item.group_name : '-'">{{ item.group_name ? item.group_name : '-' }}</td>
                  <td :title="item.domain_classified_name ? item.domain_classified_name : '-'">{{ item.domain_classified_name ? item.domain_classified_name : '-' }}</td>
                  <td>{{ item.final_version_date }}</td>
                  <td>{{ $yyyyMMdd(item.created_date) }}</td>
                  <td>{{ getUsed(item.used) }}</td>
                  <td>
                    <button type="button" class="mu-btn mu-btn-sm" @click="deleteSoft(item.id, 'domain')"><i
                      class="mu-icon-svg del"></i><span>삭제</span>
                    </button>
                  </td>
                </tr>
                <tr v-if="dataList.length === 0">
                  <td colspan="9">
                    <p class="nodata">데이터가 존재하지 않습니다.</p>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <!-- mu-pagination -->
          <pagination :paging="pagingObj" @onClickPage="onClickPage" />
          <!-- //mu-pagination -->
        </div>
        <div style="text-align: center; margin-top: 15px;">
          <button @click="goToRegister(currentTab)" type="button" class="mu-btn mu-btn mu-btn-primary" style="margin-left: 5px;">
            <i class="mu-icon-svg add"></i>
            <span>신규 등록</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { confirmAlert, errorAlert, successAlert } from '@/components/functional/alert/alert-default';
import selectTab from "@/components/functional/selectTab/select-tab.vue";
import PageSize from "@/pages/admin/manager/system/user/page-size";
import Pagination from "@functional/pagination/pagination";
import StandardBorderListSearch from "@organisms/standard/standard-border-list-search.vue";
import { useRouter } from 'vue-router';

definePageMeta({
  name: "AdminMetaDataStandard",
  layout: 'admin',
})

const { $ofetch } = useNuxtApp();

const tabs = ref([
  { idx: 0, key: "표준 용어", type: "term" },
  { idx: 1, key: "표준 단어", type: "word" },
  { idx: 2, key: "표준 도메인", type: "domain" },
])

// 사용/미사용 필터 항목
const filterStatusList = ref([
  {
    name: "",
    description: "전체"
  },
  {
    name: "Y",
    description: "사용"
  },
  {
    name: "N",
    description: "미사용"
  },
])

// 분류어 여부
const filterFormCheckedOnlyList = ref([
  {
    name: "",
    description: "전체"
  },
  {
    name: "Y",
    description: "분류어만"
  }
])
const currentFormCheckedOnly = ref("")

const currentUsed = ref("Y");

const currentTab = ref(0)
const selectType = ref("term")
const pagingObj = ref({
  total: 0,
  limit: 10,
  block: 10,
  currentPage: 1,
});
const dataList = ref([]);
const searchObj = ref({
  searchType: "all",
  searchWord: "",
  changedTab: false,
});
const searchList = ref([
  {
    value: "kor",
    text: "한글명"
  },
  {
    value: "eng",
    text: "영문명"
  },
  {
    value: "eng_sign",
    text: "영문약어명"
  },
  {
    value: "domain_name",
    text: "도메인명"
  },
])

const route = useRoute();
const router = useRouter();

const getMetaDataStandardList = async (offset = 1) => {
  const param = {
    select_type: selectType.value,
    offset,
    limit: pagingObj.value.limit,
    searchType: searchObj.value.searchType,
    searchWord: searchObj.value.searchWord,
    used: currentUsed.value,
    formChecked: currentFormCheckedOnly.value
  };

  await $ofetch("/api/metadata/standard/list", {
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
      dataList.value = res.data.list;
    } else {
      initListData()
      await errorAlert("조회에 실패하였습니다.")
    }
  }).catch(async (err) => {
    initListData()
    await errorAlert("조회에 실패하였습니다.")
  });
};

const goToDetail = (item, selectType) => {
    router.push({
      path: `/admin/data/meta-data/standard/${selectType}/detail`,
      query: { id: item.id },
    });
};

const onClickPage = (page) => {
  getMetaDataStandardList(page);
};

onMounted(() => {
  getMetaDataStandardList();
});

// 표준 용어: term / 표준 단어: word / 표준 도메인: domain
const onSelectCurrentTab = (curTabObj) => {
  if (curTabObj) {
    currentTab.value = curTabObj.idx
    selectType.value = curTabObj.type
  }
  else {
    currentTab.value = 0
    selectType.value = "term"
  }

  initListData()
  initSearchOption()
  settingSearchList(currentTab.value)
  getMetaDataStandardList()
}

const settingSearchList = (tab) => {
  if (tab == 0) {
    searchList.value = [
      {
        value: "kor",
        text: "한글명"
      },
      {
        value: "eng",
        text: "영문명"
      },
      {
        value: "eng_sign",
        text: "영문약어명"
      },
      {
        value: "domain_name",
        text: "도메인명"
      },
    ]
  } else if (tab == 1) {
    searchList.value = [
      {
        value: "kor",
        text: "한글명"
      },
      {
        value: "eng_sign",
        text: "영문약어명"
      },
      {
        value: "word_eng",
        text: "단어영문명"
      }
    ]
  } else if (tab == 2) {
    searchList.value = [
      {
        value: "kor",
        text: "도메인명"
      },
      {
        value: "group_name",
        text: "도메인그룹명"
      },
      {
        value: "domain_classified_name",
        text: "도메인분류명"
      },
    ]
  } else {
    searchList.value = [
      {
        value: "kor",
        text: "한글명"
      },
      {
        value: "eng",
        text: "영문명"
      },
      {
        value: "eng_sign",
        text: "영문약어명"
      },
      {
        value: "domain_name",
        text: "도메인명"
      },
    ]
  }
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

  currentUsed.value = 'Y'
  currentFormCheckedOnly.value = ''
}

const initListData = () => {
  dataList.value = []
  pagingObj.value = {
    total: 0,
    limit: 10,
    block: 10,
    currentPage: 1,
  }
}

const onChangeStatus = (e) => {
  currentUsed.value = e.target.value
  getMetaDataStandardList();
}

const onChangeFormCheckedOnly = (e) => {
  currentFormCheckedOnly.value = e.target.value
  getMetaDataStandardList();
}

const getUsed = (used) => {
  if (used) {
    if (used == 'Y') return "사용"
    if (used == 'N') return "미사용"
    return used
  } else {
    return "-"
  }
}

// 표준 용어, 단어, 도메인 신규등록 페이지 이동
const goToRegister = (tab) => {
  if (tab == 0) router.push(`/admin/data/meta-data/standard/term/register`)
  if (tab == 1) router.push(`/admin/data/meta-data/standard/word/register`)
  if (tab == 2) router.push(`/admin/data/meta-data/standard/domain/register`)
}

// 표준 용어, 단어, 도메인 삭제처리 (미사용으로 변경)
const deleteSoft = async (id, select_type) => {
  if (await confirmAlert("삭제하시겠습니까?")) {
    await $ofetch(`/api/metadata/standard/deleteSoft?id=${id}&select_type=${select_type}`, {
      method: 'DELETE'
    }).then(async (res) => {
      if (res && res.data) {
        if (res.data.deleteResult) {
          await successAlert("삭제되었습니다.")
          getMetaDataStandardList(pagingObj.value.currentPage)
        } else {
          await errorAlert("삭제에 실패하였습니다.")
        }
      }
    }).catch(async (err) => {
      await errorAlert("삭제에 실패하였습니다.")
    })
  }
}

// 액셀 샘플 다운로드
const downloadSampleFile = () => {
  const filename = `sample_datastandard.xlsx`;
  const filepath = `/excel/${filename}`;
  const link = document.createElement('a');
  link.href = filepath;
  link.download = filename;
  link.click();
}

// 액셀 일괄 업로드
const popupFileBrowser = () => {

}
</script>
