<template>
  <div class="contents">
    <div>
      <!-- content-search-wrap -->
      <div class="content-search-wrap">
        <form action="">
          <fieldset>
            <legend>통합검색</legend>
            <div class="inner-search">
              <div class="mu-input-icon">
                <div class="category-group" v-if="checkUseFilter">
                  <div class="category">
                    <em class="category-label">
                      필터적용중 <span>
                      <button @click="deleteCategoryTag()"><i class="mu-icon-svg item-delete"></i></button>
                    </span>
                    </em>
                  </div>
                </div>
                <input type="text" class="mu-input" placeholder=" 검색어를 입력해 주세요." title="검색어입력"
                 @keypress.enter.prevent="checkValidate(word)" @input="checkValidateInput(word)"
                 v-model="word" />
                <button v-if="word" type="button" class="mu-btn mu-btn-delete"
                  @click="onClickDeleteButton">
                  <i class="mu-icon-svg item-delete"></i>
                  <span>삭제</span>
                </button>
                <button type="button" class="mu-btn mu-btn-sch"
                  @click="checkValidate(word)">
                  <i class="mu-icon-svg search"></i>
                  <span>검색</span>
                </button>
              </div>
            </div>
            <div class="inner-filter">
              <button type="button" :class="searchOption" @click="openDetailSearch()">
                <!-- data-filter-wrap를 열고 닫는 버튼 -->
                <i class="mu-icon-svg" :class="isDetailSearch ? 'delete' : 'add'"></i>
                <span>상세검색</span>
              </button>
            </div>
          </fieldset>
        </form>
        <!-- 동의어 -->
        <dl class="synonym-keyword" v-if="printSynonymList.length > 0">
          <dt>동의어</dt>
          <dd>
            <div class="tags-group">
              <div class="tag" v-for="item in printSynonymList">
                <span class="tag-label" :style="isTermStyle(item)">{{ item.name }}</span>
              </div>
            </div>
          </dd>
        </dl>
        <!-- //동의어 -->
        <!-- data-filter-wrap -->
        <div v-if="isDetailSearch" class="data-filter-wrap">
          <div class="content-detail-search">
            <div class="mu-heading">
              <h4>상세검색</h4>
            </div>
            <div class="detail-search-form">
              <div class="mu-form mu-form-vertical">
                <div class="mu-form-item" v-for="(df, idx) in detailFilters" >
                  <div class="btn-category">
                    <div class="mu-checkbox all" v-if="currentTab == 1 && idx == 'SYS'">
                      <input :type="df.all ? 'radio' : 'checkbox'" :name="idx" :id="'ckbex_'+idx" value="ALLs" checked disabled>
                      <label :for="'ckbex_'+idx"> <span>{{ getRootDescription(idx) }} 전체</span></label>
                    </div>
                    <div class="mu-checkbox all" v-else>
                      <input :type="df.all ? 'radio' : 'checkbox'" :name="idx" :id="'ckb_'+idx" value="ALL" @change="onChangeCheckBox(idx, $event);" :checked="df.all">
                      <label :for="'ckb_'+idx"> <span>{{ getRootDescription(idx) }} 전체</span></label>
                    </div>
                    <div v-for="(tg, idx2) in df.content" class="mu-checkbox">
                      <input v-if="currentTab == 1 && idx == 'SYS'" :type="idx != 'DATE' ? 'checkbox' : 'radio'" :name="idx" :id="'ckbex_'+tg.name" :value="tg.name" @change="idx != 'DATE' ? onChangeCheckBox(idx, $event) : onChangeDateCheckBox(idx, $event)" :checked="false" disabled>
                      <input v-else type="checkbox" :name="idx" :id="'ckb_'+tg.name" :value="tg.name" @change="idx != 'DATE' ? onChangeCheckBox(idx, $event) : onChangeDateCheckBox(idx, $event)" :checked="tg.checked">
                      <label v-if="currentTab == 1 && idx == 'SYS'" :for="'ckbex_'+tg.name"><span>{{ tg.description }}<em v-if="idx != 'TAG' && idx != 'DATE'">({{ tg.count }})</em></span></label>
                      <label v-else :for="'ckb_'+tg.name"><span>{{ tg.description }}<em v-if="idx != 'TAG' && idx != 'DATE'">({{ tg.count }})</em></span></label>
                    </div>
                    <!-- 달력 -->
                    <div v-if="idx == 'DATE'" class="period-set">
                      <date-picker
                        @change="selectStartDate($event)"
                        v-model:date="startDateText"
                        v-model="startDate"
                        :type="datetimeType"
                        :time-picker-options="timePickerOptions"
                        :lang="lang"
                        :clearable="false"
                        placeholder="시작일"
                      ></date-picker>
                      <span>-</span>
                      <date-picker
                        @change="selectEndDate($event)"
                        v-model:date="endDateText"
                        v-model="endDate"
                        :type="datetimeType"
                        :time-picker-options="timePickerOptions"
                        :lang="lang"
                        :clearable="false"
                        placeholder="종료일"
                      ></date-picker>
                      &nbsp;&nbsp;
                      <button type="button" class="mu-btn mu-btn-icon-only"
                        title="달력 초기화"
                        @click.stop="() => refreshDate(idx)"><i class="mu-icon-svg refresh"></i>
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="mu-button-wrap">
              <button type="button" class="mu-btn mu-btn-lg"
                @click="resetSearchParam()">
                <i class="mu-icon-svg refresh"></i>
                <span>초기화</span>
              </button>
              <button type="button" class="mu-btn mu-btn-lg mu-btn-primary"
                @click="currentTab == 0 ? getSearch(0) : getSearchExternal(0)">
                <span>검색</span>
              </button>
            </div>
            <button type="button" class="mu-btn mu-btn-bg-non btn-filter-close" @click="openDetailSearch">
              <span>상세 필터 닫기</span>
            </button>
          </div>
        </div>
        <!-- //data-filter-wrap -->
      </div>
      <!-- //content-search-wrap -->
      <div class="inner">
        <!-- mu-section : 영역나누기 -->
        <div class="mu-section">
          <!-- category-wrap : L 카테고리리스트 -->
          <left-menu-list-v2
            @onClickMenuItem="onClickMenuItem"
            :detailFilters="detailFilters"
            @onClickDelete="onClickDelete"
            :currentTab="currentTab"/>
          <!-- //category-wrap -->
          <!-- data-result-wrap : R 통합검색리스트 -->
          <div class="data-result-wrap">
            <div class="mu-tab-box sub-tab">
              <ul class="mu-tab">
                <li :class="currentTab == 0 ? 'active' : ''"><a @click="onChangeCurrentTab(0)" title="선택됨">데이터 레이크 ({{ entitiesDatalakeCount }})</a></li>
                <li :class="currentTab == 1 ? 'active' : ''"><a @click="onChangeCurrentTab(1)">외부 데이터 ({{ entitiesExternalCount }})</a></li>
              </ul>
            </div>
            <!-- mu-heading -->
            <div class="mu-heading">
              <h4 v-if="currentTab == 0">
                총<span><strong>{{ entitiesDatalakeCount }}</strong>개</span>
              </h4>
              <h4 v-if="currentTab == 1">
                총<span><strong>{{ entitiesExternalCount }}</strong>개</span>
              </h4>
              <div class="mu-item-group">
                <div class="btn-sort">
                  <div class="mu-radio">
                    <input type="radio" id="sortDate" name="listSorting" @click="onSelectSort('createTime|DESCENDING')">
                    <label for="sortDate"><span>등록일 순</span></label>
                  </div>
                  <div class="mu-radio">
                    <input type="radio" id="sortTable" name="listSorting" @click="onSelectSort('name|ASCENDING')">
                    <label for="sortTable"><span>테이블명 순</span></label>
                  </div>
                </div>
              </div>
            </div>
            <!-- //mu-heading -->
            <div class="data-result">
              <!-- 데이터 레이크 -->
              <ul class="data-box-list" v-if="currentTab == 0">
                <!-- data-box 목록에 li data-box하나가 그대로 상세페이지상단 요약임 -->
                <li v-if="entitiesDatalakeCount > 0" class="data-box" v-for="(item, index) in entities" :key="index">
                  <div class="inner-data visual" @click="onSelectDetail(item.guid)">
                    <!-- 데이터 타입 유형 아이콘 출력 (총 7개) -->
                    <i v-if="item.typeName && item.typeName.includes('column')" class="ico-data data-01"></i>
                    <i v-else-if="item.typeName && item.typeName.includes('table')" class="ico-data data-05"></i>
                    <i v-else-if="item.typeName && item.typeName.includes('DB')" class="ico-data data-02"></i>
                    <i v-else class="ico-data data-03"></i>
                  </div>
                  <!-- inner-data : 데이터내용 -->
                  <div class="inner-data" @click="onSelectDetail(item.guid)">
                    <!-- category-group : 맵핑된 카테고리출력 (최대 1줄) (모든 카테고리는 상세페이지에 접속하여 확인 가능) -->
                    <div class="category-group">
                      <div class="category" v-if="checkCollectiontype(item)">
                        <em class="category-label">{{ getCollectiontype(item) }}</em>
                      </div>
                      <div v-for="(classificationName, idx1) in item.classificationNames" :key="idx1">
                        <div class="category" v-if="categoryTagFilter(classificationName)">
                          <em class="category-label">{{ getKrName(classificationName) }}</em>
                        </div>
                      </div>
                    </div>
                    <!-- //category-group -->
                    <!-- title-group -->
                    <div class="title-group">
                      <a @click="onSelectDetail(item.guid)">
                        <strong class="data-title">{{ item.attributes && item.attributes.comment ? item.attributes.comment : "" }}</strong>
                        <p class="data-name">{{ item.displayText }}</p>
                        <p class="data-desc">{{ item.attributes && item.attributes.userDescription ? item.attributes.userDescription : "" }}</p>
                      </a>
                    </div>
                    <!-- //title-group -->
                    <!-- tags-group -->
                    <div class="tags-group">
                      <em v-for="(tag, idx1) in item.labels" :key="idx1" class="tag">
                        <span class="tag-label">{{ tag }}</span>
                      </em>
                    </div>
                    <!-- //tags-group -->
                  </div>
                  <div class="inner-data option">
                    <p >생성일 : {{ getDateFormat(item.attributes && item.attributes.parameters && item.attributes.parameters.created_date ? item.attributes.parameters.created_date : null) }}</p>
                    <p >수정일 : {{ getDateFormat(item.attributes && item.attributes.parameters && item.attributes.parameters.modified_date ? item.attributes.parameters.modified_date : null) }}</p>
                    <p v-html="checkDataAccess(item.dataAccess)"></p>
                    <p v-if="item.attributes && item.attributes.qualifiedName">DB : {{ item.attributes.qualifiedName.split(".")[0] }}</p>
                    <p v-if="item.attributes && item.attributes.table">테이블 : {{ item.attributes.qualifiedName.split(".")[1] }}</p>
                  </div>
                </li>
                <li v-else><p class="nodata">검색결과가 존재하지 않습니다.<span>다른 검색어를 입력해주세요.</span></p></li>
                <!-- //data-box -->
              </ul>
              <!-- 외부 데이터 -->
              <ul class="data-box-list" v-if="currentTab == 1">
                <li v-if="entitiesExternalCount > 0" class="data-box" v-for="(item, index) in entities">
                  <div class="inner-data visual" @click="onSelectDetail(item.id)">
                    <!-- 데이터 타입 유형 아이콘 출력 (총 7개) -->
                    <i class="ico-data data-07"></i>
                  </div>
                  <div class="inner-data">
                    <div class="category-group">
                      <div
                        v-for="(categories, idx1) in item.categories"
                        :key="idx1"
                      >
                        <div class="category">
                          <em class="category-label">{{ getDescription(categories) }}</em>
                        </div>
                      </div>
                      <div
                        v-for="(system, idx2) in item.systems"
                        :key="idx2"
                      >
                        <div class="category">
                          <em class="category-label" style="max-width: 1000px;" :title="system">{{ system }}</em>
                        </div>
                      </div>
                    </div>
                    <div class="title-group">
                      <a @click="onSelectDetail(item.id)">
                        <strong class="data-title">{{ item.name }}</strong>
                        <p class="data-link" style="color: #2855ce;">{{ item.link }}</p>
                        <p class="data-desc">{{ formattedDescription(item) }}</p>
                        <!-- <div v-html="ellipsisText(item.description)"></div> -->
                      </a>
                    </div>
                    <div class="tags-group">
                      <em v-for="(tag, idx1) in item.tags" :key="idx1" class="tag">
                        <span class="tag-label">{{ tag }}</span>
                      </em>
                    </div>
                  </div>
                  <div class="inner-data option">
                    <p>등록일 : {{ $yyyyMMdd(item.create_date) }}</p>
                  </div>
                </li>
                <li v-else><p class="nodata">검색결과가 존재하지 않습니다.<span>다른 검색어를 입력해주세요.</span></p></li>
              </ul>
            </div>
            <!-- mu-pagination -->
            <pagination :paging="pagingObj" @onClickPage="onClickPage" />
            <!-- //mu-pagination -->
          </div>
          <!-- //data-result-wrap -->
        </div>
        <!-- //mu-section -->
      </div>
    </div>
  </div>
  <!-- //contents -->
