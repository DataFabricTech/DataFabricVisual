<template>
  <div class="mu-modal">
    <div class="mu-dialog-background"></div>
    <div class="mu-dialog w1200">
      <div class="mu-dialog-head">
        <p class="title">UDF 관리</p>
      </div>

      <div class="mu-dialog-body udf-dialog-body">
        <!-- ── 좌측: UDF 목록 ── -->
        <div class="udf-panel-left">
          <div class="udf-panel-left-head">
            <span>UDF 목록</span>
            <button type="button" class="mu-btn mu-btn-sm mu-btn-primary" @click="newForm"><span>+ 신규</span></button>
          </div>
          <div class="udf-panel-left-body">
            <table class="mu-grid mu-grid-data mu-grid-narrow">
              <colgroup>
                <col style="width: 36px" />
                <col />
                <col style="width: 86px" />
                <col style="width: 48px" />
              </colgroup>
              <thead>
                <tr>
                  <th>No.</th>
                  <th>함수명</th>
                  <th>반환타입</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="udfList.length === 0">
                  <td colspan="4" class="udf-empty">등록된 UDF가 없습니다.</td>
                </tr>
                <tr
                  v-for="(udf, idx) in udfList"
                  :key="udf.udf_id"
                  :class="['udf-row', { 'udf-row-active': currentIdx === idx }]"
                  @click="loadRow(idx)"
                >
                  <td class="udf-no">{{ idx + 1 }}</td>
                  <td>{{ udf.udf_name }}</td>
                  <td>
                    <em class="udf-tag">{{ udf.return_type }}</em>
                  </td>
                  <td class="udf-actions">
                    <button
                      class="mu-btn udf-btn-mini"
                      type="button"
                      title="에디터에 삽입"
                      @click.stop="insertSnippet(idx)"
                    >
                      ↗</button
                    ><button class="mu-btn udf-btn-mini" type="button" title="삭제" @click.stop="deleteRow(idx)">
                      ✕
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- ── 우측: 폼 ── -->
        <div class="udf-panel-right">
          <!-- 폼 영역 (스크롤 가능) -->
          <div class="udf-panel-body">
            <div class="udf-form-cols">
              <!-- 기본 필드 -->
              <div class="udf-form-main">
                <div class="udf-row-fields">
                  <div class="udf-field" style="flex: 2">
                    <label class="udf-label">함수명 <span class="udf-req">*</span></label>
                    <input
                      class="mu-input"
                      type="text"
                      v-model="fName"
                      placeholder="예) mask_email"
                      @input="updateSqlLive"
                    />
                  </div>
                  <div class="udf-field">
                    <label class="udf-label">반환 타입 <span class="udf-req">*</span></label>
                    <div class="mu-selectbox">
                      <select v-model="fReturn" @change="updateSqlLive">
                        <option value="">선택</option>
                        <option v-for="t in TYPES" :key="t" :value="t">{{ t }}</option>
                      </select>
                    </div>
                  </div>
                </div>

                <div class="udf-field" style="margin-top: 10px">
                  <label class="udf-label">설명</label>
                  <textarea
                    class="mu-area"
                    v-model="fDesc"
                    rows="2"
                    placeholder="함수의 용도를 간략히 입력하세요"
                  ></textarea>
                </div>

                <div class="udf-field udf-field-grow" style="margin-top: 10px">
                  <div class="udf-label-row">
                    <label class="udf-label">함수 본문 <span class="udf-req">*</span></label>
                  </div>
                  <textarea
                    class="mu-area udf-mono udf-body-textarea"
                    v-model="fBody"
                    placeholder="단순: regexp_replace(email, '(?<=.{2}).(?=.*@)', '*')&#10;블록: BEGIN&#10;  DECLARE v VARCHAR DEFAULT p;&#10;  RETURN v;&#10;END"
                    @input="updateSqlLive"
                  ></textarea>
                </div>
              </div>

              <!-- 파라미터 -->
              <div class="udf-form-params">
                <div class="udf-params-head">
                  <span>파라미터</span>
                  <button class="mu-btn mu-btn-sm" type="button" @click="addParam">+ 추가</button>
                </div>
                <div class="udf-params-body">
                  <table class="mu-grid mu-grid-data mu-grid-narrow">
                    <colgroup>
                      <col style="width: 12%" />
                      <col />
                      <col />
                      <col style="width: 28px" />
                    </colgroup>
                    <thead>
                      <tr>
                        <th>No.</th>
                        <th>파라미터명</th>
                        <th>타입</th>
                        <th></th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-if="params.length === 0">
                        <td colspan="4" class="udf-empty">파라미터가 없는 함수는 비워두세요.</td>
                      </tr>
                      <tr v-for="(p, i) in params" :key="i">
                        <td class="udf-no">{{ i + 1 }}</td>
                        <td>
                          <input
                            class="mu-input"
                            type="text"
                            v-model="p.param_name"
                            placeholder="파라미터명"
                            @input="updateSqlLive"
                          />
                        </td>
                        <td>
                          <div class="mu-selectbox">
                            <select v-model="p.param_type" @change="updateSqlLive">
                              <option value="">선택</option>
                              <option v-for="t in TYPES" :key="t" :value="t">{{ t }}</option>
                            </select>
                          </div>
                        </td>
                        <td>
                          <button class="mu-btn udf-btn-mini" type="button" @click="removeParam(i)">✕</button>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>

          <!-- SQL 고정 영역 -->
          <div class="udf-sql-area">
            <div class="udf-sql-head">
              <span class="udf-sql-label">
                생성된 SQL
                <span class="udf-sql-sub">— 붙여넣기 후 [← 폼에 적용] 가능</span>
              </span>
              <button class="mu-btn mu-btn-sm" type="button" @click="applyPastedSql">← 폼에 적용</button>
            </div>
            <textarea
              class="mu-area udf-mono"
              v-model="sqlLive"
              rows="4"
              style="width: 100%; resize: none"
              placeholder="-- 단순 표현식&#10;CREATE OR REPLACE FUNCTION name(param TYPE)&#10;RETURNS TYPE&#10;RETURN expression&#10;&#10;-- BEGIN/END 블록&#10;CREATE OR REPLACE FUNCTION name(param TYPE)&#10;RETURNS TYPE&#10;BEGIN&#10;  DECLARE v TYPE DEFAULT param;&#10;  RETURN v;&#10;END"
            ></textarea>
            <p v-if="parseError" class="udf-parse-error">{{ parseError }}</p>
          </div>
        </div>
      </div>
      <!-- 버튼 -->
      <div class="mu-dialog-foot">
        <button class="mu-btn" type="button" @click="cancelForm">취소</button>
        <button class="mu-btn mu-btn-primary" type="button" @click="saveUdf">
          <span>{{ isEditMode ? "수정" : "저장" }}</span>
        </button>
      </div>

      <div class="mu-dialog-close-btn">
        <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="cancelForm">
          <i class="mu-icon-svg close"></i><span>팝업닫기</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, defaultAlert, errorAlert, successAlert } from "@/components/functional/alert/alert-default";

