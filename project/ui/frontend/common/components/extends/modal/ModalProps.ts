export interface ModalProps {
  modalId: string;
  modalClass: string;
  displayDirective: "if" | "show" | "visible";
  hideOverlay: boolean;
  overlayTransition: "vfm-fade" | "vfm-slide-down" | "vfm-slide-up" | "vfm-slide-right" | "vfm-slide-left";
  contentTransition: "vfm-fade" | "vfm-slide-down" | "vfm-slide-up" | "vfm-slide-right" | "vfm-slide-left";
  overlayClass: object | any[] | string;
  contentClass: object | any[] | string;
  clickToClose: boolean;
  escToClose: boolean;
  background: "interactive" | "non-interactive";
  lockScroll: boolean;
  swipeToClose: "none" | "up" | "right" | "down" | "left";
  dragAndResize: boolean;
  modalElementPosition: Position;
}

export interface Position {
  left: number;
  top: number;
  width: number;
  height: number;
}
