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
                      필터적용중 <span>
                      <button @click="deleteCategoryTag()"><i class="mu-icon-svg item-delete"></i></button>
                    </span>
                    </em>
                  </div>
                </div>
                <input
                  type="text"
                  class="mu-input"
                  placeholder="테이블을 제외한 컬럼/타 메타 검색결과를 조회하려면 상세필터로 검색하세요"
                  title="검색어를 입력해 주세요."
                  @keypress.enter.prevent="getSearch(0)"
                  v-model="word"
                />
                <button
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
              <div class="tag" v-for="item in printSynonymList">
                <span class="tag-label" :style="isTermStyle(item)">{{ item.name }}</span>
              </div>
            </div>
          </dd>
        </dl>
        <div class="data-filter-wrap" v-show="!showFilterWrap1" style="display: block">
          <div class="content-detail-search">
            <div class="mu-heading">
              <h4>상세필터</h4>
            </div>
            <div class="detail-search-form">
              <div class="mu-form mu-form-vertical">
                <div class="mu-form-item" v-for="(df, idx) in detailFilters" >
                  <div class="btn-category">
                    <div class="mu-checkbox all">
                      <input :type="df.all ? 'radio' : 'checkbox'" :name="idx" :id="'ckbox_'+idx" value="ALLs" checked disabled>
                      <label :for="'ckbox_'+idx"> <span>{{ getRootDescription(idx) }}</span></label>
                    </div>
                    <div class="mu-checkbox all">
                      <input :type="df.all ? 'radio' : 'checkbox'" :name="idx" :id="'ckb_'+idx" value="ALL" @change="onChangeCheckBox(idx, $event);" :checked="df.all">
                      <label :for="'ckb_'+idx"> <span>{{ getRootDescription(idx) }}</span></label>
                    </div>
                    <div v-for="(tg, idx2) in df.content" class="mu-checkbox">
                      <input :type="idx == 'ORDER' ? 'radio' : 'checkbox'" :name="idx" :id="'ckbox_'+tg.name" :value="tg.name" @change="onChangeCheckBox(idx, $event)" :checked="false" disabled>
                      <label :for="'ckbox_'+tg.name"><span>{{ tg.description }}<em v-if="idx != 'TAG' && idx != 'ORDER'">({{ tg.count }})</em></span></label>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="on tab-content">
              <div class="mu-search-group">
                <div class="mu-search-item">
                </div>
                <div
                  class="attribute-filter-item slim-scroll"
                  v-show="showFilterWrap2"
                  style="display: block"
                >
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
            <div class="data-result" >
              <ul class="data-box-list">
                <li class="data-box" v-if="listData" v-for="data in listData">
                  <div class="inner-data visual" @click="onSelectDetailPage(data)">
                    <i v-if="data.metatype && data.metatype.toUpperCase() == 'CONTAINER'" class="ico-data data-04"></i>
                    <i v-else-if="data.metatype && data.metatype.toUpperCase() == 'DATABASE'" class="ico-data data-02"></i>
                    <i v-else-if="data.metatype && data.metatype.toUpperCase() == 'DATABASESCHEMA'" class="ico-data data-06"></i>
                    <i v-else-if="data.metatype && data.metatype.toUpperCase() == 'DATABASESERVICE'" class="ico-data data-06"></i>
                    <i v-else-if="data.metatype && data.metatype.toUpperCase() == 'DIRECTORY'" class="ico-data data-06"></i>
                    <i v-else-if="data.metatype && data.metatype.toUpperCase() == 'FILESYSTEMSERVICE'" class="ico-data data-06"></i>
                    <i v-else-if="data.metatype && data.metatype.toUpperCase() == 'STORAGESERVICE'" class="ico-data data-04"></i>
                    <i v-else-if="data.metatype && data.metatype.toUpperCase() == 'TABLE'" class="ico-data data-05"></i>
                    <i v-else-if="data.metatype && data.metatype.toUpperCase() == 'COLUMN'" class="ico-data data-01"></i>
                    <i v-else class="ico-data data-06"></i>
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
                        <strong class="data-title">{{ data && data.attributes && data.attributes.description ? data.attributes.description : data.description }}</strong>
                        <p class="data-name"> {{ data && data.name ? data.name : ''}} </p>
                        <p class="data-desc">{{data && data.userdescription? data.userdescription : ''}}</p>
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
                    <p >생성일 : {{ yyyyMMdd(data.createtime) }}</p>
                    <p >수정일 : {{ yyyyMMdd(data.updatetime) }}</p>
                    <p >최종접근시간 : {{ yyyyMMddHHmmSS(data.lastaccesstime ? data.lastaccesstime: null) }}</p>
                    <p v-html="checkDataAccess(data.dataAccess)"></p>
                    <p >시스템 타입 : {{ data.systemtype }}</p>
                    <p >메타 타입 : {{ data.metatype }}</p>
                    <p v-if="data.qualifiedname">DB : {{ getDbName(data.qualifiedname) }}</p>
                    <p v-if="data.qualifiedname && data.qualifiedname.split('.')[2]">스키마 : {{ getSchemaName(data.qualifiedname) }}</p>
                  </div>
                </li>
                <li v-else><p class="nodata">검색결과가 존재하지 않습니다.<span>다른 검색어를 입력해주세요.</span></p></li>
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
const getCategoryList = categoryStore.getCategoryList;