const props = defineProps<{ initialUdfList?: any[] }>();
const emit = defineEmits(["close", "insert"]);

const { $ofetch } = useNuxtApp();

const TYPES = [
  "BOOLEAN",
  "TINYINT",
  "SMALLINT",
  "INTEGER",
  "BIGINT",
  "REAL",
  "DOUBLE",
  "VARCHAR",
  "VARBINARY",
  "DATE",
  "TIMESTAMP"
];

const udfList = ref<any[]>(props.initialUdfList ?? []);
const currentIdx = ref(-1);
const isEditMode = computed(() => currentIdx.value >= 0);

const fName = ref("");
const fReturn = ref("");
const fBody = ref("");
const fDesc = ref("");
const params = ref<{ param_name: string; param_type: string }[]>([]);
const sqlLive = ref("");
const parseError = ref("");

// ── 목록 조회 ──
const loadUdfList = async () => {
  await $ofetch("/api/metadata/fusion/udf/list")
    .then((res: any) => {
      if (res?.data) udfList.value = res.data;
    })
    .catch(() => {
      errorAlert("UDF 목록 조회에 실패하였습니다.");
    });
};

// ── 신규 모드 ──
const newForm = () => {
  currentIdx.value = -1;
  clearForm();
};

// ── 수정 모드: 폼 로드 ──
const loadRow = (idx: number) => {
  currentIdx.value = idx;
  const udf = udfList.value[idx];
  fName.value = udf.udf_name ?? "";
  fReturn.value = udf.return_type ?? "";
  fBody.value = udf.body ?? "";
  fDesc.value = udf.description ?? "";
  params.value = (udf.params ?? []).map((p: any) => ({
    param_name: p.param_name ?? "",
    param_type: p.param_type ?? ""
  }));
  updateSqlLive();
};

// ── 폼 초기화 ──
const clearForm = () => {
  fName.value = "";
  fReturn.value = "";
  fBody.value = "";
  fDesc.value = "";
  params.value = [];
  sqlLive.value = "";
  parseError.value = "";
};

