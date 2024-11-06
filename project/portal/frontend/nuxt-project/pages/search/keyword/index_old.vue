<template>
  <div class="contents">
    <div>
      <div class="content-search-wrap">
        <form action="">
          <fieldset>
            <legend>통합검색</legend>
            <div class="inner-search">
              <!-- <div
                class="category"
                v-if="
                  classification != '' &&
                  classification.toUpperCase() != '_NOT_CLASSIFIED'
                "
              >
                <em class="category-label">
                  {{
                    getKrName(
                      classification.toUpperCase() != "_NOT_CLASSIFIED"
                        ? classification
                        : ""
                    )
                  }}
                </em>
                <button
                  type="button"
                  class="mu-btn mu-btn-delete"
                  @click="deleteCategoryTag"
                >
                  <i class="mu-icon-svg item-delete"></i><span>삭제</span>
                </button>
              </div> -->
              <div class="mu-input-icon">
                <input
                  type="text"
                  class="mu-input"
                  placeholder="테이블을 제외한 컬럼/타 메타 검색결과를 조회하려면 상세필터로 검색하세요"
                  title="검색어를 입력해 주세요."
                  v-on:keypress.enter.prevent="checkValidate(word)"
                  v-model="word"
                />
                <button
                  type="button"
                  class="mu-btn mu-btn-delete"
                  v-on:click="onClickDeleteButton"
                >
                  <i class="mu-icon-svg item-delete"></i>
                  <span>삭제</span>
                </button>
                <button
                  type="button"
                  class="mu-btn mu-btn-sch"
                  @click="checkValidate(word)"
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
              <div class="tag" v-for="item in printSynonymList">
                <span class="tag-label" :style="isTermStyle(item)">{{ item.name }}</span>
              </div>
            </div>
          </dd>
        </dl>

        <div class="data-filter-wrap" v-show="showFilterWrap1" style="display: block">
          <div class="inner-data-filter">
            <div class="on tab-content">
              <div class="mu-search-group">
                <div class="mu-search-item">
                  <table>
                    <caption>
                      필터검색 게시판
                    </caption>
                    <colgroup>
                      <col style="width: 100px" />
                      <col />
                    </colgroup>
                    <tbody>
                      <tr>
                        <th scope="row">데이터유형</th>
                        <td>
                          <div class="mu-item-group">
                            <div class="mu-selectbox w240">
                              <select
                                v-model="filterDataTypeVal"
                                @change="onSelectFilterChange()"
                              >
                                <option value="" selected>전체선택</option>
                                <option
                                  v-for="(item, index) in filterDataType"
                                  :key="index"
                                  :value="item"
                                >
                                  데이터레이크(HIVE)
                                </option>
                              </select>
                            </div>
                            <div class="mu-selectbox w240">
                              <select
                                v-model="filterDataTypeSubVal"
                                @change="onSelectFilterSubChange()"
                              >
                                <option value="" selected>전체선택</option>
                                <option
                                  v-for="(item, index) in filterDataTypeSub"
                                  :key="index"
                                  :value="item"
                                >
                                  {{ item }}
                                </option>
                              </select>
                            </div>
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <th scope="row">정렬방식</th>
                        <td>
                          <div class="mu-selectbox w240">
                            <select
                              v-model="filterSearchSortType"
                              :disabled="filterDataTypeSubVal === ''"
                            >
                              <option value="">없음</option>
                              <option value="ASCENDING">오름차순</option>
                              <option value="DESCENDING">내림차순</option>
                            </select>
                          </div>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
                <div
                  class="attribute-filter-item slim-scroll"
                  v-show="showFilterWrap2"
                  style="display: block"
                >
                  <searchFilter
                    :filterAddFilter_s="filterAddFilter"
                    :filterDataTypeSubObj_s="filterDataTypeSubObj"
                    :filterDataTypeSubVal_s="filterDataTypeSubVal"
                    :filterDataTypeVal_s="filterDataTypeVal"
                    :attributeDefsArr_s="attributeDefsArr"
                    :filterCnt_s="filterCnt"
                    :filterCondition_s="filterCondition"
                    :filterFactor_s="filterFactor"
                    @deleteGroupInit="deleteGroupInit"
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
                <div
                  v-if="
                    authority == 'Manager' ||
                    authority == 'Admin' ||
                    authority == 'Super-Admin'
                  "
                  class="open-attribute-filter"
                >
                  <button
                    type="button"
                    title="추가속성필터 열기"
                    @click="showSearchOptionSub()"
                    :class="searchOptionSub"
                    :disabled="filterDataTypeSubVal === ''"
                    :filterCnt_s="filterCnt"
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
            <div class="data-result">
              <ul class="data-box-list" v-if="listData">
                <li class="data-box" v-for="(item, index) in listData" :key="index">
                  <div class="inner-data visual">
                    <i
                      class="ico-data data-column"
                      v-if="item.typeName.includes('_column')"
                    />
                    <i
                      class="ico-data data-table"
                      v-else-if="item.typeName.includes('_table')"
                    />
                  </div>
                  <div class="inner-data">
                    <div class="category-group">
                      <div
                        class="category"
                        v-for="(classificationName, idx1) in item.classificationNames"
                        :key="idx1"
                      >
                        <em class="category-label">{{
                          getKrName(classificationName)
                        }}</em>
                      </div>
                    </div>
                    <div class="title-group">
                      <a v-if="item.guid == -1">
                        <strong class="data-title"> * 권한이 필요합니다. * </strong>
                        <p class="data-name">{{ item.displayText }}</p>
                        <p
                          class="data-desc"
                          v-if="Object.keys(item.attributes).includes('userDescription')"
                        >
                          권한이 필요합니다.
                        </p>
                      </a>
                      <a v-else @click="onSelectDetail(item.guid)">
                        <strong class="data-title">{{ item.attributes.comment }}</strong>
                        <p class="data-name">{{ item.displayText }}</p>
                        <p
                          class="data-desc"
                          v-if="Object.keys(item.attributes).includes('userDescription')"
                        >
                          {{ item.attributes.userDescription }}
                        </p>
                      </a>
                    </div>
                  </div>
                  <div class="inner-data options">
                    <div class="data-info-group">
                      <div class="data-info">
                        <dl>
                          <dt><span>DB</span></dt>
                          <dd v-if="item.attributes.qualifiedName">
                            {{ item.attributes.qualifiedName.split(".")[0] }}
                          </dd>
                          <dd v-else>-</dd>
                        </dl>
                        <dl>
                          <dt v-if="item.typeName.includes('_column')">
                            <span>테이블</span>
                          </dt>
                          <dd v-if="item.typeName.includes('_column')">
                            <a
                              v-if="item.attributes.table"
                              @click="onSelectDetail(item.attributes.table.guid)"
                            >
                              {{ item.attributes.qualifiedName.split(".")[1] }}
                            </a>
                            <a v-else></a>
                          </dd>
                        </dl>
                      </div>
                    </div>
                  </div>
                </li>
              </ul>
              <ul class="data-box-list">
                <li v-if="!listData">
                  <p class="nodata">
                    검색 결과가 존재하지 않습니다.<span>다른 검색어를 입력해주세요.</span>
                  </p>
                </li>
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
import ObjectUtils from "@utils/ObjectUtils";
import pagination from "@functional/pagination/pagination.vue";
import { useUserStore } from "~/store/user.ts";
import { useCategoryStore } from "~/store/category.ts";
import { errorAlert } from "@functional/alert/alert-default.ts";
import searchFilter from "./searchfilter.vue";
import LeftMenuList from "@functional/leftMenuList/left-menu-list.vue";
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";

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

