<template>
  <Modal
    title="태그 추가"
    :modal-id="props.modalId"
    :width="480"
    :height="400"
    :top="380"
    :esc-to-close="true"
    :btn-msg="'저장'"
    @close="closeModal"
    @cancel="closeModal"
    @confirm="validateForm"
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
                v-model="classificationTagForm.name"
                maxlength="20"
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
                v-model="classificationTagForm.description"
              ></textarea>
              <div
                class="notification notification-sm notification-error"
                v-show="isShowDescNoti"
              >
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">설명을 입력하세요.</p>
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
import $constants from "~/utils/constant";

const useClassificationStore = classificationStore();
const { addClassificationTag, getClassificationTags } = useClassificationStore;

interface FormState {
  name: string;
  description: string;
  classification: string;
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
  classification: "",
};

const classificationTagForm = reactive<FormState>({ ...initialFormState });

let nameErrorMsg: Ref<string> = ref("");

const isShowNameNoti: Ref<boolean> = ref(false);
const isShowDescNoti: Ref<boolean> = ref(false);

function validateForm(): void {
  if (!classificationTagForm.name) {
    nameErrorMsg.value = $constants.GOVERNANCE.TITLE.EMPTY_ERROR_MSG;
    isShowNameNoti.value = true;
    return;
  }

  if (classificationTagForm.name.length === 1) {
    nameErrorMsg.value = $constants.GOVERNANCE.TITLE.MINIMUM_LENGTH_ERROR_MSG;
    isShowNameNoti.value = true;
    return;
  }

  if (!$constants.GOVERNANCE.TITLE.REGEX.test(classificationTagForm.name)) {
    nameErrorMsg.value = $constants.GOVERNANCE.TITLE.REGEX_ERROR_MSG;
    isShowNameNoti.value = true;
    return;
  }

  isShowNameNoti.value = false;

  if (!classificationTagForm.description) {
    isShowDescNoti.value = true;
    return;
  }

  isShowDescNoti.value = false;

  saveClassificationTag().then(
    (response: { errorMessage: string; result: number }) => {
      if (response.errorMessage === "Duplicate classification name") {
        nameErrorMsg.value = "이름이 중복되었습니다.";
        isShowNameNoti.value = true;
        return;
      } else if (response.result === 1) {
        getClassificationTags(); // 태그리스트조회 재호출
        // 성공시 모달 닫기
        closeModal();
      }
    },
  );
}

function saveClassificationTag(): Promise<{
  errorMessage: string;
  result: number;
}> {
  // 추가할 내용 저장(name, description)
  const addData = {
    name: classificationTagForm.name,
    description: classificationTagForm.description,
  };
  // 태그 추가 API호출
  return addClassificationTag(addData);
}

function closeModal(): void {
  resetForm();
  emit("close");
}

function resetForm(): void {
  Object.assign(classificationTagForm, { ...initialFormState });
}
</script>

<style lang="scss" scoped></style>
