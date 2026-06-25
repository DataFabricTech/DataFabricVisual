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
    }
  }
);

const onClose = () => {
  emit("onCloseModal");
};

const onConfirm = () => {
  if (!form.value.name.trim()) return;
  emit("onSubmit", {
    ...form.value,
    name: form.value.name.trim(),
    use_yn: "Y"
  });
};

defineExpose({ onClose });
</script>

