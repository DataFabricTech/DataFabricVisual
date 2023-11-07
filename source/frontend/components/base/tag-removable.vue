<template>
  <div class="tag-removable" :disabled="disable">
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
let disable = ref('false');
function closeTag() {
  disable.value = 'true';
  console.log("show값 ? ", disable);
  emit('close')
}

</script>

