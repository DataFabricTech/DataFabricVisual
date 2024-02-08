<template>
  <div>
    <h1>
      Vue Final Modal
    </h1>
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
    title: 'Hello World!',
    onConfirm() {
      close()
    },
  },
  slots: {
    default: '<p>The content of the modal</p>',
  },
})
</script>

<style lang="css" scoped>

</style>
