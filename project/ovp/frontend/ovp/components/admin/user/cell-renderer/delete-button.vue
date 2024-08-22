<template>
  <button
    class="button button-lg button-error-stroke"
    type="button"
    @click="handleClick"
  >
    <span class="button-title">삭제</span>
  </button>
</template>

<script setup>
const props = defineProps({
  params: {
    type: Object,
  },
});
const params = props.params;

const handleClick = () => {
  params.context.deleteBtnClicked(props.params.data);
};
</script>
