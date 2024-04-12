import { ModalProps, Position } from "./ModalProps";
import { ComputedRef, Ref } from "vue";
import { useNuxtApp } from "nuxt/app";
import _ from "lodash";

interface ModalComposition extends ModalProps {
  position: Ref<Position>;

  dynamicModalClass(): ComputedRef<String>;

  dynamicModalPosition(): ComputedRef<String>;
}

export function ModalComposition(props: ModalProps): ModalComposition {
  const { $vfm } = useNuxtApp();

  const dynamicModalClass: ComputedRef<String> = computed(() => {
    if (_.isNil(props.modalPosition)) {
      return "modal-fixed";
    } else {
      return "";
    }
  });

  const dynamicModalPosition: ComputedRef<String> = computed(() => {
    if (props.modalPosition !== undefined) {
      const { top, left } = props.modalPosition;
      return `modal; position: absolute; top: ${top};left: ${left}; `;
    } else {
      return "";
    }
  });
  const closeModal = (modalId: string | number | symbol): void => {
    $vfm.close(modalId);
  };

  return { ...props, dynamicModalClass, dynamicModalPosition, closeModal };
}
