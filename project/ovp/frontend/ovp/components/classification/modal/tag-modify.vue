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
// import { classificationStore } from "@/store/classification/index";
// const useClassificationStore = classificationStore();
// const { formInfo } = storeToRefs(useClassificationStore);
// const { getClassificationTags, editClassificationTag } = useClassificationStore; // TODO : 태그 수정하는 API 가진 함수 호출 할 곳!

const { $vfm } = useNuxtApp();

const emit = defineEmits(["close-modal"]);

const closeModalTag = () => {
  emit("close-modal");
};

interface TagFormState {
  name: string;
  description: string;
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

watchEffect(() => {
  tagFormState.value.name = props.formInfo.name || "";
  tagFormState.value.description = props.formInfo.description || "";
});

let nameErrorMsg: Ref<string> = ref("");
const descriptionErrorMsg = "설명을 입력하세요.";

const isShowNameNoti: Ref<boolean> = ref(false);
const isShowDescNoti: Ref<boolean> = ref(false);

// function validateForm(): void {
//   console.log("validate 실행");
// }
//
// function validateForm(): void {
//   isShowDescNoti.value = false;
//   if (!tagFormState.description) {
//     isShowDescNoti.value = true;
//   }
//   nameErrorMsg.value = "";
//   if (!tagFormState.name) {
//     nameErrorMsg.value = "이름을 입력하세요.";
//     isShowNameNoti.value = true;
//     return;
//   }
//   nameErrorMsg.value = "";
//   isShowNameNoti.value = false;
//   if (!tagFormState.description) {
//     isShowDescNoti.value = true;
//     return;
//   }
//   isShowDescNoti.value = false;
//
//   // saveTag().then((response: any) => {
//   //   if (response.errorMessage === "Duplicate tag name") {
//   //     nameErrorMsg.value = "이름이 중복되었습니다.";
//   //     isShowNameNoti.value = true;
//   //     return;
//   //   } else if (response.result === 1) {
//   //     getClassificationTags(); // 태그 리스트 조회 재호출
//   //     closeModal();
//   //   }
//   // });
//   saveTag();
// }
//
// function saveTag() {
//   const editData = {
//     name: tagFormState.name,
//     description: tagFormState.description,
//   };
//   console.log(
//     "수정된 에디트 데이터 : 추후 patch형식으로 바꿔야함 ===> ",
//     editData,
//   );
//   return editClassificationTag(editData);
// }

function closeModal(): void {
  resetForm();
  $vfm.close(props.modalId);
}

function resetForm(): void {
  // tagFormState.name = "";
  // tagFormState.description = "";
}
</script>

<style lang="scss" scoped></style>
