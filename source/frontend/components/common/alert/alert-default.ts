import { useModal } from "vue-final-modal";
import AlertDefault from "./alert-default.vue";

export interface IAlertProp {
  type: string;
  message: string;
}

export const enum AlertType {
  normal = "normal",
  info = "info",
  success = "success",
  warning = "warning",
  error = "error",
  confirm = "confirm"
}

export const defaultAlert = (type: AlertType, message: string): Promise<any> => {
  return new Promise((resolve) => {
    const { open } = useModal({
      component: AlertDefault,
      attrs: {
        type: type,
        message: message,
        onConfirm(value: any) {
          resolve(value);
        }
      } as IAlertProp
    });
    return open();
  });
};

export const infoAlert = (message: string): Promise<any> => {
  return defaultAlert(AlertType.info, message);
};

export const confirmAlert = (message: string): Promise<any> => {
  return defaultAlert(AlertType.confirm, message);
};

export const successAlert = (message: string): Promise<any> => {
  return defaultAlert(AlertType.success, message);
};

export const errorAlert = (message: string): Promise<any> => {
  return defaultAlert(AlertType.error, message);
};
