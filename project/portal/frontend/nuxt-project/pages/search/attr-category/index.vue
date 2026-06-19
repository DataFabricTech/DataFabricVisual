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
        <left-menu-list :only-similar-attr="true" @onClickMenuItem="onClickMenuItem" />

        <!-- 사이드바 ↔ 차트 구분선 -->
        <div id="divider" @mousedown="mouseDown($event)"></div>

        <!-- 네트워크 맵 차트 -->
        <div class="visual-map-wrap" id="chart-map-wrap">
          <div v-if="loading" class="nodata-msg">데이터를 불러오는 중입니다...</div>
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
const DRILL_LIMIT = 10;
const MORE_NODE_PREFIX = "__MORE__";

// ── 상태 ──────────────────────────────────────────────────────────────────────

const loading = ref(true);
const word = ref("");
const categoryList = ref<any[]>([]);
const networkChart = ref<any>(null); // Highcharts 인스턴스 참조

// 확장 상태 관리 — Set/Map 대신 plain object로 Vue 3 reactivity 보장
const expandedCategories = ref<Record<string, boolean>>({});
const expandedTables = ref<Record<string, { list: any[]; total: number }>>({});
const loadingCategory = ref<string | null>(null); // 로딩 중인 카테고리명

// 드래그 리사이즈
const divider = ref<HTMLElement | null>(null);
const leftSide = ref<HTMLElement | null>(null);
const rightSide = ref<HTMLElement | null>(null);
const mouseX = ref(0);
const leftWidth = ref(0);

// ── 색상 팔레트 ────────────────────────────────────────────────────────────────

const parentPalette = ["#e57373", "#64b5f6", "#81c784", "#ffb74d", "#ba68c8", "#4db6ac", "#9575cd"];

// ── 노드 클릭 핸들러 ──────────────────────────────────────────────────────────

const onNodeClick = function (this: any) {
  // 링크(엣지)는 from/to 속성이 있음 — 노드 클릭만 처리
  if (this.from !== undefined || this.to !== undefined) return;

  const pointId = String(this.id ?? this.options?.id ?? "");
  if (!pointId) return;

  if (pointId === ROOT_NAME) return;

  if (pointId.startsWith(MORE_NODE_PREFIX)) {
    const catName = pointId.slice(MORE_NODE_PREFIX.length);
    const cat = categoryList.value.find((c) => c.name === catName);
    if (cat) {
      categoryStore.setSelectedItemId(cat.id);
      moveToSearchPage({ id: cat.id, name: cat.name, description: cat.description || "" });
    }
    return;
  }

  // 카테고리 노드 클릭 → 확장 / 축소
  const cat = categoryList.value.find((c) => c.name === pointId);
  if (cat) {
    toggleCategory(cat);
    return;
  }

  // 테이블 노드 클릭 → 상세 페이지
  router.push(`/search/detail?guid=${pointId}`);
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
        style: { fontSize: "0.8em", fontWeight: "normal", textOutline: "2px contrast" },
        formatter() {
          return (this as any).point.name || (this as any).point.id;
        },
      },
      point: {
        events: { click: onNodeClick },
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
      if (self.from && self.to) return `<b>${self.from}</b> → <b>${self.to}</b>`;
      const p = self.point;
      const id = String(p.id);
      if (id === ROOT_NAME) {
        return `<b>${p.options.name}</b><br/><span>총 ${p.options.tableCount ?? 0}개</span>`;
      }
      if (id.startsWith(MORE_NODE_PREFIX)) {
        return `<b>${p.options.name}</b>`;
      }
      if (p.options.similarity != null) {
        const pct = Math.round(p.options.similarity * 100);
        return `<b>${p.options.name}</b><br/><span>유사도 ${pct}%</span>`;
      }
      return `<b>${p.options.name}</b><br/><span>${p.options.tableCount ?? 0}개</span>`;
    },
    style: { fontSize: "12px", fontFamily: "NanumSquare", textOutline: "none", color: "#222" },
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

// ── 그래프 빌드 ───────────────────────────────────────────────────────────────