const searchStore = useSearchStore();
const searchParams = ref({
  query: "",
  systemtype: "",
  metatype: "",
  limit: 10,
  offset: 0,
  classification: {
    id: "",
    name: "",
    description: ""
  },
  sortOrder: "",
  entityFilters: {}
});

// 카테고리, 데이터유형, 태그, 등록일
const detailFilters = ref({
  CATEGORY: {
    all: true,
    ord: 1,
    name: '카테고리',
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
    count: 0
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
    count: 0
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
      "name": "databaseSchema",
      "description": "databaseSchema",
      "count": 0,
      "checked": false,
    },
    ],
    count: 0
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
    count: 0
  },
  ORDER: {
    all: true,
    ord: 5,
    name: '정렬기준',
    content: [
      {
        "root": "ORDER",
        "rootDescription": "정렬기준",
        "id": 1,
        "name": "ASC",
        "description": "오름차순",
        "count": 0,
        "checked": false,
      },
      {
        "root": "ORDER",
        "rootDescription": "정렬기준",
        "id": 1,
        "name": "DESC",
        "description": "내림차순",
        "count": 0,
        "checked": false,
      },
    ],
    count: 0
  }
})

// data
const divider = ref<HTMLElement>(null); // 패널 사이즈 조절 DOM
const leftSide = ref<Element>(null); // 왼쪽 DOM
const rightSide = ref<Element>(null); // 오른쪽 DOM
const x = ref<number>(0); // 현재 마우스 X 좌표
const leftWidth = ref<number>(0); // 왼쪽 DOM 너비

const word = ref<string>(""); // 검색어

const filterSystemtypes = ref<Array<string>>([]); // 시스템 타입 필터 항목
const filterMetatypes = ref<Array<string>>([]); // 메타데이터 타입 필터 항목
const filterSortOrders = ref<Array<string>>(["ASC", "DESC"]); // 정렬 필터 항목

