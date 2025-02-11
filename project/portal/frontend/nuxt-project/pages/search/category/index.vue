<template>
  <div class="contents">
    <!-- content-search-wrap -->
    <div class="content-search-wrap">
      <form @submit.prevent="onSearchWord()">
        <fieldset>
          <legend>통합검색</legend>
          <div class="inner-search">
            <div class="mu-input-icon">
              <input
                type="text"
                :value="word"
                @input="onInputWord"
                class="mu-input"
                placeholder="검색어를 입력해 주세요."
                title="검색어입력"
              />
              <button type="button" class="mu-btn mu-btn-delete" @click="onClickDeleteButton()">
                <i class="mu-icon-svg item-delete"></i>
                <span>삭제</span>
              </button>
              <button type="button" class="mu-btn mu-btn-sch" @click.prevent="onSearchWord()">
                <i class="mu-icon-svg search"></i>
                <span>검색</span>
              </button>
            </div>
          </div>
        </fieldset>
      </form>
    </div>
    <!-- //content-search-wrap -->
    <div class="inner">
      <!-- mu-section : 영역나누기 -->
      <div class="mu-section">
        <!-- category-wrap : L 카테고리리스트 -->
        <left-menu-list @onClickFromTreeMap="setChildColor" @onClickMenuItem="onClickMenuItem" />

        <!-- 카테고리 리스트 사이즈 조절 바 -->
        <div id="divider" @mousedown="mouseDown($event)"></div>

        <!-- visual-map-wrap : 맵영역 -->
        <div class="visual-map-wrap" id="chart-map-wrap">
          <div class="inner">
            <highcharts :constructorType="'chart'" :options="chartOptions" ref="chart" :callback="chartInit" />
          </div>
        </div>
        <!-- //visual-map-wrap -->
      </div>
      <!-- //mu-section -->
    </div>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@utils/ObjectUtils";
import LeftMenuList from "@functional/leftMenuList/left-menu-list";
import { errorAlert } from "@functional/alert/alert-default";
import { useCategoryStore } from "@/store/category";
import { useSearchStore } from "@/store/search";
import { useUserStore } from "@/store/user";

definePageMeta({
  name: "SearchCategory",
  layout: "user"
});

const { $ofetch } = useNuxtApp();
const route = useRoute();
const router = useRouter();
const categoryStore = useCategoryStore();
const searchStore = useSearchStore();
const userStore = useUserStore();

const heatmap = ref({});
const currentClassification = ref({
  id: "",
  name: "",
  description: ""
})

// 창 조절 요소
const divider = ref(null); // 패널 사이즈 조절 DOM
const leftSide = ref(null); // 왼쪽 DOM
const rightSide = ref(null); // 오른쪽 DOM
const x = ref(0); // 현재 마우스 X 좌표
const leftWidth = ref(0); // 왼쪽 DOM 너비
const word = ref(""); // 검색어 input 제어
const categoryIdMap = ref({});
const categoryNameMap = ref([]);
const rootCategory = ref({ id: "rootCategory", name: "Root Category", subTypes: [] });
const categoryList = ref([]); // 카테고리 아이콘 렌더 위한 목록
// const colorList = ref([ // 색상 로테이션 리스트
//   'rgb(83,120,217)',  // #5378d9
//   'rgb(136,169,255)',  // #88a9ff
//   'rgb(35,209,222)',  // #23d1de
//   'rgb(119,225,230)',  // #77e1e6
//   'rgb(22,225,176)',  // #16e1b0
//   'rgb(146,236,212)',  // #92ecd4
//   'rgb(239,159,149)',  // #ef9f95
//   'rgb(226,127,107)',  // #e27f6b
//   'rgb(235,201,89)',  // #ebc959
//   'rgb(44,76,161)',   // #2c4ca1
// ])

