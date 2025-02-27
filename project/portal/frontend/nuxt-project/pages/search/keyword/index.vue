<template>
  <div class="contents">
    <div>
      <div class="content-search-wrap">
        <form action="">
          <fieldset>
            <legend>통합검색</legend>
            <div class="inner-search">
              <div class="mu-input-icon">
                <div class="category-group" v-if="checkUseFilter">
                  <div class="category">
                    <em class="category-label">
                      필터적용중<span>
                      <button type="button" @click="deleteCategoryTag"><i class="mu-icon-svg item-delete"></i></button>
                    </span>
                    </em>
                  </div>&nbsp;
                </div>
                <input
                  type="text"
                  class="mu-input"
                  placeholder="테이블을 제외한 컬럼/타 메타 검색결과를 조회하려면 상세필터로 검색하세요"
                  title="검색어를 입력해 주세요."
                  @keypress.enter.prevent="checkValidate(word)"
                 v-model="word"
                />
                <button v-if="word"
                  type="button"
                  class="mu-btn mu-btn-delete"
                  @click="onClickDeleteButton"
                >
                  <i class="mu-icon-svg item-delete"></i>
                  <span>삭제</span>
                </button>
                <button
                  type="button"
                  class="mu-btn mu-btn-sch"
                  @click="getSearch(0)"
                >
                  <i class="mu-icon-svg search"></i>
                  <span>검색</span>
                </button>
              </div>
            </div>
            <div class="inner-filter">
              <button type="button" @click="showSearchOption()" :class="searchOption">
                <i class="mu-icon-svg filter"></i>
                <span>{{ filteroptToggle }}</span>
              </button>
            </div>
          </fieldset>
        </form>
        <dl class="synonym-keyword" v-if="printSynonymList.length > 0">
          <dt>동의어</dt>
          <dd>
            <div class="tags-group">
              <div class="tag" v-for="synonyms in printSynonymList">
                <span class="tag-label" :style="isTermStyle(synonyms)">{{ synonyms.name }}</span>
              </div>
            </div>
          </dd>
        </dl>
        <div class="data-filter-wrap" v-show="showFilterWrap1" style="display: block">
          <div class="content-detail-search">
            <div class="mu-heading">
              <h4>상세필터</h4>
            </div>
            <div class="detail-search-form">
              <div class="mu-form mu-form-vertical">
                <div class="mu-form-item" v-for="(df, idx) in detailFilters" >
                  <div class="btn-category" v-if="df.content && df.content.length > 0">
                    <div class="mu-checkbox all">
                      <input :type="df.allChecked ? 'radio' : 'checkbox'" :name="idx" :id="'ckb_'+idx" value="ALL" @change="onChangeCheckBox(idx, $event);" :checked="df.allChecked">
                      <label :for="'ckb_'+idx"> <span>{{ getRootDescription(idx) }}</span></label>
                    </div>
                    <div v-for="(tg, idx2) in df.content" class="mu-checkbox">
                      <input :type="'checkbox'" :name="idx" :id="'ckbox_'+tg.name" :value="tg.name" @change="onChangeCheckBox(idx, $event)" :checked="tg.checked">
                      <!-- <label :for="'ckbox_'+tg.name"><span>{{ tg.description }}<em v-if="idx != 'TAG' && idx != 'ORDER'">({{ tg.count }})</em></span></label> -->
                      <label :for="'ckbox_'+tg.name"><span>{{ tg.description }}</span></label>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="on tab-content">
              <div class="mu-search-group">
                <div
                  class="attribute-filter-item"
                  v-show="showFilterWrap2"
                  style="display: block;"
                >
                  <searchfilter
                    :filterCnt="filterCnt"
                    :entityFiltersList="entityFiltersList"
                    :entityFilterCondition="entityFilterCondition"
                    :entityFilterCriterion="entityFilterCriterion"
                    @deleteGroupNothing="deleteGroupNothing()"
                  />
                </div>
                <div class="mu-search-btn">
                  <button
                    type="button"
                    class="mu-btn mu-btn-lg"
                    @click="resetSearchParam"
                  >
                    <span>초기화</span>
                  </button>
                  <button
                    type="button"
                    class="mu-btn mu-btn-lg mu-btn-primary"
                    @click="getSearch(0)"
                  >
                    <span>검색</span>
                  </button>
                </div>
              </div><div class="open-attribute-filter">
                  <button
                    type="button"
                    title="추가속성필터 열기"
                    @click="showSearchOptionSub()"
                    :class="searchOptionSub"
                    class="mu-btn mu-btn-bg-non btn-attribute-filter-toggle"
                  >
                    <i class="mu-icon-svg chevron-down"></i>
                    <span>추가속성필터</span>
                  </button>
                </div>
            </div>
          </div>
        </div>
      </div>

      <div class="inner">
        <div class="mu-section">
          <left-menu-list @onClickMenuItem="onClickMenuItem" />

          <div data-v-10a51f6f id="divider" @mousedown="mouseDown($event)" />

          <div class="data-result-wrap">
            <div class="mu-heading">
              <h4>
                총<span
                  ><strong>{{ pagingObj.total }}</strong
                  >개</span
                >
              </h4>
              <div class="mu-item-group">
                <div class="mu-selectbox w160">
                  <select @change="onSelectChange()" v-model="pagingObj.limit">
                    <option value="10">10개씩 보기</option>
                    <option value="30">30개씩 보기</option>
                    <option value="50">50개씩 보기</option>
                    <option value="100">100개씩 보기</option>
                  </select>
                </div>
              </div>
            </div>
            <div class="data-results" >
              <ul class="data-box-list" v-if="listData && listData.length > 0">
                <li class="data-box" v-for="data in listData">
                  <div class="inner-data visual" @click="onSelectDetailPage(data)">
                    <i :class="getMetaIcon(data.meta_type)"></i>
                  </div>
                  <div class="inner-data">
                    <div class="category-group" v-if="data && data.classifications && data.classifications.length > 0">
                      <div class="category" v-for="(c, idx) in data.classifications" :key="idx">
                        <span em class="category-label">{{ c.c_description }}</span>
                      </div>
                    </div>
                    <div class="category-group" v-else>
                      <div class="category">
                        <em class="category-label">카테고리 미지정</em>
                      </div>
                    </div>
                    <div class="title-group">
                      <a @click="onSelectDetailPage(data)">
                        <!-- <strong class="data-title">{{ data && data.attributes && data.attributes.description ? data.attributes.description : data.description }}</strong> -->
                        <strong class="data-title">{{ data && data.description ? data.description : '' }}</strong>
                        <p class="data-name"> {{ data && data.meta_name ? data.meta_name : ''}} </p>
                        <p class="data-desc">{{data && data.user_description ? data.user_description : ''}}</p>
                      </a>
                    </div>
                    <!-- LABELS -->
                    <!-- <div class="tags-group">
                      <em v-for="(label, idx1) in data.labels" :key="idx1" class="tag">
                        <span class="tag-label">{{ label }}</span>
                      </em>
                    </div> -->
                    <!-- TAGS -->
                    <div class="tags-group" style="margin-top: 10px;">
                      <em v-for="(tag, idx1) in data.tags" :key="idx1" class="tag">
                        <span class="tag-label">{{ tag }}</span>
                      </em>
                    </div>
                  </div>
                  <div class="inner-data option">
                    <p >시스템 명 : <strong>{{ data.system_name ? data.system_name : "-" }}</strong></p>
                    <p v-if="data.qualified_name && data.system_type && data.system_type.toUpperCase() != 'MYSQL'">데이터베이스 : <strong>{{ getDbName(data.qualified_name) }}</strong></p>
                    <p v-if="data.system_type && data.system_type.toUpperCase() != 'HIVE' && data.qualified_name && data.qualified_name.split('.')[2]">스키마 : <strong>{{ getSchemaName(data.qualified_name) }}</strong></p>
                    <p >최종 수집일 : <strong>{{ $yyyyMMddHHmmSS(data.meta_sync_time ? data.meta_sync_time : null) }}</strong></p>
                  </div>
                </li>
              </ul>
              <ul v-else class="data-box-list">
                <li style="height: 867px; align-content: center;"><p class="nodata">검색결과가 존재하지 않습니다.<span>다른 검색어를 입력해주세요.</span></p></li>
              </ul>
            </div>
            <pagination :paging="pagingObj" @onClickPage="onClickPage" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import pagination from "@functional/pagination/pagination.vue";
