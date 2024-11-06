<template>
  <vue-final-modal
    class="vue-final-modal"
    :click-to-close="options.clickToClose"
    :esc-to-close="options.escToClose"
  >
    <div class="alert" :class="alertIconTheme" :style="{ ...style, width, height }">
      <div class="alert__body">
        <div v-if="showCloseIcon" class="alert__close-button">
          <button class="button button--icon" @click="onClose">
            <!-- <svgo-close class="svg-icon" /> -->
          </button>
        </div>
        <div class="alert__contents">
          <p class="alert__title">{{ title }}</p>
          <p class="alert__text" v-html="content" />
        </div>
      </div>
      <div class="alert__foot">
        <div class="alert__foot-main-buttons">
          <button
            class="button button--lg"
            v-if="type === 'confirm'"
            :class="alertCancelButtonTheme"
            @click.stop="onCancel"
          >
            {{ cancelButtonText }}
          </button>
          <button
            class="button button--lg"
            :class="alertConfirmButtonTheme"
            @click.stop="onConfirm"
          >
            {{ confirmButtonText }}
          </button>
        </div>
      </div>
    </div>
  </vue-final-modal>
</template>

<script setup lang="ts">
import { VueFinalModal } from "vue-final-modal";
import { computed } from "vue";

/** props **/
const props = defineProps({
  width: {
    type: String,
  },
  height: {
    type: String,
  },
  style: {
    type: Object,
  },
  options: {
    type: Object,
  },
  iconTheme: {
    type: String,
  },
  title: {
    type: String,
  },
  content: {
    type: String,
  },
  showCloseIcon: {
    type: Boolean,
  },
  type: {
    type: String,
  },
  confirmButtonTheme: {
    type: String,
  },
  confirmButtonText: {
    type: String,
  },
  cancelButtonTheme: {
    type: String,
  },
  cancelButtonText: {
    type: String,
  },
});

/** emit **/
const emit = defineEmits<{
  (e: "close"): void;
  (e: "cancel"): void;
  (e: "confirm"): void;
}>();

/** computed */
const alertIconTheme = computed(() => "alert--" + props.iconTheme);
const alertConfirmButtonTheme = computed(() => "button--" + props.confirmButtonTheme);
const alertCancelButtonTheme = computed(() => "button--" + props.cancelButtonTheme);

/** methods **/
function onCancel() {
  emit("cancel");
}

function onClose() {
  emit("close");
}

function onConfirm() {
  emit("confirm");
}
</script>
