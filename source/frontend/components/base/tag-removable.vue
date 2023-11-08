<template>
  <div class="tag-removable">
    <button class="tag">
      <slot></slot>
    </button>
    <button class="button button-icon button-sm">
      <svg-icon name="close" class="svg-icon" @click="closeTag"/>
    </button>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits(['close']);

const props = defineProps({
  indexValue: {
    type: Number
  }
})
function closeTag() {
  emit('close', props.indexValue);
}
</script>
