<template>
  <div>
    <div class="inner">
      <div class="mu-section">
        <!-- 카테고리 관리 & 더보기 툴팁 -->
        <meta-data-tooltip :tooltip-obj="tooltipObj" :selected-category="selectedCategory"
          @onClickMenu="onClickCategoryManageMenu" />

        <!-- 카테고리 목록 -->
        <left-menu-list @onClickMenuItem="onClickMenuItem" @onClickOptionalButtons="onClickOptionalButtons"
          @onClickCategoryOrderChange="onClickCategoryOrderChange" />

        <!-- 카테고리 리스트 사이즈 조절 바 -->
        <div data-v-10a51f6f id="divider" @mousedown="mouseDown($event)"></div>

        <!-- page-content : R 서브내용 -->
        <div class="page-content">
          <div class="board-list-wrap">
            <classification-info :selected-category="selectedCategory" @onClickUpdate="onClickCategoryManageMenu" />

            <!-- 메타 테이블 헤더  -->
            <div class="mu-heading" style="display: block;">
              <!-- board-list-search -->
              <div style="display: flex;">
                <div class="board-list-search" style="flex: 1; margin-right: 5px;">
                  <form action="" @submit.prevent="onFilterSearch">
                    <fieldset>
                      <legend>게시물검색</legend>
                      <div class="inner-search">
                        <div class="mu-selectbox w120">
                          <select name="searchType" :value="filterSearchObj.searchType" @change="onChange">
                            <option v-for="(option, index) in filterSearchObj.options" :key="index" :value="option.value">
                              {{ option.desc }}
                            </option>
                          </select>
                        </div>
                        <div class="mu-input-icon">
                          <input type="text" class="mu-input" placeholder="Search" title="검색어입력" name="word"
                            :value="filterSearchObj.word" @keyup="onChange" />
                          <button type="button" class="mu-btn mu-btn-sch" @click.prevent="onFilterSearch">
                            <i class="mu-icon-svg search"></i><span>검색</span>
                          </button>
                        </div>
                      </div>
                    </fieldset>
                  </form>
                </div>
                <page-size v-model:pageSize="pagingObj.limit" @searchMethod="onFilterSearch"/>
              </div>
              <!-- //board-list-search -->
              <div style="margin-top: 10px; display: flex; justify-content: space-between; align-items: center;">
                <div class="mu-item-group">
                  <div class="mu-selectbox w160">
                    <select name="searchType2" :value="currentMetaType" @change="onChangeMetaType">
                      <option v-for="(item, index) in filterMetaTypeList" :key="index" :value="item.value">
                        {{ item.desc }}
                      </option>
                    </select>
                  </div>
                  <div class="mu-selectbox w160">
                    <select name="searchType4" :value="currentSystemType" @change="onChangeSystemType">
                      <option v-for="(item, index) in filterSystemTypeList" :key="index" :value="item.value">
                        {{ item.desc }}
                      </option>
                    </select>
                  </div>
                  <div class="mu-selectbox w160">
                    <select name="searchType3" :value="currentRelease" @change="onChangeRelease">
                      <option v-for="(item, index) in filterReleaseList" :key="index" :value="item.value">
                        {{ item.desc }}
                      </option>
                    </select>
                  </div>
                  <button type="button" class="mu-btn" @click.stop="onClickButton('roles')">
                    <span>공개설정</span>
                  </button>
                  <button type="button" class="mu-btn mu-btn-primary" @click.stop="onClickButton('category')">
                    <span>분류맵핑</span>
                  </button>
                  <button type="button" class="mu-btn mu-btn-primary" @click.stop="onClickButton('extract')">
                    <span>메타추출</span>
                  </button>
                </div>
              </div>
            </div>
            <!-- //메타 테이블 헤더  -->

            <!-- 메타 테이블 -->
            <div class="board-list">
              <table class="mu-grid mu-grid-data">
                <caption>
                  카테고리 관리 목록 게시판
                </caption>
                <colgroup>
                  <col style="width: 40px" />
                  <col style="width: 180px" />
                  <col style="width: 180px" />
                  <col style="width: 180px" />
                  <col style="width: 85px" />
                  <col style="width: 250px" />
                  <col style="width: 85px" />
                </colgroup>
                <thead>
                  <tr>
                    <th scope="col">
                      <div class="mu-checkbox">
                        <input type="checkbox" id="ckb1" title="전체선택" :checked="isAllChecked()"
                          @click.stop="(e) => onToggleCheckbox(e, true)" />
                        <label for="ckb1"><span class="blind">전체선택</span></label>
                      </div>
                    </th>
                    <th scope="col">데이터 설명</th>
                    <th scope="col">데이터 명</th>
                    <th scope="col">시스템 명</th>
                    <th scope="col">유형</th>
                    <th scope="col">카테고리</th>
                    <th scope="col">공개설정</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(item, index) in dataList" :key="index">
                    <td>
                      <div class="mu-checkbox">
                        <input type="checkbox" title="행선택" :id="`checkbox${index}`" :checked="isChecked(item)"
                          @click.stop="(e) => onToggleCheckbox(e, false, item)" />
                        <label :for="`checkbox${index}`"><span class="blind">행선택</span></label>
                      </div>
                    </td>
                    <td>
                      <!-- 데이터설명 -->
                      <a :title="item.description ? item.description : '-'" @click="onClickDataName(item)">{{
                        item.description ? item.description : "-"
                        }}</a>
                    </td>
                    <td>
                      <!-- 데이터명 -->
                      <a :title="item.meta_name ? item.meta_name : '-'" @click="onClickDataName(item)">{{
                        item.meta_name ? item.meta_name : "-"
                        }}</a>
                    </td>
                    <td :title="item.system_name ? item.system_name : '-'">
                      <!-- 시스템명 -->
                      {{ item.system_name ? item.system_name : "-" }}
                    </td>
                    <td :title="item.meta_type ? item.meta_type : '-'">
                      <!-- 유형 -->
                      {{ item.meta_type ? item.meta_type : "-" }}
                    </td>
                    <td>
                      <!-- 카테고리 -->
                      <category-group :classifications="item.classifications" :item="item" />
                    </td>
                    <td :title="item.release ? item.release : '-'">
                      <span :class="getReleaseClass(item.release)">{{ getRelease(item.release) }}</span>
                    </td>
                  </tr>
                  <tr v-if="dataList.length === 0">
                    <td colspan="7">
                      <p class="nodata">검색 결과가 존재하지 않습니다.</p>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <!-- //메타 테이블 -->

            <pagination :paging="pagingObj" @onClickPage="onClickPage" />
            <div class="mu-button-wrap">
              <button type="button" class="mu-btn mu-btn-lg mu-btn-primary" @click="goToFileRegister">
                <span>비정형 데이터 추가</span>
              </button>
            </div>
          </div>
        </div>
        <!-- //page-content -->
      </div>

      <!-- 카테고리 추가/수정/매핑 처리 모달 -->
      <manage-classification-modal ref="refManageModal" :manage-modal="manageModalObj"
        @onCloseModal="onToggleManageModal" @onSubmit="onSubmitCategory" />

      <!-- 메타추출 처리 모달 -->
      <manage-extract-meta-modal ref="refManageExtractModal" :extract-modal="extractModalObj"
        @onCloseModal="onToggleExtractModal" @onSubmit="downloadMetadata" />

      <!-- 공개설정 처리 모달 -->
      <manage-role-mapping-modal ref="refManageRoleMappingModal" :auth-modal="authModalObj"
        @onCloseModal="onToggleAuthModal" @onSubmit="onSubmitAuth" />
    </div>
  </div>
