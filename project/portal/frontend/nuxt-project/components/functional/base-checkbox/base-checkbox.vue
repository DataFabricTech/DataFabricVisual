<template>
  <!--  최소 단위인 checkbox Component - 기능 별로 쪼개는 작업 필요-->
  <div class="checkbox">
    <input type="checkbox" class="checkbox__input" :id="props.checkboxId" :name="props.name" :checked="props.checked"
      :disabled="props.disabled" />
    <label class="checkbox__label" :for="props.checkboxId">
      <slot name="label" class="checkbox__label">Default Label</slot>
    </label>
  </div>
</template>

<script setup lang="ts">

definePageMeta({
  name: "BaseCheckbox",
})

const props = defineProps({
  disabled: {
    type: Boolean,
    default: false
  },
  checkboxId: {
    type: String,
    default: null
  },
  name: {
    type: String,
    default: null
  },
  checked: {
    type: Boolean,
    default: false
  }
})

</script>

<style lang="scss">
@import "@functional/base-checkbox/base-checkbox";
</style>
