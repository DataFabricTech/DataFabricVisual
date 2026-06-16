<template>
  <div class="mu-modal" v-if="props.manageModal.toggle">
    <div class="mu-dialog-background"></div>

    <div :class="['mu-dialog', props.manageModal.type === 'map' ? 'w600' : 'w720']">
      <div class="mu-dialog-head">
        <p class="title">카테고리 {{ isTabMode ? "관리" : modalTypeText }}</p>
      </div>

      <!-- 탭 (type === 'add' 일 때만) -->
      <div class="tab-header" v-if="isTabMode">
        <button type="button" :class="['tab-btn', activeTab === 'category' ? 'active' : '']"
          @click="activeTab = 'category'">
          카테고리 추가
        </button>
        <button type="button" :class="['tab-btn', activeTab === 'similarAttr' ? 'active' : '']"
          @click="onClickSimilarAttrTab">
          유사속성 카테고리 추가
        </button>
      </div>

      <div class="mu-dialog-body">
        <!-- ── Tab 1 / 기존 모드: 카테고리 추가·수정·매핑 ── -->
        <div v-show="!isTabMode || activeTab === 'category'">
          <table class="mu-formbox">
            <caption>카테고리 {{ modalTypeText }} 게시판</caption>
            <colgroup>
              <col style="width: 170px" />
              <col />
            </colgroup>

            <!-- 카테고리 맵핑 -->
            <tbody v-if="props.manageModal.type === 'map'">
              <tr>
                <th scope="row">카테고리 명</th>
                <td>
                <td>
                  {{ upperCategory && upperCategory.description ? upperCategory.description + '(' + upperCategory.value +
                    ')' : "없음" }}
                  <button type="button" class="mu-btn mu-btn-primary" @click="openSelectCategoryModal"
                    style="margin-left:10px">
                    <i class="mu-icon-svg search"></i><span>찾아보기</span>
                  </button>
                </td>
                </td>
              </tr>
            </tbody>

            <!-- 카테고리 추가/수정 -->
            <tbody v-else>
              <tr>
                <th scope="row">카테고리 명<strong class="required">필수</strong></th>
                <td>
                  <input type="text" placeholder="카테고리 명을 입력하세요. (최대 20자)" class="mu-input" title="카테고리 명 입력"
                    name="krName" maxlength="20" v-model="krName" />
                </td>
              </tr>
              <tr>
                <th scope="row">카테고리 ID<strong class="required">필수</strong></th>
                <td>
                  <input type="text" placeholder="카테고리 ID를 입력하세요. (최대 20자)" class="mu-input" title="카테고리 ID 입력"
                    name="enName" maxlength="20" :disabled="props.manageModal.type === 'update'" v-model="enName"
                    @input="onInput" />
                </td>
              </tr>
              <tr v-if="upperCategory.value === ''">
                <th scope="row">카테고리 이미지</th>
                <td>
                  <div class="mu-item-group">
                    <img v-if="categoryImageFile" :src="categoryImageFile"
                      style="width: 70px; height: 70px; background-color: #0D2254; background-size: cover"
                      :class="['ico-category', 'category-11']" />
                    <img v-else src="/category_basic.png"
                      style="width: 70px; height: 70px; background-color: #0D2254; background-size: cover"
                      :class="['ico-category', 'category-11']" />
                    <input id="categoryImageFileInput" type="file" name="categoryImage" @change="fileUpload"
                      v-show="false" />
                    <label for="categoryImageFileInput" class="mu-btn" v-if="!categoryImageFile">이미지업로드</label>
                    <label for="categoryImageFileInput" class="mu-btn" v-if="categoryImageFile">이미지수정</label>
                  </div>
                </td>
              </tr>
              <tr v-if="props.manageModal.type !== 'update'">
                <th scope="row">상위카테고리</th>
                <td>
                  {{ upperCategory && upperCategory.description ? upperCategory.description + '(' + upperCategory.value +
                    ')' : "없음" }}
                  <button type="button" class="mu-btn mu-btn-primary" @click="openSelectCategoryModal"
                    style="margin-left:10px">
                    <i class="mu-icon-svg search"></i><span>찾아보기</span>
                  </button>
                </td>
              </tr>
              <tr>
                <th scope="row">Ord no</th>
                <td>
                  <input type="number" placeholder="카테고리 순서" class="mu-input" title="Ord no" name="ordNo"
                    maxlength="1000" v-model="categoryOrdNo" />
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- ── Tab 2: 유사속성 카테고리 추가 ── -->
        <div v-if="isTabMode && activeTab === 'similarAttr'">
          <!-- 20개 상한 도달 -->
          <div v-if="similarAttrTotal >= SIMILAR_ATTR_MAX" class="limit-notice">
            유사속성 카테고리는 최대 {{ SIMILAR_ATTR_MAX }}개까지 등록할 수 있습니다.
            (현재 {{ similarAttrTotal }}개 등록됨)
          </div>

          <!-- 추가 폼 -->
          <table v-else class="mu-formbox">
            <caption>유사속성 카테고리 추가</caption>
            <colgroup>
              <col style="width: 130px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">카테고리명<strong class="required">필수</strong></th>
                <td>
                  <input type="text" class="mu-input" v-model="saForm.name"
                    placeholder="카테고리명 (최대 100자)" maxlength="100" />
                </td>
              </tr>
              <tr>
                <th scope="row">설명</th>
                <td>
                  <input type="text" class="mu-input" v-model="saForm.description"
                    placeholder="설명 (최대 512자)" maxlength="512" />
                </td>
              </tr>
              <tr>
                <th scope="row">키워드</th>
                <td>
                  <div class="tag-input-wrap">
                    <span v-for="(tag, i) in saKeywords" :key="i" class="tag-item">
                      {{ tag }}
                      <button type="button" @click="removeSaKeyword(i)">×</button>
                    </span>
                    <input type="text" v-model="saKeywordInput"
                      placeholder="입력 후 Enter 또는 Space"
                      @keydown="onSaKeywordKeydown" />
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">정렬 순서</th>
                <td>
                  <input type="number" class="mu-input" v-model.number="saForm.ord_no"
                    placeholder="숫자 입력" style="width: 120px" />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- 푸터 -->
      <div class="mu-dialog-foot">
        <!-- Tab 2: 유사속성 카테고리 추가 -->
        <template v-if="isTabMode && activeTab === 'similarAttr'">
          <button type="button" class="mu-btn" @click="onCancel"><span>취소</span></button>
          <button type="button" class="mu-btn mu-btn-primary" @click="onConfirmSimilarAttr"
            :disabled="isSimilarAttrDisabled">
            <span>등록</span>
          </button>
        </template>
        <!-- Tab 1 / 기존 모드 -->
        <template v-else>
          <button type="button" class="mu-btn" @click="onCancel"><span>취소</span></button>
          <button type="button" class="mu-btn mu-btn-primary" @click="onConfirm"
            v-if="props.manageModal.type !== 'update'" :disabled="isDisabled">
            <span>확인</span>
          </button>
          <button type="button" class="mu-btn mu-btn-primary" @click="onUpdate"
            v-if="props.manageModal.type === 'update'" :disabled="isDisabled">
            <span>수정</span>
          </button>
        </template>
      </div>

      <!-- 접근성: 닫기 버튼 -->
      <div class="mu-dialog-close-btn">
        <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="onClose">
          <i class="mu-icon-svg close"></i><span>팝업닫기</span>
        </button>
      </div>
    </div>

    <select-category-modal v-if="toggleSelectCategoryModal" :upperCategory="upperCategory"
      :toggle-select-category-modal="toggleSelectCategoryModal" @onSelect="onSelectCategory"
      @onCloseModal="onClickCategoryInfoSearchCloseButton" />
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@utils/ObjectUtils";

