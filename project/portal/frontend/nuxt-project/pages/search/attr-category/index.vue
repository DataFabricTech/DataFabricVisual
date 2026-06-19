<template>
  <div class="contents">
    <!-- 검색 바 -->
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

    <div class="inner">
      <div class="mu-section">
        <!-- 카테고리 사이드바 -->
        <left-menu-list @onClickMenuItem="onClickMenuItem" />

        <!-- 사이드바 ↔ 차트 구분선 -->
        <div id="divider" @mousedown="mouseDown($event)"></div>

        <!-- 네트워크 맵 차트 -->
        <div class="visual-map-wrap" id="chart-map-wrap">
          <!-- 드릴다운 헤더 (뒤로가기) -->
          <div v-if="drillMode" class="chart-toolbar">
            <button type="button" class="mu-btn mu-btn-sm" @click="goBackToOverview">
              <span>← 전체 카테고리</span>
            </button>
            <span class="drill-category-name">{{ currentDrillCategory?.name }}</span>
          </div>

          <div v-if="loading || tableLoading" class="nodata-msg">
            {{ tableLoading ? '테이블 목록을 불러오는 중...' : '데이터를 불러오는 중입니다...' }}
          </div>
          <highcharts
            v-else
            :constructorType="'chart'"
            :options="networkChartOptions"
            ref="networkChart"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import LeftMenuList from "@functional/leftMenuList/left-menu-list";
import { useCategoryStore } from "@/store/category";
import { useSearchStore } from "@/store/search";
import { errorAlert } from "@functional/alert/alert-default";

definePageMeta({
  name: "SearchAttrCategory",
  layout: "user",
});

const { $ofetch } = useNuxtApp();
const router = useRouter();
const categoryStore = useCategoryStore();
const searchStore = useSearchStore();

// ── 상수 ──────────────────────────────────────────────────────────────────────

const ROOT_NAME = "유사 속성 카테고리";
const MORE_ID = "__MORE__";
const DRILL_LIMIT = 10;

// ── 상태 ──────────────────────────────────────────────────────────────────────

const loading = ref(true);
const tableLoading = ref(false);
const word = ref("");
const categoryList = ref<any[]>([]);
const drillMode = ref(false);
const currentDrillCategory = ref<any>(null);

// 드래그 리사이즈
const divider = ref<HTMLElement | null>(null);
const leftSide = ref<HTMLElement | null>(null);
const rightSide = ref<HTMLElement | null>(null);
const mouseX = ref(0);
const leftWidth = ref(0);

// ── 색상 팔레트 ────────────────────────────────────────────────────────────────

const parentPalette = ["#e57373", "#64b5f6", "#81c784", "#ffb74d", "#ba68c8", "#4db6ac", "#9575cd"];

// ── 노드 클릭 핸들러 (chartOptions보다 먼저 정의) ─────────────────────────────

const onNodeClick = function (this: any) {
  const pointId = String(this.id);

  if (!drillMode.value) {
    // 개요 모드: 카테고리 노드 클릭
    if (pointId === ROOT_NAME) return;
    const cat = categoryList.value.find((c) => c.name === pointId);
    if (cat) drillIntoCategory(cat);
  } else {
    // 드릴다운 모드: 테이블 / 더보기 노드 클릭
    if (pointId === MORE_ID) {
      // 더보기 → 키워드 검색으로 이동
      if (currentDrillCategory.value) {
        moveToSearchPage({ id: "", name: "", description: "" });
      }
    } else if (pointId === currentDrillCategory.value?.name) {
      // 중심(카테고리) 노드 클릭 → 뒤로가기
      goBackToOverview();
    } else {
      // 테이블 노드 클릭 → 상세 페이지
      router.push(`/search/detail?guid=${pointId}`);
    }
  }
};

// ── 차트 옵션 ─────────────────────────────────────────────────────────────────

