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

// ── 상태 ──────────────────────────────────────────────────────────────────────

const loading = ref(true);
const word = ref("");

// 드래그 리사이즈
const divider = ref<HTMLElement | null>(null);
const leftSide = ref<HTMLElement | null>(null);
const rightSide = ref<HTMLElement | null>(null);
const mouseX = ref(0);
const leftWidth = ref(0);

// ── 색상 팔레트 (search/category 네트워크 맵과 동일) ───────────────────────────

const parentPalette = ["#e57373", "#64b5f6", "#81c784", "#ffb74d", "#ba68c8", "#4db6ac", "#9575cd"];

// ── 네트워크 데이터 빌드 ───────────────────────────────────────────────────────

const ROOT_NAME = "유사 속성 카테고리";

const buildNetwork = (categories: any[]) => {
  const nodes: Record<string, any> = {};
  const links: any[] = [];

  const maxCount = Math.max(...categories.map((c) => Number(c.table_count) || 0), 1);

  // 루트 노드
  nodes[ROOT_NAME] = {
    id: ROOT_NAME,
    name: ROOT_NAME,
    tableCount: categories.reduce((sum, c) => sum + (Number(c.table_count) || 0), 0),
    color: "#999999",
    marker: { radius: 40 },
    fixedPosition: { x: 0, y: 0 },
  };

  categories.forEach((cat, idx) => {
    const name = cat.name;
    const tableCount = Number(cat.table_count) || 0;
    // table_count 비례 반지름 (10 ~ 26)
    const radius = Math.round(10 + (tableCount / maxCount) * 16);
    const color = parentPalette[idx % parentPalette.length];

    nodes[name] = {
      id: name,
      name,
      tableCount,
      color,
      marker: { radius },
    };

    links.push({ from: ROOT_NAME, to: name, color });
  });

  return { nodes: Object.values(nodes), links };
};

// ── 차트 옵션 (search/category 네트워크 맵과 동일) ─────────────────────────────

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
      return `<b>${p.options.name}</b><br/><span>${p.options.tableCount} 개</span>`;
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

// ── 데이터 조회 ────────────────────────────────────────────────────────────────

const fetchCategories = async () => {
  try {
    const res = await $ofetch("/api/attr-category/list", { method: "GET" });
    const categories = Array.isArray(res?.data) ? res.data : [];
    const { nodes, links } = buildNetwork(categories);
    networkChartOptions.value.series[0].data = links;
    networkChartOptions.value.series[0].nodes = nodes;
  } catch {
    // 빈 차트 유지
  } finally {
    loading.value = false;
  }
};

// ── 이벤트 핸들러 ─────────────────────────────────────────────────────────────

const onClickMenuItem = (item: any) => {
  if (!item) return;
  moveToSearchPage({
    id: item.id || "",
    name: item.name || "",
    description: item.description || "",
  });
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
.nodata-msg {
  text-align: center;
  color: #999;
  padding: 60px 0;
  font-size: 14px;
}
</style>
