import { ModalProps, Position } from "@/components/extends/modal/ModalProps";
import { Ref } from "vue";

interface ModalComposition extends ModalProps {
  position: Ref<Position>;
}

export function ModalComposition(props: ModalProps): ModalComposition {
  const position: Position = {
    width: 0,
    height: 0,
    top: 0,
    left: 0
  };
  const dragResize = (newPosition: Position): void => {
    position.width = newPosition.width;
    position.height = newPosition.height;
    position.top = newPosition.top;
    position.left = newPosition.left;
  };
  onMounted(() => {
    if (props.modalElementPosition && props.dragAndResize) {
      Object.assign(position, props.modalElementPosition);
    }
  });
  return { ...props, position, dragResize };
}