const networkChartOptions = ref<any>({
  credits: { enabled: false },
  chart: { type: "networkgraph", height: "850px", width: "1030" },
  title: { text: "" },
  plotOptions: {
    networkgraph: {
      keys: ["from", "to"],
      layoutAlgorithm: {
        enableSimulation: true,
        linkLength: 20,
        friction: -0.9,
      },
      dataLabels: {
        enabled: true,
        linkFormat: "",
        style: {
          fontSize: "0.8em",
          fontWeight: "normal",
          textOutline: "2px contrast",
        },
        formatter() {
          return (this as any).point.name || (this as any).point.id;
        },
      },
      point: {
        events: {
          click: onNodeClick,
        },
      },
    },
  },
  tooltip: {
    useHTML: true,
    backgroundColor: "rgba(255,255,255,0.9)",
    borderRadius: 4,
    shadow: false,
    formatter() {
      const self = this as any;
      if (self.from && self.to) {
        return `<b>${self.from}</b> → <b>${self.to}</b>`;
      }
      const p = self.point;
      const id = String(p.id);
      if (id === ROOT_NAME) {
        return `<b>${p.options.name}</b><br/><span>총 ${p.options.tableCount ?? 0}개</span>`;
      }
      if (id === MORE_ID) {
        return `<b>${p.options.name}</b><br/><span>클릭하면 전체 검색으로 이동합니다</span>`;
      }
      if (p.options.similarityScore != null) {
        const pct = Math.round(p.options.similarityScore * 100);
        return `<b>${p.options.name}</b><br/><span>유사도 ${pct}%</span>`;
      }
      return `<b>${p.options.name}</b><br/><span>${p.options.tableCount ?? 0}개</span>`;
    },
    style: {
      fontSize: "12px",
      fontFamily: "NanumSquare",
      textOutline: "none",
      color: "#222",
    },
  },
  series: [
    {
      accessibility: { enabled: false },
      dataLabels: {
        enabled: true,
        linkFormat: "",
        style: { fontSize: "14px", fontWeight: "normal" },
      },
      id: "attr-category-network",
      data: [],
      nodes: [],
    },
  ],
});

// ── 네트워크 빌드 함수 ─────────────────────────────────────────────────────────

const buildCategoryNetwork = (categories: any[]) => {
  const nodes: any[] = [];
  const links: any[] = [];
  const maxCount = Math.max(...categories.map((c) => Number(c.table_count) || 0), 1);
  const total = categories.reduce((s, c) => s + (Number(c.table_count) || 0), 0);

  nodes.push({
    id: ROOT_NAME,
    name: ROOT_NAME,
    tableCount: total,
    color: "#999999",
    marker: { radius: 40 },
    fixedPosition: { x: 0, y: 0 },
  });

  categories.forEach((cat, idx) => {
    const tableCount = Number(cat.table_count) || 0;
    const radius = Math.round(10 + (tableCount / maxCount) * 16);
    const color = parentPalette[idx % parentPalette.length];

    nodes.push({
      id: cat.name,
      name: cat.name,
      tableCount,
      color,
      marker: { radius },
    });
    links.push({ from: ROOT_NAME, to: cat.name, color });
  });

  networkChartOptions.value.series[0] = {
    ...networkChartOptions.value.series[0],
    data: links,
    nodes,
  };
};

const buildTableNetwork = (cat: any, tables: any[]) => {
  const nodes: any[] = [];
  const links: any[] = [];
  const displayTables = tables.slice(0, DRILL_LIMIT);
  const remaining = tables.length - displayTables.length;

  // 중심: 카테고리 노드
  nodes.push({
    id: cat.name,
    name: cat.name,
    color: "#999999",
    marker: { radius: 36 },
    fixedPosition: { x: 0, y: 0 },
  });

  // 테이블 노드
  displayTables.forEach((tbl) => {
    const score = tbl.similarity_score != null ? Number(tbl.similarity_score) : null;
    nodes.push({
      id: tbl.meta_id,
      name: tbl.meta_name,
      similarityScore: score,
      color: "#64b5f6",
      marker: { radius: 16 },
    });
    links.push({ from: cat.name, to: tbl.meta_id, color: "#64b5f6" });
  });

  // 더보기 노드
  if (remaining > 0) {
    nodes.push({
      id: MORE_ID,
      name: `+${remaining}개 더보기`,
      color: "#bdbdbd",
      marker: { radius: 20 },
    });
    links.push({ from: cat.name, to: MORE_ID, color: "#bdbdbd" });
  }

  networkChartOptions.value.series[0] = {
    ...networkChartOptions.value.series[0],
    data: links,
    nodes,
  };
};

// ── 드릴다운 / 뒤로가기 ────────────────────────────────────────────────────────

const drillIntoCategory = async (cat: any) => {
  currentDrillCategory.value = cat;
  drillMode.value = true;
  tableLoading.value = true;

  try {
    const res = await $ofetch(`/api/attr-category/${cat.id}/tables`, { method: "GET" });
    const tables = Array.isArray(res?.data) ? res.data : [];
    buildTableNetwork(cat, tables);
  } catch {
    buildTableNetwork(cat, []);
  } finally {
    tableLoading.value = false;
  }
};

