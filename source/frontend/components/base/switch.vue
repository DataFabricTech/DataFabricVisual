<template>
<!-- .switch에서 props.id값 전달 필요  -->
  <div class="switch">
    <span class="switch-text">
      <slot></slot>
    </span>
    <label class="switch-switch" :for="props.id" :disabled="props.disabled">
      <input class="switch-input" :id="props.id" type="checkbox">
      <span class="switch-handle"></span>
      <span class="hidden">
        <slot></slot>
      </span>
    </label>
  </div>
</template>

<script setup lang="ts">

const props = defineProps({
  id: { type: String, default: null},
  disabled: { type: Boolean, default: false }
});

</script>
