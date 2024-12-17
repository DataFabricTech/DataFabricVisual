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
      :title="'모달 1번 테스트'"
      :top="200"
      :width="600"
      :height="800"
      @cancel="closeModal1"
      @confirm="closeModal1"
    >
    </Modal>

    <Modal
      :background="'interactive'"
      :click-to-close="true"
      :display-directive="'show'"
      :esc-to-close="true"
      :hide-overlay="false"
      :modal-id="'modal2'"
      :title="'모달 2번 테스트'"
      :width="1000"
      :height="800"
    >
      <template v-slot:body>
        <div>testtesttetesttesttesttesttesttesttesttesttestteststtesttest</div>
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

function closeModal1(id: string | number | symbol) {
  $vfm.close(id);
}

function openModal2() {
  $vfm.open("modal2");
}
</script>

<style lang="css" scoped></style>
