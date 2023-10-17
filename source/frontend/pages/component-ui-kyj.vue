<template>
<div class="layout-guide">
  <div class="layout-guide-title">
    <h1>Fabric UI Components</h1>
  </div>
  <table class="guide-table">
    <tbody>
    <tr>
      <th scope="row">Step</th>
    </tr>
    <tr>
      <td>
        <div class="flex gap-8">
          <baseStep>step</baseStep>
          <baseStep class="step-current">step</baseStep>
        </div>
      </td>
    </tr>
    </tbody>
  </table>
</div>
  <modal v-model="show" title="Modal Sample">
    <template v-slot:body>
      ~ Modal is Here! Modal is Here! ~
    </template>
  </modal>
</template>
<script lang="ts" setup>
import { ref } from "@vue/reactivity";
import { useRouter } from "vue-router";

const show = ref(false);
const router = useRouter();

function onClickOpen() {
  show.value = !show.value;
}

function onClickMain() {
  router.push("/main");
}


</script>

<style lang="scss">
@import "component-ui";
</style>
