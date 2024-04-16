import { ModalProps } from "./ModalProps";
import { ComputedRef, onMounted, onUnmounted } from "vue";
import { useNuxtApp } from "nuxt/app";
import _ from "lodash";

interface ModalComposition extends ModalProps {
  dynamicModalClass(): ComputedRef<String>;

  dynamicModalPosition(): ComputedRef<String>;

  closeModal(modalId: string | number | symbol): void;
}

export function ModalComposition(props: ModalProps): ModalComposition {
  const { $vfm } = useNuxtApp();

  const dynamicModalClass: ComputedRef<String> = computed(() => {
    if (_.isNil(props.top) && _.isNil(props.left)) {
      return "modal-fixed";
    } else {
      return "";
    }
  });

  const dynamicModalPosition: ComputedRef<String> = computed(() => {
    if (props.top !== undefined || props.left !== undefined) {
      return `modal; position: absolute; top: ${modalPosition.top}px; left: ${modalPosition.left}px `;
    } else {
      return "";
    }
  });
  const closeModal = (modalId: string | number | symbol): void => {
    $vfm.close(modalId);
  };

  const modalPosition = reactive<{ top: number | null; left: number | null }>({ top: null, left: null });

  function getDocumentSize() {
    let documentSize = {
      width: window.innerWidth || document.body.clientWidth,
      height: window.innerHeight || document.body.clientHeight
    };
    let maxHeight = documentSize.height;
    let maxWidth = documentSize.width;
    modalPosition.top = props.top ?? (documentSize.height! - props.height) / 2;
    modalPosition.left = props.left ?? (documentSize.width! - props.width) / 2;
    if (props.top) {
      maxHeight = documentSize.height! - props.height;
      if (props.top > maxHeight) {
        modalPosition.top = maxHeight;
      } else if (props.top < 0) {
        modalPosition.top = 0;
      } else {
        modalPosition.top = props.top;
      }
    }
    if (props.left) {
      maxWidth = documentSize.width! - props.width;
      if (props.left < maxWidth) {
        modalPosition.left = maxWidth;
      } else if (props.left < 0) {
        modalPosition.left = 0;
      } else {
        modalPosition.left = props.left;
      }
    }
  }

  onMounted(() => {
    getDocumentSize();
    window.addEventListener("resize", getDocumentSize);
  });

  onUnmounted(() => {
    window.removeEventListener("resize", getDocumentSize);
  });

  return { ...props, dynamicModalClass, dynamicModalPosition, closeModal };
}