const colorList = ref([
  "rgb(136, 169, 255)", // 가장 연한 색
  "rgb(122, 156, 240)",
  "rgb(110, 145, 225)",
  "rgb(100, 135, 210)",
  "rgb(90, 125, 195)",
  "rgb(80, 115, 185)",
  "rgb(70, 105, 175)",
  "rgb(60, 95, 168)",
  "rgb(52, 85, 164)",
  "rgb(44, 76, 161)"  // 가장 진한 색
]);

// const colorList = ref([ // 색상 로테이션 리스트
//   'rgb(255, 99, 71)',   // #FF6347 (Tomato)
//   'rgb(30, 144, 255)',  // #1E90FF (DodgerBlue)
//   'rgb(60, 179, 113)',  // #3CB371 (MediumSeaGreen)
//   'rgb(255, 165, 0)',  // #FFA500 (Orange)
//   'rgb(75, 0, 130)',  // #4B0082 (Indigo)
//   'rgb(255, 192, 203)',  // #FFC0CB (Pink)
//   'rgb(123, 104, 238)',  // #7B68EE (MediumSlateBlue)
//   'rgb(34, 139, 34)',  // #228B22 (ForestGreen)
//   'rgb(255, 215, 0)',  // #FFD700 (Gold)
//   'rgb(255, 20, 147)',  // #FF1493 (DeepPink)
//   'rgb(70, 130, 180)',  // #4682B4 (SteelBlue)
// ])