const buildGraph = async () => {
  const nodes: any[] = [];
  const links: any[] = [];
  const maxCount = Math.max(...categoryList.value.map((c) => Number(c.table_count) || 0), 1);
  const total = categoryList.value.reduce((s, c) => s + (Number(c.table_count) || 0), 0);

  // 루트 노드
  nodes.push({
    id: ROOT_NAME,
    name: ROOT_NAME,
    tableCount: total,
    color: "#999999",
    marker: { radius: 40 },
    fixedPosition: { x: 0, y: 0 },
  });

  categoryList.value.forEach((cat, idx) => {
    const tableCount = Number(cat.table_count) || 0;
    const radius = Math.round(10 + (tableCount / maxCount) * 16);
    const color = parentPalette[idx % parentPalette.length];
    const isExpanded = !!expandedCategories.value[cat.name];
    const isLoading = loadingCategory.value === cat.name;

    nodes.push({
      id: cat.name,
      name: isLoading ? `${cat.name} ...` : cat.name,
      tableCount,
      color,
      marker: {
        radius,
        lineColor: isExpanded ? "#ffffff" : color,
        lineWidth: isExpanded ? 3 : 0,
      },
    });
    links.push({ from: ROOT_NAME, to: cat.name, color });

    if (isExpanded && expandedTables.value[cat.name]) {
      const { list, total: tableTotal } = expandedTables.value[cat.name];
      const remaining = tableTotal - list.length;

      list.forEach((tbl) => {
        nodes.push({
          id: tbl.meta_id,
          name: tbl.meta_name,
          similarity: tbl.similarity != null ? Number(tbl.similarity) : null,
          color: "#64b5f6",
          marker: { radius: 10 },
        });
        links.push({ from: cat.name, to: tbl.meta_id, color: "#64b5f6" });
      });

      if (remaining > 0) {
        const moreId = `${MORE_NODE_PREFIX}${cat.name}`;
        nodes.push({
          id: moreId,
          name: `+${remaining}개 더보기`,
          color: "#bdbdbd",
          marker: { radius: 14 },
        });
        links.push({ from: cat.name, to: moreId, color: "#bdbdbd" });
      }
    }
  });

  // 차트 인스턴스가 있으면 API 직접 호출 (Vue 반응성 우회)
  await nextTick();
  const chart = networkChart.value?.chart;
  if (chart?.series?.[0]) {
    chart.series[0].update({ data: links, nodes }, true);
  } else {
    networkChartOptions.value.series[0].data = links;
    networkChartOptions.value.series[0].nodes = nodes;
  }
};

// ── 카테고리 확장 / 축소 ───────────────────────────────────────────────────────

const toggleCategory = async (cat: any) => {
  if (loadingCategory.value) return; // 로딩 중 중복 클릭 방지

  if (expandedCategories.value[cat.name]) {
    delete expandedCategories.value[cat.name];
    categoryStore.setSelectedItemId("");
    await buildGraph();
    return;
  }

  // 다른 카테고리가 펼쳐져 있으면 닫기 (expandedTables 캐시는 유지)
  expandedCategories.value = {};

  // 캐시된 데이터가 없으면 API 호출
  if (!expandedTables.value[cat.name]) {
    loadingCategory.value = cat.name;
    await buildGraph(); // 로딩 중 라벨 반영
    try {
      const res = await $ofetch(`/api/attr-category/${cat.id}/tables?sort=similarity&limit=${DRILL_LIMIT}`, { method: "GET" });
      const list = Array.isArray(res?.data?.list) ? res.data.list : [];
      const total = Number(res?.data?.total ?? list.length);
      expandedTables.value[cat.name] = { list, total };
    } catch {
      expandedTables.value[cat.name] = { list: [], total: 0 };
    } finally {
      loadingCategory.value = null;
    }
  }

  expandedCategories.value[cat.name] = true;
  categoryStore.setSelectedItemId(cat.id);
  await buildGraph();
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
  await buildGraph();
};

// ── 검색 / 사이드바 핸들러 ─────────────────────────────────────────────────────

const onClickMenuItem = (item: any) => {
  moveToSearchPage(item ?? null);
};

const moveToSearchPage = (cat: any) => {
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
    classification: { id: "", name: "", description: "" },
    attrCategory: cat?.id ? { id: cat.id, name: cat.name } : null,
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
  const parentWidth = (divider.value?.parentNode as HTMLElement)?.getBoundingClientRect().width ?? 1;
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
  categoryStore.setToggleTargets({ all: false, similarAttrAll: true });
  categoryStore.setSelectedItemId("");
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
.nodata-msg {
  text-align: center;
  color: #999;
  padding: 60px 0;
  font-size: 14px;
}
</style>