const goBackToOverview = () => {
  drillMode.value = false;
  currentDrillCategory.value = null;
  buildCategoryNetwork(categoryList.value);
};

// ── 데이터 조회 ────────────────────────────────────────────────────────────────

const fetchCategories = async () => {
  try {
    const res = await $ofetch("/api/attr-category/list", { method: "GET" });
    categoryList.value = Array.isArray(res?.data) ? res.data : [];
  } catch {
    categoryList.value = [];
  } finally {
    loading.value = false;
  }
  buildCategoryNetwork(categoryList.value);
};

// ── 검색 / 사이드바 핸들러 ─────────────────────────────────────────────────────

const onClickMenuItem = (item: any) => {
  if (!item) return;
  moveToSearchPage({ id: item.id || "", name: item.name || "", description: item.description || "" });
};

const moveToSearchPage = (classification: any) => {
  categoryStore.setCalledFrom({ from: "searchByKeyword" });
  searchStore.setSearchParams({
    query: word.value,
    detailFilters: {},
    pagingObj: {},
    system_type: "",
    meta_type: "table",
    tag: "",
    limit: 10,
    offset: 0,
    classification,
    sortOrder: "",
    entityFilters: {},
  });
  router.push("/search/keyword");
};

const onInputWord = (e: Event) => {
  word.value = (e.target as HTMLInputElement).value;
};

const onClickDeleteButton = () => {
  word.value = "";
};

const onSearchWord = () => {
  if (word.value.length >= 2) {
    moveToSearchPage({ id: "", name: "", description: "" });
  } else {
    errorAlert("최소 2글자 이상 입력하세요");
  }
};

// ── 드래그 리사이즈 ────────────────────────────────────────────────────────────

const mouseDown = (e: MouseEvent) => {
  mouseX.value = e.clientX;
  leftWidth.value = leftSide.value?.getBoundingClientRect().width ?? 0;
  document.addEventListener("mousemove", mouseDragging);
  document.addEventListener("mouseup", mouseUp);
};

const mouseDragging = (e: MouseEvent) => {
  const dx = e.clientX - mouseX.value;
  document.body.style.cursor = "col-resize";
  if (leftSide.value) {
    leftSide.value.style.userSelect = "none";
    leftSide.value.style.pointerEvents = "none";
  }
  if (rightSide.value) {
    rightSide.value.style.userSelect = "none";
    rightSide.value.style.pointerEvents = "none";
  }
  const parentWidth =
    (divider.value?.parentNode as HTMLElement)?.getBoundingClientRect().width ?? 1;
  const newLeftPct = ((leftWidth.value + dx) * 100) / parentWidth;
  if (newLeftPct >= 10 && newLeftPct <= 90) {
    if (leftSide.value) leftSide.value.style.width = `${newLeftPct}%`;
    if (rightSide.value) rightSide.value.style.width = `${100 - newLeftPct}%`;
  }
};

const mouseUp = () => {
  document.body.style.removeProperty("cursor");
  if (leftSide.value) {
    leftSide.value.style.removeProperty("user-select");
    leftSide.value.style.removeProperty("pointer-events");
  }
  if (rightSide.value) {
    rightSide.value.style.removeProperty("user-select");
    rightSide.value.style.removeProperty("pointer-events");
  }
  document.removeEventListener("mousemove", mouseDragging);
  document.removeEventListener("mouseup", mouseUp);
  const currentWidth = document.getElementById("chart-map-wrap")?.clientWidth;
  if (currentWidth) networkChartOptions.value.chart.width = currentWidth;
};

// ── 라이프사이클 ──────────────────────────────────────────────────────────────

onMounted(async () => {
  await categoryStore.fetchCategories({ from: "searchByAttrCategory" });
  await categoryStore.fetchSimilarAttrCategories();
  await fetchCategories();

  divider.value = document.getElementById("divider");
  leftSide.value = divider.value?.previousElementSibling as HTMLElement;
  rightSide.value = divider.value?.nextElementSibling as HTMLElement;
});

onUnmounted(() => {
  document.removeEventListener("mousemove", mouseDragging);
  document.removeEventListener("mouseup", mouseUp);
});
</script>

<style scoped>
.chart-toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 12px;
  border-bottom: 1px solid #e0e0e0;
  background: #f9f9f9;
}

.drill-category-name {
  font-size: 15px;
  font-weight: 700;
  color: #333;
}

.nodata-msg {
  text-align: center;
  color: #999;
  padding: 60px 0;
  font-size: 14px;
}
</style>