// data
const divider = ref<HTMLElement>(null); // 패널 사이즈 조절 DOM
const leftSide = ref<Element>(null); // 왼쪽 DOM
const rightSide = ref<Element>(null); // 오른쪽 DOM
const x = ref<number>(0); // 현재 마우스 X 좌표
const leftWidth = ref<number>(0); // 왼쪽 DOM 너비

const word = ref<string>(""); // 검색어

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

const synonymList = ref<Array<string>>([]); // 동의어 목록
const printSynonymList = ref<any>([]); // 검색창 아래 동의어 목록

const listData = ref<any>([]); // 통합 검색 결과 리스트

const classification = ref<string>(""); // 카테고리

// 검색 필터 옵션
const authority = ref<string>("User");
const searchOption = ref<string>("mu-btn mu-btn-lg mu-btn-bg-non btn-filter-toggle");
const showFilterWrap1 = ref<boolean>(false);

// 추가 속성 필터 옵션
const searchOptionSub = ref<string>("mu-btn btn-attribute-filter-toggle");
const showFilterWrap2 = ref<boolean>(false);

const filterData = ref<Array<any>>([]); // 필터 검색 결과
const filterDataType = ref<Array<string>>([]); // 필터 목록 ["hive"]
const filterDataTypeVal = ref<string>("hive"); // 필터 값