</template>

<script setup lang="ts">
import pagination from "@functional/pagination/pagination.vue";
import { useUserStore } from "~/store/user.ts";
import { useCategoryStore } from "~/store/category.ts";
import { errorAlert } from "@functional/alert/alert-default.ts";
import DatePicker from "@functional/datepicker/date-picker";
import LeftMenuListV2 from "@functional/leftMenuList/left-menu-list-v2.vue";
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";
import {onMounted, ref } from "vue";
import { onBeforeRouteUpdate } from "vue-router";

definePageMeta({
  layout: "user",
});

const { $ofetch } = useNuxtApp();

// route
const route = useRoute();
const router = useRouter();

// store
const userStore = useUserStore();
const fromPath = userStore.getFromPath;
const params = userStore.getParams;

const categoryStore = useCategoryStore();
const searchParams = categoryStore.getSearchParams

// data
const word = ref<string>(""); // 검색어

interface PagingObj {
  total: number;
  limit: number;
  block: number;
  currentPage: number;
}

// 현재 탭
const currentTab = ref(0);

// 페이징
const pagingObj = ref<PagingObj>({
  total: 0, // 전체 데이터 건수
  limit: 10, // 한 페이지에 보여줄 데이터 건수
  block: 10, // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
});

// 카테고리, 시스템, 태그, 등록일
const detailFilters = ref({
  CAT: {
    all: true,
    ord: 1,
    name: '카테고리',
    content: [
    {
        "root": "CAT",
        "rootDescription": "카테고리",
        "name": "CATFM",
        "description": "재경",
        "count": 2,
        "checked": false,
      },
    ],
    count: 0
  },
  SYS: {
    all: true,
    ord: 2,
    name: '시스템',
    content: [
    {
      "root": "SYS",
      "rootDescription": "시스템",
      "name": "SYSERP",
      "description": "ERP 시스템",
      "count": 2,
      "checked": false,
    },
    ],
    count: 0
  },
  // TAG: {
  //   all: true,
  //   ord: 3,
  //   name: '태그',
  //   content: [
  //     {
  //       "root": "TAG",
  //       "rootDescription": "태그",
  //       "name": "태그1",
  //       "description": "태그1",
  //       "count": 0,
  //       "checked": false,
  //     },
  //   ],
  //   count: 0
  // },
  DATE: {
    all: true,
    ord: 4,
    name: '등록일',
    content: [
      {
        "root": "DATE",
        "rootDescription": "등록일",
        "name": "2024",
        "description": "2024년",
        "count": 0,
        "checked": false,
      },
    ],
    count: 0
  }
})