import { errorAlert } from "@functional/alert/alert-default";
import { useCategoryStore } from "@store/category";
import { useNuxtApp } from "nuxt/app";
import { computed, ref } from "vue";
import { isEmpties } from "@composables/ramda";
import selectCategoryModal from "@functional/classificationModal/select-classification-modal";
import { useCategoryTreeStore } from "@store/categoryTree";

const { $ofetch } = useNuxtApp();

const categoryStore = useCategoryStore();
const categoryTreeStore = useCategoryTreeStore();
const emit = defineEmits(["onCloseModal", "onSubmit", "onSubmitSimilarAttr"]);
const props = defineProps(["manageModal", "onCloseModal", "onSubmit"]);

// ── 탭 ────────────────────────────────────────────────────────────────────────
const SIMILAR_ATTR_MAX = 21;
const activeTab = ref<"category" | "similarAttr">("category");
const isTabMode = computed(() => props.manageModal.type === "add");

// ── 기존 카테고리 상태 ─────────────────────────────────────────────────────────
const krName = ref("");
const originalKrName = ref("");
const categoryImageFileId = ref("");
const tempCategoryImageFileId = ref("");
const enName = ref("");
const upperCategory = ref({ description: "", value: "", level: "", parent_id: "", id: "" });
const originalUpperCategory = ref("");
const categoryOrdNo = ref(0);
const selectedDept = ref({});
const originalDeptCd = ref("");
const isEdited = ref(false);
const categoryImageFile = ref("");
const toggleSelectCategoryModal = ref(false);