const chartOptions = ref({
  chart: { height: "850px", width: "1030" },
  credits: { enabled: false },
  series: [
    {
      name: "전체",
      type: "treemap",
      borderRadius: "7px",
      layoutAlgorithm: "squarified",
      allowDrillToNode: true,
      breadcrumbs: {
        buttonSpacing: 10,
        position: {
          align: "left"
        },
        separator: {
          style: {
            color: "green"
          }
        },
        showFullPath: true,
        events: {
          click: (button, breadcrumbs) => {
            if (breadcrumbs.level > 0) {
              categoryStore.setSelectedItemId(breadcrumbs.levelOptions.point.id);
              categoryStore.setToggleTargetOnClickTreemap(breadcrumbs.levelOptions.point.id);
              searchStore.setClassification({
                id: breadcrumbs.levelOptions.point.options.id,
                name: breadcrumbs.levelOptions.point.options.catName,
                description: breadcrumbs.levelOptions.point.options.name
              })
            } else {
              categoryStore.setSelectedItemId("");
            }
          }
        }
      },
      cursor: "pointer",
      animationLimit: 1000,
      dataLabels: {
        enabled: false
      },
      levels: [
        {
          level: 1,
          dataLabels: {
            // 상단 레이블
            enabled: true,
            align: "left",
            verticalAlign: "top",
            useHTML: true,
            formatter: function () {
              const data = this;

              const shapeArgs = data.point.shapeArgs;

              const keyFontSize = "18px";
              const keyFontWeight = "900";
              const keyFontFamily = "NanumSquare";

              const tableFontSize = "14px";
              const tableFontWeight = "900";
              const tableFontFamily = "NanumSquare";

              const colorStyle = `color: #fff; text-shadow: -1px 0 #000, 0 -1px #000, 1px 0 #000, 0 1px #000; text-align: left;`;

              let keyTextStyle = `${colorStyle} font-size: ${keyFontSize}; font-weight: ${keyFontWeight}; font-family:${keyFontFamily} /* text-shadow:#333 1px 0 1px;  */`;
              let tableTextStyle = `${colorStyle} font-size: ${tableFontSize}; font-weight: ${tableFontWeight}; font-family:${tableFontFamily}/* text-shadow:#333 1px 0 1px; */`;

              // width가 작은 경우 ellipsis(...) 처리
              let overflowStyle = `overflow: hidden; text-overflow: ellipsis;`;

              if (shapeArgs) {
                // height가 작은 경우 테이블 개수 미표기
                if (shapeArgs.height < 60 && shapeArgs.height >= 25) {
                  tableTextStyle = `display: none;`;
                  overflowStyle = `overflow: hidden; text-overflow: ellipsis;`;
                } else if (shapeArgs.height < 25) {
                  keyTextStyle = `display: none;`;
                  tableTextStyle = `display: none;`;
                  overflowStyle = `overflow: hidden; text-overflow: ellipsis;`;
                }
              }

              return `<div>
                    <p style="${keyTextStyle}; ${overflowStyle}; padding: 0 10px 0 10px;">${this.key}</p>
                    <p style="${tableTextStyle}; ${overflowStyle}; padding: 0 10px 0 10px;;">테이블 ${ObjectUtils.numberFormatter(this.point.value)}개</p>
                  </div>`;
            }
          },
          borderWidth: 2,
          borderColor: "#fff",
          levelIsConstant: false
        }
      ],
      accessibility: {
        exposeAsGroupOnly: true
      },
      events: { click: (e) => onClickTreeMapNode(e) },
      data: []
    }
  ],
  title: { text: undefined },
  tooltip: {
    enabled: true,
    shared: true,
    useHTML: true,
    // tooltip 형태 설정
    formatter: function () {
      if (!this || !this.key) {
        return "";
      }
      const text_overflow_style = `overflow: hidden; text-overflow: ellipsis; white-space: normal;`;

      return `<div>
                <div id="diagram"></div>
                <div id="tooltip" class="mu-tooltip-info w360">
                  <div class="mu-tooltip-head">
                    <p>카테고리 정보</p>
                  </div>
                  <div class="mu-tooltip-body">
                    <!-- data-info2 -->
                    <div class="data-info2">
                      <dl>
                        <dt>이름</dt>
                        <dd> ${this.key} </dd>
                      </dl>
                      <dl>
                        <dt>테이블 개수</dt>
                        <dd> ${this.point.value} 개 </dd>
                      </dl>
                    </div>
                    <!-- //data-info2 -->
                  </div>
                </div>
              </div>`;
    },
    // tooltip 위치 설정
    positioner: function (labelWidth, labelHeight, point) {
      // tooltip 좌표
      let tooltipX = point.plotX;
      let tooltipY = point.plotY;

      // tooltip 크기
      let tooltip_width = 360;
      let tooltip_height = 180;
      let tooltip_margin_left_right = 30;
      let tooltip_margin_top_bottom = 10;

      // Tooltip 위치 조정
      // X좌표
      if (tooltipX >= tooltip_width + tooltip_margin_left_right) {
        tooltipX -= tooltip_width + tooltip_margin_left_right;
      } else {
        tooltipX += tooltip_margin_left_right;
      }

      // Y좌표
      if (tooltipY >= tooltip_height + tooltip_margin_top_bottom) {
        tooltipY -= tooltip_height + tooltip_margin_top_bottom;
      } else {
        tooltipY += tooltip_margin_top_bottom;
      }

      return {
        x: tooltipX,
        y: tooltipY
      };
    },
    hideDelay: 100, // 사라지는 속도
    followPointer: true, // 포인터 위치에 tooltip 생성
    shadow: false, // tooltip 선 그림자
    borderWidth: 0, // tooltip 선 크기
    backgroundColor: "none" // tooltip 선이 도달하는 배경표시 색상 제거
  }
});

await categoryStore.fetchCategories();

onMounted(async () => {
  await initSearchParams(); // 검색조건 초기화 처리
  await initCategoryColor();
  await getCategoryData();

  // 조절할 창 및 조절 요소 설정
  divider.value = document.getElementById("divider");
  leftSide.value = divider.value.previousElementSibling;
  rightSide.value = divider.value.nextElementSibling;
  // 메인 화면에서 진입한 경우 파라미터 추출하여 세팅
  const { params } = route;
  if (params.item) {
    categoryStore.setToggleTargetOnClickTreemap(params.item.id);
    categoryStore.setSelectedItemId(params.item.id); // 카테고리 메뉴 클릭 이벤트 핸들러
    onClickMenuItem(params.item);
  }
});

const chartInit = (chart) => {
  heatmap.value = chart;
};

