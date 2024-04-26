<template>
  <div>
    <button @click="open">Open Modal</button>
    <ModalsContainer />
  </div>
</template>

<script setup lang="ts">
import { ModalsContainer, useModal } from 'vue-final-modal';
import ModalConfirmPlainCss from './ModalConfirmPlainCss.vue';

const { open, close } = useModal({
  component: ModalConfirmPlainCss,
  attrs: {
    title: 'insert modal title here',
    onConfirm() {
      close()
    },
  },
  slots: {
    default: '<p>medium size modal</p>',
  },
})
</script>

<style lang="css" scoped>

</style>
