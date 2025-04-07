<template>
  <div v-if="data && data.name !== 'admin'" style="display: flex; gap: 8px;">
  <button
      v-if="data.name !== 'admin'"
      class="button button-sm button-secondary-stroke"
      type="button"
      @click="editHandleClick"
  >
    <span class="button-title">수정</span>
  </button>
  <button
      v-if="data.name !== 'admin'"
      class="button button-sm button-error-stroke"
      type="button"
      @click="deleteHandleClick"
  >
    <span class="button-title">삭제</span>
  </button>
 </div>
</template>

<script setup>
const props = defineProps({
  params: {
    type: Object,
  },
});
const data = props.params.data;

const editHandleClick = () => {
  props.params.context.tagModifyOpen(data);
};
const deleteHandleClick = () => {
  props.params.context.confirmDelete(data);
};
</script>