// ── 유사속성 카테고리 추가 상태 ────────────────────────────────────────────────
const similarAttrTotal = ref(0); // 현재 등록 수 (Tab 2 진입 시 조회)
const saForm = ref({ name: "", description: "", ord_no: null as number | null });
const saKeywords = ref<string[]>([]);
const saKeywordInput = ref("");

// ── Computed ──────────────────────────────────────────────────────────────────
const modalTypeText = computed(() => {
  const TYPE_MAP = new Map([
    ["add", "추가"],
    ["insert", "추가"],
    ["update", "수정"],
    ["map", "맵핑"]
  ]);
  return TYPE_MAP.get(props.manageModal.type);
});

const isDisabled = computed(() => {
  switch (props.manageModal.type) {
    case "map":
      return !(upperCategory.value.id && upperCategory.value.value && upperCategory.value.description);
    case "add":
    case "insert": {
      const { description, value } = upperCategory.value;
      return !(krName.value && enName.value && ((value && description) || (!value && !description)));
    }
    case "update":
      return !isEdited.value;
    default:
      return true;
  }
});

const isSimilarAttrDisabled = computed(() => {
  return similarAttrTotal.value >= SIMILAR_ATTR_MAX || !saForm.value.name.trim();
});

// ── Watchers ──────────────────────────────────────────────────────────────────
watch(
  () => props.manageModal,
  (newVal, oldVal) => {
    if (newVal.toggle && !oldVal.toggle) {
      // 모달 열릴 때 탭·폼 초기화
      activeTab.value = "category";
      resetSaForm();

      const target = ObjectUtils.getPropertyValue(newVal, "target", null);
      if (newVal.type === "update") {
        const description = ObjectUtils.getPropertyValue(newVal, "target/description", "");
        const name = ObjectUtils.getPropertyValue(newVal, "target/name", "");
        const localCategoryImageFileId = ObjectUtils.getPropertyValue(newVal, "target/file_id", "");
        const ordNo = ObjectUtils.getPropertyValue(newVal, "target/ord_no", 0);

        categoryOrdNo.value = ordNo;
        krName.value = description;
        originalKrName.value = description;
        enName.value = name;
        categoryImageFileId.value = localCategoryImageFileId;
        tempCategoryImageFileId.value = localCategoryImageFileId;

        if (location.hostname === "localhost" && localCategoryImageFileId !== "") {
          categoryImageFile.value = "http://localhost:38080/api/ckeditor/attachFile/" + localCategoryImageFileId;
        } else if (localCategoryImageFileId !== "") {
          categoryImageFile.value = "/api/ckeditor/attachFile/" + localCategoryImageFileId;
        }

        const matched = categoryStore._categoryMap[target.id];
        if (matched && matched.superTypes.length > 0) {
          originalUpperCategory.value = matched.superTypes[0];
          const superItem = categoryStore._categoryMap[matched.superTypes[0]];
          $ofetch(`/api/metadata/classification/depth?name=${superItem.name}`).then((res) => {
            if (superItem) {
              upperCategory.value = {
                description: superItem.description,
                value: superItem.name,
                level: parseInt(res),
                parent_id: superItem.parent_id,
                id: superItem.id
              };
            }
          });
        }

        const dept = ObjectUtils.getPropertyValue(newVal, "target/dept", null);
        if (dept) selectedDept.value = dept;

      } else if (newVal.type === "insert") {
        originalUpperCategory.value = target.name;
        const superItem = categoryStore.getOriginalList.find((el) => el.name === target.name);
        $ofetch(`/api/metadata/classification/depth?name=${superItem.name}`).then((res) => {
          if (superItem) {
            upperCategory.value = {
              description: superItem.description,
              value: superItem.name,
              level: parseInt(res),
              parent_id: superItem.parent_id,
              id: superItem.id
            };
          }
        });
      }
    }
  }
);