// 정렬 - 선택 항목
// 정렬 기준 - name(테이블명 순), createTime (등록일 순)
// 정렬 순서 - ASCENDING, DESCENDING
const sort = ref("createTime|DESCENDING")

const synonymList = ref<Array<string>>([]); // 동의어 목록
const printSynonymList = ref<any>([]); // 검색창 아래 동의어 목록

const entities = ref<any>([]) // 통합검색 결과 리스트
const entitiesCount = ref<any>(0) // 통합검색 결과 전체 개수
const entitiesDatalakeCount = ref<any>(0) // 데이터 레이크 전체 개수
const entitiesExternalCount = ref<any>(0) // 외부 데이터 전체 개수

const classification = ref<string>(""); // 카테고리

// 검색 필터 옵션
const searchOption = ref<string>("mu-btn mu-btn-lg mu-btn-bg-non btn-filter-toggle");
const isDetailSearch = ref(false)

// ================================================================================================

// 신규 검색 필터 옵션
const showFilterWrap = ref<boolean>(false);

const datetimeType = ref<string>("datetime");

interface TimeOptions {
  start: string;
  step: string;
  end: string;
  format: string;
  timePickerIncrement?: number;
}

const timePickerOptions = ref<TimeOptions>({
  start: "00:00",
  step: "00:30",
  end: "23:30",
  format: "hh:mm",
  timePickerIncrement: 30,
});
const startDate = ref<number>(0);
const startDateText = ref<string>("")

const endDate = ref<number>(0);
const endDateText = ref<string>("")

interface Lang {
  yearFormat: string;
  days: Array<string>;
  months: Array<string>;
  pickers: Array<string>;
  placeholder: {
    date: string;
    dateRange: string;
  }
}
// ================================================================================================

const lang = ref<Lang>({
  yearFormat: "YYYY년",
  days: ["일", "월", "화", "수", "목", "금", "토"],
  months: [
    "1월",
    "2월",
    "3월",
    "4월",
    "5월",
    "6월",
    "7월",
    "8월",
    "9월",
    "10월",
    "11월",
    "12월",
  ],
  pickers: ["next 7 days", "next 30 days", "previous 7 days", "previous 30 days"],
  placeholder: {
    date: "Select Date",
    dateRange: "Select Date Range",
  },
});

const checkUseFilter = computed(() => { // 상세검색 필터 사용중 여부
  // if (detailFilters.value['CAT'] && detailFilters.value['SYS'] && detailFilters.value['TAG'] && detailFilters.value['DATE']) {
  //   if (detailFilters.value['CAT'].all && detailFilters.value['SYS'].all && detailFilters.value['TAG'].all && detailFilters.value['DATE'].all) {
  //     return false
  //   }
  // }
  if (detailFilters.value['CAT'] && detailFilters.value['SYS'] && detailFilters.value['DATE']) {
    if (detailFilters.value['CAT'].all && detailFilters.value['SYS'].all && detailFilters.value['DATE'].all) {
      return false
    }
  }
  return true
})