import { useUserStore } from "~/store/user.ts";
import { useCategoryStore } from "~/store/category.ts";
import { errorAlert } from "@functional/alert/alert-default.ts";
import LeftMenuList from "@functional/leftMenuList/left-menu-list.vue";
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";
import { useSearchStore } from "@/store/search";
import searchfilter from "./searchfilter.vue";

definePageMeta({
  layout: "user",
});

const { $ofetch } = useNuxtApp();

// route
const route = useRoute();
const router = useRouter();

// store
const userStore = useUserStore();
const user = userStore.getUser;
const fromPath = userStore.getFromPath;
const params = userStore.getParams;

const categoryStore = useCategoryStore();
const getOriginalList = categoryStore.getOriginalList;

const searchStore = useSearchStore();
const searchParams = ref({
  query: "",
  detailFilters: {},
  pagingObj: {},
  system_type: "",
  meta_type: "table",
  tag: "",
  limit: 10,
  offset: 0,
  classification: {
    id: "",
    name: "",
    description: ""
  },
  sortBy: "create_time",
  sortOrder: "",
  entityFilters: {}
});

// 카테고리, 데이터유형, 태그, 등록일
const detailFilters = ref({
  CATEGORY: {
    all: true,
    ord: 1,
    name: '카테고리',
    level: "1",
    content: [
    {
        "root": "CATEGORY",
        "rootDescription": "카테고리",
        "id": 1,
        "name": "MG",
        "description": "경영",
        "count": 2,
        "checked": false,
      },
    ],
    checkedCount: 0
  },
  SYSTEMTYPE: {
    all: true,
    ord: 2,
    name: '시스템 타입',
    content: [
    {
      "root": "SYSTEMTYPE",
      "rootDescription": "시스템 타입",
      "id": 1,
      "name": "Postgres",
      "description": "Postgres",
      "count": 0,
      "checked": false,
    },
    ],
    checkedCount: 0
  },
  METATYPE: {
    all: true,
    ord: 3,
    name: '시스템 타입',
    content: [
    {
      "root": "METATYPE",
      "rootDescription": "메타 타입",
      "id": 1,
      "name": "table",
      "description": "table",
      "count": 0,
      "checked": false,
    },
    ],
    checkedCount: 0
  },
  TAG: {
    all: true,
    ord: 4,
    name: '태그',
    content: [
      {
        "root": "TAG",
        "rootDescription": "태그",
        "id": 1,
        "name": "태그1",
        "description": "태그1",
        "count": 0,
        "checked": false,
      },
    ],
    checkedCount: 0
  },
  ORDER: {
    all: true,
    ord: 5,
    name: '수집일',
    content: [
      {
        "root": "ORDER",
        "rootDescription": "수집일",
        "id": 1,
        "name": "ASC",
        "description": "오름차순",
        "count": 0,
        "checked": false,
      },
      {
        "root": "ORDER",
        "rootDescription": "수집일",
        "id": 1,
        "name": "DESC",
        "description": "내림차순",
        "count": 0,
        "checked": false,
      },
    ],
    checkedCount: 0
  }
})

