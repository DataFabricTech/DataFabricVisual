<template>
  <p v-html="params.fn(params.value)"></p>
</template>

<script setup>
const props = defineProps({
  params: {
    type: Object
  }
});
const params = props.params;
</script>