await categoryStore.fetchCategories(); // 카테고리 목록 조회

onBeforeRouteLeave((to, from, next) => {
  userStore.setFromPath(from.path) // 검색화면에서 상세페이지로 이동했는지 확인용
  next()
})

onMounted(async () => {
  // 상세페이지에서 통합검색으로 넘어온 경우 처리
  if (fromPath == '/search/detail' || fromPath == '/search/external/detail') {
    // 검색 탭 설정
    if (fromPath == '/search/external/detail') {
      currentTab.value = userStore.getCurrentSearchTab
    }

    // 검색 후 상세페이지를 간 것이 아닌 경우
    if (!searchParams.word &&
    (Object.keys(searchParams.detailFilters).length === 0 && searchParams.detailFilters.constructor === Object)) {
      // 상세필터 조회
      await getDetailFilters()
      // 검색어 설정
      await refreshDate('DATE') // 날짜 초기화
      pagingObj.value = {
        total: 0, // 전체 데이터 건수
        limit: 10, // 한 페이지에 보여줄 데이터 건수
        block: 10, // 한 화면에서 보여줄 페이지 수
        currentPage: 1, // 현재 페이지 번호
      }; // 페이징 초기화

      if (currentTab.value == 0) await getSearch(0) // 통합검색 - 데이터 레이크
      if (currentTab.value == 1) await getSearchExternal(0) // 통합검색 - 외부 데이터
    } else {
      // 1. 기존 검색어, 상세필터 내용 가져오기
      word.value = searchParams.word
      detailFilters.value = searchParams.detailFilters
      pagingObj.value = searchParams.pagingObj

      // 2. 등록일 전체가 아닌 경우 필터 설정
      if (!detailFilters.value['DATE'].all) {
        detailFilters.value['DATE'].content.forEach((dt) => {
          if (dt.checked)  {
            setDateFilter(dt.name)
          }
        })
      } else {
        await refreshDate('DATE') // 날짜 초기화
      }
      if (currentTab.value == 0) await getSearch(pagingObj.value.currentPage - 1)// 통합검색 - 데이터 레이크
      if (currentTab.value == 1) await getSearchExternal(pagingObj.value.currentPage - 1)// 통합검색 - 외부 데이터
    }
  } else {
    // 상세필터 조회
    await getDetailFilters()
    // 검색어 설정
    await refreshDate('DATE') // 날짜 초기화
    pagingObj.value = {
      total: 0, // 전체 데이터 건수
      limit: 10, // 한 페이지에 보여줄 데이터 건수
      block: 10, // 한 화면에서 보여줄 페이지 수
      currentPage: 1, // 현재 페이지 번호
    }; // 페이징 초기화

    if ((fromPath == '/main' || fromPath == '/search/category') && categoryStore.getMainSearch) { // 메인화면에서 검색 기능으로 넘어온 경우
      // 카테고리 클릭으로 넘어온 경우
      if (categoryStore.getMainSelectCategory && categoryStore.getMainSelectCategory.name) {
        // 상세필터에서 카테고리 필터 선택 상태로 설정
        if (categoryStore.getMainSelectCategory.name.startsWith('CAT') && detailFilters.value['CAT']) {
          if (detailFilters.value['CAT'].content) {
            detailFilters.value['CAT'].content.forEach((el) => {
              if (categoryStore.getMainSelectCategory.name == el.name) {
                el.checked = true
                detailFilters.value['CAT'].all = false
              } else {
                el.checked = false
              }
            })
            await categoryStore.setSearchParamsDetailFilters(detailFilters.value)
          }
        } else if (categoryStore.getMainSelectCategory.name.startsWith('SYS') && detailFilters.value['SYS']) {
          if (detailFilters.value['SYS'].content) {
            detailFilters.value['SYS'].content.forEach((el) => {
              if (categoryStore.getMainSelectCategory.name == el.name) {
                el.checked = true
                detailFilters.value['SYS'].all = false
              } else {
                el.checked = false
              }
            })
            await categoryStore.setSearchParamsDetailFilters(detailFilters.value)
          }
        }
      } else {
        word.value = searchParams.word
      }
      categoryStore.setMainSearch(false) // 검색화면으로 왔으므로 false 처리
      categoryStore.setMainSelectCategory(null)
    }
    if (currentTab.value == 0) await getSearch(pagingObj.value.currentPage - 1)// 통합검색 - 데이터 레이크
    if (currentTab.value == 1) await getSearchExternal(pagingObj.value.currentPage - 1)// 통합검색 - 외부 데이터
  }

  await getDbInfo(); // DB 정보 조회
});

const formattedDescription = (item: { description: string; }) => {
  if (item.description) {
    const firstLine = item.description.split(/(\r\n|\n|\r)/)[0].trim(); // 줄바꿈을 기준으로 데이터 가져오기
    return firstLine;
  } else {
    return item.description; // 공백이 없으면 전체 텍스트 반환
  }
};

// 데이터 레이크 | 외부 데이터
const onChangeCurrentTab = (tab) => {
  currentTab.value = tab
  if (currentTab.value == 0) getSearch(0)
  if (currentTab.value == 1) getSearchExternal(0)
}

const openDetailSearch = async () => {
  isDetailSearch.value = !isDetailSearch.value
}

const getRootDescription = (key) => {
  if (key == 'CAT') return "카테고리"
  else if (key == 'SYS') return "시스템"
  // else if (key == 'TAG') return "태그"
  else if (key == "DATE") return "등록일"
  else return ""
}

const setDateFilter = async (selectedDate: string) => {
  const selectedStartDate = new Date(selectedDate + '-01-01')
  const selectedEndDate = new Date(selectedDate + '-12-31')

  startDate.value = selectedStartDate.getTime();
  startDateText.value = selectedDate + "-01-01 00:00";
  endDate.value = selectedEndDate.getTime();
  endDateText.value = selectedDate + "-12-31 00:00";
}

// 카테고리 최상위 뎁스 표기 필터링 처리
const categoryTagFilter = (name) => {
  // 카테고리, 시스템
  if (name != 'CAT' && name != 'SYS') return true;
  return false;
}