// data
const divider = ref<HTMLElement>(null); // 패널 사이즈 조절 DOM
const leftSide = ref<Element>(null); // 왼쪽 DOM
const rightSide = ref<Element>(null); // 오른쪽 DOM
const x = ref<number>(0); // 현재 마우스 X 좌표
const leftWidth = ref<number>(0); // 왼쪽 DOM 너비

const word = ref<string>(""); // 검색어

const system_type = ref<string>(""); // 시스템 타입 (hive, hbase, postgreSQL 등 시스템 종류)
const meta_type = ref<string>("table"); // 메타데이터 타입으로 대상 system에서 수집되는 메타 종류를 의미, TABLE만 조회
const tag = ref<string>(""); // 태그
const sortBy = ref<string>("create_time") // 정렬 필드
const sortOrder = ref<string>(""); // 정렬
const classification = ref({
  id: "",
  name: "",
  description: ""
}); // 카테고리

const synonymList = ref<Array<string>>([]); // 동의어 목록
const printSynonymList = ref<any>([]); // 검색창 아래 동의어 목록

const filteroptToggle = ref('Filter')
const showFilterWrap1 = ref<boolean>(false);
const searchOption = ref<string>("mu-btn mu-btn-lg mu-btn-bg-non btn-filter-toggle");

const showFilterWrap2 = ref<boolean>(false);
const searchOptionSub = ref<string>("mu-btn btn-attribute-filter-toggle");

// 추가 속성 필터
const checkFilterWrap2 = ref(true)
const filterCnt = ref(0) // 필터 개수
const entityFiltersList = ref([])
const entityFilters = ref({
  "condition": "AND",
  "criterion": [
    {
      "attributeName": "meta_id",
      "operator": "eq",
      "attributeValue": ""
    }
  ]
})
const entityFilterCriterion = ref([
  {
    name: "meta_id",
    displayName: "meta_id",
    typeName: "string"
  },
  {
    name: "meta_name.keyword",
    displayName: "meta_name",
    typeName: "string"
  },
  {
    name: "qualified_name",
    displayName: "qualified_name",
    typeName: "string"
  },
  {
    name: "system_type",
    displayName: "system_type",
    typeName: "string"
  },
  {
    name: "system_id",
    displayName: "system_id",
    typeName: "string"
  },
  {
    name: "system_name",
    displayName: "system_name",
    typeName: "string"
  },
  {
    name: "db",
    displayName: "db",
    typeName: "string"
  },
  {
    name: "db_schema",
    displayName: "schema",
    typeName: "string"
  },
  {
    name: "attributes.table_type",
    displayName: "table_type",
    typeName: "string"
  },
  {
    name: "description.keyword",
    displayName: "description",
    typeName: "string"
  },
  {
    name: "user_description.keyword",
    displayName: "user_description",
    typeName: "string"
  },
  {
    name: "attributes.columns.meta_name.keyword",
    displayName: "column_name",
    typeName: "string"
  },
  {
    name: "attributes.columns.description.keyword",
    displayName: "column_description",
    typeName: "string"
  },
  {
    name: "classifications.c_description.keyword",
    displayName: "classifications",
    typeName: "string"
  },
  {
    name: "tags",
    displayName: "tags",
    typeName: "string"
  },
  {
    name: "create_time",
    displayName: "create_time",
    typeName: "date"
  },
  {
    name: "update_time",
    displayName: "update_time",
    typeName: "date"
  },
  {
    name: "last_access_time",
    displayName: "last_access_time",
    typeName: "date"
  },
  {
    name: "meta_sync_time",
    displayName: "meta_sync_time",
    typeName: "date"
  },
])
const entityFilterCondition = ref({
  condition1: [
    { name: '=', description: '=' },
    { name: '!=', description: '!='},
    { name: 'contains', description: 'contains' },
    { name: 'startsWith', description: 'startsWith' },
    { name: 'endsWith', description: 'endsWith' },
    { name: 'isNull', description: 'isNull' },
    { name: 'notNull', description: 'notNull' }
  ],
  condition2: [
    { name: '=', description: '=' },
    { name: '!=', description: '!=' },
    { name: 'isNull', description: 'isNull' },
    { name: 'notNull', description: 'notNull' }
  ],
  condition3: [
    { name: 'dateEqual', description: '=' },
    { name: 'dateNotEqual', description: '!=' },
    { name: 'date>', description: '>' },
    { name: 'date<', description: '<' },
    { name: 'date>=', description: '>=' },
    { name: 'date<=', description: '<=' },
    { name: 'isNull', description: 'isNull' },
    { name: 'notNull', description: 'notNull' }
  ],
  condition4: [
    { name: '=', description: '=' },
    { name: '!=', description: '!=' },
    { name: '>', description: '>' },
    { name: '<', description: '<' },
    { name: '>=', description: '>=' },
    { name: '<=', description: '<=' },
    { name: 'isNull', description: 'isNull' },
    { name: 'notNull', description: 'notNull' }
  ]
})

interface PagingObj {
  total: number;
  limit: number;
  block: number;
  currentPage: number;
}

// 페이징
const pagingObj = ref<PagingObj>({
  total: 0, // 전체 데이터 건수
  limit: 10, // 한 페이지에 보여줄 데이터 건수
  block: 10, // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
});

const listData = ref<any>([]); // 통합 검색 결과 리스트

await categoryStore.fetchCategories();