// 마우스를 클릭했을 때
const mouseDown = (e) => {
  // 현재 마우스 위치 값
  x.value = e.clientX;

  // leftElement에 Viewport 상 width 값을 가져와 대입
  leftWidth.value = leftSide.value.getBoundingClientRect().width;

  // 마우스 이벤트 등록
  document.addEventListener("mousemove", mouseDragging);
  document.addEventListener("mouseup", mouseUp);
};

// 마우스를 드래그 하고 있을 때
const mouseDragging = (e) => {
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
  if (newLeftWidth >= 10 && newLeftWidth <= 90) {
    leftSide.value.style.width = `${newLeftWidth}%`;
    rightSide.value.style.width = `${100 - newLeftWidth}%`;
  }
};

// 마우스 클릭을 떼었을 때
const mouseUp = () => {
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

  // 현재 너비에 맞춰서 차트 너비 설정
  const currentWidth = document.getElementById("chart-map-wrap").clientWidth;
  chartOptions.value.chart.width = currentWidth;
};

const getChildData = (obj, ar) => {
  if (obj) {
    if (obj.subTypes && obj.subTypes.length > 0) {
      let list = obj.subTypes;
      for (let i = 0; i < list.length; i++) {
        let subObj = getCategoryMap(list[i]);
        if (!subObj) continue;
        let subData = {
          id: subObj.id,
          name: subObj.description,
          catName: subObj.name,
          value: subObj.tableCount,
          color: obj.color,
          parent: subObj.parent_id,
          superTypes: subObj.superTypes,
          subTypes: subObj.subTypes
        };
        ar.push(subData);
        getChildData(subObj, ar); // 하위 자식 노드가 존재한지 확인하여 추가
      }
    }
  }
  return;
};

const getCategoryMap = (data) => {
  if (categoryIdMap.value[data]) return categoryIdMap.value[data];
};

// 좌측 카테고리 메뉴 목록 & 트리맵 데이터 조회
const getCategoryData = async () => {
  const list = await categoryStore.fetchCategories({ from: "searchByCategory" }); // 카테고리 메뉴 조회 이벤트 핸들러

  list.forEach((el) => {
    // 최상위 카테고리일 경우 별도 저장
    if (!el.superTypes || el.superTypes.length < 1) {
      rootCategory.value.subTypes.push(el.id);
    }

    categoryNameMap.value[el.name] = el;
    categoryIdMap.value[el.id] = {
      ...el,
    superTypes: [],
    subTypes: []
    };
  });

  list.forEach((el) => {
    if (el.parent_id) {
      categoryIdMap.value[el.parent_id].subTypes.push(el)
      categoryIdMap.value[el.id].superTypes.push(categoryIdMap.value[el.parent_id])
    }
  })

  // 우측 트리맵 데이터 가공
  let ar = [];
  getChildData(rootCategory.value, ar); // 루트 카테고리로부터하위 데이터를 배열 형태로 변경


  // 우측 트리맵 데이터 최상위색상 지정
  ar.forEach((el) => {
    const matchedColor = categoryList.value.find((cl) => cl.id === el.id);
    if (matchedColor) {
      el.color = matchedColor.color;
    }
  });

  chartOptions.value.series[0].data = ar;
};

// 아이콘 영역 렌더에 사용하는 데이터 값 설정
const initCategoryColor = async () => {
  // 카테고리 정보 받아오기
  const { data } = await $ofetch("/api/search/category/categoryList");
  categoryList.value = data;

  // 카테고리마다 색상 저장
  for (let i in categoryList.value) {
    let mod = i % colorList.value.length;
    categoryList.value[i].color = colorList.value[mod];
  }
};

// 검색어 입력 이벤트 핸들러
const onInputWord = (e) => {
  word.value = e.target.value;
};

// 검색어 입력창 내 x 버튼 클릭 이벤트 핸들러
const onClickDeleteButton = () => {
  word.value = "";
};

// 검색 이벤트 핸들러
const onSearchWord = () => {
  // 통합 검색 화면으로 이동
  if (word.value.length >= 2) {
    moveToSearchPage(currentClassification.value);
  } else {
    errorAlert("최소 2글자 이상 입력하세요");
  }
};