// 날짜 클릭 이벤트
const selectStartDate = async (event) => {
  if (event != null) {
    detailFilters.value['DATE'].all = false
    startDate.value = new Date(event).getTime()
    startDateText.value = event

    // 등록일 필터 해제 처리, startDate 검색기록 저장
    detailFilters.value['DATE'].content.forEach((dt) => {
      dt.checked = false
    })
  }
}

const selectEndDate = async (event) => {
  if (event != null) {
    detailFilters.value['DATE'].all = false
    endDate.value = new Date(event).getTime()
    endDateText.value = event

    // 등록일 필터 해제 처리, startDate 검색기록 저장
    detailFilters.value['DATE'].content.forEach((dt) => {
      dt.checked = false
    })
  }
}

// 날짜 초기화 버튼
const refreshDate = (key) => {
  const now = new Date()
  let date = new Date(now);
  let year = date.getFullYear();
  let month = String(date.getMonth() + 1).padStart(2, "0");
  let day = String(date.getDate()).padStart(2, "0");

  // startDate.value = now.getTime()
  // startDateText.value = year + "-" + month + "-" + day + " 00:00";
  // endDate.value = now.getTime()
  // endDateText.value = year + "-" + month + "-" + day + " 00:00";

  startDate.value = 0
  startDateText.value = "";
  endDate.value = 0
  endDateText.value = "";

  // 필터에도 checked false 적용
  detailFilters.value[key].all = true
  detailFilters.value[key].content.forEach((el) => {el.checked = false})
}

// 체크박스 단일 클릭 시 날짜 설정
const settingOneDate = (name) => {

  let minDateText = name + "-01-01 00:00";
  let maxDateText = name + "-12-31 00:00"

  let minDate = new Date(minDateText).getTime()
  let maxDate = new Date(maxDateText).getTime()

  startDate.value = minDate
  startDateText.value = minDateText;
  endDate.value = maxDate
  endDateText.value = maxDateText;

}

// 체크박스 클릭 시 날짜 설정
const settingDate = (key) => {
  let minYear = ""
  let maxYear = ""
  let minDate = 0
  let maxDate = 0
  let minDateText = ""
  let maxDateText = ""
  let dateArr = []
  let sortedDateArr = []


  detailFilters.value[key].content.forEach((el) => {
    if (el.checked)
      dateArr.push(el.name)
  })

  // 체크박스 여러개의 날짜 조회해서 가장 작은 수를 왼쪽 날짜, 가장 큰 수를 오른쪽 날짜로 설정
  if (dateArr.length >= 2) {
    sortedDateArr = dateArr.sort((a, b) => a - b)
  } else if (dateArr.length == 1) {
    sortedDateArr = dateArr
  }

  minYear = sortedDateArr[0]
  maxYear = sortedDateArr[sortedDateArr.length - 1]

  minDateText = minYear + "-01-01 00:00";
  maxDateText = maxYear + "-12-31 00:00"

  minDate = new Date(minDateText).getTime()
  maxDate = new Date(maxDateText).getTime()

  startDate.value = minDate
  startDateText.value = minDateText;
  endDate.value = maxDate
  endDateText.value = maxDateText;

}

// 조건 클릭 이벤트
const onChangeCheckBox = async (key, event: any) => {
  if (event.target.value == 'ALL') {
    detailFilters.value[key].content.forEach((el) => {el.checked = false})
    detailFilters.value[key].all = true
    detailFilters.value[key].count = 0

    // 등록일 전체 클릭 시 날짜필터값 초기화
    if (key == 'DATE') {
      startDate.value = 0
      startDateText.value = ""
      endDate.value = 0
      endDateText.value = ""
    }
  } else {
    // 외부 데이터의 경우 시스템 필터 전체로 고정(미사용)
    if (currentTab.value == 1 && key == 'SYS') {
      console.log('외부 데이터 시스템 필터 미사용')
    } else {
      detailFilters.value[key].content.forEach((el) => {
        if (el.name == event.target.value) el.checked = event.target.checked
      })
      detailFilters.value[key].count = detailFilters.value[key].content.filter((el) => el.checked == true).length
      detailFilters.value[key].all = false
    }
  }

  // 아무것도 선택하지 않은 상태인 경우 전체 표시
  if (detailFilters.value[key].count == 0) detailFilters.value[key].all = true

  if (currentTab.value == 0) getSearch(0)
  if (currentTab.value == 1) getSearchExternal(0)
}

// 날짜조건 클릭 이벤트
const onChangeDateCheckBox = (key, event: any) => {
  if (event.target.value == 'ALL') {
    detailFilters.value[key].content.forEach((el) => {el.checked = false})
    detailFilters.value[key].all = true
    detailFilters.value[key].count = 0
    startDate.value = 0
    startDateText.value = ''
    endDate.value = 0
    endDateText.value = ''
  } else {
    detailFilters.value[key].content.forEach((el) => {
      if (el.name == event.target.value) {
        el.checked = event.target.checked

        const year = event.target.value
        startDate.value = new Date(year, 0, 1).getTime(); // 1월은 0
        startDateText.value = new Date(year, 0, 1).toDateString();

        // 올해인 경우 오늘까지만 설정
        const todayYear = new Date().getFullYear()

        endDate.value = new Date(year, 11, 31).getTime();
        endDateText.value = new Date(year, 11, 31).toISOString();

      }
    })

    detailFilters.value[key].content.forEach((el) => {
      if (el.name == event.target.value) el.checked = event.target.checked
    })
    detailFilters.value[key].count = detailFilters.value[key].content.filter((el) => el.checked == true).length
    detailFilters.value[key].all = false

  }

  // 아무것도 선택하지 않은 상태인 경우 전체 표시
  if (detailFilters.value[key].count == 0) detailFilters.value[key].all = true

  // 오른쪽 날짜 범위 값
  if (detailFilters.value[key].count >= 1) {
    settingDate('DATE')
  } else {
    refreshDate('DATE')
  }


  if (currentTab.value == 0) getSearch(0)
  if (currentTab.value == 1) getSearchExternal(0)
}

// methods
// 용어 스타일 설정
const isTermStyle = (item: any): { fontWeight: number } | {} => {
  if (item.isTerm) {
    return { fontWeight: 800 };
  } else {
    return {};
  }
};

