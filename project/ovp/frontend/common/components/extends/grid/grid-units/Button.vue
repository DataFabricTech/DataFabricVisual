<template>
  <div>
    <button @click="handleClick">
      {{ params.buttonText }}
    </button>
  </div>
</template>

<script setup>
const props = defineProps();
props.params = undefined;
const params = props.params;

const handleClick = () => {
  if (params.onClickRenderer) {
    params.onClickRenderer(params.value);
  }
};
</script>