</template>

<script setup lang="ts">
import PageSize from "@/pages/admin/manager/system/user/page-size";
import { isEmpties } from "@composables/ramda";
import { confirmAlert, errorAlert } from "@functional/alert/alert-default";
import ManageClassificationModal from "@functional/classificationModal/manage-classification-modal";
import portalConfirm from "@functional/confirm/portal-confirm";
import LeftMenuList from "@functional/leftMenuList/left-menu-list.vue";
import ManageExtractMetaModal from "@functional/metaExtractModal/manage-extract-meta-modal.vue";
import Pagination from "@functional/pagination/pagination";
import ManageRoleMappingModal from "@functional/roleMappingModal/manage-role-mapping-modal";
import CategoryGroup from "@molecules/metaDataManage/category-group";
import MetaDataTooltip from "@molecules/metaDataManage/meta-data-tooltip";
import ClassificationInfo from "@organisms/metaDataManage/classification-info";
import { useCategoryStore } from "@store/category.ts";
import { useCategoryTreeStore } from "@store/categoryTree.ts";
import { useEventStore } from "@store/event.ts";
import { useUserStore } from "@store/user.ts";
import ObjectUtils from "@utils/ObjectUtils";
import { useNuxtApp, useRouter } from "nuxt/app";
import { onMounted, onUnmounted, ref } from "vue";

definePageMeta({
  name: "AdminMetaDataManage",
  layout: "admin"
});

const { $ofetch, $getNowWithSeconds } = useNuxtApp();

// route
const router = useRouter();

const userStore = useUserStore();

const categoryStore = useCategoryStore();
const categoryTreeStore = useCategoryTreeStore();
const selectedId = categoryStore.getSelectedItemId;

const eventStore = useEventStore();

// interface

interface FilterOption {
  value: string;
  desc: string;
}

interface FilterSearchObj {
  searchType: string; // 필터 조건
  word: string; // 검색어
  options: FilterOption[];
}

interface FilterRelease {
  value: number | string;
  desc: string; // 검색어
}

interface FilterMetaType {
  value: number | string;
  desc: string; // 검색어
}

interface FilterSystemType {
  value: number | string;
  desc: string; // 검색어
}

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
  currentPage: 1 // 현재 페이지 번호
});

interface SelectedCategory {
  id: string;
  parent_id: string;
  level: string;
  name: string;
  description: string;
  dept: { [key: string]: any };
  file_id: string;
  ord_no: number;
}

interface TooltipObj {
  display: boolean;
  type: string;
  e: Event | {};
  targets: any;
}

interface ManageModalObj {
  toggle: boolean;
  type: string | number;
  target: null;
}