// DB 정보 조회
const getDbInfo = async (): Promise<void> => {
  await categoryStore.getDbData();
};

// 검색에서 필터적용중 삭제
const deleteCategoryTag = (): void => {
  detailFilters.value['CAT'].all = true
  detailFilters.value['CAT'].content.forEach((el) => {
    el.checked = false
  })
  detailFilters.value['SYS'].all = true
  detailFilters.value['SYS'].content.forEach((el) => {
    el.checked = false
  })
  // detailFilters.value['TAG'].all = true
  // detailFilters.value['TAG'].content.forEach((el) => {
  //   el.checked = false
  // })
  detailFilters.value['DATE'].all = true
  detailFilters.value['DATE'].content.forEach((el) => {
    el.checked = false
  })

  startDate.value = 0;
  startDateText.value = "";
  endDate.value = 0;
  endDateText.value = "";
  if (currentTab.value == 0) getSearch(0)
  if (currentTab.value == 1) getSearchExternal(0)
};

// 한글명 가져오기 - 카테고리
const getKrName = (key: string): string => {
  if (key) {
    return categoryStore.getClassificationNameKr(key);
  } else {
    // 카테고리 미분류
    return "";
  }
};

// collectiontype 파라미터 확인
const checkCollectiontype = (tblEnities) => {
  if (tblEnities && tblEnities.attributes) {
    if (tblEnities.attributes.parameters) {
      if (tblEnities.attributes.parameters.collectiontype) {
        return true
      }
    }
  }
  return false
}

// 카테고리 표기 맨 앞에 collectiontype값이 있으면 표기
const getCollectiontype = (tblEnities) => {
  if (tblEnities && tblEnities.attributes) {
    if (tblEnities.attributes.parameters) {
      if (tblEnities.attributes.parameters.collectiontype) {
        return tblEnities.attributes.parameters.collectiontype
      }
    }
  }
}

// 데이터 레이크 검색
const getSearch = async (offset: number = 0): Promise<void> => {
  printSynonymList.value = [];
  try {

    let param = {};

    // 전체 조회 파라미터
    // const allParam = {
    //   query: word.value,
    //   typeName: '_ALL_ENTITY_TYPES',
    //   limit: 10,
    //   offset: 0,
    //   classification: '',
    //   sortOrder: '',
    //   entityFilters: {
    //     condition: 'OR',
    //     criterion: [
    //       { attributeName: '__typeName', operator: 'eq', attributeValue: 'hive_table'},
    //       { attributeName: '__typeName', operator: 'eq', attributeValue: 'hive_column'}
    //     ]
    //   }
    // }

    // 테이블 조회 파라미터
    param = {
      query: word.value,
      typeName: 'hive_table',
      limit: 10,
      offset: offset,
      classification: classification.value,
      sortBy: sort.value.split("|")[0],
      sortOrder: sort.value.split("|")[1],
      categoryList: [],
      systemList: [],
      // tagList: [],
      checkUseFilter: false,
    }

    // 상세필터 사용 시 파라미터 추가
    if (checkUseFilter.value) {
      param.checkUseFilter = true
      param.categoryList = detailFilters.value.CAT.content.filter((el) => el.checked).map((el) => el.name)
      param.systemList = detailFilters.value.SYS.content.filter((el) => el.checked).map((el) => el.name)
      // param.tagLists = detailFilters.value.TAG.content.filter((el) => el.checked).map((el) => el.name)
      param.startDate = startDate.value
      param.endDate = endDate.value
    }

    // 기존 Atlas 필터 검색
    if (word.value !== "") {
      param.query = word.value;
    }

    param.limit = pagingObj.value.limit;
    param.offset = pagingObj.value.limit * offset;

    // 기존 검색 결과값 초기화
    // entities.value = []
    // entitiesDatalakeCount.value = 0

    // 검색조건 저장
    let searchParams = {
      word: word.value,
      detailFilters: detailFilters.value,
      pagingObj: pagingObj.value
    }
    categoryStore.setSearchParams(searchParams)

    // 외부데이터 전체 카운트 조회
    await getSearchExternalCount(0);

    // Atlas 검색 결과 가져오기
    let res = await $ofetch(`/api/search/keyword`, { method: "POST", body: param });
    if (res && res.errorMessage == "" && res.data.entities) {
      entities.value = res.data.entities
      entitiesDatalakeCount.value = res.data.approximateCount

    } else {
      entitiesDatalakeCount.value = res.data.approximateCount
    }

    pagingObj.value = {
      ...pagingObj.value,
      total: res.data.approximateCount,
      currentPage: offset + 1,
    }

    // ============================= 동의어 조회 파트 ====================================
    // 검색을 했을 때만 동의어 리스트 조회
    if (word.value != "") {
      synonymList.value = [];

      if (word.value.includes(" ")) {
        // " "을 포함하는지 확인
        var words = word.value.split(" "); // 공백을 기준으로 단어 분할
      } else {
        var words = [word.value];
      }

      var filteredWords = words.filter(function (word) {
        return word !== "";
      });
      let originUniqueList = [];
      printSynonymList.value = [];
      for (let k in filteredWords) {
        const param = encodeURI(filteredWords[k]);
        let keywordList = [];
        let temp = {};
        temp.name = filteredWords[k];
        temp.isTerm = true;
        keywordList.push(temp);

        await $ofetch(
          `/api/synonym/list?pageSize=-1&pnum=-1&searchType=all&searchWord=${param}`,
          { method: "GET" }
        ).then((res: { data: { list: any; }; }) => {
          let cur_synonym_list = res.data.list;
          for (let i in cur_synonym_list) {
            let temp = JSON.parse(JSON.stringify(cur_synonym_list[i]));
            temp.name = cur_synonym_list[i].term;
            keywordList.push(temp);
            synonymList.value.push(cur_synonym_list[i].term);
            for (let j in cur_synonym_list[i].synonyms) {
              let subtemp = JSON.parse(JSON.stringify(cur_synonym_list[i].synonyms[j]));
              subtemp.name = subtemp.synonym;
              keywordList.push(subtemp);
              synonymList.value.push(cur_synonym_list[i].synonyms[j].synonym);
            }
          }
        });

        // 동의어가 있는 경우만 정렬 처리 후 저장
        if (keywordList.length > 1) {
          let uniqueKeywordList = keywordList.filter((item, i) => {
            return (
              keywordList.findIndex((item2, j) => {
                return item.name == item2.name;
              }) === i
            );
          });

          originUniqueList.push(uniqueKeywordList);
        }
      }

      originUniqueList.forEach((el) => {
        el.forEach((el2) => {
          printSynonymList.value.push(el2);
        });
      });
    } else {
      synonymList.value = [];
    }
    // ===========================================================================

  } catch (error) {
    console.log(error);
    entities.value = null;
    entitiesCount.value = 0;

    pagingObj.value = {
      ...pagingObj.value,
      total: 0,
      currentPage: 1,
    };
  }
};