onMounted(async () => {
  // 조절할 창 및 조절 요소 설정
  divider.value = document.getElementById("divider");
  leftSide.value = divider.value.previousElementSibling;
  rightSide.value = divider.value.nextElementSibling;

  searchParams.value = searchStore.getSearchParams // 검색 조건 불러오기

  if (fromPath.includes('/search/detail')) {
    if (!searchParams.value.query && (Object.keys(searchParams.value.detailFilters).length === 0 && searchParams.value.detailFilters.constructor === Object)) {
      // 상세필터 조회
      await getDetailFilters()
      pagingObj.value = {
        total: 0,
        limit: 10,
        block: 10,
        currentPage: 1,
      }; // 페이징 초기화

    } else {
      // 기존 검색어, 상세필터 내용 가져오기
      word.value = searchParams.value.query ? searchParams.value.query : "" // 검색어
      detailFilters.value = searchParams.value.detailFilters
      pagingObj.value = searchParams.value.pagingObj

      classification.value = searchParams.value.classification ? searchParams.value.classification : {
        id: "",
        name: "",
        description: ""
      }

      system_type.value = searchParams.value.system_type ? searchParams.value.system_type : "" // 시스템타입
      meta_type.value = searchParams.value.meta_type ? searchParams.value.meta_type : "table" // 메타타입
      sortOrder.value = searchParams.value.sortOrder ? searchParams.value.sortOrder : "" // 정렬방식
      pagingObj.value.currentPage = searchParams.value.pagingObj.currentPage // 현재 페이지
      pagingObj.value.limit = searchParams.value.limit // 보여줄 개수

      // 왼쪽메뉴 활성화 처리
      leftMenuToggleAndBold(searchParams.value.classification.id ? searchParams.value.classification.id : "")
    }
    // 추가속성 필터
    // 추가속성필터 store에 있는 경우 entityFilter 설정
    if (searchParams.value.entityFilters && Object.keys(searchParams.value.entityFilters).length > 0) {
      entityFiltersList.value = []
      entityFiltersList.value.push(searchStore.getEntityFilters)
      entityFilters.value = searchStore.getEntityFilters

      // 추가속성필터가 있는 경우 필터 열어두기
      if (entityFilters.value && entityFilters.value.criterion.length > 0 && entityFilters.value.criterion[0].attributeValue && entityFilters.value.criterion[0].attributeValue != "") {
        showFilterWrap1.value = true
        showFilterWrap2.value = true
      }
    } else {
      initSubFilter()
    }
  } else {
    // 상세필터 조회
    await getDetailFilters()
    pagingObj.value = {
      total: 0, // 전체 데이터 건수
      limit: 10, // 한 페이지에 보여줄 데이터 건수
      block: 10, // 한 화면에서 보여줄 페이지 수
      currentPage: 1, // 현재 페이지 번호
    }; // 페이징 초기화

    // 메인화면에서 통합검색으로 넘어온 경우
    if (fromPath == '/main' && searchStore.getMainSearch) {
      // 메인에서 카테고리 클릭으로 넘어온 경우
      if (searchStore.getMainSelectCategory && searchStore.getMainSelectCategory.name) {
        classification.value = searchParams.value.classification ? searchParams.value.classification : {
          id: "",
          name: "",
          description: ""
        }
        // 상세필터에서 카테고리 필터 선택 상태로 설정
        if (detailFilters.value['CATEGORY'].content) {
          detailFilters.value['CATEGORY'].content.forEach((el) => {
            if (searchStore.getMainSelectCategory.id == el.id) {
              el.checked = true
              detailFilters.value['CATEGORY'].allChecked = false
              leftMenuToggleAndBold(el.id)
            } else {
              el.checked = false
            }
          })
          await searchStore.setSearchParamsDetailFilters(detailFilters.value)
        }
      } else { // 메인화면 검색어 입력 or 추천검색어 클릭
        word.value = searchParams.value.query ? searchParams.value.query : "" // 검색어
        classification.value = searchParams.value.classification ? searchParams.value.classification : {
          id: "",
          name: "",
          description: ""
        } // 카테고리
      }
      searchStore.setMainSearch(false) // 검색화면으로 왔으므로 false 처리
      searchStore.setMainSelectCategory(null)

      // 추가속성 필터
      initSubFilter()

    } else if (fromPath.includes('/search/category')) {
      word.value = searchParams.value.query ? searchParams.value.query : "" // 검색어
      classification.value = searchParams.value.classification ? searchParams.value.classification : {
        id: "",
        name: "",
        description: ""
      }

      // 선택된 카테고리의 최상위 카테고리 확인
      if (classification.value && classification.value.id) {
        // 최상위 카테고리 가져오기
        const root_classification = getRootClassification(classification.value)

        // 상세필터에서 카테고리 필터 선택 상태로 설정
        // 미분류 클릭해서 온 경우 처리
        if (classification.value.id == '_NOT_CLASSIFIED') {
          leftMenuToggleAndBold("_NOT_CLASSIFIED")
        } else {
          if (detailFilters.value['CATEGORY'].content) {
            detailFilters.value['CATEGORY'].content.forEach((el) => {
              // 최상위 카테고리(1depth) 확인
              if (root_classification.id == el.id) {
                el.checked = true
                detailFilters.value['CATEGORY'].allChecked = false
                leftMenuToggleAndBold(classification.value.id)

                // 상위메뉴도 펼치기
                const matched = getOriginalList.find((el2) => el2.id === classification.value.parent_id);
                toggleParents(matched)
              } else {
                el.checked = false
              }
            })

            await searchStore.setSearchParamsDetailFilters(detailFilters.value)
          }
        }

      }

      // 추가속성 필터
      initSubFilter()
    } else { // 그 외 검색조건 초기화
      searchStore.setSearchParams(searchParams.value)
      initSubFilter() // 추가속성 필터
    }

  }
  await getSearch(pagingObj.value.currentPage - 1);

});

// 검색 유효성 체크
const checkValidate = async (word: string): Promise<void> => {
  // if (word.length == 1) {
  //   await errorAlert("2글자 이상 입력하세요.");
  // } else {
  //   getSearch(0)
  // }
  getSearch(0)
};

