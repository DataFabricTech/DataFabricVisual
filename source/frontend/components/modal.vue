<template>
  <vue-final-modal
    class="flex justify-center items-center"
    content-class="flex flex-col p-4 bg-white"
    @update:model-value="(val) => emit('update:modelValue', val)"
  >
    <div class="modal__title">
      <h1 v-if="title" class="text-2xl">
        {{ title }}
      </h1>
    </div>
    <div class="modal__content">
      <p v-if="contents">
        {{ contents }}
      </p>
      <!-- 샘플코드 -->


      <p>
        Vue Final Modal is a renderless, stackable, detachable and lightweight
        modal component.
      </p>
    </div>
    <div class="modal__footer">
      <button @click="emit('update:modelValue', false)">취소</button>
    </div>
  </vue-final-modal>
</template>

<script setup lang="ts">
import {VueFinalModal} from "vue-final-modal";

defineProps<{
  title?: string;
  contents?: string;
}>();

const emit = defineEmits<{
  (e: "update:modelValue", modelValue: boolean): void;
}>();
</script>
