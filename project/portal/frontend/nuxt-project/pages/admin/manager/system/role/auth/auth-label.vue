<template>
  <div class="category">
    <em class="category-label" :title="props.auth">{{ props.auth }}</em>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits(["onClickTableBodyButtons"]);
const props = defineProps({
  item: {
    type: Object
  },
  auth: {
    type: String,
    default: ""
  },
  readOnly: {
    type: Boolean,
    default: false
  }
});
</script>
