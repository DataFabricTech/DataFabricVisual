export interface ModalProps {
  modalId: string | number | symbol;
  modalClass?: string;
  displayDirective?: "if" | "show" | "visible";
  hideOverlay?: boolean;
  overlayTransition?: "vfm-fade" | "vfm-slide-down" | "vfm-slide-up" | "vfm-slide-right" | "vfm-slide-left";
  contentTransition?: "vfm-fade" | "vfm-slide-down" | "vfm-slide-up" | "vfm-slide-right" | "vfm-slide-left";
  overlayClass?: object | any[] | string;
  clickToClose?: boolean;
  escToClose: boolean;
  background?: "interactive" | "non-interactive";
  lockScroll?: boolean;
  swipeToClose?: "none" | "up" | "right" | "down" | "left";
  top?: number;
  left?: number;
  width: number;
  height: number;
  title?: string;
  subTitle?: string;
  useCancelBtn?: boolean;
  btnMsg?: string;
}