// 가공된 상세필터 정보 가져오기
const getDetailFilters = async () => {
  // 카테고리, 시스템, 태그, 등록일
  await $ofetch('/api/search/category/detailFilters').then(async (res) => {
    if (res && !res.errorMessage) {
      // 키의 배열을 원하는 순서로 정렬
      // let keys = ["CATEGORY", "SYSTEMTYPE", "METATYPE", "TAG", "ORDER"];
      let keys = Object.keys(detailFilters.value);
      let reorderedObject = {};

      // 정렬된 순서에 따라 새로운 객체 생성
      keys.forEach(key => {
          if (res.data[key]) {
            reorderedObject[key] = res.data[key];
          }
      });
      detailFilters.value = reorderedObject

      let searchParams = {
        query: word.value,
        detailFilters: detailFilters.value
      }

      await searchStore.setSearchParams(searchParams)
    }
  }).catch((e) => {
    console.log(e)
  })
}

// methods
// 좌측 상위메뉴 펼치기 처리
const toggleParents = (matched) => {
  if (matched) {
    const parent_matched = getOriginalList.find((el2) => el2.id === matched.parent_id)
    if (parent_matched) {
      categoryStore.getToggleTargets.value[parent_matched.id] = true
      toggleParents(parent_matched)
    }
  }
}

const getRootClassification = (obj) => {
  if (obj && obj.parent_id && obj.parent_id != "" && obj.parent_id != null && obj.parent_id != undefined) {
    let parent_obj = getOriginalList.find((el) => el.id == obj.parent_id)
    if (parent_obj && parent_obj != null && parent_obj != undefined) {
      return getRootClassification(parent_obj)
    } else {
      return obj
    }
  } else {
    return obj
  }
}

// 검색
const getSearch = async (offset: number = 0): Promise<void> => {
  printSynonymList.value = []; // 동의어

  // 추가속성필터 유효성 체크
  if (showFilterWrap2.value && checkFilterWrap2.value) {
    let checkVal = checkFilterSearch(entityFiltersList.value)
    if (typeof checkVal !== undefined) {
      if (checkVal === true) {
        await errorAlert('추가속성필터를 삭제 또는 입력해주세요');
        return
      }
    } else {

    }
  } else {
    showFilterWrap2.value = false
    checkFilterWrap2.value = false
  }

  let paramEntityFilters = Object.assign(entityFiltersList.value[0])

  let currentLimit = pagingObj.value.limit ? pagingObj.value.limit : 10
  const params = {
    query: word.value ? word.value : "",
    system_type: system_type.value ? system_type.value : "",
    meta_type: meta_type.value ? meta_type.value : "table",
    tag: "",
    limit: currentLimit,
    offset: offset ? offset * currentLimit : 0,
    classification: classification.value && classification.value.name ? classification.value.name : "", // 검색할 때는 name값 사용
    sortBy: sortBy.value ? sortBy.value : "create_time",
    sortOrder: sortOrder.value ? sortOrder.value : "",
    entityFilters: showFilterWrap2.value ? paramEntityFilters : {}
  };

  await $ofetch(`/api/search/getEsSearch`, {
    method: "POST",
    body: params,
  }).then((res) => {
    if (res && res.data && res.data.results) {
      listData.value = res.data.results
      // 페이징
      pagingObj.value = {
        ...pagingObj.value,
        total: res.data.total_count,
        currentPage: offset + 1
      };

      // 카테고리의 항목 순서 정렬
      listData.value.forEach(el => {
        if (el.classifications && el.classifications.length > 1) {
          el.classifications.sort((a, b) => a.c_id - b.c_id);
        }
      });
    } else {
      // 페이징
      pagingObj.value = {
        ...pagingObj.value,
        total: 0,
        currentPage: 1
      };
      listData.value = []
    }

  }).catch((err) => {
    console.log(err)
    // 페이징
    pagingObj.value = {
      ...pagingObj.value,
      total: 0,
      currentPage: 1
    };
    listData.value = []
  })

  // 검색을 했을 때만 동의어 리스트 조회
  if (word.value && word.value != "") {
    synonymList.value = [];

    let words = []
    if (word.value.includes(" ")) {
      // " "을 포함하는지 확인
      words = [...word.value.split(" ")]; // 공백을 기준으로 단어 분할
    } else {
      words = [word.value];
    }

    let filteredWords = words.filter(function (word) {
      return word !== "";
    });

    printSynonymList.value = [];

    let params = {
      searchType: 'all',
      searchWords: filteredWords,
      pnum: -1,
      pageSize: -1
    }

    await $ofetch(`/api/synonym/list`, {
      method: 'POST',
      body: params
    }).then((res) => {
      if (res && res.data && res.data.list && res.data.list.length > 0) {
        let tmpList = []
        const nameSet = new Set(); // 동의어 중복 제거 확인용 이름 목록
        const searchWordSet = new Set(); // 동의어 중복 제거 확인용 검색어 목록

        const originList = res.data.list

        // 1. 검색어에 대한 대표어, 동의어가 없는 경우 항목에서 제거
        let tmpOriginList = originList.filter((el) => el.synonyms && el.synonyms.length > 0)

        for (let i=0; i<tmpOriginList.length; i++) {
          const printSynonymSearchWord = {
            isTerm: true,
            name: tmpOriginList[i].searchWord
          }
          tmpList.push(printSynonymSearchWord)
          nameSet.add(tmpOriginList[i].searchWord)
          searchWordSet.add(tmpOriginList[i].searchWord)


          if (tmpOriginList[i].term && tmpOriginList[i].term != "") {
            const printSynonymTerm = {
              isTerm: false,
              name: tmpOriginList[i].term
            }
            tmpList.push(printSynonymTerm)
            nameSet.add(tmpOriginList[i].term)
          }

          // 중복 동의어 처리
          for (let j=0; j<tmpOriginList[i].synonyms.length; j++) {
            const printSynonym = {
              isTerm: false,
              name: tmpOriginList[i].synonyms[j]
            }
            tmpList.push(printSynonym)
            nameSet.add(tmpOriginList[i].searchWord)
          }
        }

        // 2. 중복단어 저장
        let uniqueTmpList = []
        const dupIsTermNameSet = new Set()
        const dupNameSet = new Set()
        tmpList.forEach((el) => {
          // 검색어는 일단 추가
          if (el.isTerm) {
            // 동일한 이름으로 이미 있는 경우는 추가하지 않음
            if (dupIsTermNameSet.has(el.name)) {
              uniqueTmpList.push(el)
              dupNameSet.add(el.name)
              dupIsTermNameSet.add(el.name)
            }
          } else {
            // 검색어든 이미 표기가 되어있는 경우
            if (!dupNameSet.has(el.name)) {
              uniqueTmpList.push(el)
              dupNameSet.add(el.name)
            }
          }
        })

        // 3. 검색어와 일치하는 단어를 진하게 표기
        uniqueTmpList.forEach((el) => {
          if (searchWordSet.has(el.name)) {
            el.isTerm = true
          }
        })

        printSynonymList.value = uniqueTmpList
      } else {
        printSynonymList.value = []
      }

    }).catch((err) => {
      // error
      printSynonymList.value = []
    })

  } else {
    synonymList.value = [];
  }

  // 검색 조건 저장
  params.classification = classification.value // 카테고리 검색조건 저장용으로 변경
  params.entityFilters = paramEntityFilters // 추가속성필터 검색조건 저장용으로 변경
  // 검색어, 카테고리 저장
  searchStore.setSearchParams({
    query: word.value,
    detailFilters: detailFilters.value,
    pagingObj: pagingObj.value,
    ...params
  })
};