// ── 취소 ──
const cancelForm = () => {
  if (isEditMode.value) loadRow(currentIdx.value);
  else clearForm();

  close();
};

// ── 파라미터 추가 / 제거 ──
const addParam = () => {
  params.value.push({ param_name: "", param_type: "" });
  updateSqlLive();
};

const removeParam = (i: number) => {
  params.value.splice(i, 1);
  updateSqlLive();
};

// ── SQL 실시간 생성 ──
const updateSqlLive = () => {
  if (!fName.value.trim()) {
    sqlLive.value = "";
    return;
  }
  const paramStr = params.value
    .map((p) => [p.param_name, p.param_type].filter(Boolean).join(" "))
    .filter(Boolean)
    .join(", ");
  const body = fBody.value.trim();
  const isBlock = body.toUpperCase().startsWith("BEGIN");
  const bodyPart = isBlock ? body : `RETURN ${body || "-- 표현식 입력"}`;
  sqlLive.value = `CREATE OR REPLACE FUNCTION internalhive.default.${fName.value.trim()}(${paramStr})\nRETURNS ${fReturn.value || "VARCHAR"}\n${bodyPart}`;
};

// ── SQL 붙여넣기 → 폼 채우기 ──
const applyPastedSql = () => {
  parseError.value = "";
  const sql = sqlLive.value.trim();
  if (!sql) return;

  const funcMatch = sql.match(/FUNCTION\s+([\w.]+)\s*\(([^)]*)\)/i);
  const returnsMatch = sql.match(/RETURNS\s+(\S+)/i);

  if (!funcMatch || !returnsMatch) {
    parseError.value = "파싱 실패: FUNCTION / RETURNS 절을 찾을 수 없습니다.";
    return;
  }

  const afterReturns = sql.replace(/[\s\S]*?RETURNS\s+\S+\s*/i, "").trim();
  let body = "";
  if (afterReturns.toUpperCase().startsWith("BEGIN")) {
    body = afterReturns;
  } else {
    const returnMatch = afterReturns.match(/^RETURN\s+([\s\S]+)/i);
    if (!returnMatch) {
      parseError.value = "파싱 실패: RETURN 또는 BEGIN/END 블록을 찾을 수 없습니다.";
      return;
    }
    body = returnMatch[1].trim();
  }

  const funcName = funcMatch[1].split(".").pop() ?? "";
  const paramStr = funcMatch[2].trim();
  const returnType = returnsMatch[1].trim();

  fName.value = funcName;
  fReturn.value = TYPES.includes(returnType) ? returnType : "";
  fBody.value = body;
  params.value = paramStr
    ? paramStr.split(",").map((p) => {
        const tokens = p.trim().split(/\s+/);
        return { param_name: tokens[0] ?? "", param_type: tokens.slice(1).join(" ") || "" };
      })
    : [];
};

// ── 저장 / 수정 ──
const saveUdf = async () => {
  if (!fName.value.trim()) {
    await defaultAlert("함수명은 필수입니다.");
    return;
  }
  if (!fReturn.value) {
    await defaultAlert("반환 타입은 필수입니다.");
    return;
  }
  if (!fBody.value.trim()) {
    await defaultAlert("RETURN 표현식은 필수입니다.");
    return;
  }

  const payload = {
    udf_name: fName.value.trim(),
    return_type: fReturn.value,
    body: fBody.value.trim(),
    description: fDesc.value.trim(),
    params: params.value.filter((p) => p.param_name && p.param_type)
  };

  try {
    if (isEditMode.value) {
      await $ofetch("/api/metadata/fusion/udf/update", {
        method: "PUT",
        body: { ...payload, udf_id: udfList.value[currentIdx.value].udf_id }
      });
      await successAlert("수정되었습니다.");
    } else {
      await $ofetch("/api/metadata/fusion/udf/register", {
        method: "POST",
        body: payload
      });
      await successAlert("저장되었습니다.");
      clearForm();
    }
    await loadUdfList();
  } catch {
    errorAlert("저장에 실패하였습니다.");
  }
};

// ── 삭제 ──
const deleteRow = (idx: number) => {
  const udf = udfList.value[idx];
  confirmAlert(`"${udf.udf_name}" UDF를 삭제하시겠습니까?`, {
    confirm: async () => {
      try {
        const res: any = await $ofetch(`/api/metadata/fusion/udf/delete?udf_id=${udf.udf_id}`, { method: "DELETE" });
        if (res?.data?.result === false) {
          await errorAlert(res.data.reason ?? "삭제에 실패하였습니다.");
          return;
        }
        if (currentIdx.value === idx) {
          currentIdx.value = -1;
          clearForm();
        } else if (currentIdx.value > idx) {
          currentIdx.value--;
        }
        await loadUdfList();
      } catch {
        errorAlert("삭제에 실패하였습니다.");
      }
    }
  });
};

