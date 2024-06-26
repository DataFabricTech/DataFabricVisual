<template>
  <div>{{ params.fn(params.value) }}</div>
</template>

<script setup>
const props = defineProps({
  params: {
    type: Object
  }
});
const params = props.params;
</script>