const filterDataTypeSub = ref<Array<string>>([]); // 서브 필터 목록 ["hive_table", "hive_column"]
const filterDataTypeSubVal = ref<string>("hive_table"); // 서브 필터 값
const filterDataTypeSubObj = ref<Array<any>>([]); // 서브 필터 갑들

const filterSearchSortType = ref<string>(""); // 정렬 조건 [ ASCENDING, DESCENDING ]

const filterAddFilter = ref<Array<any>>([]); // 추가 속성 필터 [condition: "AND", criterion: [{}, {}, { condition: "OR", criterion: []}]
const attributeDefsArr = ref<Array<any>>([]); // 추가 속성 필터의 내용
const filteroptToggle = ref<string>("Filter"); // [Filter, Close]

const filterCnt = ref<number>(0); // 필터 개수

const filterCondition = ref<any>({
  // 필터 조건
  // Atlas의 경우 검색 시 begins with, ends with --> startsWith, endsWith 파라미터 이용
  condition1: ["=", "!=", "contains", "startsWith", "endsWith", "isNull", "notNull"],
  condition2: ["=", "!=", "isNull", "notNull"],
  condition3: ["=", "!=", ">", "<", ">=", "<=", "isNull", "notNull"],
});

const filterFactor = ref<any>({
  factor1: ["USER", "ROLE", "GROUP"],
  factor2: ["TRUE", "FALSE"],
});

const searchParams = ref<any>({});

await categoryStore.fetchCategories();

onMounted(() => {
  // 권한 조회
  authority.value = user.authority;

  // 현재 라우트 경로
  const path = ObjectUtils.getPropertyValue(params, "path", "");

  // 상세 조회 페이지에서 통합검색 페이지로 이동 시, 상세 페이지 이동 직전 사용자가 세팅한 검색조건으로 목록 조회
  if (fromPath.includes("/search/detail") && path === "/search/keyword") {
    getSearchByPrevCondition();
  } else {
    // 메인 카테고리 아이콘 클릭 or 카테고리 검색 화면에서 카테고리 메뉴 클릭 or 트리맵 내부 클릭을 통해 진입
    if (history.state.name) {
      const matched = getOriginalList.find((el) => el.name === history.state.name);
      if (matched || history.state.name === "_NOT_CLASSIFIED") {
        classification.value = history.state.name;
        categoryStore.setSelectedItemGuid(
          history.state.name === "_NOT_CLASSIFIED" ? "_NOT_CLASSIFIED" : matched.guid
        );

        // 좌측 메뉴 펼치기 처리
        toggleParents(matched)
      }
    } else {
      // 통합 검색 화면으로 바로 진입 or 카테고리 검색 화면에서 검색어 입력하여 진입한 경우
      if (history.state.word) {
        word.value = history.state.word; // 카테고리 검색 화면에서 입력된 검색어 설정
      }
      getCategoryData(); // 카테고리 전체 조회
    }

    getFilterData(); // 필터링
  }

  getDbInfo(); // DB 정보 조회

  // 조절할 창 및 조절 요소 설정
  divider.value = document.getElementById("divider");
  leftSide.value = divider.value.previousElementSibling;
  rightSide.value = divider.value.nextElementSibling;
});

