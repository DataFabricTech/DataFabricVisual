<template>
  <Modal
    title="태그수정"
    :modal-id="props.modalId"
    :formInfo="props.formInfo"
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
                v-model="tagFormState.name"
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
                v-model="tagFormState.description"
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
import { type Ref, ref } from "vue";
import { classificationStore } from "@/store/classification/index";
const useClassificationStore = classificationStore();
const { getClassificationTags, editClassificationTag } = useClassificationStore;

const emit = defineEmits<{
  (e: "close"): void;
}>();

interface TagFormState {
  name: string;
  description: string;
}

interface JsonPatchOperation {
  op: string;
  path: string;
  value: any;
}

const props = defineProps({
  modalId: {
    type: String,
    required: true,
  },
  formInfo: {
    type: Object,
    required: true,
  },
});

const tagFormState: Ref<TagFormState> = ref({
  name: "",
  description: "",
});

// 기존 태그 정보값 저장
const defaultInfo: TagFormState = {
  name: "",
  description: "",
};

const patch: JsonPatchOperation[] = [];

watchEffect(() => {
  // 기존 정보 저장
  defaultInfo.name = props.formInfo.name;
  defaultInfo.description = props.formInfo.description;

  tagFormState.value.name = props.formInfo.name || "";
  tagFormState.value.description = props.formInfo.description || "";
});

let nameErrorMsg: Ref<string> = ref("");
const descriptionErrorMsg = "설명을 입력하세요.";

const isShowNameNoti: Ref<boolean> = ref(false);
const isShowDescNoti: Ref<boolean> = ref(false);

function validateForm(): void {
  isShowDescNoti.value = false;
  if (!tagFormState.value.description) {
    isShowDescNoti.value = true;
  }
  nameErrorMsg.value = "";
  // 이름 값이 없을 경우 return 시키기
  if (!tagFormState.value.name) {
    nameErrorMsg.value = "이름을 입력하세요.";
    isShowNameNoti.value = true;
    return;
  }
  nameErrorMsg.value = "";
  isShowNameNoti.value = false;
  // 설명 값이 없을 경우 return 시키기
  if (!tagFormState.value.description) {
    isShowDescNoti.value = true;
    return;
  }
  isShowDescNoti.value = false;

  // store에 있는 edit 호출하기
  saveTag(tagFormState.value.name, tagFormState.value.description).then(
    (response: any) => {
      if (response.errorMessage === "Duplicate tag name") {
        nameErrorMsg.value = "이름이 중복되었습니다.";
        isShowNameNoti.value = true;
        return;
      } else if (response.result === 1) {
        getClassificationTags(); // 태그 리스트 조회 재호출
        closeModal();
      }
    },
  );
}

function saveTag(name: any, description: any) {
  // patch 배열 초기화
  patch.length = 0;

  // 기존 name값과 수정하려는 name값이 다를 경우
  if (defaultInfo.name !== name) {
    patch.push({ op: "replace", path: "/name", value: name });
  }

  // 기존 description값과 수정하려는 description값이 다를 경우
  if (defaultInfo.description !== description) {
    patch.push({ op: "replace", path: "/description", value: description });
  }
  // patch 배열이 비어있지 않은 경우에만 서버에 요청
  if (patch.length > 0) {
    const editData = {
      param: patch,
    };
    return editClassificationTag(editData, props.formInfo.id);
  }
}

function closeModal(): void {
  emit("close");
}
</script>

<style lang="scss" scoped></style>
