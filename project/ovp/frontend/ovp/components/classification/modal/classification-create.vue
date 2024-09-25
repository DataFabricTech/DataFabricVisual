<template>
  <Modal
    title="분류 추가"
    :modal-id="props.modalId"
    :height="500"
    :width="480"
    :esc-to-close="true"
    :btn-msg="'저장'"
    @close="closeModal"
    @cancel="closeModal"
    @confirm="validateForm"
    @before-open="initForm"
  >
    <template v-slot:body>
      <div class="form form-lg">
        <div class="form-body">
          <div class="form-item">
            <label for="data-model-save-name" class="form-label">
              이름
              <span class="required">*</span>
            </label>
            <div class="form-detail">
              <input
                id="data-model-save-name"
                class="text-input text-input-lg"
                placeholder="이름을 입력하세요."
                v-model="classificationForm.name"
              />
              <div
                class="notification notification-sm notification-error"
                v-show="isShowNameNoti"
              >
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">{{ nameErrorMsg }}</p>
              </div>
            </div>
          </div>
          <div class="form-item">
            <label class="form-label" for="data-model-save-description">
              설명
              <span class="required">*</span>
            </label>
            <div class="form-detail">
              <textarea
                id="data-model-save-description"
                class="textarea h-28"
                placeholder="설명을 입력하세요."
                v-model="classificationForm.description"
              ></textarea>
              <div
                class="notification notification-sm notification-error"
                v-show="isShowDescNoti"
              >
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">{{ descriptionErrorMsg }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>
  </Modal>
</template>

<script setup lang="ts">
import Modal from "@extends/modal/Modal.vue";
import { classificationStore } from "@/store/classification/index";
import type { Ref } from "vue";

const useClassificationStore = classificationStore();
const { addClassification, getClassificationList, getClassificationDetail } =
  useClassificationStore;

interface FormState {
  name: string;
  description: string;
}

const props = defineProps({
  modalId: {
    type: String,
    required: true,
  },
});

const emit = defineEmits<{
  (e: "close"): void;
}>();

const initialFormState: FormState = {
  name: "",
  description: "",
};
const classificationForm = reactive<FormState>({ ...initialFormState });

let nameErrorMsg: Ref<string> = ref("");
const descriptionErrorMsg = "설명을 입력하세요.";

const isShowNameNoti: Ref<boolean> = ref(false);
const isShowDescNoti: Ref<boolean> = ref(false);

function validateForm(): void {
  isShowDescNoti.value = false;
  if (!classificationForm.description) {
    isShowDescNoti.value = true;
  }
  nameErrorMsg.value = "";

  if (!classificationForm.name) {
    // 이름의 값이 없을 경우,
    nameErrorMsg.value = "이름을 입력하세요.";
    isShowNameNoti.value = true;
    return;
  }
  nameErrorMsg.value = "";
  isShowNameNoti.value = false;
  if (!classificationForm.description) {
    // 설명의 값이 없을 경우,
    isShowDescNoti.value = true;
    return;
  }
  isShowDescNoti.value = false;

  // 저장 API 호출
  saveClassification().then(async (response: { errorMessage: string; result: number }) => {
    if (response.errorMessage === "Duplicate classification name") {
      nameErrorMsg.value = "이름이 중복되었습니다.";
      isShowNameNoti.value = true;
      return;
    } else if (response.result === 1) {
      // 분류항목 리스트 재호출
      getClassificationList();
      await getClassificationDetail(response.data.id);
      // 성공시 모달 닫기
      closeModal();
    }
  });
}

// 모달창 열기전에 실행되는 함수
function initForm(): void {
  nameErrorMsg.value = ""; // 이름 에러메시지 "" 초기화
  isShowNameNoti.value = false;
  isShowDescNoti.value = false;
}

function saveClassification(): Promise<{
  errorMessage: string;
  result: number;
}> {
  // 추가할 내용 저장(name, description)
  const addData = {
    name: classificationForm.name,
    description: classificationForm.description,
  };
  // 추가 API 호출
  return addClassification(addData);
}

function closeModal(): void {
  resetForm();
  emit("close");
}

function resetForm(): void {
  Object.assign(classificationForm, { ...initialFormState });
}
</script>

<style lang="scss" scoped></style>