// entityFilter의 criterion 내 미사용 필드 제거
const filterCondition = (criterion) => {
  let criterionJsonData = JSON.parse(JSON.stringify(criterion))
  if (criterionJsonData) {
    criterion.filter(item => {
      if (item.criterion === undefined) {
        delete item.condition
        if (item.idx !== undefined) delete item.idx
        if (item.attributeValueText !== undefined) delete item.attributeValueText
        if (item.typeName !== undefined) delete item.typeName
      } else {
        filterCondition(item.criterion)
      }
      return item
    })
    return criterionJsonData
  } else {
    return criterion
  }
}

// 추가 속성 필터 사용 시 값을 입력했는지 여부
const checkFilterSearch = (filterData) => {
  let returnVal = false
  function filterRoof(data) {
    for (const item of data) {
      if (typeof item.criterion !== 'undefined') {
        filterRoof(item.criterion)
      } else {
        // 날짜 타입의 경우 attributeValueText 값이 없으면 안됨
        if (item.typeName == 'date') {
          if (!item.attributeValueText) {
            returnVal = true
          }
        }
        if (item.attributeValue !== null && item.attributeValue === '') {
          if (item.operator !== 'isNull' && item.operator !== 'notNull') {
            returnVal = true
            break
          }
        }
      }
    }
  }
  filterRoof(filterData);
  return returnVal
}


// 마우스를 클릭했을 때 (divider)
const mouseDown = (e: any): void => {
  // 현재 마우스 위치 값
  x.value = e.clientX;

  // leftElement에 Viewport(보이는 화면 전체) 상 width 값을 가져와 대입
  leftWidth.value = leftSide.value.getBoundingClientRect().width;

  // 마우스 이벤트 등록
  document.addEventListener("mousemove", mouseDragging);
  document.addEventListener("mouseup", mouseUp);
};

// 마우스를 드래그 하고 있을 때 (divider)
const mouseDragging = (e: any): void => {
  // 처음 마우스 위치값과 드래그한 위치값 차이 계산
  let dx = e.clientX - x.value;

  // 사이즈 조절 중 마우스 위치가 변경되어도 커서 표시를 유지하기 위해 body에 설정
  document.body.style.cursor = "col-resize";

  // 마우스 이동 중 왼쪽, 오른쪽 영역에서 텍스트 선택, 드래그 이벤트 효과 제거
  leftSide.value.style.userSelect = "none";
  leftSide.value.style.pointerEvents = "none";
  rightSide.value.style.userSelect = "none";
  rightSide.value.style.pointerEvents = "none";

  // (처음 왼쪽 너비 값 + 마우스 드래그 거리) / 왼쪽, 오른쪽 DOM 부모 너비 사이즈 --> 퍼센티지 계산
  // 계산된 퍼센티지는 새롭게 left의 width로 적용
  let newLeftWidth =
    ((leftWidth.value + dx) * 100) /
    divider.value.parentNode.getBoundingClientRect().width;

  // 사이즈 조절 범위 제한
  if (newLeftWidth >= 10 && newLeftWidth <= 40) {
    leftSide.value.style.width = `${newLeftWidth}%`;
    rightSide.value.style.width = `${100 - newLeftWidth}%`;
  }
};

// 마우스 클릭을 떼었을 때 (divider)
const mouseUp = (): void => {
  // 마우스 커서 변경 속성 제거
  document.body.style.removeProperty("cursor");

  // 왼쪽, 오른쪽 마우스 텍스트 선택, 이벤트 효과 재설정
  leftSide.value.style.removeProperty("user-select");
  leftSide.value.style.removeProperty("pointer-events");
  rightSide.value.style.removeProperty("user-select");
  rightSide.value.style.removeProperty("pointer-events");

  // 등록된 마우스 이벤트 제거
  document.removeEventListener("mousemove", mouseDragging);
  document.removeEventListener("mouseup", mouseUp);
};

// 동의어 스타일 설정
const isTermStyle = (item: any): { fontWeight: number } | {} => {
  if (item.isTerm) {
    return { fontWeight: 800 };
  } else {
    return {};
  }
};

