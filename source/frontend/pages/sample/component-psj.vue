<script lang="ts" setup>
import { defaultAlert, infoAlert, confirmAlert } from "~/components/common/alert/alert-default";
import { AlertType } from "~/components/common/alert/alert-default";

definePageMeta({
  layout: "default-layout"
});

function onDefaultAlert() {
  defaultAlert(AlertType.normal, "야 임마야!!!");
}
function onInfoAlert() {
  infoAlert("정보 창이다 임마야");
}

async function onConfirmAlert() {
  let value = await confirmAlert("할꺼야 말꺼야? <b> 확인 </b> <b> 취소 </b>");
  alert(value);
}
</script>

<template>
  <button @click="onDefaultAlert">[기본알랏]</button>
  <button @click="onInfoAlert">[정보알랏]</button>
  <button @click="onConfirmAlert">[선택알람]</button>
</template>
