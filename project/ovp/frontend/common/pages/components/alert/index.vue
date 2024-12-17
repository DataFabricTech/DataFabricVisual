<template>
  <div style="font-size: 50px; padding: 20px">Alert</div>
  <div style="display: flex; justify-content: left; align-items: center">
    <div style="margin-left: 20px; margin-right: 20px">모달 타입:</div>
    <div style="">
      <select-box
        class="select-lg"
        :data="options"
        label-key="label"
        value-key="value"
        :selectedItem="selectedItem"
        @select="selectItem"
      ></select-box>
    </div>
  </div>
  <div style="margin-left: 20px; margin-top: 20px; margin-bottom: 10px">
    <button type="button" class="button button-primary" @click="showAlert">Show Alert</button>
  </div>
  <div style="font-size: 50px; padding: 20px; margin-bottom: 10px">Confirm</div>
  <div style="margin-left: 20px">
    <button type="button" class="button button-primary" @click="showConfirm">Show Confirm</button>
  </div>
  <div style="font-size: 50px; padding: 20px; margin-bottom: 10px">Modal</div>
  <div style="margin-left: 20px">
    <button type="button" class="button button-primary" @click="openModal">Open Modal</button>
  </div>

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
    @cancel="closeModal"
    @confirm="closeModal"
  >
    <template v-slot:body>
      <button type="button" class="button button-primary" @click="showAlert">Show Alert</button>
      <button type="button" class="button button-primary" @click="showConfirm">Show Confirm</button>
    </template>
  </Modal>
</template>

<script lang="ts" setup>
import { useNuxtApp } from "nuxt/app";

import SelectBox from "~/components/extends/select-box/SelectBox.vue";

const nuxtApp = useNuxtApp();
const { $vfm, $alert, $confirm } = useNuxtApp();

const options: { [key: string]: string | number }[] = [
  {
    label: "Normal",
    value: "normal"
  },
  {
    label: "Info",
    value: "info"
  },
  {
    label: "Warning",
    value: "warning"
  },
  {
    label: "Error",
    value: "error"
  },
  {
    label: "Success",
    value: "success"
  }
];

const selectedItem = "info";
let modalType = selectedItem;

function selectItem(item: string | number) {
  modalType = item;
}

const clickBtn = () => {
  console.log("click button");
};

function showAlert() {
  // type -> normal, info, warning, error, success
  // $alert("This is an alert message!", "OK").then(() => {
  //   alert("Confirmed!");
  // });
  $alert("This is an alert message!", modalType);
}

async function showConfirm() {
  const result = await $confirm("Are you sure?");
  console.log(result);
  if (result) {
    alert("Confirmed!");
  } else {
    alert("Cancelled!");
  }
}

function openModal() {
  $vfm.open("modal1");
}

function closeModal(id: string | number | symbol) {
  $vfm.close(id);
}
</script>

<style scoped></style>