const systemtype = ref<string>(""); // 시스템 타입 (hive, hbase, postgreSQL 등 시스템 종류)
const metatype = ref<string>(""); // 메타데이터 타입으로 대상 system에서 수집되는 메타 종류를 의미
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

  await getDetailFilters()

  searchParams.value = searchStore.getSearchParams // 검색 조건 불러오기

  // 메인화면, 카테고리 검색에서 넘어온 경우
  if (fromPath == '/main' || fromPath == '/search/category') {
    // 단순히 메뉴를 클릭한 경우

    word.value = searchParams.value.query // 검색어
    classification.value = searchParams.value.classification ? searchParams.value.classification : {
      id: "",
      name: "",
      description: ""
    } // 카테고리
    categoryStore.setSelectedItemId(classification.value.id); // 체크 처리
    categoryStore.setToggleTargetOnClickTreemap(classification.value.id); // 메뉴 활성화
    const matched = getOriginalList.find((el) => el.id === classification.value.id);
    toggleParents(matched)
  } else if (fromPath.includes('/search/detail')) { // 상세페이지에서 넘어온 경우
    word.value = searchParams.value.query ? searchParams.value.query : "" // 검색어
    classification.value = searchParams.value.classification ? searchParams.value.classification : {
      id: "",
      name: "",
      description: ""
    }
    systemtype.value = searchParams.value.systemtype ? searchParams.value.systemtype : "" // 시스템타입
    metatype.value = searchParams.value.metatype ? searchParams.value.metatype : "" // 메타타입
    sortOrder.value = searchParams.value.sortOrder ? searchParams.value.sortOrder : "" // 정렬방식
    pagingObj.value.currentPage = searchParams.value.offset + 1 // 현재 페이지
    pagingObj.value.limit = searchParams.value.limit // 보여줄 개수

    // 왼쪽메뉴 활성화 처리
    categoryStore.setSelectedItemId(searchParams.value.classification.id ? searchParams.value.classification.id : ""); // 체크 처리
    categoryStore.setToggleTargetOnClickTreemap(searchParams.value.classification.id ? searchParams.value.classification.id : ""); // 메뉴 활성화
    const matched = getOriginalList.find((el) => el.id === searchParams.value.classification.id);
    toggleParents(matched)

  } else { // 그 외 검색조건 초기화
    searchStore.setSearchParams(searchParams.value)
  }

  getSearch(pagingObj.value.currentPage - 1);
});

const checkUseFilter = computed(() => { // 상세검색 필터 사용중 여부
  return false
})

// 가공된 상세필터 정보 가져오기
const getDetailFilters = async () => {
  // 카테고리, 시스템, 태그, 등록일
  await $ofetch('/api/search/category/detailFilters').then(async (res) => {
    if (res && !res.errorMessage) {
      // 키의 배열을 원하는 순서로 정렬
      // let keys = ["CAT", "SYS", "TAG", "DATE"];
      let keys = ["CATEGORY", "SYSTEMTYPE", "METATYPE", "TAG", "ORDER"];
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
      await searchStore.setSearchParams(searchParams)
    }
    console.log(detailFilters.value)
  }).catch((e) => {
    console.log(e)
  })
}

// methods
// 좌측 메뉴 펼치기 처리
const toggleParents = (matched) => {
  if (matched) {
    const parent_matched = getOriginalList.find((el2) => el2.id === matched.parent_id)
    if (parent_matched) {
      categoryStore.getToggleTargets.value[parent_matched.id] = true
      toggleParents(parent_matched)
    }
  }
}

const getSystemtypeDesc = (sys) => {
  console.log(sys)
  return sys
}

const getMetatypesDesc = (meta) => {
  return meta
}

const getSortOrderDesc = (order) => {
  if (order == 'DESC') return "내림차순"
  if (order == 'ASC') return "오름차순"
  return order
}