// ── 에디터 스니펫 삽입 ──
const insertSnippet = (idx: number) => {
  const udf = udfList.value[idx];
  const args = (udf.params ?? []).map((p: any) => p.param_type).join(", ");
  emit("insert", `${udf.udf_name}(${args})`);
};

// ── 닫기 ──
const close = () => emit("close");

onMounted(async () => {
  // 부모에서 초기 목록을 받지 못한 경우에만 API 호출
  if (!props.initialUdfList || props.initialUdfList.length === 0) {
    await loadUdfList();
  }
});
</script>

<style scoped>
.udf-dialog-body {
  padding: 0 !important;
  display: flex !important;
  height: 480px;
  overflow: hidden;
}

/* ── 좌측 패널 ── */
.udf-panel-left {
  width: 300px;
  border-right: 1px solid #e4e7ef;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}
.udf-panel-left-head {
  padding: 10px 14px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 13px;
  flex-shrink: 0;
}
.udf-panel-left-body {
  flex: 1;
  overflow-y: auto;
}

/* ── 우측 패널 ── */
.udf-panel-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}
.udf-panel-body {
  flex: 1;
  overflow: hidden;
  padding: 16px;
  min-height: 0;
  display: flex;
  flex-direction: column;
}
.udf-panel-foot {
  padding: 10px 16px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  flex-shrink: 0;
}

/* ── 폼 레이아웃 ── */
.udf-form-cols {
  display: flex;
  gap: 16px;
  flex: 1;
  min-height: 0;
  overflow: hidden;
}
.udf-form-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0;
  min-width: 0;
  min-height: 0;
  overflow: hidden;
}
.udf-field-grow {
  flex: 1;
  min-height: 0;
}
.udf-body-textarea {
  flex: 1;
  min-height: 0;
  resize: none;
  height: 100%;
}
.udf-form-params {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
  min-height: 0;
}
.udf-row-fields {
  display: flex;
  gap: 10px;
}
.udf-field {
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin-bottom: 0;
}
.udf-label-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.udf-label {
  font-size: 11px;
  color: #555;
  font-weight: 600;
}
.udf-req {
  color: #e53935;
}
.udf-hint {
  font-size: 11px;
  color: #888;
}

/* ── 파라미터 ── */
.udf-params-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-weight: 600;
  font-size: 13px;
}
.udf-params-body {
  flex: 1;
  overflow-y: auto;
  min-height: 0;
}

/* ── SQL 영역 ── */
.udf-sql-area {
  padding: 10px 16px 12px;
  border-top: 1px solid #eee;
  flex-shrink: 0;
}
.udf-sql-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}
.udf-sql-label {
  font-size: 12px;
  font-weight: 600;
  color: #444;
}
.udf-sql-sub {
  font-size: 11px;
  font-weight: 400;
  color: #888;
}
.udf-parse-error {
  margin-top: 4px;
  font-size: 11px;
  color: #e53935;
}

/* ── 목록 테이블 ── */
.udf-panel-left-body :deep(th),
.udf-panel-left-body :deep(td) {
  padding: 4px 6px !important;
}
.udf-actions {
  white-space: nowrap;
  padding: 2px 4px !important;
}
.udf-row {
  cursor: pointer;
}
.udf-row-active {
  background: #eaf0ff !important;
}
.udf-no {
  text-align: center;
  color: #999;
}
.udf-empty {
  text-align: center;
  color: #999;
  font-size: 12px;
  padding: 14px 0;
}
.udf-tag {
  display: inline-block;
  padding: 1px 6px;
  border-radius: 10px;
  font-size: 10px;
  font-style: normal;
  font-weight: 600;
  background: #e8edf8;
  color: #3b6ff5;
}

/* ── 기타 ── */
.udf-mono {
  font-family: "Consolas", "Courier New", monospace;
  font-size: 12px;
}

/* ── 미니 버튼 (삽입/삭제) ── */
.udf-btn-mini {
  padding: 1px 4px !important;
  font-size: 11px !important;
  min-width: 0 !important;
  line-height: 1.4;
}

/* ── 하단 버튼 영역 구분선 ── */
.mu-dialog-foot {
  border-top: 1px solid #e4e7ef;
}
</style>