interface AuthModalObj {
  toggle: boolean;
  type: string | number;
  target: null;
}

interface ExtractModalObj {
  toggle: boolean;
  type: string | number;
  target: null;
}

interface SelectedDataItem {
  typeName: string;
}

interface Criterion {
  attributeName: string;
  operator: string;
  attributeValue: string;
}

interface Filter {
  condition?: string;
  criterion: Criterion[];
}

interface EntityFilters {
  condition?: string;
  criterion: Array<Criterion | Filter>;
}

interface Params {
  word: string;
  offset: number;
  limit: number;
  searchType?: string;
  entityFilters?: EntityFilters;
  classification?: string;
  typeName?: string;
}

//data
// 창 조절 요소
const divider = ref<HTMLElement>(null); // 패널 사이즈 조절 DOM
const leftSide = ref<Element>(null); // 왼쪽 DOM
const rightSide = ref<Element>(null); // 오른쪽 DOM
const x = ref<number>(0); // 현재 마우스 X 좌표
const leftWidth = ref<number>(0); // 왼쪽 DOM 너비
const visible = ref<boolean>(false); // 순서 변경 모달 flag

const dataList = ref<any[]>([]); // 테이블 데이터
const selectedData = ref<any>([]); // 테이블 내 체크박스 체크한 데이터 목록
const catSelectedData = ref<any>(null); // 카테고리 체크박스 체크한 데이터 목록

// 필터검색 제어
const filterSearchObj = ref<FilterSearchObj>({
  searchType: "",
  word: "",
  options: [
    {
      // 두번째 필터 선택 가능 옵션 목록
      value: "",
      desc: "전체"
    },
    {
      value: "description",
      desc: "데이터 설명"
    },
    {
      value: "meta_name",
      desc: "데이터명"
    },
    {
      value: "system_name",
      desc: "시스템명"
    },
  ]
});

// 공개설정 필터 항목
const filterReleaseList = ref<Array<FilterRelease>>([
  {
    value: "",
    desc: "공개설정(전체)"
  },
  {
    value: 0,
    desc: "secret"
  },
  {
    value: 1,
    desc: "public"
  },
  {
    value: 2,
    desc: "private"
  },
])
const currentRelease = ref<number | string>("");

// 메타 데이터 유형 필터 항목
const filterMetaTypeList = ref<Array<FilterMetaType>>([
  {
    value: "",
    desc: "유형(전체)"
  }
])

// 메타 데이터 시스템 타입 필터 항목
const filterSystemTypeList = ref<Array<FilterSystemType>>([
  {
    value: "",
    desc: "시스템 타입(전체)"
  }
])
const currentMetaType = ref<number | string>("");
const currentSystemType = ref<number | string>("");

// 선택된 카테고리 정보
const selectedCategory = ref<SelectedCategory>({
  ord_no: 0,
  id: "",
  parent_id: "",
  name: "",
  description: "전체 카테고리",
  level: "",
  dept: {
    dept_cd: "",
    dept_nm: ""
  },
  file_id: ""
});

// 툴팁 제어
const tooltipObj = ref<TooltipObj>({
  display: false, // 툴팁 화면 렌더 여부 flag
  type: "",
  e: {},
  targets: null
});

// 카테고리 관리 모달 제어
const manageModalObj = ref<ManageModalObj>({
  toggle: false,
  type: null,
  target: null
});
const refManageModal = ref<any>(null); // 카테고리 모달 ref

// 공개설정 관리 모달 제어
const authModalObj = ref<AuthModalObj>({
  toggle: false,
  type: null,
  target: null
});
const refManageRoleMappingModal = ref<any>(null); // 공개설정 모달 ref

// 공개설정 관리 모달 제어
const extractModalObj = ref<ExtractModalObj>({
  toggle: false,
  type: null,
  target: null
});

await categoryStore.fetchCategories();
await categoryTreeStore.fetchCategories()

onMounted(() => {
  // 필터옵션 조회
  getFilterOptions();
  // NOTE: 카테고리 목록 컴포넌트 import한 페이지 정보 저장
  categoryStore.setCalledFrom({ from: "metaDataManage" });

  const userParams = userStore.getParams
  const fromPath = userStore.getFromPath
  const path = ObjectUtils.getPropertyValue(userParams, "path", "");
  if (fromPath === '/admin/data/meta-data/detail' && path === "/admin/data/meta-data/manage") {
    // 상세 조회 페이지에서 메타 데이터 통합 관리 페이지로 이동 시, 상세 페이지 이동 직전 사용자가 세팅한 검색조건으로 목록 조회
    onFetchPageDataByPrevCondition();
  } else {
    // 화면 렌더 시 필요한 데이터 조회 API 호출
    onFetchPageData();
  }

  // event bus에 이벤트 핸들러 등록
  eventStore.on("onToggleMetaDataTooltip", onToggleTooltip); // 툴팁 제어 핸들러
  eventStore.on("onClickTableBodyButtons", onClickTableBodyButtons); //테이블 내 버튼 클릭 이벤트 핸들러

  // 전체 페이지 컴포넌트 내 클릭 이벤트 리스너 바인딩, 버튼 이외의 영역 클릭 시 툴팁 제거
  document.addEventListener("click", (e) => onToggleTooltip(e));

  eventStore.on("onCategorySelect", onCategorySelect);

  // 조절할 창 및 조절 요소 설정
  divider.value = document.getElementById("divider");
  leftSide.value = divider.value.previousElementSibling;
  rightSide.value = divider.value.nextElementSibling;
});

