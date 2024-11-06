<template>
  <div class="contents">
    <div>
      <div class="mu-tab-box sub-tab">
        <ul class="mu-tab">
          <li :class="{ active: currentTab == 0}" @click="changeTab(0)">
            <a href="#">카테고리</a>
          </li>
          <li :class="{ active: currentTab == 1}" @click="changeTab(1)">
            <a href="#">시스템</a>
          </li>
        </ul>
      </div>
        <!-- visual-map-wrap : 맵영역 -->
        <div class="visual-map-wrap" id="chart-map-wrap">
          <div class="inner">
            <highcharts
              :constructorType="'chart'"
              :options="chartOptions"
              ref="chart"
              :callback="chartInit"
            />
          </div>
        </div>
        <!-- //visual-map-wrap -->
    </div>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from '@utils/ObjectUtils';
import { errorAlert } from "@functional/alert/alert-default";
import { useCategoryStore } from '@/store/category';

definePageMeta({
  name: 'SearchCategory',
  layout: 'user'
})

const { $ofetch } = useNuxtApp()
const route = useRoute()
const router = useRouter()
const categoryStore = useCategoryStore()

const currentTab = ref(0)
const catCategory = ref([])
const sysCategory = ref([])

const heatmap = ref({})

const categoryIdMap = ref({})
const categoryNameMap = ref([])
const rootCategory = ref({id:'rootCategory', name:'Root Category',subTypes:[]})
const categoryList = ref([]) // 카테고리 아이콘 렌더 위한 목록
const colorList = ref([ // 색상 로테이션 리스트
  'rgb(44, 76, 161)',
  'rgb(235, 201, 89)',
  'rgb(226, 127, 107)',
  'rgb(239, 159, 149)',
  'rgb(83, 120, 217)',
  'rgb(136, 169, 255)',
  'rgb(35, 209, 222)',
  'rgb(119, 225, 230)',
  'rgb(22, 225, 176)',
  'rgb(146, 236, 212)'
])
const chartOptions = ref({
  chart: {
    type: 'treemap',
    height: 650,
    spacing: [0,0,0,0],
    backgroundColor: 'transparent'
  },
  title: {
    text: ""
  },
  credits: { enabled: false },
  legend: {
    enabled: false
  },
  plotOptions: {
    series: {
      type: 'treemap',
      borderColor: '#f0f2f7',
      borderRadius: '15px',
      borderWidth: 4,
      clip: false,
      dataLabels: {
        align: 'center',
        enabled: true,
        formatter: function() {
          return this.point.name + '<br><span style="font-size:24px">' + this.point.value + '</span>';
        },
        style: {
          fontWeight: '600',
          fontSize: '20px',
          color: '#fff',
          textAlign: 'center',
          textOutline: 'none'
        }
      },
      point: {
        events: {
            click: function () {
                // 현재 창에서 url로 이동
                onClickChartItem(this)
            }
        }
      }
    }
  },
  series: [{
    name: '전체',
    type: 'treemap',
    borderRadius: '7px',
    layoutAlgorithm: 'squarified',
    allowDrillToNode: true,
    states: {
      hover: {
        borderColor: '#f0f2f7'
      }
    },
    breadcrumbs: {
      enabled: false,
      buttonSpacing: 10,
      position: {
        align: 'left'
      },
      separator: {
        style: {
          color: 'green'
        }
      },
      showFullPath: true,
      events: {
        click: (button, breadcrumbs) => {
          if (breadcrumbs.level > 0) {
            categoryStore.setSelectedItemGuid(breadcrumbs.levelOptions.point.id)
          } else {
            categoryStore.setSelectedItemGuid('')
          }
        }
      }
    },
    cursor: 'pointer',
    animationLimit: 1000,
    dataLabels: {
      enabled: false,
    },
    levels: [{
      level: 1,
      dataLabels: { // 상단 레이블
        enabled: true,
        align: 'left',
        verticalAlign: 'top',
        useHTML: true,
        formatter: function() {
          const data = this;

          const shapeArgs = data.point.shapeArgs

          const keyFontSize = "18px";
          const keyFontWeight = "900";
          const keyFontFamily = "NanumSquare"

          const tableFontSize = "14px";
          const tableFontWeight = "900";
          const tableFontFamily = "NanumSquare"

          const colorStyle = `color: #fff; text-shadow: -1px 0 #000, 0 -1px #000, 1px 0 #000, 0 1px #000; text-align: left;`;

          let keyTextStyle = `${ colorStyle } font-size: ${keyFontSize}; font-weight: ${keyFontWeight}; font-family:${keyFontFamily} /* text-shadow:#333 1px 0 1px;  */`;
          let tableTextStyle = `${ colorStyle } font-size: ${tableFontSize}; font-weight: ${tableFontWeight}; font-family:${tableFontFamily}/* text-shadow:#333 1px 0 1px; */`;

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
                    <p style="${ keyTextStyle }; ${overflowStyle}; padding: 0 10px 0 10px;">${this.key}</p>
                    <p style="${ tableTextStyle }; ${overflowStyle}; padding: 0 10px 0 10px;;">테이블 ${ObjectUtils.numberFormatter(this.point.value)}개</p>
                  </div>`;
        }
      },
      borderColor: '#f0f2f7',
      borderRadius: '15px',
      borderWidth: 4,
      levelIsConstant: false
    }],
    accessibility: {
      exposeAsGroupOnly: true
    },
    events: { click: e => { } },
    data: []
  }],  
  tooltip: {
    enabled: true,
    shared: true,
    useHTML: true,
    // tooltip 형태 설정
    formatter: function() {
      return`<div id="tooltip" class="mu-tooltip-info w360">
              <div class="mu-tooltip-head">
                <p>${getCurrentTabLabel()} 정보</p>
              </div>
              <div class="mu-tooltip-body">
                <div class="data-info2">
                  <dl>
                    <dt>이름</dt>
                    <dd>${this.key}</dd>
                  </dl>
                  <dl>
                    <dt>테이블 개수</dt>
                    <dd>${this.point.value} 개</dd>
                  </dl>
                </div>
              </div>
            </div>`
    },
    hideDelay: 100, // 사라지는 속도
    followPointer: true, // 포인터 위치에 tooltip 생성
    shadow: false, // tooltip 선 그림자
    borderWidth: 0, // tooltip 선 크기
    backgroundColor: 'none', // tooltip 선이 도달하는 배경표시 색상 제거,
    style: {
      fontSize: '1em'
    },
    // tooltip 위치 설정
    positioner: function(labelWidth, labelHeight, point) {
      // tooltip 좌표
      let tooltipX = point.plotX;
      let tooltipY = point.plotY;
      
      // tooltip 크기
      let tooltip_width = 360
      let tooltip_height = 180
      let tooltip_margin_left_right = 30
      let tooltip_margin_top_bottom = 10

      // Tooltip 위치 조정
      // X좌표
      if (tooltipX >= tooltip_width + tooltip_margin_left_right) {
        tooltipX -= (tooltip_width + tooltip_margin_left_right)
      } else {
        tooltipX += tooltip_margin_left_right
      }

      // Y좌표
      if (tooltipY >= tooltip_height + tooltip_margin_top_bottom) {
        tooltipY -= (tooltip_height + tooltip_margin_top_bottom)
      } else {
        tooltipY += tooltip_margin_top_bottom
      }

      return {
        x: tooltipX,
        y: tooltipY
      }
    }
  }
})

// await categoryStore.fetchCategories();

onMounted(async () => {
  await getCategoryData();

  // 메인 화면에서 진입한 경우 파라미터 추출하여 세팅
  const { params } = route;
  if ( params.item ) {
    categoryStore.setToggleTargetOnClickTreemap(params.item.name)
    categoryStore.setSelectedItemGuid(params.item.guid) // 카테고리 메뉴 클릭 이벤트 핸들러
    onClickMenuItem(params.item);
  }
})

const chartInit = (chart) => {
  heatmap.value = chart
}

const getChildData = (obj, ar) => {
  if( obj ) {
    if(obj.subTypes && obj.subTypes.length > 0) {
      let list = obj.subTypes
      for (let i=0; i<list.length; i++) {
        let subObj = getCategoryMap(list[i]);
        let subData = {id: subObj.guid, name: subObj.description, catName: subObj.name, value: subObj.tableCount, color: obj.color, parent: obj.guid,superTypes: subObj.superTypes, subTypes: obj.subTypes}
        ar.push(subData)
        getChildData(subObj, ar)  // 하위 자식 노드가 존재한지 확인하여 추가
      }
    }
  }
  return
}

const getCategoryMap = (data) => {
  if (categoryNameMap.value[data])
    return categoryNameMap.value[data]
  return categoryNameMap.value[data.name]
}

// 좌측 카테고리 메뉴 목록 & 트리맵 데이터 조회
const getCategoryData = async () => {
  const list = await categoryStore.fetchCategories({ from: 'searchByCategory' }); // 카테고리 메뉴 조회 이벤트 핸들러
  list.forEach(el => {
    // 최상위 카테고리일 경우 별도 저장
    if(!el.superTypes || el.superTypes.length < 1) {
      rootCategory.value.subTypes.push(el.name)
    }

    categoryNameMap.value[el.name] = el
    categoryIdMap.value[el.guid] = el
  })

  // 우측 트리맵 데이터 가공
  let ar = []
  getChildData(rootCategory.value, ar)  // 루트 카테고리로부터하위 데이터를 배열 형태로 변경

  // 2가지 분류
  const catAr = ar.filter(el => {
    return el.catName.startsWith('CAT') && el.catName != 'CAT'
  })
  const sysAr = ar.filter(el => {
    return el.catName.startsWith('SYS') && el.catName != 'SYS'
  })

  // 2가지 컬러 지정
  catAr.sort((a, b) => b.value - a.value).forEach((el, idx) => {
    el.color = colorList.value[idx]
  })

  sysAr.sort((a, b) => b.value - a.value).forEach((el, idx) => {
    // el.color = colorList.value[idx-3]
    el.color = colorList.value[idx]
  })

  catCategory.value = catAr
  sysCategory.value = sysAr

  chartOptions.value.series[0].data = catAr;
}

// 전체, 테이블, 컬럼 탭 설정
const changeTab = (tab) => {
  currentTab.value = tab
  if (tab == 0) chartOptions.value.series[0].data = catCategory.value;
  if (tab == 1) chartOptions.value.series[0].data = sysCategory.value;
}

const getCurrentTabLabel = () => {
  if (currentTab.value == 0)  return "카테고리";
  else return "시스템";
}

const onClickChartItem = (item) => {
  let obj = {
    name: item.catName
  }
  goToSearch(obj);
}

// 트리맵 클릭 시 검색페이지로 이동
const goToSearch = async (cat) => {
  await categoryStore.setMainSelectCategory(cat);
  await categoryStore.setMainSearch(true);
  router.push("/search/keyword")
}

</script>
