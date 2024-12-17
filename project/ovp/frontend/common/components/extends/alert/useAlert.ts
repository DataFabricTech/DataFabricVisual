import { ref, Ref } from "vue";

// State 정의
const isModeless: Ref<boolean> = ref(false);
const isVisible: Ref<boolean> = ref(false);
const width: Ref<number> = ref(150);
const type: Ref<string> = ref("info");
const message: Ref<string> = ref("");
const confirmButtonText: Ref<string> = ref("확인");
const cancelButtonText: Ref<string> = ref("취소");
const isConfirm: Ref<boolean> = ref(false);
let resolve: ((value: boolean) => void) | null = null;

export function useAlert() {
  function showAlert(
    newMessage: string = "",
    modalType: string = "info",
    newConfirmButtonText: string = "확인",
    isModelessMode: boolean = true
  ) {
    type.value = modalType;
    isModeless.value = isModelessMode;
    isVisible.value = true;
    message.value = newMessage;
    confirmButtonText.value = newConfirmButtonText;
    isConfirm.value = false;

    return new Promise<boolean>((res) => {
      resolve = res;
    });
  }

  // confirm 표시 함수 (Promise 반환)
  function showConfirm(
    newMessage: string = "",
    modalType: string = "warning",
    newConfirmButtonText: string = "확인",
    newCancelButtonText: string = "취소",
    isModelessMode: boolean = true
  ): Promise<boolean> {
    isModeless.value = isModelessMode;
    isVisible.value = true;
    type.value = modalType;
    message.value = newMessage;
    confirmButtonText.value = newConfirmButtonText;
    cancelButtonText.value = newCancelButtonText;
    isConfirm.value = true;

    return new Promise<boolean>((res) => {
      resolve = res;
    });
  }

  // 확인 버튼 클릭 처리
  function handleConfirm() {
    isModeless.value = false;
    isVisible.value = false;
    if (resolve) {
      resolve(true); // 확인 버튼 클릭 시 true 반환
    }
  }

  // 취소 버튼 클릭 처리
  function handleCancel() {
    isModeless.value = false;
    isVisible.value = false;
    if (resolve) {
      resolve(false); // 취소 버튼 클릭 시 false 반환
    }
  }

  return {
    isModeless,
    isVisible,
    width,
    type,
    message,
    confirmButtonText,
    cancelButtonText,
    isConfirm,
    showAlert,
    showConfirm,
    handleConfirm,
    handleCancel
  };
}