onUnmounted(() => {
  // 페이지 이동 전 엔티티 검색에 사용된 파라미터 저장 조회 조건 중 페이지 번호 변경 시 페이징 처리 파라미터 저장
  const paging = pagingObj.value.currentPage !== 1 ? pagingObj.value : null;

  // 조회 조건 중 검색어 변경 시 필터검색 제어 파라미터 저장
  const condition = filterSearchObj.value;

  // 좌측 메뉴 목록에서 카테고리 선택하여 조회 시 선택된 카테고리 정보 저장
  const category = selectedId.value ? selectedCategory.value : null;

  // 검색창 오른쪽 공개설정 필터 정보 저장
  const release = currentRelease.value ? currentRelease.value : null;

  // 시스템 타입(전체) 필터 정보 저장
  const system_type = currentSystemType.value ? currentSystemType.value : null;

  // 유형(전체) 필터 정보 저장
  const meta_type = currentMetaType.value ? currentMetaType.value : null;

  // 검색에 사용된 파라미터 중 default 값과 상이한 값이 없으면 path를 empty string으로 저장하여 저장된 파라미터 초기화
  const path = paging || condition || category || release ? "/admin/data/meta-data/manage" : "";

  const params = {
    path,
    paging,
    condition,
    category,
    meta_type,
    system_type,
    release,
  };

  setParams(params);

  // 등록했던 툴팁 제어 핸들러 바인딩 제거
  eventStore.off("onToggleMetaDataTooltip", onToggleTooltip);
  eventStore.off("onClickTableBodyButtons", onClickTableBodyButtons);
  eventStore.off("onCategorySelect", onCategorySelect);

  document.removeEventListener("click", onToggleTooltip);
});

//method
const getFilterOptions = () => {
  $ofetch(`/api/metadata/manage/getFilterOptions`).then((res) => {
    if (res && res.data) {
      if (res.data.metaTypeList && res.data.metaTypeList.length > 0) {
        filterMetaTypeList.value = [...filterMetaTypeList.value, ...res.data.metaTypeList];
      }
      if (res.data.systemTypeList && res.data.systemTypeList.length > 0) {
        filterSystemTypeList.value = [...filterSystemTypeList.value, ...res.data.systemTypeList];
      }
    }
  });
};

const fetchCategories = async (payload: any) => {
  await categoryStore.fetchCategories(payload);

  return categoryStore.getCategoryList;
};

const setSelectedItemId = (guid: string) => {
  categoryStore.setSelectedItemId(guid);
};

const setParams = (params: any) => {
  userStore.setParams(params);
};

