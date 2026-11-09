import { useModal } from "vue-final-modal";
import alertDefault from "./alert-default-component.vue";
import { $R, isEmpties } from "@/composables/ramda";

interface AlertType {
  title: string;
  iconTheme: string;
  confirmButtonTheme: string;
  confirmButtonText: string;
  cancelButtonTheme: string;
  cancelButtonText: string;
  type: string;
}

const ALERT_TYPE = {
  DEFAULT: <AlertType>{
    title: "",
    iconTheme: "info",
    confirmButtonTheme: "primary",
    confirmButtonText: "확인",
    cancelButtonTheme: "",
    cancelButtonText: "취소",
    type: "default",
  },
  CONFIRM: <AlertType>{
    title: "",
    iconTheme: "info",
    confirmButtonTheme: "primary",
    confirmButtonText: "확인",
    cancelButtonTheme: "",
    cancelButtonText: "취소",
    type: "confirm",
  },
  SUCCESS: <AlertType>{
    title: "[성공]",
    iconTheme: "success",
    confirmButtonTheme: "primary",
    confirmButtonText: "확인",
    cancelButtonTheme: "",
    cancelButtonText: "취소",
    type: "default",
  },
  ERROR: <AlertType>{
    title: "[오류]",
    iconTheme: "error",
    confirmButtonTheme: "primary",
    confirmButtonText: "확인",
    cancelButtonTheme: "",
    cancelButtonText: "취소",
    type: "default",
  }
};


const getAttr = (alertType: AlertType = ALERT_TYPE.DEFAULT) => ({
  ...alertType,
  content: "default 메세지입니다.",
  showCloseIcon: false,
  width: "400px",
  height: "auto",
  style: {},
  // vue-final-modal props
  // <참고 페이지>
  // - https://vue-final-modal.org/api/components/vue-final-modal#props
  // - https://vue-final-modal.org/use-cases/playground
  // 아래 명시된 것 이외에의 option을 추가하려면 alert-default.vue에도 추가해주어야함
  options: {
    clickToClose: false,
    escToClose: false
  }
});

// default alert에서
// case 1. attr이 string인 경우 content 값으로 적용
// case 2. attr이 object인 경우 각각의 attribute 값으로 적용
export const defaultAlert = (
  attr: any,
  event: {
    confirm?: Function;
    close?: Function;
    cancel?: Function;
  } = {}) => {
  const defaultAttr = getAttr(ALERT_TYPE.DEFAULT);
  let _attr = defaultAttr;
  if ($R.is(Object, attr)) {
    _attr = $R.mergeLeft(attr, defaultAttr);
    if (!isEmpties(attr.options)) {
      _attr.options = $R.mergeLeft(attr.options, defaultAttr.options);
    }
  }

  if ($R.is(String, attr)) _attr = $R.mergeLeft({ content: attr }, defaultAttr);

  return new Promise((resolve) => {
    let result = false;

    const { open, close } = useModal({
      component: alertDefault,
      attrs: {
        ..._attr,
        onConfirm: async () => {
          result = true;
          if (!isEmpties(event.confirm)) await event.confirm();
          await close();
          resolve(result);
        },
        onClose: async () => {
          if (!isEmpties(event.close)) await event.close();
          await close();
          resolve(result);
        },
        onCancel: async () => {
          if (!isEmpties(event.cancel)) await event.cancel();
          await close();
          resolve(result);
        }
      }
    });
    open();
  });
};

export const confirmAlert = (
  param = {},
  event: {
    confirm?: Function;
    cancel?: Function;
  } = {}) => {
  const attr = getTypeAttr(ALERT_TYPE.CONFIRM, param);
  return defaultAlert(attr, event);
};

export const successAlert = (
  param = {},
  event: {
    confirm?: Function;
  } = {}) => {
  const attr = getTypeAttr(ALERT_TYPE.SUCCESS, param);
  return defaultAlert(attr, event);
};

export const errorAlert = (
  param = {},
  event: {
    confirm?: Function;
  } = {}) => {
  const attr = getTypeAttr(ALERT_TYPE.ERROR, param);
  return defaultAlert(attr, event);
};

function getTypeAttr(type: AlertType, param: object) {
  let typeAttr = getAttr(type);
  if ($R.is(Object, param)) {
    typeAttr = $R.mergeLeft(param, typeAttr);
  }
  if ($R.is(String, param)) {
    typeAttr.content = param;
  }
  return typeAttr;
}