// 검색
const getSearch = async (offset: number = 0): Promise<void> => {
  printSynonymList.value = [];
  const params = {
    query: word.value ? word.value : "",
    systemtype: systemtype.value,
    metatype: metatype.value,
    limit: pagingObj.value.limit,
    offset: offset,
    classification: classification.value && classification.value.name ? classification.value.name : "", // 검색할 때는 name값 사용
    sortOrder: sortOrder.value,
    entityFilters: {}
  };

  const result = await $ofetch(`/api/search/getEsSearch`, {
    method: "POST",
    body: params,
  });

  listData.value = result.data.result.results

  // 페이징
  pagingObj.value = {
    ...pagingObj.value,
    total: result.data.result.total_count,
    currentPage: result.data.result.page_index
  };

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

  // 검색 조건 저장
  params.classification = classification.value // 카테고리 검색조건 저장용으로 변경
  searchStore.setSearchParams(params)
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

// 동의어 스타일 설정
const isTermStyle = (item: any): { fontWeight: number } | {} => {
  if (item.isTerm) {
    return { fontWeight: 800 };
  } else {
    return {};
  }
};

// 필터 검색 조건 초기화
const resetSearchParam = (): void => {
  word.value = "";

  detailFilters.value['CATEGORY'].all = true
  detailFilters.value['CATEGORY'].content.forEach((el) => {
    el.checked = false
  })
  detailFilters.value['SYSTEMTYPE'].all = true
  detailFilters.value['SYSTEMTYPE'].content.forEach((el) => {
    el.checked = false
  })

  detailFilters.value['METATYPE'].all = true
  detailFilters.value['METATYPE'].content.forEach((el) => {
    el.checked = false
  })

  detailFilters.value['TAG'].all = true
  detailFilters.value['TAG'].content.forEach((el) => {
    el.checked = false
  })

  detailFilters.value['ORDER'].all = true
  detailFilters.value['ORDER'].content.forEach((el) => {
    el.checked = false
  })
};

// 상세 데이터 이동
function onSelectDetailPage(item: any): void {
  router.push(`/search/detail?guid=${item.id}`);
  userStore.setGuid(item.id);
  window.scrollTo(0, 0);
}

// 좌측 카테고리 리스트 내 노드 클릭 이벤트 핸들러
const onClickMenuItem = (item: any): void => {
  console.log(item)
  if (!item) {
    classification.value = {
      id: "",
      name: "",
      description: ""
    };

  } else {
    categoryStore.setToggleTargetOnClickTreemap(item.id)
    classification.value = item;

    // 상세필터 카테고리 체크 설정
    detailFilters.value.CATEGORY.content.forEach((el) => {
      if (el.id == item.id) {
        el.checked = true;
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
  pagingObj.value = {
    ...pagingObj.value,
    total: page
  };

  getSearch(page);

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
  word.value = "";
};

// 날짜변환
const getDateFormat = (datetime) => {
  if(datetime == null || datetime == undefined || datetime == ""){
    return " - "
  }

  let [dateStr, timeStr] = datetime.split(' ');

  // 날짜 부분 변환
  let year = dateStr.substring(0, 4);
  let month = dateStr.substring(5, 7);
  let day = dateStr.substring(8, 10);

  let formattedDateTime = `${year}-${month}-${day}`;

  return formattedDateTime;
}

const yyyyMMdd = (value) => {
  let date = new Date(value);
  let year = date.getFullYear();
  let month = String(date.getMonth() + 1).padStart(2, "0");
  let day = String(date.getDate()).padStart(2, "0");
  let hours = String(date.getHours()).padStart(2, "0");
  let min = String(date.getMinutes()).padStart(2, "0");
  let sec = String(date.getSeconds()).padStart(2, "0");
  if (value == null || value == undefined) {
    return "-";
  }
  return year + "-" + month + "-" + day;
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
    return "-";
  }
  return year + "-" + month + "-" + day + " " + hours + ":" + min + ":" + sec;
};

const checkDataAccess = (flag) => {
  let dataAccessComment = "데이터 조회권한 ";
  dataAccessComment += flag ? "<span style='color: blue;'>있음</span>" : "<span style='color: red;'>없음</span>" ;

  return dataAccessComment;
}

// 검색에서 필터적용중 삭제
const deleteCategoryTag = (): void => {
  console.log('필터 전체 제거')
};

const getRootDescription = (key) => {
  if (key == 'CATEGORY') return "카테고리"
  else if (key == 'SYSTEMTYPE') return "시스템 타입"
  else if (key == 'METATYPE') return "메타 타입"
  else if (key == 'TAG') return "태그"
  else if (key == "ORDER") return "정렬기준"
  else return ""
}

// 조건 클릭 이벤트
const onChangeCheckBox = async (key, event: any) => {
  if (event.target.value == 'ALL') {
    detailFilters.value[key].content.forEach((el) => {el.checked = false})
    detailFilters.value[key].all = true
    detailFilters.value[key].count = 0

  }

  // 아무것도 선택하지 않은 상태인 경우 전체 표시
  if (detailFilters.value[key].count == 0) detailFilters.value[key].all = true

  getSearch(0)
}

// DB명
const getDbName = (qualifiedName) => {
  if (qualifiedName) {
    return qualifiedName.split(".")[1]
  }
  return ""
}

// 스키마명
const getSchemaName = (qualifiedName) => {
  if (qualifiedName) {
    return qualifiedName.split(".")[2]
  }
  return ""
}

</script>