const mouseDown = (e: MouseEvent) => {
  if (leftSide.value) {
    // 현재 마우스 위치 값
    x.value = e.clientX;

    // leftElement에 Viewport 상 width 값을 가져와 대입
    leftWidth.value = leftSide.value.getBoundingClientRect().width;

    // 마우스 이벤트 등록
    document.addEventListener("mousemove", mouseDragging);
    document.addEventListener("mouseup", mouseUp);
  }
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
  let newLeftWidth = ((leftWidth.value + dx) * 100) / divider.value.parentNode.getBoundingClientRect().width;

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

// 화면 구성에 필요한 데이터 조회
const onFetchPageData = (): void => {
  // onSearchCategories();
  onSearchDataList();
};

const onFetchPageDataByPrevCondition = () => {
  const userParams = userStore.getParams
  const paging = ObjectUtils.getDeepCopiedPropertyValue(userParams, "paging", null);
  const condition = ObjectUtils.getDeepCopiedPropertyValue(userParams, "condition", null);
  const category = ObjectUtils.getDeepCopiedPropertyValue(userParams, "category", null);
  const release = ObjectUtils.getDeepCopiedPropertyValue(userParams, "release", "");
  const system_type = ObjectUtils.getDeepCopiedPropertyValue(userParams, "system_type", "");
  const meta_type = ObjectUtils.getDeepCopiedPropertyValue(userParams, "meta_type", "");

  let offset = 0;
  const item: any = {};

  if (category) {
    selectedCategory.value = category;
    item.name = category.name;
    item.guid = category.guid;
    item.description = category.description;
  }

  if (condition) {
    filterSearchObj.value = condition;
  }

  if (paging) {
    offset = paging.currentPage - 1;
  }

  if (release) {
    currentRelease.value = release
  }

  if (system_type) {
    currentSystemType.value = system_type
  }

  if (meta_type) {
    currentMetaType.value = meta_type
  }

  onSearchDataList(offset, item);
};

const getCategoryList = async () => {
  const level = 1;
  // 카테고리 목록의 Level 1만 조회
  const result = await $ofetch(`/api/metadata/classification/list?level=${level}`, { method: "GET" });
  categoryStore.setCategoryList(result);
};

const onClickCategoryOrderChange = async () => {
  await getCategoryList();
  visible.value = !visible.value;
};

// 좌측 상위메뉴 펼치기 처리
const toggleParents = (matched) => {
  if (matched) {
    const parent_matched = categoryStore.getOriginalList.find((el) => {
      return el.id === matched.parent_id
    })
    if (parent_matched) {
      categoryStore.getToggleTargets.value[parent_matched.id] = true
      toggleParents(parent_matched)
    }
  }
}

const onSearchCategories = async (item: { name?: string } = {}) => {
  // 선택된 카테고리의 name 추출
  const { name = null } = item;

  // 카테고리 목록 조회
  await fetchCategories({ from: "metaDataManage" });
  await categoryTreeStore.fetchCategories()

  // 실제 조회된 카테고리와 store에 저장된 선택 카테고리가 상이할 경우 발생, 일치하도록 조정
  if (name) {
    const matched = categoryStore.getOriginalList.find((el) => el.name === name);
    if (matched && matched.id !== selectedId.value) {
      setSelectedItemId(matched.id);
      toggleParents(matched)
      selectedCategory.value.ord_no = matched.ord_no
    }
  } else {
    // 전체 카테고리 데이터 조회 시
    if (selectedId.value !== "") {
      setSelectedItemId("");
    }
    onClickMenuItem();
  }
};

// 카테고리 삭제 이벤트 핸들러
const onDeleteCategory = async (target: any) => {
  const confirm = await portalConfirm({
    message: "선택한 <strong>*카테고리</strong>를 영구적으로 삭제합니다.<br>정말 삭제 하시겠습니까?"
  });

  if (confirm) {
    if (Array.isArray(target) && target.length > 0) {
      for (let index = 0; index < target.length; index++) {
        try {
          await $ofetch(`/api/metadata/classification/${target[index].id}`, {
            method: "POST"
          }).then((res) => {
            const resData = res.data || {};
            if (!resData || resData.isFailed) {
              const msg = "요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.";
              errorAlert(msg);
            } else {
              // 카테고리 삭제 성공 시 좌측 카테고리 목록 & 테이블 데이터 재조회
              selectedCategory.value = {
                id: "",
                level: "",
                ord_no: 0,
                parent_id: "",
                name: "",
                description: "전체 카테고리",
                dept: {
                  dept_cd: "",
                  dept_nm: ""
                },
                file_id: ""
              };
              onSearchCategories(); // 좌측 카테고리 목록 데이터 조회
              onSearchDataList(pagingObj.value.currentPage - 1); // 테이블 데이터 조회
              onSearchDept(); // 카테고리 담당부서 조회
            }
          });
        } catch (error) {
          errorAlert("요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.");
        }
      }
    } else {
      try {
        await $ofetch(`/api/metadata/classification/${target.id}`, {
          method: "POST"
        }).then((res) => {
          const resData = res.data || {};
          if (!resData || resData.isFailed) {
            const msg = resData.errorMessage
              ? resData.errorMessage
              : "요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.";
            errorAlert(msg);
          } else {
            // 카테고리 삭제 성공 시 좌측 카테고리 목록 & 테이블 데이터 재조회
            selectedCategory.value = {
              id: "",
              level: "",
              ord_no: 0,
              parent_id: "",
              name: "",
              description: "전체 카테고리",
              dept: {
                dept_cd: "",
                dept_nm: ""
              },
              file_id: ""
            };
            onSearchCategories(); // 좌측 카테고리 목록 데이터 조회
            onSearchDataList(0); // 테이블 데이터 조회
            onSearchDept(); // 카테고리 담당부서 조회
          }
        });
      } catch (error) {
        errorAlert("요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.");
      }
    }
  }
};

// 카테고리 추가/수정/매핑 모달 제어
const onToggleManageModal = (type: string | null, targets: any = null) => {
  let target = null;
  const toggle = manageModalObj.value.toggle;

  if (!toggle) {
    // 모달 여는 시점에만 관련 데이터 세팅
    switch (type) {
      case "add": // 카테고리 목록 상단 추가 버튼 클릭 시
        break;
      case "insert": // 카테고리 추가 버튼 클릭 시
      case "update": // 카테고리 수정 버튼 클릭 시
        target = selectedCategory.value;
        break;
      case "map": // 카테고리 맵핑 시
        target = targets;
        break;
    }
  }

  manageModalObj.value = {
    type: type ? type : null,
    toggle: !toggle,
    target
  };
};

// 공개설정 제어 모달
const onToggleAuthModal = (type: string | null, targets: any = null) => {
  let target = null;
  const toggle = authModalObj.value.toggle;

  if (!toggle) {
    // 모달 여는 시점에만 관련 데이터 세팅
    target = targets;
  }

  authModalObj.value = {
    type: type ? type : null,
    toggle: !toggle,
    target
  };
};

// 메타추출 제어 모달
const onToggleExtractModal = (type: string | null, targets: any = null) => {
  let target = null;
  const toggle = extractModalObj.value.toggle;

  if (!toggle) {
    // 모달 여는 시점에만 관련 데이터 세팅
    target = targets;
  }

  extractModalObj.value = {
    type: type ? type : null,
    toggle: !toggle,
    target
  };
};

// 툴팁 토글 이벤트 핸들러
const onToggleTooltip = (e: Event, item: any = null, type: string = "") => {
  if (item && !tooltipObj.value.display) {
    // 툴팁 제어 버튼 클릭 시
    tooltipObj.value.display = true;
    tooltipObj.value.type = type;
    tooltipObj.value.e = e;
    tooltipObj.value.targets = item;

    // 더보기 툴팁 렌더할 경우 카테고리 목록 데이터 전달
    if (type === "more") {
      tooltipObj.value.targets = item;
    }
  } else if (tooltipObj.value.display) {
    tooltipObj.value.display = false;
    tooltipObj.value.type = "";
    tooltipObj.value.e = {};
    tooltipObj.value.targets = null;
  }
};

// 테이블 내 카테고리 맵핑/카테고리 맵핑 삭제 버튼 클릭 이벤트 핸들러
const onClickTableBodyButtons = (type: string = "", target: any = null) => {
  switch (type) {
    case "category-map": // 카테고리 맵핑
      onToggleManageModal("map", target);
      break;
    case "category-del": // 카테고리 맵핑 삭제
      onDeleteCategoryMap(target);
      break;
  }
};

const onCategorySelect = () => {
  catSelectedData.value = selectedData.value;
};

// 카테고리 추가/수정/맵핑 이벤트 핸들러
const onSubmitCategory = async (params: any) => {
  switch (manageModalObj.value.type) {
    case "update": // 카테고리 수정
      try {
        await $ofetch(`/api/metadata/classification`, {
          method: "PUT",
          body: params
        }).then(async (res) => {
          const resData = res.data || {};
          if (!resData || resData.isFailed) {
            const msg = "요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.";
            errorAlert(msg);
          } else {
            // 카테고리 수정 성공 시
            await onSearchDept(selectedCategory.value); // 기존 선택 카테고리 담당부서 정보 재조회
            await onSearchCategories(selectedCategory.value); // 좌측 카테고리 목록 재조회
          }
        });
      } catch (error) {
        console.error(error);
        errorAlert("요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.");
      }
      break;
    case "map": // 카테고리 맵핑
      try {
        await $ofetch(`/api/metadata/classification/map`, {
          method: "POST",
          body: params
        }).then(async (res) => {
          const resData = res.data || {};
          if (!resData || resData.isFailed) {
            const msg = "요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.";
            errorAlert(msg);
          } else {
            // 카테고리 맵핑 성공 시
            const syncParams = {
              meta_ids: [],
              event_status: 2,
              event_name: "메타 데이터 카테고리 매핑[변경]"
            }

            for (const entity of params.entityTargets) {
              syncParams.meta_ids.push(entity.meta_id)
            }

            await $ofetch(`/api/metadata/classification/essyncUpdates`, {
              method: 'POST',
              body: syncParams
            }).then((res) => {
              if (res && res.data) {
              }
            }).catch((err) => {
              const msg = "ES SYNC 요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.";
            })

            onSearchDataList(pagingObj.value.currentPage - 1); // 테이블 데이터 재조회
            // onSearchCategories(selectedCategory.value); // 좌측 카테고리 목록 재조회
          }
        });
      } catch (error) {
        console.error(error);
        errorAlert("요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.");
      }
      break;
    default: // 카테고리 추가
      let check = !(await checkBeforeInsert(params.name));
      if (!check) {
        if (
          !(await confirmAlert(`이미 같은 카테고리명으로 등록된 카테고리가 있습니다.<br/>그래도 등록 하시겠습니까?`))
        ) {
          return;
        }
      }

      try {
        await $ofetch(`/api/metadata/classification/insert`, {
          method: "POST",
          body: params
        }).then((res: any) => {
          const resData = res.data || {};
          if (!resData || resData.isFailed) {
            errorAlert(
              !isEmpties(resData.errorMessage)
                ? resData.errorMessage
                : "요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요."
            );
          } else {
            // 카테고리 추가 성공 시 좌측 카테고리 목록 재조회
            onSearchCategories(selectedCategory.value);
          }
        });
      } catch (e) {
        console.error(e);
        errorAlert("요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.");
      }
      break;
  }
  refManageModal.value.onClose();
};

// 카테고리명 중복 체크
const checkBeforeInsert = async (name) => {
  let isDup = false;
  try {
    await $ofetch(`/api/metadata/classification/check/${name}`, {
      method: "GET"
    }).then(async (res: any) => {
      const resData: any = res.data || {};
      isDup = resData.isDuplicate;
    });
  } catch (e) {
    console.error(e);
    errorAlert("요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.");
  }
  return isDup;
};

// 카테고리 세팅 툴팁 내 추가/삭제/수정 버튼 클릭 이벤트 핸들러
const onClickCategoryManageMenu = (type) => {
  if (type === "delete") {
    onDeleteCategory(selectedCategory.value); // 카테고리 삭제 실행
  } else {
    if (selectedCategory.value) {
      // 전체 카테고리, 카테고리 미분류는 수정할 수 없도록 지정
      if (selectedCategory.value.id != "" && selectedCategory.value.id != "_NOT_CLASSIFIED") {
        onToggleManageModal(type); // 카테고리 추가/수정 모달 제어
      }
    }
  }
};

// 테이블 상단 버튼 그룹 내 버튼 클릭 이벤트 핸들러
const onClickButton = (type) => {
  if (selectedData.value.length > 0) {
    let targets: SelectedDataItem[] = [];
    switch (type) {
      case "category": // 카테고리 맵핑 버튼 클릭 시
        targets = selectedData.value;
        onToggleManageModal("map", targets);
        break;
      case "roles": // 공개설정 버튼 클릭 시
        targets = selectedData.value;
        onToggleAuthModal("roles", targets);
        break;
      case "extract": // 메타 추출 버튼 클릭 시
        targets = selectedData.value;
        onToggleExtractModal("extract", targets);
        break;
    }
  } else {
    // 선택된 데이터가 없을 경우
    const MESSAGE_MAP = new Map<string, string>([
      ["category", "카테고리 맵핑"],
      ["extract", "메타데이터 추출"],
      ["roles", "공개설정"],
      ["delete", "삭제"]
    ]);

    errorAlert(`${MESSAGE_MAP.get(type)} 대상 데이터를 먼저 선택하시기 바랍니다.`);
  }
};

// 페이지 번호 클릭 이벤트 핸들러
const onClickPage = (page: number) => {
  onSearchDataList(page - 1);
};

// 필터 검색 조건 변경 이벤트 핸들러
const onChange = (e) => {
  const {
    target: { name, value }
  } = e;
  const searchObj = JSON.parse(JSON.stringify(filterSearchObj.value)) as FilterSearchObj;
  searchObj[name] = value;
  filterSearchObj.value = searchObj;
};

const onChangeRelease = (e) => {
  currentRelease.value = e.target.value
  onSearchDataList();
}

const onChangeMetaType = (e) => {
  currentMetaType.value = e.target.value
  onSearchDataList();
}

const onChangeSystemType = (e) => {
  currentSystemType.value = e.target.value
  onSearchDataList();
}

const onFilterSearch = () => {
  onSearchDataList();
};

// 메타 데이터 통합 관리 목록 조회
const onSearchDataList = async (offset = 0, item = {}) => {

  categoryStore.setSelectedItemId(selectedCategory.value && selectedCategory.value.id ? selectedCategory.value.id : "");

  // 체크한 데이터 목록 초기화
  selectedData.value = [];

  const params = {
    word: filterSearchObj.value.word,
    offset: offset * pagingObj.value.limit,
    limit: pagingObj.value.limit
  }; // 데이터 조회 파라미터 객체

  // 카테고리 검색
  params.searchClassification = ObjectUtils.getPropertyValue(selectedCategory.value, "id", "");

  const { word } = filterSearchObj.value;
  params.searchType = filterSearchObj.value.searchType === "" ? "all" : filterSearchObj.value.searchType;
  params.metaType = currentMetaType.value
  params.systemType = currentSystemType.value
  params.release = currentRelease.value
  params.word = word.trim();

  // 테이블 데이터 조회
  const res = await $ofetch(`/api/metadata/manage/list`, { method: "POST", body: params });
  if (res && !res.errorMessage) {
    let result = res.data;

    // 조회 결과 추출
    const list: any = ObjectUtils.getPropertyValue(result, "list", []); // 테이블 데이터
    dataList.value = list.map((el: { [key: string]: any }) => {
      return {
        ...el,
        attributes: !isEmpties(el.attributes) ? JSON.parse(el.attributes) : {}
      };
    });

    if (result.totalCount > 0) {
      pagingObj.value = <PagingObj>{
        ...pagingObj.value,
        total: ObjectUtils.getPropertyValue(result, "totalCount", 0),
        currentPage: offset + 1
      };
    } else {
      pagingObj.value = {
        ...pagingObj.value,
        total: 0,
        currentPage: offset + 1
      };
    }
  } else {
    dataList.value = [];
    pagingObj.value = {
      ...pagingObj.value,
      total: 0,
      currentPage: 1
    };
  }
};

const onSearchDept = async (item = {}) => {
  const { id = "", parent_id = "", level = "", name = null, description = "", file_id = "", ord_no = 0 } = item;

  const result = {
    id,
    parent_id,
    level,
    name,
    description,
    dept: {
      dept_cd: "", // 담당부서코드
      dept_nm: "" // 담당부서명
    },
    file_id,
    ord_no
  };

  if (name && name !== "_NOT_CLASSIFIED") {
    const response = await $ofetch(`/api/metadata/classification/dept/${name}`, { method: "GET" });

    const dept = {
      dept_cd: response.data?.dept_cd || "",
      dept_nm: response.data?.dept_nm || ""
    };

    const description = response.data?.description || "";
    if (description) {
      result.description = description;
    }

    result.dept = dept;
  } else {
    result.description = name == "_NOT_CLASSIFIED" ? "카테고리 미분류" : "전체 카테고리";
  }

  selectedCategory.value = result;
};

// 좌측 카테고리 리스트 내 노드 클릭 이벤트 핸들러
const onClickMenuItem = async (item: any = null) => {
  if (!item) {
    await onSearchDept();
    onSearchDataList();
  } else {
    await onSearchDept(item);
    onSearchDataList(0, item);
  }
};

// 좌측 카테고리 목록 상단 추가 옵션 버튼 클릭 이벤트 핸들러
const onClickOptionalButtons = (type: any): void => {
  if (type === "add") {
    // 추가 버튼 클릭 시 카테고리 추가 모달 생성
    onToggleManageModal(type);
  } else if (type === "refresh") {
    // 새로고침 버튼 클릭 시 좌측 카테고리 목록 새로고침
    onSearchCategories();
  }
};

// 전체 체크박스 체크 표시 여부 계산
const isAllChecked = () => {
  return dataList.value.length > 0 && selectedData.value.length === dataList.value.length;
};

// 체크박스 체크 표시 여부 계산
const isChecked = (item: any): boolean => {
  if (item && item.checked) return true;
  else return false;
};

// 체크박스 클릭 이벤트 핸들러
const onToggleCheckbox = (e, isAll: boolean, item: any) => {
  const {
    target: { checked }
  } = e;
  let newItems = [...selectedData.value];

  if (isAll) {
    // 테이블 최상단 체크박스 클릭 시 전체 선택/해제
    let tmpList = dataList.value;
    tmpList.forEach((el) => {
      el.checked = checked;
    });
    newItems = checked ? [...tmpList] : [];
  } else {
    // 각 행별 체크박스 클릭 시
    item.checked = checked;
    if (checked) {
      newItems.push(item);
    } else {
      const targetIndex = newItems.findIndex((el: any) => el.meta_id === item.meta_id);
      newItems.splice(targetIndex, 1);
    }
  }
  selectedData.value = newItems;
};

const onClickDataName = (item) => {
  router.push("/admin/data/meta-data/detail?guid=" + item.meta_id);
};

const getReleaseClass = (release) => {
  let className = "release-status"
  if (release == 0) className += " secret";
  else if (release == 1) className += " public";
  else if (release == 2) className += " private";
  return className;
}

const getRelease = (release) => {
  if (release == 0) return "secret";
  else if (release == 1) return "public";
  else if (release == 2) return "private";
  else return release;
};

// 카테고리 맵핑 삭제 버튼 클릭 이벤트 핸들러
const onDeleteCategoryMap = async (target) => {
  const confirm = await confirmAlert(
    "선택한 <strong>*카테고리 맵핑</strong>을 영구적으로 삭제합니다.<br>정말 삭제 하시겠습니까?"
  );

  if (confirm) {
    const guid = target.item.meta_id;
    const deleteCategoryId = target.targetId;

    $ofetch(`/api/metadata/classification/deleteMapping`, {
      method: "POST",
      body: { guid, deleteCategoryId }
    }).then((res) => {
      if (!res || res.isFailed) {
        const msg = "요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.";
        errorAlert(msg);
      } else {
        onSearchDataList(pagingObj.value.currentPage - 1); // 테이블 데이터 재조회
      }
    });
  }
};

// 공개설정 버튼 이벤트 핸들러
const onSubmitAuth = async (params: any) => {
  let auth_params = {
    meta_ids: selectedData.value.map((item: any) => item.meta_id),
    release: params.updatedRelease
  };

  if (await confirmAlert("권한을 설정하시겠습니까?")) {
    await $ofetch(`/api/metadata/manage/setDataAuth`, {
      method: "PUT",
      body: auth_params
    })
      .then((res) => {
        if (res && res.data && res.data.result) {
          onSearchDataList(pagingObj.value.currentPage - 1); // 테이블 데이터 재조회
          refManageRoleMappingModal.value.onClose();
        } else {
          const msg = "요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.";
          errorAlert(msg);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  }
};

// 메타 데이터 다운로드
const downloadMetadata = async (params) => {
  const extractType = params.extractType == 0 ? "json" : "xml";

  const body = {
    meta_ids: selectedData.value.map((item: any) => item.meta_id),
    extract_type: extractType
  }

  // 메타 추출
  try {
    const response = await $ofetch(`/api/metadata/manage/extractMetadata`, {
      method: "POST",
      body:  body,
      responseType: "blob"
    })

    // 2) blob → URL 객체 생성
    const downloadUrl = window.URL.createObjectURL(response)

    // 3) a 태그로 다운로드 트리거
    const link = document.createElement('a')
    link.href = downloadUrl
    link.setAttribute('download', `${`metadata_extract_${$getNowWithSeconds(new Date())}`}.${extractType}`) // 다운로드 파일명 설정
    document.body.appendChild(link)
    link.click()
    link.remove()

    // 4) 메모리 해제
    window.URL.revokeObjectURL(downloadUrl)

    // 5) 모달 닫기
    onToggleExtractModal();

  } catch (err) {
    console.log(err);
    errorAlert("메타 데이터를 추출 요청에 실패하였습니다.");
  }
}

const goToFileRegister = () => {
  router.push("/admin/data/meta-data/register")
}
</script>
<style lang="scss">
@import "index";

</style>
