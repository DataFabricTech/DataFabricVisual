<template>
  <div>
    <div class="inner">
      <div class="mu-section">
        <!-- 카테고리 관리 & 더보기 툴팁 -->
        <meta-data-tooltip :tooltip-obj="tooltipObj" :selected-category="selectedCategory"
        @onClickMenu="onClickCategoryManageMenu" />

        <left-menu-list @onClickMenuItem="onClickMenuItem" @onClickOptionalButtons="onClickOptionalButtons"
                        @onClickCategoryOrderChange="onClickCategoryOrderChange"/>

        <classification-ord-update-modal ref="classificationOrdupdateModal" :visible.sync="visible" @searchMethod="getCategoryList" 
                                          @onSearchCategories="onSearchCategories" />
        
        <!-- 카테고리 리스트 사이즈 조절 바 -->
        <div data-v-10a51f6f id="divider" @mousedown="mouseDown($event)" ></div>

        <!-- page-content : R 서브내용 -->
        <div class="page-content">
          <div class="board-list-wrap">
            <classification-info :selected-category="selectedCategory" @onClickUpdate="onClickCategoryManageMenu" />

            <meta-data-table-header :filter-search-obj="filterSearchObj"
                            @onClickButtons="onClickTableHeaderButtons" @onChangeFilter="onChangeFilter"
                            @onFilterSearch="onSearchDataList" :pagingObj="pagingObj" />

            <meta-data-table :data-list="dataList" :selected-data="selectedData" @onSelectItems="onSelectItems" />
            
            <pagination :paging="pagingObj" @onClickPage="onClickPage" />                        
          </div>          
        </div>
        <!-- //page-content -->
      </div>

      <!-- 카테고리 추가/수정/매핑 처리 모달 -->
      <manage-classification-modal :manage-modal="manageModalObj" @onCloseModal="onToggleManageModal" 
                @onSubmit="onSubmitCategory" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from "~/store/user.ts";
import { useCategoryStore } from "~/store/category.ts";
import { useEventStore  } from "~/store/event.ts";
import LeftMenuList from "@functional/leftMenuList/left-menu-list.vue";
import ClassificationInfo from "@organisms/metaDataManage/classification-info";
import MetaDataTableHeader from "@organisms/metaDataManage/meta-data-table-header";
import MetaDataTable from "@organisms/metaDataManage/meta-data-table";
import MetaDataTooltip from "@molecules/metaDataManage/meta-data-tooltip";
import ManageClassificationModal from '@functional/classificationModal/manage-classification-modal';
import ClassificationOrdUpdateModal from '@organisms/metaDataManage/classification-ord-update-modal';
import ObjectUtils from "~/utils/ObjectUtils";
import Pagination from "@functional/pagination/pagination";
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";
import { onMounted, onUnmounted, ref, computed } from 'vue';
import portalConfirm from '@functional/confirm/portal-confirm';
import { errorAlert } from "@functional/alert/alert-default";

definePageMeta({
  name: "AdminMetaDataManage",
  layout: "admin",
});


const { $ofetch } = useNuxtApp();

// route
const route = useRoute();
const router = useRouter();

const userStore = useUserStore();
const user = userStore.getUser;
const fromPath = userStore.getFromPath;
const params = userStore.getParams;

const categoryStore = useCategoryStore();
const getOriginalList = categoryStore.getOriginalList
const selectedGuid = categoryStore.getSelectedItemGuid

const eventStore = useEventStore();

// interface

interface FilterOption {
  value: string;
  desc: string;
}

interface FilterSearchObj {
  searchType1: string;   // 첫번째 필터 조건 - hive_table or hive_column
  searchType2: string;   // 두번째 필터 조건 - 데이터명, 오너 등
  word: string;          // 검색어
  options: FilterOption[];
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
  currentPage: 1, // 현재 페이지 번호
});

interface SelectedCategory {
  name: string;
  description: string;
  guid: string;
  dept: { [key: string]: any };
  file_id: string;
}

interface TooltipObj {
  display: boolean;
  type: string;
  e: Event | {},
  targets: any
}