// 데이터 레이크 카운트 조회
const getSearchCount = async (offset: number = 0): Promise<void> => {
  printSynonymList.value = [];
  try {

    let param = {};

    // 전체 조회 파라미터
    // const allParam = {
    //   query: word.value,
    //   typeName: '_ALL_ENTITY_TYPES',
    //   limit: 10,
    //   offset: 0,
    //   classification: '',
    //   sortOrder: '',
    //   entityFilters: {
    //     condition: 'OR',
    //     criterion: [
    //       { attributeName: '__typeName', operator: 'eq', attributeValue: 'hive_table'},
    //       { attributeName: '__typeName', operator: 'eq', attributeValue: 'hive_column'}
    //     ]
    //   }
    // }

    // 테이블 조회 파라미터
    param = {
      query: word.value,
      typeName: 'hive_table',
      limit: 10,
      offset: offset,
      classification: classification.value,
      sortBy: sort.value.split("|")[0],
      sortOrder: sort.value.split("|")[1],
      categoryList: [],
      systemList: [],
      // tagList: [],
      checkUseFilter: false,
    }

    // 상세필터 사용 시 파라미터 추가
    if (checkUseFilter.value) {
      param.checkUseFilter = true
      param.categoryList = detailFilters.value.CAT.content.filter((el) => el.checked).map((el) => el.name)
      param.systemList = detailFilters.value.SYS.content.filter((el) => el.checked).map((el) => el.name)
      // param.tagLists = detailFilters.value.TAG.content.filter((el) => el.checked).map((el) => el.name)
      param.startDate = startDate.value
      param.endDate = endDate.value
    }

    // 기존 Atlas 필터 검색
    if (word.value !== "") {
      param.query = word.value;
    }

    param.limit = pagingObj.value.limit;
    param.offset = pagingObj.value.limit * offset;

    // Atlas 검색 카운트 조회
    let res = await $ofetch(`/api/search/keyword`, { method: "POST", body: param });
    if (res && !res.errorMessage) {
      entitiesDatalakeCount.value = res.data.approximateCount
    } else {
      entitiesDatalakeCount.value = 0
    }

  } catch (error) {
    console.log(error);
    entities.value = [];
    entitiesDatalakeCount.value = 0;
  }
};

// 외부 데이터 검색
const getSearchExternal = async (offset: number = 0) => {
  let params = {
    searchWord: word.value,
    offset: offset,
    limit: 10,
    classification: classification.value,
    sortBy: sort.value.split("|")[0],
    sortOrder: sort.value.split("|")[1],
    categoryList: [],
    // systemList: [],
    // tagList: [],
    checkUseFilter: false,
  }

  // 상세필터 사용 시 파라미터 추가
  if (checkUseFilter.value) {
    params.checkUseFilter = true
    params.categoryList = detailFilters.value.CAT.content.filter((el) => el.checked).map((el) => el.name)
    // params.systemList = detailFilters.value.SYS.content.filter((el) => el.checked).map((el) => el.name)
    // param.tagLists = detailFilters.value.TAG.content.filter((el) => el.checked).map((el) => el.name)
    params.startDate = startDate.value
    params.endDate = endDate.value
  }

  params.limit = pagingObj.value.limit;
  params.offset = pagingObj.value.limit * offset;

  try {
    await $ofetch(`/api/search/keyword/externalDataList`, { method: 'POST', body: params }).then((res) => {
      if (res && !res.errorMessage) {
        pagingObj.value = {
          ...pagingObj.value,
          total: res.data.total,
          currentPage: offset + 1
        }

        entities.value = res.data.list
        entitiesExternalCount.value = res.data.total
      } else {
        entities.value = []
        entitiesExternalCount.value = 0
      }
    })

    await getSearchCount(0)

  } catch (error) {
    console.log(error);
  }
}

// 외부 데이터 검색 (카운트용)
const getSearchExternalCount = async (offset: number = 0) => {
  let params = {
    searchWord: word.value,
    offset: offset,
    limit: 10,
    classification: classification.value,
    sortBy: sort.value.split("|")[0],
    sortOrder: sort.value.split("|")[1],
    categoryList: [],
    // systemList: [],
    // tagList: [],
    checkUseFilter: false,
  }

  // 상세필터 사용 시 파라미터 추가
  if (checkUseFilter.value) {
    params.checkUseFilter = true
    params.categoryList = detailFilters.value.CAT.content.filter((el) => el.checked).map((el) => el.name)
    // params.systemList = detailFilters.value.SYS.content.filter((el) => el.checked).map((el) => el.name)
    // param.tagLists = detailFilters.value.TAG.content.filter((el) => el.checked).map((el) => el.name)
    params.startDate = startDate.value
    params.endDate = endDate.value
  }

  params.limit = pagingObj.value.limit;
  params.offset = pagingObj.value.limit * offset;

  try {
    await $ofetch(`/api/search/keyword/externalDataListCount`, { method: 'POST', body: params }).then((res) => {
      if (res && !res.errorMessage) {
        entitiesExternalCount.value = res.data
      } else {
        entitiesExternalCount.value = 0
      }
    })
  } catch (error) {
    console.log(error);
  }
}

// 카테고리 한글명
const getDescription = (key) => {
  return categoryStore.getClassificationNameKr(key)
}