const checkUseFilter = computed(() => { // 상세검색 필터 사용중 여부
  if (showFilterWrap2.value) { // 추가속성필터 사용중인 경우 처리
    return true;
  }

  if (classification.value.id == "") { // 카테고리 필터 적용중인 경우 처리
    return false
  }

  // 상세필터 항목 사용중인경우 처리
  if (detailFilters.value['CATEGORY']
      && detailFilters.value['SYSTEMTYPE']
      && detailFilters.value['ORDER']
    ) {
    if (!detailFilters.value['CATEGORY'].allChecked
        || !detailFilters.value['SYSTEMTYPE'].allChecked
        || !detailFilters.value['ORDER'].allChecked) {
      return true
    }
  }

  // 추가 속성 필터 사용중 여부
  if (showFilterWrap2.value == false) {
    return false
  }

  return true
})

// 필터 검색 조건 초기화
const resetSearchParam = (): void => {
  word.value = ""; // 1. 검색어

  let filterKeys = Object.keys(detailFilters.value)

  // 2. 상세필터 내 체크박스상태 false 처리
  for (let i=0; i<filterKeys.length; i++) {
    clearFilter(filterKeys[i])
  }

  // 3. 추가 속성 필터 초기화
  entityFiltersList.value.splice(0);
  initSubFilter();
  showFilterWrap2.value = true
  showSearchOptionSub();

  // getSearch(0)
};

// 상세필터 내 카테고리 및 카테고리 초기화
const clearFilter = (key) => {
  if (key == 'CATEGORY') {
    classification.value = {
      id: "",
      name: "",
      description: ""
    };
    leftMenuToggleAndBold("") // 왼쪽메뉴 연동
  } else if (key == 'SYSTEMTYPE') {
    system_type.value = ""
  } else if (key == 'METATYPE') {
    meta_type.value = "table"
  } else if (key == 'TAG') {
    tag.value = ""
  } else if (key == 'ORDER') {
    sortOrder.value = ""
  } else {
    if (key == '_NOT_CLASSIFIED') {
      leftMenuToggleAndBold("_NOT_CLASSIFIED")
      detailFilters.value['CATEGORY'].content.forEach((el) => {el.checked = false})
      detailFilters.value['CATEGORY'].allChecked = true
      detailFilters.value['CATEGORY'].checkedCount = 0
      classification.value = {
      id: "",
      name: "_NOT_CLASSIFIED",
      description: "_NOT_CLASSIFIED"
    };
    }
    return
  }

  detailFilters.value[key].content.forEach((el) => {el.checked = false})
  detailFilters.value[key].allChecked = true
  detailFilters.value[key].checkedCount = 0
}

// 상세 데이터 이동
function onSelectDetailPage(item: any): void {
  router.push(`/search/detail?guid=${item.meta_id}`);
  userStore.setGuid(item.meta_id);
  window.scrollTo(0, 0);
}

// 좌측 카테고리 리스트 내 노드 클릭 이벤트 핸들러
const onClickMenuItem = (item: any): void => {
  // 1. 카테고리 전체
  if (!item) {
    clearFilter("CATEGORY")

  } else if (item.id == "_NOT_CLASSIFIED") { // 2. 카테고리 미분류
    clearFilter("_NOT_CLASSIFIED")

  } else { // 3. 카테고리들
    leftMenuToggleAndBold(item.id)
    classification.value = item;

    // 상세필터 카테고리 체크 설정
    // 최상위 뎁스만 있기 때문에 최상위 뎁스에 대해 카테고리 필터 선택 처리
    const root_classification = getRootClassification(item)
    detailFilters.value.CATEGORY.content.forEach((el) => {
      if (el.name == root_classification.name) {
        el.checked = true;
        detailFilters.value.CATEGORY.allChecked = false
      } else {
        el.checked = false;
      }
    })
  }

  getSearch(0);
};

// 한 페이지에 보여줄 개수 변경 이벤트
const onSelectChange = (): void => {
  getSearch(0);
};

// 페이지 번호 클릭
const onClickPage = (page: number): void => {
  pagingObj.value.currentPage = page
  searchStore.setSearchParamsPagingObj(pagingObj.value)

  getSearch(pagingObj.value.currentPage - 1);

};

// 필터 검색 표시
const showSearchOption = (): void => {
  if (showFilterWrap1.value === false) {
    showFilterWrap1.value = true;
    searchOption.value = "mu-btn mu-btn-lg mu-btn-bg-non btn-filter-toggle on";
    filteroptToggle.value = "Close";
  } else {
    showFilterWrap1.value = false;
    searchOption.value = "mu-btn mu-btn-lg mu-btn-bg-non btn-filter-toggle";
    filteroptToggle.value = "Filter";
  }
};

// 필터 > 추가 속성 필터 표시
const showSearchOptionSub = (): void => {
  if (showFilterWrap2.value === false) {
    showFilterWrap2.value = true;
    checkFilterWrap2.value = true;
    searchOptionSub.value = "mu-btn btn-attribute-filter-toggle on";
  } else {
    showFilterWrap2.value = false;
    checkFilterWrap2.value = true;
    searchOptionSub.value = "mu-btn btn-attribute-filter-toggle";
  }
};

// 추가속성필터 설정
const initSubFilter = () => {
  // 추가 속성 필터의 기본 필드 값 설정
  if (entityFilterCriterion.value.length > 0) {
    const filterDetailData = filterDetailCondition(entityFilterCriterion.value[0])

    let filterAttributeValue = ''
    let filterAttributeValueText = ''

    // 기본 필터 조건 값 설정
    entityFiltersList.value[0] = {
      condition: 'AND',
      criterion: [{
        idx: 0,
        attributeName: filterDetailData.name,
        typeName: filterDetailData.typeName,
        operator: filterDetailData.filterOperator,
        attributeValue: filterAttributeValue,
        attributeValueText: filterAttributeValueText
      }]
    }
    entityFilters.value = Object.assign(entityFiltersList.value[0])
  } else {

  }
  checkFilterWrap2.value = false
}

