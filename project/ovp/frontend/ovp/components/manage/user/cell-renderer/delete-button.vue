<template>
  <button
    v-if="data.name !== 'admin'"
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
const data = props.params.data;

const handleClick = () => {
  props.params.context.deleteBtnClicked(data);
};
</script>
