<template>
  <div>
    <h1>Vue Final Modal</h1>
    <button @click="openModal">Open Modal</button>
    <button @click="openModal2">Open Modal2</button>

    <Modal
      :content-transition="'vfm-slide-right'"
      :esc-to-close="true"
      :hide-overlay="false"
      :modal-id="'modal1'"
      :overlay-transition="'vfm-slide-down'"
    >
      <template v-slot:head><span>header</span></template>
      <template v-slot:body>
        <span>body</span>
        <span>기본 모달입니다.</span>
      </template>
      <template v-slot:foot>
        <span>footer</span>
        <button class="button" @click="">Confirm</button>
      </template>
    </Modal>

    <Modal
      :background="'interactive'"
      :click-to-close="true"
      :content-class="''"
      :content-transition="'vfm-slide-right'"
      :display-directive="'show'"
      :drag-and-resize="true"
      :esc-to-close="true"
      :hide-overlay="false"
      :modal-id="'modal2'"
      :overlay-transition="'vfm-slide-down'"
    >
      <template v-slot:head><span>header</span></template>
      <template v-slot:body>
        <span>body</span><br />
        <span>드래그 되는 모달입니다.</span> <br />
        <span>리사이즈도 되는 모달입니다.</span>
      </template>
      <template v-slot:foot>
        <span>footer</span>
        <button class="button" @click="">Confirm</button>
      </template>
    </Modal>
  </div>
</template>

<script lang="ts" setup>
import { useNuxtApp } from "nuxt/app";
const { $vfm } = useNuxtApp();

function openModal() {
  $vfm.open("modal1");
}

function openModal2() {
  $vfm.open("modal2");
}
</script>

<style lang="css" scoped></style>