export const enum NotificationType {
  normal = "normal",
  info = "info",
  success = "success",
  warning = "warning",
  error = "error",
  confirm = "confirm"
}

export interface INotificationProp {
  theme: NotificationType;
  useClose: boolean;
  link: string;
  message: string;
}
