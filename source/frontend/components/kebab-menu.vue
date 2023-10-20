<template>
  <div class="kebab-menu">
    <BaseButton @focus="state.isOpen=true" @blur="state.isOpen = false" class="button button-icon button-lg" title="열기" >
      <span class="hidden">상세설정 열기</span>
      <svg-icon name="kebab-menu" class="svg-icon" />
    </BaseButton>
    <BaseContext class="kebab-context" v-if="state.isOpen"></BaseContext>
  </div>
</template>

<script setup lang="ts">
const state = reactive({
  isOpen: false
});
</script>
