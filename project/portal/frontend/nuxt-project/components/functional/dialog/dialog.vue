<template>
  <vue-final-modal class="vue-final-modal" :modal-id="name" @opend="onOpen" :click-to-close="false"
    :esc-to-close="false">
    <div class="modal" tabindex="0" role="dialog" aria-modal="true" aria-labelledby="modalTitle"
      aria-describedby="modalDescription" :style="{ width, height }">
      <div class="modal__head">
        <div class="modal__head-title">
          <span id="modalTitle" class="modal__head-title-text">
            {{ title }}
          </span>
        </div>
      </div>

      <div id="modalDescription" name="body" class="modal__body">
        <slot />
      </div>
      <div name="foot" class="modal__foot">
        <div class="modal__foot-main-buttons">
          <button v-if="viewDeleteButton" class="button button--negative button--lg" @click="onDelete">
            <span class="button__text"> {{ deleteButtonText }} </span>
          </button>
          <button class="button button--lg" @click="onClose" aria-label="Close modal">
            <span class="button__text"> {{ cancelButtonText }} </span>
          </button>
          <button v-if="viewConfirmButton" :disabled="confirmButtonDisabled" class="button button--primary button--lg"
            @click="onConfirm">
            <span class="button__text"> {{ confirmButtonText }} </span>
          </button>
        </div>
      </div>
    </div>
  </vue-final-modal>
</template>

<script setup lang="ts">
import { useVfm, VueFinalModal } from "vue-final-modal";
import { confirmAlert } from "@/components/functional/alert/alert-default";

const vfm = useVfm();

const props = defineProps({
  title: {
    type: String
  },
  name: {
    type: String,
    required: true
  },
  height: {
    type: String,
    default: "300px"
  },
  width: {
    type: String,
    default: "400px"
  },
  viewDeleteButton: {
    type: Boolean,
    default: false
  },
  deleteButtonText: {
    type: String,
    default: "삭제"
  },
  cancelButtonText: {
    type: String,
    default: "취소"
  },
  confirmButtonText: {
    type: String,
    default: "확인"
  },
  confirmButtonDisabled: {
    type: Boolean,
    default: false
  },
  viewConfirmButton: {
    type: Boolean,
    default: true
  },
  closeCheck: {
    type: Boolean,
    default: false
  },
  closeCheckText: {
    type: String,
    default: "창을 닫으시겠습니까?"
  }
});

const emit = defineEmits<{
  (e: "open"): void;
  (e: "close"): void;
  (e: "confirm"): void;
  (e: "delete"): void;
}>();

function onOpen() {
  scrollHandler();
  emit("open");
}

function onClose() {
  scrollHandler(false);
  if (props.closeCheck) {
    confirmAlert(props.closeCheckText, {
      confirm: () => {
        _onClose();
      }
    });
  } else {
    _onClose();
  }
}

function _onClose() {
  scrollHandler(false);
  emit("close");
  vfm.close(props.name);
}

function onConfirm() {
  scrollHandler(false);
  emit("confirm");
}

function onDelete() {
  scrollHandler();
  emit("delete");
}

function scrollHandler(open = true) {
  const className = "not-scroll";
  let $html = document.querySelector("html");
  if ($html) {
    if (open) $html.classList.add(className);
    else $html.classList.remove(className);
  }
  let $body = document.querySelector("body");
  if ($body) {
    if (open) $body.classList.add(className);
    else $body.classList.remove(className);
  }
}
</script>