// 추가속성필터의 필드 타입에 따라 operator 및 값부분 설정
const filterDetailCondition = (criterion) => {
  const filterDetailData = {}
  let filterOperator
  let typeName = criterion.typeName
  let name = criterion.name
  if (typeName === 'string') {
    filterOperator = entityFilterCondition.value.condition1[0].name
  }
  else if (typeName === 'hive_principal_type') {
    filterOperator = entityFilterCondition.value.condition1[0].name
  }
  else if (typeName === 'sap_principal_type') {
    filterOperator = entityFilterCondition.value.condition1[0].name
  }
  else if (typeName === 'boolean') {
    filterOperator = entityFilterCondition.value.condition2[0].name
  }
  else if (typeName === 'int') {
    filterOperator = entityFilterCondition.value.condition4[0].name
  }
  else if (typeName === 'date') {
    filterOperator = entityFilterCondition.value.condition3[0].name
  } else {
    filterOperator = entityFilterCondition.value.condition1[0].name
  }
  filterDetailData.name = name
  filterDetailData.typeName = typeName
  filterDetailData.filterOperator = filterOperator

  return filterDetailData
}

// X 버튼
const onClickDeleteButton = (): void => {
  word.value = "";
};

// 검색에서 필터적용중 삭제
const deleteCategoryTag = async (): void => {
  let filterKeys = Object.keys(detailFilters.value)
  // 1. 상세필터 내 체크박스상태 false 처리
  for (let i=0; i<filterKeys.length; i++) {
    await clearFilter(filterKeys[i])
  }

  // 2. 카테고리 왼쪽메뉴 토글 및 Bold 해제
  await leftMenuToggleAndBold("")

  // 3. 추가속성 필터 해제 및 유효성 검사 안하도록 설정
  await initSubFilter()

  // 4. 검색
  await getSearch(0)
};


const getRootDescription = (key) => {
  if (key == 'CATEGORY') return "카테고리"
  else if (key == 'SYSTEMTYPE') return "시스템 타입"
  else if (key == 'METATYPE') return "메타 타입"
  else if (key == 'TAG') return "태그"
  else if (key == "ORDER") return "수집일"
  else return ""
}

// 조건 클릭 이벤트
const onChangeCheckBox = async (key, event: any) => {
  // 추가속성필터가 열려있는 경우 유효성 체크
  if (showFilterWrap2.value && checkFilterWrap2.value) {
    let checkVal = checkFilterSearch(entityFiltersList.value)
    if (typeof checkVal !== undefined) {
      if (checkVal === true) {
        event.target.checked = !event.target.checked
        await errorAlert('추가속성필터를 삭제 또는 입력해주세요');
        return
      }
    } else {

    }
  }

  // 전체 클릭
  if (event.target.value == 'ALL') {
    clearFilter(key)
  } else {
    detailFilters.value[key].content.forEach((el) => {
      if (el.name == event.target.value) {
        el.checked = event.target.checked

        // 카테고리
        if (key == 'CATEGORY') {
          classification.value = {
            id: el.id,
            name: el.name,
            description: el.description
          };
          leftMenuToggleAndBold(el.id) // 왼쪽메뉴 연동 처리
        }
        if (key == 'SYSTEMTYPE') system_type.value = el.name
        if (key == 'METATYPE') meta_type.value = 'table'
        if (key == 'TAG') tag.value = el.name
        if (key == 'ORDER') sortOrder.value = el.name
      } else {
        el.checked = false // 단일로 체크되도록 설정
      }
    })

    detailFilters.value[key].checkedCount = detailFilters.value[key].content.filter((el) => el.checked == true).length
    detailFilters.value[key].allChecked = false

    // 아무것도 선택하지 않은 상태인 경우 전체 표시
    if (detailFilters.value[key].checkedCount == 0) clearFilter(key)
  }

  getSearch(0)
}

// DB명
const getDbName = (qualified_name) => {
  if (qualified_name) {
    return qualified_name.split(".")[1]
  }
  return "-"
}

// 스키마명
const getSchemaName = (qualified_name) => {
  if (qualified_name) {
    return qualified_name.split(".")[2]
  }
  return "-"
}

// 테이블명
const getTableName = (qualified_name) => {
  if (qualified_name) {
    return qualified_name.split(".")[3]
  }
  return "-"
}

// 카테고리 왼쪽메뉴 Open 및 선택한 카테고리 Bold 처리
const leftMenuToggleAndBold = (id) => {
  // 1. 현재 카테고리명 하위메뉴 열기
  categoryStore.setToggleTargetOnClickTreemap(id ? id : "")

  // 2. 현재 카테고리명 Bold 처리
  categoryStore.setSelectedItemId(id ? id : "");

  // 3. 현재 카테고리에명 상위메뉴 열기
  if (id) {
    const matched = getOriginalList.find((ol) => ol.id === id);
    toggleParents(matched)
  }
}

const deleteGroupNothing = () => {
  initSubFilter()
  showFilterWrap2.value = false
  searchOptionSub.value = "mu-btn btn-attribute-filter-toggle";
}

// 아이콘 클래스명
const getMetaIcon = (type) => {
  if (type && type.toUpperCase() == 'CONTAINER') return "ico-data data-04"
  if (type && type.toUpperCase() == 'DATABASE') return "ico-data data-02"
  if (type && type.toUpperCase() == 'DATABASESCHEMA') return "ico-data data-06"
  if (type && type.toUpperCase() == 'DATABASESERVICE') return "ico-data data-06"
  if (type && type.toUpperCase() == 'DIRECTORY') return "ico-data data-06"
  if (type && type.toUpperCase() == 'FILESYSTEMSERVICE') return "ico-data data-06"
  if (type && type.toUpperCase() == 'STORAGESERVICE') return "ico-data data-04"
  if (type && type.toUpperCase() == 'TABLE') return "ico-data data-05"
  if (type && type.toUpperCase() == 'COLUMN') return "ico-data data-01"

  return 'ico-data data-06'
}

</script>