// 좌측 메뉴 펼치기 처리
const toggleParents = (matched) => {
  if (matched) {
    const parent_matched = getOriginalList.find((el2) => el2.name === matched.superTypes[0])
    if (parent_matched) {
      categoryStore.getToggleTargets.value[parent_matched.name] = true
      toggleParents(parent_matched)
    }
  }
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

// 상세 페이지 이동 전 검색조건을 토대로 목록 조회
const getSearchByPrevCondition = (): void => {
  const prevSearchParams = ObjectUtils.getPropertyValue(
    params.value,
    "searchParams",
    null
  );
  // 검색어, 선택 카테고리 세팅
  word.value = ObjectUtils.getPropertyValue(prevSearchParams, "query", "");
  classification.value = ObjectUtils.getPropertyValue(
    prevSearchParams,
    "classification",
    ""
  );

  // 필터 검색 조건 관련 값 세팅
  filterSearchSortType.value = ObjectUtils.getPropertyValue(
    prevSearchParams,
    "sortOrder",
    ""
  );
  getFilterData();
  filterData.value = ObjectUtils.getPropertyValue(prevSearchParams, "filterData", []);
  filterDataType.value = ObjectUtils.getPropertyValue(
    prevSearchParams,
    "filterDataType",
    ""
  );
  filterDataTypeVal.value = "hive";
  onSelectFilterChange();
  filterDataTypeSub.value = ObjectUtils.getPropertyValue(
    prevSearchParams,
    "filterDataTypeSub",
    ""
  );
  filterDataTypeSubVal.value = ObjectUtils.getPropertyValue(
    prevSearchParams,
    "typeName",
    ""
  );
  filterAddFilter.value = ObjectUtils.getPropertyValue(
    prevSearchParams,
    "filterAddFilter",
    ""
  );

  // 페이징 관련 값 세팅
  const defaultPagingObj = {
    total: 0,
    limit: 10,
    block: 10,
    currentPage: 1,
  };
  const paging = ObjectUtils.getPropertyValue(params.value, "paging", defaultPagingObj);
  pagingObj.value = paging;

  getSearch(paging.currentPage - 1);
};

// 좌측 카테고리 목록 조회
const getCategoryData = async (): Promise<void> => {
  await categoryStore.fetchCategories({ from: "searchByKeyword" });
};

// DB 정보 조회
const getDbInfo = async (): Promise<void> => {
  await categoryStore.getDbData();
};

// 필터를 적용하여 검색
const getFilterData = async (): Promise<void> => {
  // entityDefs 전체 조회
  const list = $ofetch("/api/search/getApiSearch", {
    method: "POST",
    body: {
      url: "/types/typedefs",
      params: { type: "entity" },
    },
  });

  await Promise.all([list]).then((results) => {
    if (results[0].data.hasOwnProperty("entityDefs")) {
      filterData.value = ObjectUtils.getPropertyValue(results[0].data, "entityDefs");
      if (filterData.value.length > 0) {
        filterDataType.value = filterData.value
          .filter((item) => {
            return (
              item.superTypes.includes("__internal") === false &&
              item.serviceType === "hive"
            );
          })
          .map((item) => item.serviceType);
        filterDataType.value = filterDataType.value.filter((val, idx) => {
          return filterDataType.value.indexOf(val) === idx;
        });
      }
    }
  });
  filterDataTypeVal.value =
    filterDataType.value.length > 0 ? filterDataType.value[0] : "none"; // 'hive'
  onSelectFilterChange();
  getSearch(0);
};

// 검색어에서 카테고리 삭제
const deleteCategoryTag = (): void => {
  classification.value = "";
  getSearch(0);
};

// 데이터 유형 상위 필드 선택 시 하위 필터 내용 조회
const onSelectFilterChange = (): void => {
  // hive_table, hive_column 가져오기
  if (filterDataTypeVal.value !== "") {
    filterDataTypeSub.value = filterData.value
      .filter((item) => {
        return (
          item.serviceType === filterDataTypeVal.value &&
          item.name !== "__internal" &&
          !item.superTypes.includes("__internal")
        );
      })
      .map((item) => item.name)
      .filter((item2) => {
        return item2.endsWith("_table") || item2.endsWith("_column");
      });

    onSelectFilterSubChange();
  } else {
    attributeDefsArr.value = [];
    filterDataTypeSub.value = [];
    filterDataTypeSubVal.value = "";
    filterAddFilter.value = [];
    showFilterWrap2.value = true;
    showSearchOptionSub();
  }
};

// 한글명 가져오기
const getKrName = (key: string): string => {
  if (key) {
    return categoryStore.getClassificationNameKr(key);
  } else {
    // 카테고리 미분류
    return "";
  }
};

// 검색
const getSearch = async (offset: number): Promise<void> => {
  printSynonymList.value = [];
  try {
    const param = {};
    let searchWord = "";
    if (word.value !== "") {
      searchWord = word.value;
      param.query = searchWord;
    }
    if (filterDataTypeSubVal.value !== "") {
      // hive_table, hive_column
      param.typeName = filterDataTypeSubVal.value;
    }
    param.limit = pagingObj.value.limit;
    param.offset = pagingObj.value.limit * offset;

    param.classification = classification.value;
    param.sortOrder = filterSearchSortType.value;

    // 추가 속성 필터가 있는 경우 추가
    const searchFilterObj = [];
    if (showFilterWrap2.value) {
      if (filterAddFilter.value.length > 0) {
        // 값 입력 확인
        let checkVal = checkFilterSearch(filterAddFilter.value);
        if (typeof checkVal !== undefined) {
          if (checkVal === true) {
            await errorAlert("추가속성필터를 삭제 또는 입력해주세요");
            return;
          }
        }

        // 모든 그룹 조건 가공
        function filterCondition(criterion: any[]) {
          criterion.filter((item: { criterion: any; condition: any; idx: any; attributeValueText: any; typeName: any; }) => {
            if (item.criterion === undefined) {
              delete item.condition;
              if (item.idx !== undefined) delete item.idx;
              if (item.attributeValueText !== undefined) delete item.attributeValueText;
              if (item.typeName !== undefined) delete item.typeName;
            } else {
              filterCondition(item.criterion);
            }
            return item;
          });
        }
        let filterAddFilterJson = {};
        filterAddFilterJson.condition = filterAddFilter.value[0].condition;

        let filterAddFilterJsonData = JSON.parse(
          JSON.stringify(filterAddFilter.value[0].criterion)
        );
        filterCondition(filterAddFilterJsonData);

        filterAddFilterJson.criterion = filterAddFilterJsonData;
        for (let i = 0; i < filterAddFilterJson.criterion.length; i++) {
          if (filterAddFilterJson.criterion[i].idx)
            delete filterAddFilterJson.criterion[i].idx;
          if (!filterAddFilterJson.criterion[i].attributeValue) {
            // await errorAlert(
            //   "상세 필터 설정값 을 확인해주세요 조건이 없어도 된다면 필터 초기화가 필요합니다."
            // );
            // return;
          }
        }

        searchFilterObj.push(filterAddFilterJson);
      }
    }

    // hive_column, hive_table 선택을 안한 경우
    if (filterDataTypeSubVal.value === "") {
      let noneDataTypeObj = {};
      noneDataTypeObj.condition = "OR";
      const noneDataCriterionArr = [];
      noneDataCriterionArr.push({
        attributeName: "__typeName",
        operator: "eq",
        attributeValue: "hive_table",
      });
      noneDataCriterionArr.push({
        attributeName: "__typeName",
        operator: "eq",
        attributeValue: "hive_column",
      });
      noneDataTypeObj.criterion = noneDataCriterionArr;
      searchFilterObj.push(noneDataTypeObj);
    }

    // Atlas에 조회하기 위한 전체 필터내용 저장
    if (searchFilterObj[0]) {
      param.entityFilters = {
        condition: searchFilterObj[0].condition,
        criterion: searchFilterObj,
      };
    }

    // 검색조건 저장
    let res = await $ofetch(`/api/search/keyword`, { method: "POST", body: param });
    let result = res.data;

    setSearchParams(param);

    // 대략적인 수가 있는 경우
    if (result.approximateCount > 0) {
      listData.value = result.entities;

      pagingObj.value = {
        ...pagingObj.value,
        total: ObjectUtils.getPropertyValue(result, "approximateCount", 0),
        currentPage: offset + 1,
      };
    } else {
      listData.value = null;
      synonymList.value = [];

      pagingObj.value = {
        ...pagingObj.value,
        total: 0,
        currentPage: 1,
      };
    }

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

    showFilterWrap1.value = true;
    showSearchOption();
  } catch (error) {
    console.log(error);
    listData.value = null;

    pagingObj.value = {
      ...pagingObj.value,
      total: 0,
      currentPage: 1,
    };
  }
};

// 데이터 유형 하위 필드 선택 시 추가 속성 필터에서 사용할 필드 조회
const onSelectFilterSubChange = (): void => {
  // hive_table, hive_column가 아닌 전체선택
  if (filterDataTypeSubVal.value === "") {
    searchOptionSub.value = "mu-btn btn-attribute-filter-toggle";
    showFilterWrap2.value = false;
  }

  const attributedefs = [];
  // hive_table 또는 hive_column의 데이터 조회
  if (filterDataTypeSubVal.value !== "") {
    filterDataTypeSubObj.value = filterData.value
      .filter((item) => {
        return (
          item.serviceType === filterDataTypeVal.value &&
          item.name !== "__internal" &&
          !item.superTypes.includes("__internal") &&
          item.name === filterDataTypeSubVal.value
        );
      })
      .map((item) => item)[0];

    // 하위 필드가 있는 경우
    if (
      filterDataTypeSubObj.value &&
      filterDataTypeSubObj.value.attributeDefs.length > 0
    ) {
      // typeName 조회 (array, map, set 제외 string, date...)
      attributedefs.push(
        ...filterDataTypeSubObj.value.attributeDefs
          .filter((item: { typeName: string | string[]; }) => {
            return (
              item === item &&
              item.typeName.indexOf("array") &&
              item.typeName.indexOf("map") &&
              item.typeName.indexOf("set")
            );
          })
          .map((item: any) => item)
      );
    } else {
      // filterData에서 가공하여 조건에 사용할 필드 가져오기
      const tempArray = filterData.value
        .filter((item) => {
          return item.name === filterDataTypeSubObj.value.superTypes[0];
        })
        .map((item) => item);

      if (tempArray.length > 0) {
        const temp1 = filterData.value
          .filter((item) => {
            return item.name === filterDataTypeSubObj.value.superTypes[0];
          })
          .map((item) => item.attributeDefs)[0];
        if (temp1.length > 0) {
          attributedefs.push(
            ...temp1
              .filter((item: { typeName: string | string[]; }) => {
                return (
                  item === item &&
                  item.typeName.indexOf("array") &&
                  item.typeName.indexOf("map") &&
                  item.typeName.indexOf("set")
                );
              })
              .map((item: any) => item)
          );
          const temp2 = filterData.value
            .filter((item) => {
              return item.name === filterDataTypeSubObj.value.superTypes[0];
            })
            .map((item) => item.superTypes)[0];
          if (temp2.length > 0) {
            const temp3 = filterData
              .filter((item) => {
                return (
                  item.serviceType === filterDataTypeVal.value &&
                  item.name !== "__internal" &&
                  !item.superTypes.includes("__internal") &&
                  item.name === temp2[0]
                );
              })
              .map((item: { attributeDefs: any; }) => item.attributeDefs)[0];
            if (temp3 != null) {
              if (temp3.length > 0) {
                attributedefs.push(
                  ...temp3
                    .filter((item: { typeName: string | string[]; }) => {
                      return (
                        item === item &&
                        item.typeName.indexOf("array") &&
                        item.typeName.indexOf("map") &&
                        item.typeName.indexOf("set")
                      );
                    })
                    .map((item: any) => item)
                );
              }
            }
          }
        } else {
          const tempSub1 = filterData.value
            .filter((item) => {
              return item.name === filterDataTypeSubObj.value.superTypes[0];
            })
            .map((item) => item.superTypes);
          if (tempSub1.length > 0) {
            const tempSub1supertype = filterData.value
              .filter((item) => {
                return (
                  item.serviceType === filterDataTypeVal.value &&
                  item.name !== "__internal" &&
                  !item.superTypes.includes("__internal") &&
                  item.name === tempSub1[0]
                );
              })
              .map((item) => item.superTypes)[0];

            if (tempSub1supertype != null) {
              const tempSub2 = filterData.value
                .filter((item) => {
                  return (
                    item.serviceType === filterDataTypeVal.value &&
                    item.name !== "__internal" &&
                    !item.superTypes.includes("__internal") &&
                    item.name === tempSub1supertype
                  );
                })
                .map((item) => item.attributeDefs)[0];

              attributedefs.push(
                ...tempSub2
                  .filter((item: { typeName: string | string[]; }) => {
                    return (
                      item === item &&
                      item.typeName.indexOf("array") &&
                      item.typeName.indexOf("map") &&
                      item.typeName.indexOf("set")
                    );
                  })
                  .map((item: any) => item)
              );
            }
            const tempSub2 = filterData.value
              .filter((item) => {
                return (
                  item.serviceType === filterDataTypeVal.value &&
                  item.name !== "__internal" &&
                  !item.superTypes.includes("__internal") &&
                  item.name === tempSub1[0]
                );
              })
              .map((item) => item.attributeDefs)[0];

            attributedefs.push(
              ...tempSub2
                .filter((item: { typeName: string | string[]; }) => {
                  return (
                    item === item &&
                    item.typeName.indexOf("array") &&
                    item.typeName.indexOf("map") &&
                    item.typeName.indexOf("set")
                  );
                })
                .map((item: any) => item)
            );
          }
        }
      } else {
        // 추가 속성 필터 감추기
        showFilterWrap2.value = false;
      }
    }

    if (filterDataTypeSubObj.value && filterDataTypeSubObj.value.superTypes.length > 0) {
      const appendAttributeDefArr = (data_s: any[], filterDataTypeSubObj: any[]) => {
        // 상위 필터 내용에 하위 필터 내용 추가
        let ddd = data_s
          .filter((items: { name: any; }) => {
            return items.name === filterDataTypeSubObj.superTypes[0];
          })
          .map((items: any) => items);
        // 조건에 사용할 속성 추가 (userdescription, qualified_name...)
        attributedefs.push(
          ...ddd[0].attributeDefs
            .filter((item_s: { typeName: string | string[]; }) => {
              return item_s.typeName.includes("AtlasServer") === false;
            })
            .map((item_s: any) => item_s)
        );

        // 최하위 타입까지 존재하는 모든 속성 추가
        if (ddd[0].superTypes.length > 0) {
          appendAttributeDefArr(data_s, ddd[0]);
        }
      };
      appendAttributeDefArr(filterData.value, filterDataTypeSubObj.value);
    }
    const set = new Set(attributedefs);
    attributeDefsArr.value = [...set];
    filterAddFilter.value.splice(0); // 첫 요소 뺴고 모두 삭제

    // 추가 속성 필터의 기본 필드 값 설정
    if (attributeDefsArr.value.length > 0) {
      const filterDetailData = filterDetailCondition(attributeDefsArr.value[0]);

      let filterAttributeValue = "";
      let filterAttributeValueText = "";

      // 기본 필터 조건 값 설정
      filterAddFilter.value.push({
        condition: "AND",
        criterion: [
          {
            idx: 0,
            attributeName: filterDetailData.name,
            typeName: filterDetailData.typeName,
            operator: filterDetailData.filterOperator,
            attributeValue: filterAttributeValue,
            attributeValueText: filterAttributeValueText,
          },
        ],
      });
    }
  } else {
    filterSearchSortType.value = "";
  }
};

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

// 필터 검색 조건 초기화
const resetSearchParam = (): void => {
  word.value = "";
  filterDataTypeVal.value = "hive";
  filterDataTypeSubVal.value = "hive_table";
  filterDataTypeSub.value = ["hive_table", "hive_column"];
  filterDataTypeSubObj.value = [];
  filterSearchSortType.value = "";
  filterAddFilter.value.splice(0);
  onSelectFilterSubChange();
  showFilterWrap2.value = true;
  showSearchOptionSub();
};

// 추가속성필터 > 그룹삭제 > 그룹이 전혀 없는 경우
const deleteGroupInit = (): void => {
  filterDataTypeVal.value = "hive";
  filterDataTypeSub.value = ["hive_table", "hive_column"];
  filterDataTypeSubVal.value = "hive_table";
  filterDataTypeSubObj.value = [];
  showSearchOptionSub();
  onSelectFilterSubChange();
};

// 상세 데이터 이동
function onSelectDetail(guidv: string): void {
  router.push("/search/detail?guid=" + guidv);
}

// 좌측 카테고리 리스트 내 노드 클릭 이벤트 핸들러
const onClickMenuItem = (item: any): void => {
  if (!item) {
    classification.value = "";
  } else {
    classification.value = item.name;
  }
  getSearch(0);
};

// 한 페이지에 보여줄 개수 변경 이벤트
const onSelectChange = (): void => {
  getSearch(0);
};

// 필터 상세 조건의 필드에 따라 operator 및 값 설정
const filterDetailCondition = (attributeDefsArr: any): any => {
  const filterDetailData = {};
  let filterOperator;
  let typeName = attributeDefsArr.typeName;
  let name = attributeDefsArr.name;
  if (typeName === "string") {
    filterOperator = filterCondition.value.condition1[0];
  } else if (typeName === "hive_principal_type") {
    filterOperator = filterCondition.value.condition1[0];
  } else if (typeName === "sap_principal_type") {
    filterOperator = filterCondition.value.condition1[0];
  } else if (typeName === "boolean") {
    filterOperator = filterCondition.value.condition2[0];
  } else if (typeName === "int") {
    filterOperator = filterCondition.value.condition3[0];
  } else if (typeName === "date") {
    filterOperator = filterCondition.value.condition3[0];
  } else {
    filterOperator = filterCondition.value.condition1[0];
  }
  filterDetailData.filterOperator = filterOperator;
  filterDetailData.typeName = typeName;
  filterDetailData.name = name;

  return filterDetailData;
};

// 추가 속성 필터 사용 시 값을 입력했는지 여부
const checkFilterSearch = (filterData: any): boolean => {
  let returnVal = false;
  const filterRoof = (data: any) => {
    for (const item of data) {
      if (typeof item.criterion !== "undefined") {
        filterRoof(item.criterion);
      } else {
        if (item.attributeValue !== null && item.attributeValue === "") {
          if (item.operator !== "isNull" && item.operator !== "notNull") {
            returnVal = true;
            break;
          }
        }
      }
    }
  };
  filterRoof(filterData);
  return returnVal;
};

// 상세 페이지로부터 복귀 시 사용할 검색조건 저장
const setSearchParams = (param: any): void => {
  searchParams.value = param;
  param.filterData = filterData.value;
  param.filterDataType = filterDataType.value;
  param.filterDataTypeVal = filterDataTypeVal.value;
  param.filterDataTypeSub = filterDataTypeSub.value;
  param.filterDataTypeSubVal = filterDataTypeSubVal.value;
  param.filterAddFilter = filterAddFilter.value;
};

// 페이지 번호 클릭
const onClickPage = (page: number): void => {
  getSearch(page - 1);
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
    searchOptionSub.value = "mu-btn btn-attribute-filter-toggle on";
  } else {
    showFilterWrap2.value = false;
    searchOptionSub.value = "mu-btn btn-attribute-filter-toggle";
  }
};

// X 버튼
const onClickDeleteButton = (): void => {
  printSynonymList.value = [];
  word.value = "";
};

// 검색 유효성 체크
const checkValidate = async (word: string): Promise<void> => {
  if (word.length >= 2) {
    getSearch(0);
  } else {
    await errorAlert("최소 2글자 이상 입력하세요");
  }
};
</script>