// 필터 검색 조건 초기화
const resetSearchParam = (): void => {
  word.value = "";

  detailFilters.value['CAT'].all = true
  detailFilters.value['CAT'].content.forEach((el) => {
    el.checked = false
  })
  detailFilters.value['SYS'].all = true
  detailFilters.value['SYS'].content.forEach((el) => {
    el.checked = false
  })
  // detailFilters.value['TAG'].all = true
  // detailFilters.value['TAG'].content.forEach((el) => {
  //   el.checked = false
  // })
  detailFilters.value['DATE'].all = true
  detailFilters.value['DATE'].content.forEach((el) => {
    el.checked = false
  })

  refreshDate('DATE')
  if (currentTab.value == 0) getSearch(0)
  if (currentTab.value == 1) getSearchExternal(0)
};

// 상세 페이지로 이동
function onSelectDetail(guidv: string): void {
  if (currentTab.value == 0) {
    userStore.setCurrentSearchTab(currentTab.value)
    router.push("/search/detail?guid=" + guidv);
  }
  if (currentTab.value == 1) {
    userStore.setCurrentSearchTab(currentTab.value)
    router.push("/search/external/detail?id=" + guidv)
  }
}

// 좌측 카테고리 리스트 내 노드 클릭 이벤트 핸들러
const onClickMenuItem = (item: any): void => {
  if (!item) {
    classification.value = "";
  } else {
    classification.value = item.name;
  }
  if (currentTab.value == 0) getSearch(0)
  if (currentTab.value == 1) getSearchExternal(0)
};

// 한 페이지에 보여줄 개수 변경 이벤트
const onSelectChange = (): void => {
  if (currentTab.value == 0) getSearch(0)
  if (currentTab.value == 1) getSearchExternal(0)
};

const onSelectSort = (sortVal) => {
  sort.value = sortVal
  if (currentTab.value == 0) getSearch(0)
  if (currentTab.value == 1) getSearchExternal(0)
}

// 페이지 번호 클릭
const onClickPage = (page: number): void => {
  pagingObj.value.currentPage = page
  categoryStore.setSearchParamsPagingObj(pagingObj.value)

  if (currentTab.value == 0) getSearch(page - 1)
  if (currentTab.value == 1) getSearchExternal(page - 1)
};

// 필터 검색 표시
const showSearchOption = (): void => {
  if (showFilterWrap.value === false) {
    showFilterWrap.value = true;
    searchOption.value = "mu-btn mu-btn-lg mu-btn-bg-non btn-filter-toggle on";
  } else {
    showFilterWrap.value = false;
    searchOption.value = "mu-btn mu-btn-lg mu-btn-bg-non btn-filter-toggle";
  }
};

// 검색어 X 버튼
const onClickDeleteButton = (): void => {
  printSynonymList.value = [];
  word.value = "";
  if (currentTab.value == 0) getSearch(0)
  if (currentTab.value == 1) getSearchExternal(0)
};

// 검색 유효성 체크
const checkValidate = async (word: string): Promise<void> => {
  if (word.length == 1) {
    await errorAlert("최소 2글자 이상 입력하세요");
  } else {
    if (currentTab.value == 0) getSearch(0)
    if (currentTab.value == 1) getSearchExternal(0)
  }
};

const checkValidateInput = async (input: string) => {
  word.value = input.replace(/\bAND\b/g, 'and').replace(/\bOR\b/g, 'or');
}

// 왼쪽 메뉴 필터 X 표시 클릭 처리
const onClickDelete = (deleteItem: any) => {
  // 체크박스 해제 처리
  detailFilters.value[deleteItem.root].content.forEach((el) => {
    if (el.name == deleteItem.name) el.checked = false
  })

  detailFilters.value[deleteItem.root].count = detailFilters.value[deleteItem.root].content.filter((el) => el.checked == true).length
  detailFilters.value[deleteItem.root].all = false

  // 아무것도 선택하지 않은 상태인 경우 전체 표시
  if (detailFilters.value[deleteItem.root].count == 0) detailFilters.value[deleteItem.root].all = true

  if (currentTab.value == 0) getSearch(0)
  if (currentTab.value == 1) getSearchExternal(0)
}

// 가공된 상세필터 정보 가져오기
const getDetailFilters = async () => {
  // 카테고리, 시스템, 태그, 등록일
  await $ofetch('/api/search/category/detailFilters').then(async (res) => {
    if (res && !res.errorMessage) {
      // 키의 배열을 원하는 순서로 정렬
      // let keys = ["CAT", "SYS", "TAG", "DATE"];
      let keys = ["CAT", "SYS", "DATE"];
      let reorderedObject = {};

      // 정렬된 순서에 따라 새로운 객체 생성
      keys.forEach(key => {
          if (res.data[key]) {
            reorderedObject[key] = res.data[key];
          }
      });
      detailFilters.value = reorderedObject

      let searchParams = {
        word: word.value,
        detailFilters: detailFilters.value
      }
      await categoryStore.setSearchParams(searchParams)
    }
  }).catch((e) => {
    console.log(e)
  })
}

//날짜변환
const getDateFormat = (datetime) => {
  if(datetime == null || datetime == undefined || datetime == ""){
    return " - "
  }

  let [dateStr, timeStr] = datetime.split(' ');

  // 날짜 부분 변환
  let year = dateStr.substring(0, 4);
  let month = dateStr.substring(4, 6);
  let day = dateStr.substring(6, 8);

  let formattedDateTime = `${year}-${month}-${day}`;

  return formattedDateTime;
}

// timestamp변환
const getUNIXDate = (dt) => {
  // Unix 타임스탬프 (초 단위)
  const unixTimestamp = dt;

  // 초를 밀리초로 변환
  const date = new Date(unixTimestamp * 1000);

  // 각 요소 추출
  const year = date.getUTCFullYear();
  if(isNaN(year)){
    return ' - '
  }
  const month = String(date.getUTCMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
  const day = String(date.getUTCDate()).padStart(2, '0');
  // const hours = String(date.getUTCHours()).padStart(2, '0');
  // const minutes = String(date.getUTCMinutes()).padStart(2, '0');
  // const seconds = String(date.getUTCSeconds()).padStart(2, '0');

  // yyyy-MM-dd HH:mm:ss 형식으로 포맷
  // const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  const formattedDate = `${year}-${month}-${day}`;

  return formattedDate
}

const checkDataAccess = (flag) => {
  let dataAccessComment = "데이터 조회권한 ";
  dataAccessComment += flag ? "<span style='color: blue;'>있음</span>" : "<span style='color: red;'>없음</span>" ;

  return dataAccessComment;
}

</script>
