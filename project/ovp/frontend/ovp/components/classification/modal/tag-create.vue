<template>
  <Modal
    title="태그 추가"
    :modal-id="props.modalId"
    :height="500"
    :width="480"
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
              />
              <div class="notification notification-sm notification-error">
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">이름을 입력하세요.</p>
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
              <div class="notification notification-sm notification-error">
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
const { $vfm } = useNuxtApp();

interface FormState {
  name: string;
  description: string;
}

interface NotificationState {
  name: boolean;
  description: boolean;
}

const props = defineProps({
  modalId: {
    type: String,
    required: true,
  },
});
const initialFormState: FormState = {
  name: "",
  description: "",
};
const classificationTagForm = reactive<FormState>({ ...initialFormState });
const noneDataNoti = reactive<NotificationState>({
  name: false,
  description: false,
});

function validateForm(): void {
  const { name, description } = classificationTagForm;
  noneDataNoti.name = !name;
  noneDataNoti.description = !description;
  if (!(noneDataNoti.name || noneDataNoti.description)) {
    saveClassificationTag();
  }
}

async function saveClassificationTag(): Promise<void> {
  // 추가할 내용 저장(name, description)
  const addData = {
    name: classificationTagForm.name,
    description: classificationTagForm.description,
  };
  // TODO : 태그 추가 API호출
  closeModal();
}

function closeModal(): void {
  resetForm();
  $vfm.close(props.modalId);
}

function resetForm(): void {
  Object.assign(classificationTagForm, { ...initialFormState });
}
</script>

<style lang="scss" scoped></style>