watch(() => selectedDept.value, (newVal) => {
  if (props.manageModal.type === "update") {
    isEdited.value = newVal !== originalDeptCd.value;
  }
});

watch(() => krName.value, (newVal) => {
  const koreanOnly = /^[ㄱ-ㅎ가-힣a-zA-Z0-9]*$/;
  if (!koreanOnly.test(newVal)) {
    krName.value = newVal.replace(/[^ㄱ-ㅎ가-힣a-zA-Z0-9]/g, "");
  }
  if (props.manageModal.type === "update") {
    isEdited.value = newVal !== originalKrName.value;
  }
});

watch(() => enName.value, (newVal) => {
  enName.value = newVal.replaceAll(/[^a-zA-Z0-9]/gi, "").toUpperCase();
});

watch(() => categoryImageFileId.value, (newVal) => {
  isEdited.value = newVal !== tempCategoryImageFileId.value;
});

// ── 기존 카테고리 메서드 ────────────────────────────────────────────────────────
const onInput = (e) => {
  const { target: { value } } = e;
  enName.value = value.replaceAll(/[^a-zA-Z0-9]/gi, "").toUpperCase();
};

const onCancel = () => {
  onClear();
  emit("onCloseModal");
};

const onClose = () => {
  onClear();
  emit("onCloseModal");
};

const createMappingParams = () => {
  const categoryMap = categoryStore._categoryMap;
  let entityIds = [];
  let superTypes = [];

  if (props.manageModal.isTotalChecked) {
    entityIds = props.manageModal.totalMetaIds;
  } else if (Array.isArray(props.manageModal.target)) {
    entityIds = props.manageModal.target.map((el) => el.meta_id);
  } else {
    entityIds = [props.manageModal.target.meta_id];
  }

  const mappingItem = categoryMap[upperCategory.value.id];
  if (!isEmpties(mappingItem.superTypes)) {
    superTypes = mappingItem.superTypes;
  }
  return { entityIds, superTypes };
};

const onConfirm = async () => {
  let params = {};

  if (props.manageModal.type === "map") {
    if (upperCategory.value.id) {
      const { entityIds, superTypes } = createMappingParams();
      params = { entityIds, classificationId: upperCategory.value.id, superTypes };
    }
  } else {
    let level = 1;
    let superTypeName = "";
    let superTypeId = "";
    if (upperCategory.value?.value) {
      const superType = categoryStore.getOriginalList.find((el) => el.name === upperCategory.value.value);
      isEdited.value = false;
      if (superType) {
        level = 1;
        superTypeName = superType.name;
        superTypeId = superType.id;
      }
    }
    params = {
      level: `${level}`,
      name: enName.value,
      description: krName.value,
      ord_no: categoryOrdNo.value,
      superTypeName,
      superTypeId: isEmpties(superTypeId) ? null : superTypeId,
      categoryImageFileId: categoryImageFileId.value !== "" ? categoryImageFileId.value : null
    };
    if (selectedDept.value.dept_cd) params.deptCd = selectedDept.value.dept_cd;
  }

  emit("onSubmit", params);
};

const onUpdate = async () => {
  const target: any = ObjectUtils.getPropertyValue(props.manageModal, "target", null);
  let params: any = {
    id: target.id,
    level: target.level,
    description: krName.value,
    ord_no: categoryOrdNo.value,
    categoryImageFileId: categoryImageFileId.value !== "" ? categoryImageFileId.value : null
  };
  if (selectedDept.value.dept_cd) params.deptCd = selectedDept.value.dept_cd;
  emit("onSubmit", params);
};

const onClear = () => {
  krName.value = "";
  originalKrName.value = "";
  enName.value = "";
  upperCategory.value = { description: "", value: "", level: "" };
  categoryOrdNo.value = 0;
  originalUpperCategory.value = "";
  selectedDept.value = {};
  originalDeptCd.value = "";
  isEdited.value = false;
  categoryImageFile.value = "";
  resetSaForm();
};

const fileUpload = (e) => {
  if (!e.target.files[0]) return false;

  const fileName = e.target.files[0].name.substring(e.target.files[0].name.lastIndexOf(".") + 1);
  if (!/jpg|jpeg|png/i.test(fileName)) {
    errorAlert("JPG, JPEG, PNG 파일만 업로드 할 수 있습니다.");
    return false;
  }

  const formData = new FormData();
  formData.append("files", e.target.files[0]);
  $ofetch("/api/service/file/fileUpload.do", { method: "POST", body: formData }).then((res) => {
    categoryImageFileId.value = res.data[0].file_id;
    const base = location.hostname === "localhost" ? "http://localhost:38080" : "";
    categoryImageFile.value = `${base}/api/ckeditor/attachFile/${res.data[0].file_id}`;
  });
};

