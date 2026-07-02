<template>
  <div class="sm-panel">
    <!-- 판정 상세 툴팁 (마우스 따라다님, 스크롤에 안 잘리게 fixed) -->
    <div v-if="tip.show" class="sm-tip-float" :style="{ left: tip.x + 'px', top: tip.y + 'px' }">{{ tip.text }}</div>
    <!-- 현황 요약 -->
    <ul class="sm-summary" v-if="quality">
      <li><em>{{ quality.overall.rate }}%</em><span>전체 표준화율</span></li>
      <li><em>{{ fmt(quality.overall.total) }}</em><span>전체 컬럼</span></li>
      <li class="ok"><em>{{ fmt(quality.overall.standardized) }}</em><span>일치</span></li>
      <li class="bad"><em>{{ fmt(quality.overall.type_mismatch) }}</em><span>타입 불일치</span></li>
      <li class="bad"><em>{{ fmt(quality.overall.name_mismatch) }}</em><span>불일치</span></li>
    </ul>

    <div class="sm-body">
      <!-- 왼쪽: 테이블 목록 -->
      <div class="sm-left">
        <div class="mu-item-group sm-filter">
          <input v-model="search" class="mu-input" placeholder="테이블명 검색" />
          <div class="mu-selectbox w160">
            <select v-model="sysFilter">
              <option value="">전체 시스템</option>
              <option v-for="s in quality?.systems || []" :key="s.system_name" :value="s.system_name">
                {{ s.system_name }} ({{ s.rate }}%)
              </option>
            </select>
          </div>
        </div>
        <div class="board-list sm-scroll">
          <table class="mu-grid mu-grid-narrow">
            <colgroup><col /><col style="width:26%" /><col style="width:88px" /></colgroup>
            <thead>
              <tr><th scope="col">테이블</th><th scope="col">시스템</th><th scope="col">표준화율</th></tr>
            </thead>
            <tbody>
              <tr v-for="t in filteredTables" :key="t.meta_id"
                  :class="{ 'sm-active': selected && selected.meta_id === t.meta_id }"
                  @click="selected = t">
                <td class="sm-tname">{{ t.meta_name }}</td>
                <td class="sm-sys">{{ t.system_name }}</td>
                <td><span class="sm-rate" :style="rateStyle(t.rate)">{{ t.rate }}%</span></td>
              </tr>
              <tr v-if="!filteredTables.length"><td colspan="3" class="sm-empty">결과 없음</td></tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- 오른쪽: 컬럼 판정 -->
      <div class="sm-right">
        <div v-if="!selected" class="sm-empty sm-empty-detail">← 테이블을 선택하세요</div>
        <template v-else>
          <div class="mu-heading sm-detail-head">
            <h4>{{ selected.meta_name }}
              <span class="sm-sys">{{ selected.system_name }}</span>
              <span class="sm-rate" :style="rateStyle(selected.rate)">{{ selected.standardized }}/{{ selected.total }} ({{ selected.rate }}%)</span>
            </h4>
          </div>
          <div class="board-list sm-scroll">
            <table class="mu-grid mu-grid-narrow">
              <colgroup><col style="width:18%" /><col style="width:22%" /><col style="width:100px" /><col style="width:20%" /><col /></colgroup>
              <thead>
                <tr>
                  <th scope="col">컬럼명</th><th scope="col">타입</th><th scope="col">판정</th>
                  <th scope="col">매칭 용어/단어</th><th scope="col">추천 용어/단어</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="loadingCols"><td colspan="5" class="sm-empty">불러오는 중…</td></tr>
                <tr v-else-if="!columns.length"><td colspan="5" class="sm-empty">컬럼 없음</td></tr>
                <tr v-for="(c, i) in columns" :key="i">
                  <td class="sm-cname">{{ c.column_name }}</td>
                  <td class="sm-ctype">{{ fmtType(c.column_type) }}</td>
                  <td class="sm-judge"
                      @mouseenter="showTip($event, judgeTip(c))"
                      @mousemove="moveTip" @mouseleave="hideTip">
                    <span v-if="c.is_standardized" class="sm-ok">일치</span>
                    <span v-else-if="c.not_standardized_reason === 'TYPE_MISMATCH'" class="sm-bad">타입 불일치</span>
                    <span v-else class="sm-bad">불일치</span>
                  </td>
                  <td class="sm-match">
                    <template v-if="c.standard_term_name">{{ c.standard_term_name }}</template>
                    <template v-else-if="c.standard_word_names">{{ c.standard_word_names }}</template>
                    <template v-else>-</template>
                  </td>
                  <td class="sm-reco">{{ c.recommended_term_names || '-' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
const { $ofetch } = useNuxtApp();

const quality = ref(null);
const tables = ref([]);
const selected = ref(null);
const columns = ref([]);
const loadingCols = ref(false);
const search = ref("");
const sysFilter = ref("");

onMounted(async () => {
  // quality=GET /api/standard-mapping/quality, tables=GET /api/standard-mapping/tables
  // (컬럼은 테이블 선택 시 /list?metaId 로 지연 로드)
  try {
    const [q, t] = await Promise.all([
      $ofetch("/api/standard-mapping/quality"),
      $ofetch("/api/standard-mapping/tables"),
    ]);
    quality.value = q?.data ?? null;
    tables.value = t?.data ?? [];
  } catch (e) {
    quality.value = null;
    tables.value = [];
  }
});

// 테이블 선택 시 해당 컬럼 판정 목록 지연 로드
watch(selected, async (tbl) => {
  columns.value = [];
  if (!tbl) return;
  loadingCols.value = true;
  try {
    const res = await $ofetch(`/api/standard-mapping/list?metaId=${tbl.meta_id}`);
    columns.value = (res?.data ?? []).map(normalizeColumn);
  } catch (e) {
    columns.value = [];
  } finally {
    loadingCols.value = false;
  }
});

// API 응답 정규화: token_words(jsonb::text) 파싱, 추천 용어+단어 병합
const normalizeColumn = (c) => {
  let tw = c.token_words;
  if (typeof tw === "string") { try { tw = JSON.parse(tw); } catch (e) { tw = null; } }
  const parts = [];
  if (c.recommended_term_names) parts.push(c.recommended_term_names);
  if (c.recommended_word_names) {
    parts.push(c.recommended_word_names.split(",").filter(Boolean).map((s) => s + "(단어)").join(","));
  }
  return { ...c, token_words: tw, recommended_term_names: parts.join(",") || null };
};

const filteredTables = computed(() => {
  let list = tables.value;
  if (sysFilter.value) list = list.filter((t) => t.system_name === sysFilter.value);
  if (search.value) {
    const k = search.value.toLowerCase();
    list = list.filter((t) => t.meta_name.toLowerCase().includes(k));
  }
  return list.slice(0, 300);
});

const fmt = (n) => (n ?? 0).toLocaleString();
const fmtType = (t) => (t || "").replace(/\s+(WITHOUT|WITH)\s+TIME\s+ZONE/i, "");

const tip = reactive({ show: false, x: 0, y: 0, text: "" });
const showTip = (e, text) => { tip.text = text; tip.show = true; moveTip(e); };
const moveTip = (e) => {
  // 화면 오른쪽/아래 넘치면 반대편으로
  const x = e.clientX + 16, y = e.clientY + 16;
  tip.x = x > window.innerWidth - 380 ? e.clientX - 360 : x;
  tip.y = y > window.innerHeight - 160 ? e.clientY - 140 : y;
};
const hideTip = () => { tip.show = false; };

const judgeTip = (c) => {
  const L = [];
  const tw = c.token_words || [];
  const wordLines = () => tw.forEach(([t, w]) =>
    L.push("   " + t + " → " + (w ? w : "✗ 표준에 없음")));

  if (c.is_standardized) {
    if (c.mapping_method === "RULE_TERM") {
      L.push("판정: 일치 — 용어 전체일치");
      L.push("컬럼명 전체가 표준 용어 하나와 일치");
      if (c.standard_term_name) L.push("· 용어: " + c.standard_term_name);
      L.push("· 도메인 타입·길이 검증 통과");
    } else {
      L.push("판정: 일치 — 단어 조합일치");
      L.push("이름을 이루는 단어가 모두 표준에 있음");
      wordLines();
      L.push("· 타입 계열 검증 통과");
    }
  } else if (c.not_standardized_reason === "TYPE_MISMATCH") {
    L.push("판정: 타입 불일치");
    L.push("이름은 표준이나 타입/길이가 다름");
    if (c.standard_term_name) L.push("· 매칭 용어: " + c.standard_term_name);
    else if (c.standard_word_names) L.push("· 매칭 단어: " + c.standard_word_names);
    if (c.type_detail) L.push("· 사유: " + c.type_detail);
  } else {
    L.push("판정: 불일치 — 이름이 표준 미준수");
    const bad = tw.filter(([, w]) => !w).map(([t]) => t);
    if (tw.length) {
      L.push(bad.length
        ? "표준에 없는 단어 때문: " + bad.join(", ")
        : "단어는 표준이나 조합이 표준용어에 없음");
      wordLines();
    } else if (c.tokens && c.tokens.length) {
      L.push("단어: " + c.tokens.join(", "));
    }
    L.push(c.recommended_term_names
      ? "추천: " + c.recommended_term_names
      : "추천 없음 (비슷한 표준 용어/단어 없음)");
  }
  return L.join("\n");
};
const rateStyle = (r) => {
  const g = Math.round((r / 100) * 160);
  return { background: `hsl(${g}, 65%, 92%)`, color: `hsl(${g}, 70%, 30%)` };
};
</script>

<style scoped>
.sm-summary { display:flex; gap:10px; margin:14px 0 6px; padding:0; list-style:none; }
.sm-summary li { flex:1; border:1px solid #e5e8ee; border-radius:6px; padding:12px 14px; background:#fafbfc; }
.sm-summary li.ok { background:#f0faf3; border-color:#cbe8d5; }
.sm-summary li.bad { background:#fdf1f1; border-color:#f0d2d2; }
.sm-summary em { display:block; font-size:20px; font-weight:700; font-style:normal; }
.sm-summary span { font-size:12px; color:#8a90a0; }
.sm-body { display:flex; gap:16px; align-items:flex-start; }
.sm-left { width:46%; }
.sm-right { flex:1; min-width:0; }
.sm-filter { display:flex; gap:8px; margin-bottom:8px; }
.mu-input { flex:1; height:32px; border:1px solid #d5d9e0; border-radius:4px; padding:0 10px; }
.sm-scroll { max-height:560px; overflow:auto; }
.sm-scroll thead th { position:sticky; top:0; z-index:2; background:#f6f6f9; }
.board-list tbody tr { cursor:pointer; }
.board-list tbody tr:hover { background:#f6f9ff; }
.board-list tbody tr.sm-active { background:#eef4ff; }
.sm-tname { font-weight:600; word-break:break-all; }
.sm-cname { font-weight:600; white-space:nowrap; }
.sm-sys { color:#8890a0; font-size:12px; }
.sm-ctype { color:#7a8290; font-size:12px; white-space:nowrap; }
.sm-rate { display:inline-block; padding:2px 8px; border-radius:10px; font-weight:700; font-size:12px; }
.sm-ok { color:#2e7d32; font-weight:700; }
.sm-bad { color:#c62828; font-weight:700; }
.sm-tip-float {
  position:fixed; z-index:9999; pointer-events:none;
  background:#2c2f36; color:#fff; padding:8px 11px; border-radius:6px;
  font-size:12px; line-height:1.55; white-space:pre-line; max-width:360px;
  box-shadow:0 4px 14px rgba(0,0,0,.28);
}
.sm-match { font-size:12px; color:#555; }
.sm-reco { font-size:12px; color:#b26a00; }
.sm-detail-head h4 { display:flex; align-items:center; gap:10px; }
.sm-empty { padding:24px; color:#aaa; text-align:center; }
</style>