interface ManageModalObj {
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
  searchType2?: string;
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
const ordCategoryList = ref<any>([]); // 순서 변경을 위한 카테고리 목록

const classification = ref<string>(""); // 카테고리

const dataList = ref<any[]>([]); // 테이블 데이터
const selectedData = ref<any>([]); // 테이블 내 체크박스 체크한 데이터 목록
const catSelectedData = ref<any>(null); // 카테고리 체크박스 체크한 데이터 목록

// 필터검색 제어
const filterSearchObj = ref<FilterSearchObj>({
  searchType1: 'hive_table',  
  searchType2: '', 
  word: '', 
  options: [
        { // 두번째 필터 선택 가능 옵션 목록
            value: '',
            desc: '전체'
        }, {
            value: 'comment',
            desc: '데이터설명'
        }, {
            value: 'name',
            desc: '데이터명'
        }, {
            value: 'owner',
            desc: '소유자'
        }
  ]
});

// 선택된 카테고리 정보
const selectedCategory = ref<SelectedCategory>({
    name: '',
    description: '전체',
    guid: '',
    dept: {
        dept_cd: '',
        dept_nm: ''
    },
    file_id: ''
});

// 툴팁 제어
const tooltipObj = ref<TooltipObj>({
  display: false,  // 툴팁 화면 렌더 여부 flag
  type: '',
  e: {},
  targets: null
}); 

// 카테고리 관리 모달 제어
const manageModalObj = ref<ManageModalObj>({
  toggle: false,
  type: null,
  target: null
});

await categoryStore.fetchCategories();

onMounted(() => {
  categoryStore.setCalledFrom({ from: 'metaDataManage' });
  
  const path = ObjectUtils.getPropertyValue(params, "path", "");

  if (fromPath.value === '/admin/data/meta-data/detail' && path === '/admin/data/meta-data/manage') {
    // 상세 조회 페이지에서 메타 데이터 통합 관리 페이지로 이동 시, 상세 페이지 이동 직전 사용자가 세팅한 검색조건으로 목록 조회
    onFetchPageDataByPrevCondition();
  } else {
    // 화면 렌더 시 필요한 데이터 조회 API 호출
    onFetchPageData();
  }

  // event bus에 이벤트 핸들러 등록  
  eventStore.on('onToggleMetaDataTooltip', onToggleTooltip); // 툴팁 제어 핸들러
  eventStore.on('onClickTableBodyButtons', onClickTableBodyButtons); //테이블 내 버튼 클릭 이벤트 핸들러
  
  // 전체 페이지 컴포넌트 내 클릭 이벤트 리스너 바인딩, 버튼 이외의 영역 클릭 시 툴팁 제거
  document.addEventListener('click', (e) => onToggleTooltip(e));

  eventStore.on('onCategorySelect', onCategorySelect);

  // 조절할 창 및 조절 요소 설정  
  divider.value = document.getElementById("divider");
  leftSide.value = divider.value.previousElementSibling
  rightSide.value = divider.value.nextElementSibling  
});

onUnmounted(() => {
  // 페이지 이동 전 엔티티 검색에 사용된 파라미터 저장 조회 조건 중 페이지 번호 변경 시 페이징 처리 파라미터 저장
  const paging = pagingObj.value.currentPage !== 1 ? pagingObj.value : null;

  // 조회 조건 중 검색어 변경 시 필터검색 제어 파라미터 저장
  const condition = filterSearchObj.value.searchType1 === '_ALL_ENTITY_TYPES' ? null : filterSearchObj.value;

  // 좌측 메뉴 목록에서 카테고리 선택하여 조회 시 선택된 카테고리 정보 저장
  const category = selectedGuid.value ? selectedCategory.value : null;

  // 검색에 사용된 파라미터 중 default 값과 상이한 값이 없으면 path를 empty string으로 저장하여 저장된 파라미터 초기화
  const path = paging || condition || category
            ? '/admin/data/meta-data/manage'
            : '';

  const params = {
            path,
            paging,
            condition,
            category
        };

  setParams(params)

  // 등록했던 툴팁 제어 핸들러 바인딩 제거
  eventStore.off('onToggleMetaDataTooltip', onToggleTooltip); 
  eventStore.off('onClickTableBodyButtons', onClickTableBodyButtons);
  eventStore.off('onCategorySelect', onCategorySelect);

  document.removeEventListener('click', onToggleTooltip);
});

//method
const fetchCategories = async (payload: any) => {
  await categoryStore.fetchCategories(payload);

  return categoryStore.getCategoryList
};

const setSelectedItemGuid = (guid: string) => {  
  categoryStore.setSelectedItemGuid(guid);
};

const setParams = (params: any) => {  
  userStore.setParams(params)
};

const mouseDown = (e: MouseEvent) => {
  if (leftSide.value) {
    // 현재 마우스 위치 값
    x.value = e.clientX;

    // leftElement에 Viewport 상 width 값을 가져와 대입
    leftWidth.value = leftSide.value.getBoundingClientRect().width;

    // 마우스 이벤트 등록
    document.addEventListener('mousemove', mouseDragging);
    document.addEventListener('mouseup', mouseUp);
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

// 화면 구성에 필요한 데이터 조회
const onFetchPageData = ():void => {
  // onSearchCategories();
  onSearchDataList();
};

const onFetchPageDataByPrevCondition = () => {
  const paging = ObjectUtils.getDeepCopiedPropertyValue(params.value, 'paging', null);
  const condition = ObjectUtils.getDeepCopiedPropertyValue(params.value, 'condition', null);
  const category = ObjectUtils.getDeepCopiedPropertyValue(params.value, 'category', null);

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

  onSearchDataList(offset, item);
};

const getCategoryList = async () => {
  const level = 1;
  // 카테고리 목록의 Level 1만 조회
  const result = await $ofetch(`/api/metadata/classification/list?level=${level}`, { method: 'GET' });
  categoryStore.setCategoryList(result)  
};

const onClickCategoryOrderChange = async () => {
  await getCategoryList();
  visible.value = !visible.value;
};

const onSearchCategories = async (item: { name?: string } = {}) => {
  // 선택된 카테고리의 name 추출
  const { name = null } = item;

  // 카테고리 목록 조회
  const list = await fetchCategories({ from: 'metaDataManage' });
  
  // 실제 조회된 카테고리와 store에 저장된 선택 카테고리가 상이할 경우 발생, 일치하도록 조정
  if (name) {
    const matched = list.find(el => el.name === name);
    if (matched && matched.guid !== selectedGuid.value) {
      setSelectedItemGuid(matched.guid);      
    } 
  } else {
    // 전체 카테고리 데이터 조회 시
    if (selectedGuid.value !== '') {
      setSelectedItemGuid('');      
    }
    onClickMenuItem()
  }
};

// 카테고리 삭제 이벤트 핸들러
const onDeleteCategory = async (target: any) => {
  const confirm = await portalConfirm({
    message: "선택한 <strong>*구분</strong>을 영구적으로 삭제합니다.<br>정말 삭제 하시겠습니까?"
  });

  if (confirm) {
    if (Array.isArray(target) && target.length > 0) {
      for (let index = 0; index < target.length; index++) {
        try {
          await $ofetch(`/api/metadata/classification/${target[index].name}`, {
            method: "POST",            
          }).then((res) => {
            if (!res.data || res.data.isFailed) {
              const msg = '요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.';
              errorAlert(msg);
            } else {
              // 카테고리 삭제 성공 시 좌측 카테고리 목록 & 테이블 데이터 재조회
              selectedCategory.value = {
                name: '',
                description: '전체',
                guid: '',
                dept: {
                  dept_cd: '',
                  dept_nm: ''
                },
                file_id: ''
              };
              onSearchCategories(); // 좌측 카테고리 목록 데이터 조회
              onSearchDataList(0); // 테이블 데이터 조회
              onSearchDept(); // 카테고리 담당부서 조회
            }
          });
        } catch (error) {
          errorAlert('요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.');
        }
      }
    } else {
      try {
        await $ofetch(`/api/metadata/classification/${target.name}`, {
          method: "POST",
        }).then((res) => {
          if (!res.data || res.data.isFailed) {
            const msg = res.data.errorMessage ? res.data.errorMessage : '요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.';
            errorAlert(msg);
          } else {
            // 카테고리 삭제 성공 시 좌측 카테고리 목록 & 테이블 데이터 재조회
            selectedCategory.value = {
              name: '',
              description: '전체',
              guid: '',
              dept: {
                dept_cd: '',
                dept_nm: ''
              },
              file_id: ''
            };
            onSearchCategories(); // 좌측 카테고리 목록 데이터 조회
            onSearchDataList(0); // 테이블 데이터 조회
            onSearchDept(); // 카테고리 담당부서 조회
          }
        });
      } catch (error) {
        errorAlert('요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.');
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
      case 'add': // 카테고리 목록 상단 추가 버튼 클릭 시
        break;
      case 'insert': // 카테고리 추가 버튼 클릭 시
      case 'update': // 카테고리 수정 버튼 클릭 시
        target = selectedCategory.value;
        break;
      case 'map': // 카테고리 맵핑 시
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

// 툴팁 토글 이벤트 핸들러
const onToggleTooltip = (e: Event, item: any = null, type: string = '') => {
  if (item && !tooltipObj.value.display) {
    // 툴팁 제어 버튼 클릭 시
    tooltipObj.value.display = true;
    tooltipObj.value.type = type;
    tooltipObj.value.e = e;
    tooltipObj.value.targets = item;

    // 더보기 툴팁 렌더할 경우 카테고리 목록 데이터 전달    
    if (type === 'more') {
      tooltipObj.value.targets = item;
    }
    
  } else if ( tooltipObj.value.display ) {
    tooltipObj.value.display = false;
    tooltipObj.value.type = '';
    tooltipObj.value.e = {};
    tooltipObj.value.targets = null;
  }

};

// 테이블 내 카테고리 맵핑/카테고리 맵핑 삭제 버튼 클릭 이벤트 핸들러
const onClickTableBodyButtons = (type: string = '', target: any = null) => {
  switch (type) {
    case 'category-map': // 카테고리 맵핑
      onToggleManageModal('map', target);
      break;
    case 'category-del': // 카테고리 맵핑 삭제
      onDeleteCategoryMap(target);
      break;
  }
};

const onCategorySelect = () => {
  catSelectedData.value = selectedData.value
};

// 카테고리 추가/수정/맵핑 이벤트 핸들러
const onSubmitCategory = async (params: Params) => {
  switch (manageModalObj.value.type) {
    case 'update': // 카테고리 수정
      try {
        await $ofetch(`/api/metadata/classification`, {
          method: "PUT",
          body: params,
        }).then((res) => {
          if (!res.data || res.data.isFailed) {
            const msg = '요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.';
            errorAlert(msg);
          } else {
            // 카테고리 수정 성공 시
            onSearchDept(selectedCategory.value); // 기존 선택 카테고리 담당부서 정보 재조회
            onSearchCategories(selectedCategory.value); // 좌측 카테고리 목록 재조회
          }
        });
      } catch (error) {
        console.error(error);
        errorAlert('요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.');
      }
      break;
    case 'map': // 카테고리 맵핑
      try {
        const { entities, superTypes, tableTypes } = params;
        await $ofetch(`/api/metadata/classification/map`, {
          method: "POST",
          body: { entities, superTypes, tableTypes },
        }).then((res) => {
          if (!res.data || res.data.isFailed) {
            const msg = '요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.';
            errorAlert(msg);
          } else {
            // 카테고리 맵핑 성공 시 테이블 데이터 재조회
            onSearchDataList(pagingObj.value.currentPage - 1);
            onSearchCategories();
          }
        });
      } catch (error) {
        console.error(error);
        errorAlert('요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.');
      }
      break;
    default: // 카테고리 추가
      try {
        await $ofetch(`/api/metadata/classification/insert`, {
          method: "POST",
          body: params,
        }).then((res) => {
          if (!res.data || res.data.isFailed) {
            const msg = '요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.';
            if (res.data.errorMessage && res.data.errorMessage.indexOf("Given type " + params.forRDB.name + " already exists") !== -1) {
              errorAlert("이미 사용중인 구분 입니다.");
            } else {
              errorAlert(msg);
            }
          } else {
            // 카테고리 추가 성공 시 좌측 카테고리 목록 재조회
            onSearchCategories(selectedCategory.value);
          }
        });        
      } catch (error) {
        console.error(error);
        errorAlert('요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.');
      }
      break;
  }
};

// 카테고리 세팅 툴팁 내 추가/삭제/수정 버튼 클릭 이벤트 핸들러
const onClickCategoryManageMenu = (type) => {
  if (type === 'delete') {
    onDeleteCategory(selectedCategory.value); // 카테고리 삭제 실행
  } else {
    onToggleManageModal(type); // 카테고리 추가/수정 모달 제어
  }
};

// 테이블 상단 버튼 그룹 내 버튼 클릭 이벤트 핸들러
const onClickTableHeaderButtons = (type) => {
  if (selectedData.value.length > 0) {
    let targets: SelectedDataItem[] = [];
    switch (type) {
      case 'category': // 카테고리 맵핑 버튼 클릭 시
        // 테이블 유형에 대해서만 카테고리 맵핑 가능
        targets = selectedData.value.filter(el => el.typeName.endsWith('_table'));
        if (targets.length > 0) {
          onToggleManageModal('map', targets);
        } else {
          errorAlert('선택된 데이터 중 구분 맵핑 가능한 데이터가<br/>없습니다.<br/>※ 구분는 테이블 유형에만 맵핑됩니다.');
        }
        break;
    }
  } else {
    const MESSAGE_MAP = new Map<string, string>([
      ['category', '구분 맵핑'],
      ['delete', '삭제']
    ]);

    errorAlert(`${MESSAGE_MAP.get(type)} 대상 데이터를 먼저 선택하시기 바랍니다.`);
  }
};

// 목록 조회 시 특정 카테고리 선택 여부에 따라 API 서버에 전송할 classification 파라미터 가공
const setClassificationParam = (isSpecificCategory: boolean, name: string, params: any) => {
  if (isSpecificCategory) {
    // 특정 카테고리 선택하여 검색 시
    params.classification = name
      ? name
      : ObjectUtils.getPropertyValue(selectedCategory.value, 'name', '');
  } else {
    params.classification = '';
  }
}

const createFilterCondition = (params: any) => {
  const entityFilters: EntityFilters = {
    criterion: []
  };

  const { searchType1, word } = filterSearchObj.value;

  if (searchType1 === '_ALL_ENTITY_TYPES') {
    // 첫번째 필터 조건 미설정 시
    entityFilters.condition = 'OR';
    ['hive_table', 'hive_column'].forEach(attr => {
      entityFilters.criterion.push({ attributeName: '__typeName', operator: 'eq', attributeValue: attr });
    });
  } else {
    // 첫번째 필터 조건 설정 시
    entityFilters.condition = 'AND';
    entityFilters.criterion.push({ attributeName: '__typeName', operator: 'eq', attributeValue: searchType1 });

    // 검색어 입력 시 두번째 필터 조건에 맞춰 검색 조건 추가 가공
    const searchWord = word.trim();
    if (searchWord) {
      createSecondFilterCondition(entityFilters, searchWord);
    }
  }

  params.typeName = searchType1;
  params.entityFilters = entityFilters;
}

// 두번째 필터 조건 선택값에 따라 API 서버에 전송할 파라미터 추가 가공
const createSecondFilterCondition = (entityFilters: EntityFilters, searchWord: string) => {
  const filter: Filter = {
    criterion: []
  };
  const attributes: string[] = [];

  const { searchType1, searchType2 } = filterSearchObj.value;
  attributes.push(searchType2);
  filter.condition = 'AND';

  if (searchType2 === '') {
    filter.condition = 'OR';
    filterSearchObj.value.options.forEach(element => {
      attributes.push(element.value);
    });
  }

  attributes.forEach(attr => {
    filter.criterion.push({
      attributeName: attr,
      operator: 'contains',
      attributeValue: attr.indexOf('standard') > -1 ? searchWord.toUpperCase() : searchWord
    });
  });

  entityFilters.criterion.push(filter);
};

// 페이지 번호 클릭 이벤트 핸들러
const onClickPage = (page: number) => {
  onSearchDataList(page - 1);
};

// 필터 검색 조건 변경 이벤트 핸들러
const onChangeFilter = (name: string, value: string) => {
  const searchObj = JSON.parse(JSON.stringify(filterSearchObj.value)) as FilterSearchObj;
  searchObj[name] = value;

  if (name === 'searchType1') {
    let options: FilterOption[] = [];

    if (value === '_ALL_ENTITY_TYPES') {
      // 첫번째 검색 조건 옵션 선택 안할 경우 나머지 검색 조건 초기화
      searchObj.word = '';
      searchObj.searchType2 = '';
    } else {
      // 첫번째 검색 조건 옵션 선택 시 두번째 검색 조건 옵션 관련 값 세팅
      options = [
        { value: '', desc: '전체' },
        { value: 'comment', desc: '데이터설명' },
        { value: 'name', desc: '데이터명' },
        { value: 'owner', desc: '소유자' }
      ];
    }

    searchObj.options = options;
  }

  filterSearchObj.value = searchObj;
};

// 메타 데이터 통합 관리 목록 조회
const onSearchDataList = async (offset = 0, item = {}) => {
  const params = {
      word: filterSearchObj.value.word,
      offset: offset * pagingObj.value.limit,
      limit: pagingObj.value.limit
  }; // 데이터 조회 파라미터 객체
  const { name = '' } = item;
  const categoryName = ObjectUtils.getPropertyValue(selectedCategory.value, 'name', '');
  const isSpecificCategory = (name || categoryName) ? true : false;

  setClassificationParam(isSpecificCategory, name, params);
  createFilterCondition(params);

  // 두번째 필터가 전체 검색인 경우 필터 제거
  if (filterSearchObj.value.searchType2 == ''){
    params.entityFilters = null
    params.searchType2 = 'all'
  }

  // 테이블 데이터 조회
  const res = await $ofetch("/api/metadata/manage/list", { method: "POST", body: params });     
  let result = res.data;
  
  // 조회 결과 추출
  const list = ObjectUtils.getPropertyValue(result, 'entities', []); // 테이블 데이터
  dataList.value = list;

  // 타입이 컬럼일때 테이블 이름 출력  
  if (dataList.value.length > 0) {
    if (dataList.value[0].typeName === "hive_column") {
      dataList.value.forEach(item => {
        const temp = item.attributes.qualifiedName;
        const tableName = temp.split('.');
        item.attributes.qualifiedName = tableName[1];
      });
    }
  }

  if (result.approximateCount > 0) {
      pagingObj.value = {
        ...pagingObj.value,
        total: ObjectUtils.getPropertyValue(result, "approximateCount", 0),
        currentPage: offset + 1,
      };
    } else {
      pagingObj.value = {
        ...pagingObj.value,
        total: 0,
        currentPage: 1,
      };
    }
        
};

const onSearchDept = async (item = {}) => {      
  const {
    name = null,
    guid = '',
    description = '',
    file_id = ''
  } = item;

  const result = {
    name,
    guid,
    description,
    dept: {
      dept_cd: '', // 담당부서코드
      dept_nm: '', // 담당부서명
    },
    file_id
  };

  if (name && name !== '_NOT_CLASSIFIED') {
    const response = await $ofetch(`/api/metadata/classification/dept/${name}`, { method: "GET" });        

    const dept = {
      dept_cd: response.data?.dept_cd || '',
      dept_nm: response.data?.dept_nm || '',
      dept_cd2: response.data?.dept_cd2 || '',
      dept_nm2: response.data?.dept_nm2 || ''
    };

    const description = response.data?.description || '';
    if (description) {
      result.description = description;
    }

    result.dept = dept;
  } else {
    result.description = name == '_NOT_CLASSIFIED' ? '미분류' : '전체';
  }

  selectedCategory.value = result;
};

// 좌측 카테고리 리스트 내 노드 클릭 이벤트 핸들러
const onClickMenuItem = (item: any = null): void => {
  if (!item) {
    onSearchDept();
    onSearchDataList();
  } else {
    onSearchDept(item);
    onSearchDataList(0, item);    
  }  
};

// 좌측 카테고리 목록 상단 추가 옵션 버튼 클릭 이벤트 핸들러
const onClickOptionalButtons = (type: any): void => {
  if (type === 'add') {
    // 추가 버튼 클릭 시 카테고리 추가 모달 생성
    onToggleManageModal(type);
  } else if (type === 'refresh'){
    // 새로고침 버튼 클릭 시 좌측 카테고리 목록 새로고침
    onSearchCategories();
  }
};

// 테이블 내 체크박스 선택 이벤트 핸들러
const onSelectItems = (selected: any) => {
  selectedData.value = selected;
};

// 카테고리 맵핑 삭제 이벤트 핸들러
const onDeleteCategoryMap = async (target: any) => {
  const confirm = await portalConfirm({
    message: "선택한 <strong>*구분 맵핑</strong>을 영구적으로 삭제합니다.<br>정말 삭제 하시겠습니까?"
  });

  if (confirm) {
    const { item } = target;
    let isTableType = false;

    // 해당 엔티티가 테이블 유형일 경우 하위 컬럼에 대해서도 동일 처리 가능하도록 flag 전달
    if (item.typeName.endsWith('_table')) {
      isTableType = true;
    }

    try {
      await $ofetch(`/api/metadata/classification/map/${target.name}`, {
        method: "PUT",
        body: {guid: item.guid, isTableType},
      }).then((res) => {
        if (!res || res.isFailed) {
          const msg = '요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.';
          errorAlert(msg);
        } else {
          // 더보기 툴팁에서 렌더하던 카테고리 삭제 시 더보기 툴팁 화면에서 제거
          onToggleTooltip();
          // 카테고리 맵핑 삭제 성공 시 테이블 데이터 재조회
          onSearchDataList(pagingObj.value.currentPage - 1);
          onSearchCategories();
        }
      });      
    } catch (error) {
      errorAlert('요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.');
    }
  }
};

</script>
<style lang="scss">
  @import "index";
</style>