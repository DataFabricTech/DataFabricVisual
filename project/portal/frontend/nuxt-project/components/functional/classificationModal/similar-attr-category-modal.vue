<template>
  <div class="mu-modal" v-if="props.similarAttrModal.toggle">
    <div class="mu-dialog-background"></div>

    <div class="mu-dialog w600">
      <div class="mu-dialog-head">
        <p class="title">유사 카테고리 수정</p>
      </div>

      <div class="mu-dialog-body">
        <table class="mu-formbox">
          <caption>유사 카테고리 수정</caption>
          <colgroup>
            <col style="width: 130px" />
            <col />
          </colgroup>
          <tbody>
            <tr>
              <th scope="row">카테고리명<strong class="required">필수</strong></th>
              <td>
                <input type="text" class="mu-input" v-model="form.name"
                  placeholder="카테고리명 (최대 100자)" maxlength="100" />
              </td>
            </tr>
            <tr>
              <th scope="row">설명</th>
              <td>
                <input type="text" class="mu-input" v-model="form.description"
                  placeholder="설명 (최대 512자)" maxlength="512" />
              </td>
            </tr>
            <tr>
              <th scope="row">키워드</th>
              <td>
                <div class="tag-input-wrap">
                  <span v-for="(tag, i) in keywords" :key="i" class="tag-item">
                    {{ tag }}
                    <button type="button" @click="removeKeyword(i)">×</button>
                  </span>
                  <input type="text" v-model="keywordInput"
                    placeholder="입력 후 Enter 또는 Space"
                    @keydown="onKeywordKeydown" />
                </div>
              </td>
            </tr>
            <tr>
              <th scope="row">정렬 순서</th>
              <td>
                <input type="number" class="mu-input" v-model.number="form.ord_no"
                  placeholder="숫자 입력" style="width: 120px" />
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn" @click="onClose"><span>취소</span></button>
        <button type="button" class="mu-btn mu-btn-primary" @click="onConfirm"
          :disabled="!form.name.trim()">
          <span>저장</span>
        </button>
      </div>

      <div class="mu-dialog-close-btn">
        <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="onClose">
          <i class="mu-icon-svg close"></i><span>팝업닫기</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";

const emit = defineEmits(["onCloseModal", "onSubmit"]);
const props = defineProps(["similarAttrModal"]);

const form = ref({ id: "", name: "", description: "", ord_no: null as number | null });
const keywords = ref<string[]>([]);
const keywordInput = ref("");

// 모달이 열릴 때 target 데이터로 폼 초기화
watch(
  () => props.similarAttrModal,
  (newVal, oldVal) => {
    if (newVal.toggle && !oldVal?.toggle && newVal.target) {
      const t = newVal.target;
      form.value = {
        id: t.id ?? "",
        name: t.name ?? "",
        description: t.categoryDescription ?? "",
        ord_no: t.ord_no ?? null
      };
      keywords.value = t.keyword ? t.keyword.split(" ").filter((w: string) => w.trim()) : [];
      keywordInput.value = "";
    }
  }
);

const onKeywordKeydown = (e: KeyboardEvent) => {
  if ((e.key === " " || e.key === "Enter") && keywordInput.value.trim()) {
    e.preventDefault();
    const word = keywordInput.value.trim();
    if (!keywords.value.includes(word)) keywords.value.push(word);
    keywordInput.value = "";
  } else if (e.key === "Backspace" && !keywordInput.value && keywords.value.length > 0) {
    keywords.value.pop();
  }
};

const removeKeyword = (index: number) => {
  keywords.value.splice(index, 1);
};

const onClose = () => {
  emit("onCloseModal");
};

const onConfirm = () => {
  if (!form.value.name.trim()) return;
  const pending = keywordInput.value.trim();
  if (pending && !keywords.value.includes(pending)) keywords.value.push(pending);
  keywordInput.value = "";
  emit("onSubmit", {
    ...form.value,
    name: form.value.name.trim(),
    keyword: keywords.value.join(" "),
    use_yn: "Y"
  });
};

defineExpose({ onClose });
</script>

<style scoped>
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