// 자식 색상 설정
const setChildColor = (item) => {
  let guid = "";
  if (item.guid) {
    guid = item.guid;
  } else if (item.id) {
    guid = item.id;
  }

  categoryStore.setSelectedItemId(guid);
};

// 좌측 카테고리 리스트 내 노드 클릭 이벤트 핸들러
const onClickMenuItem = (item) => {
  if (!item) {
    // 카테고리 전체
    for (let i = 0; i < 5; i++) heatmap.value.series[0].drillUp();
    currentClassification.value = {
      id: "",
      name: "",
      description: ""
    }
    return;
  }

  currentClassification.value = {
    id: item.id,
    name: item.name,
    description: item.description,
    parent_id: item.parent_id
  }

  let obj = categoryIdMap.value[item.id];
  if (!obj) {
    // 미분류인듯
    moveToSearchPage(item);
    return;
  }

  if (!obj.subTypes || obj.subTypes.length < 1) {
    moveToSearchPage(obj);
  } else {

    // 테이블 카운트가 0보다 큰 경우가 있는지 확인
    let matched = obj.subTypes.find((el) => el.tableCount > 0)
    if (matched) {
      // categoryStore.setToggleTargetOnClick(obj.name) // 하위 메뉴 토글 이벤트 핸들러
      categoryStore.setToggleTargetOnClickTreemap(item.id)

      // 하위 카테고리에 매핑된 데이터가 있는 경우만 이동
      if (item.subTypes) {
        let mappingCount = 0;
        for (let i = 0; i < item.subTypes.length; i++) {
          mappingCount += item.subTypes[i].tableCount;
          if (mappingCount > 0) {
            break;
          }
        }

        if (mappingCount == 0) return;
      }


      heatmap.value.series[0].drillToNode(item.id, true);
    } else { // 하위 카테고리 매핑 테이블들이 모두 0건
      moveToSearchPage(obj)
    }
  }
};

// 트리맵 내 노드 클릭 이벤트 핸들러
const onClickTreeMapNode = (e) => {
  currentClassification.value = {
    id: e.point.options.id,
    name: e.point.options.catName,
    description: e.point.options.name,
    parent_id: e.point.options.parent
  }

  if (!e || !e.point || !e.point.id) return;
  let obj = categoryIdMap.value[e.point.options.id];
  if (!obj.subTypes || obj.subTypes.length < 1) {
    moveToSearchPage(obj);
  } else {
    // 테이블 카운트가 0보다 큰 경우가 있는지 확인
    let matched = obj.subTypes.find((el) => el.tableCount > 0)
    if (matched) {
      // 하위가 있는 경우 왼쪽 메뉴가 열려있으면 그대로, 닫혀있으면 열기
      // categoryStore.setToggleTargetOnClick(e.point.catName) // 하위 메뉴 토글 이벤트 핸들러
      categoryStore.setSelectedItemId(e.point.id); // 해당 카테고리 활성화
      categoryStore.setToggleTargetOnClickTreemap(e.point.id);
    } else {
      moveToSearchPage(obj);
    }
  }
};

// 데이터 검색필터 초기화
const initSearchParams = () => {
  searchStore.setSearchParams({
    query: word.value,
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
    sortOrder: "",
    entityFilters: {}
  })
}

// 데이터 상세 검색 페이지 이동 이벤트 핸들러
const moveToSearchPage = (obj) => {
  categoryStore.setCalledFrom({ from: "searchByKeyword" }); // 카테고리 메뉴 호출 위치 변경

  // 검색어, 카테고리 저장
  searchStore.setSearchParams({
    query: word.value,
    detailFilters: {},
    pagingObj: {},
    system_type: "",
    meta_type: "table",
    tag: "",
    limit: 10,
    offset: 0,
    classification: obj,
    sortOrder: "",
    entityFilters: {}
  })
  router.push("/search/keyword")
};
</script>
