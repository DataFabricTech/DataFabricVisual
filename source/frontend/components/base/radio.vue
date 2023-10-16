<template>
  <div class="radio">
    <input class="radio-input" type="radio" :id="props.id" :name="props.name" :disabled="props.disabled" :checked="props.checked">
    <label :for="props.id" class="radio-label"><slot></slot></label>
  </div>
</template>

<script setup lang="ts">

const props = defineProps({
  id: { type: String, default: null},
  name: { type: String, default: null},
  disabled: { type: Boolean, default: false },
  checked: { type: Boolean, default: false },
});

</script>