const onSelectCategory = (info) => {
  if (info) {
    upperCategory.value = {
      description: info.description,
      value: info.value,
      level: info.level,
      parent_id: info.parent_id,
      id: info.id
    };
  }
};

const openSelectCategoryModal = () => {
  toggleSelectCategoryModal.value = !toggleSelectCategoryModal.value;
};

const onClickCategoryInfoSearchCloseButton = () => {
  try {
    toggleSelectCategoryModal.value = !toggleSelectCategoryModal.value;
  } catch (error) {
    console.log(error);
  }
};

// ── 유사속성 카테고리 추가 메서드 ──────────────────────────────────────────────
const resetSaForm = () => {
  saForm.value = { name: "", description: "", ord_no: null };
  saKeywords.value = [];
  saKeywordInput.value = "";
  similarAttrTotal.value = 0;
};

const onSaKeywordKeydown = (e: KeyboardEvent) => {
  if ((e.key === " " || e.key === "Enter") && saKeywordInput.value.trim()) {
    e.preventDefault();
    const word = saKeywordInput.value.trim();
    if (!saKeywords.value.includes(word)) saKeywords.value.push(word);
    saKeywordInput.value = "";
  } else if (e.key === "Backspace" && !saKeywordInput.value && saKeywords.value.length > 0) {
    saKeywords.value.pop();
  }
};

const removeSaKeyword = (index: number) => {
  saKeywords.value.splice(index, 1);
};

const onClickSimilarAttrTab = async () => {
  activeTab.value = "similarAttr";
  try {
    const res = await $ofetch("/api/metadata/similar-attr-category/list", {
      method: "POST",
      body: { limit: 1, offset: 0 }
    });
    similarAttrTotal.value = res?.data?.total ?? 0;
  } catch {
    similarAttrTotal.value = 0;
  }
};

const onConfirmSimilarAttr = () => {
  const name = saForm.value.name.trim();
  if (!name || similarAttrTotal.value >= SIMILAR_ATTR_MAX) return;
  // 입력 중인 키워드가 있으면 마저 추가
  const pending = saKeywordInput.value.trim();
  if (pending && !saKeywords.value.includes(pending)) saKeywords.value.push(pending);
  saKeywordInput.value = "";
  emit("onSubmitSimilarAttr", {
    name,
    description: saForm.value.description,
    keyword: saKeywords.value.join(" "),
    ord_no: saForm.value.ord_no,
    use_yn: "Y"
  });
};

defineExpose({ onClose });
</script>

<style scoped>
.tab-header {
  display: flex;
  border-bottom: 2px solid #dde1e7;
  padding: 0 20px;
}
.tab-btn {
  padding: 10px 20px;
  background: none;
  border: none;
  border-bottom: 2px solid transparent;
  margin-bottom: -2px;
  font-size: 14px;
  color: #6b7280;
  cursor: pointer;
}
.tab-btn.active {
  color: #0d2254;
  border-bottom-color: #0d2254;
  font-weight: 600;
}
.limit-notice {
  padding: 24px;
  text-align: center;
  color: #6b7280;
  font-size: 13px;
}
.tag-input-wrap {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  padding: 4px 6px;
  border: 1px solid #ccc;
  border-radius: 4px;
  min-height: 36px;
  align-items: center;
  background: #fff;
  cursor: text;
}
.tag-input-wrap input {
  border: none;
  outline: none;
  flex: 1;
  min-width: 120px;
  font-size: 13px;
  padding: 2px 4px;
  background: transparent;
}
.tag-item {
  display: inline-flex;
  align-items: center;
  background: #e8edf5;
  color: #0d2254;
  border-radius: 3px;
  padding: 2px 6px;
  font-size: 12px;
  white-space: nowrap;
}
.tag-item button {
  background: none;
  border: none;
  cursor: pointer;
  margin-left: 4px;
  color: #6b7280;
  font-size: 14px;
  line-height: 1;
  padding: 0;
}
.tag-item button:hover {
  color: #374151;
}
</style>
