<template>
<div class="layout-guide">
  <table class="guide-table">
    <caption>Component UI</caption>
    <tbody>
    <tr>
      <th scope="row">Badge</th>
      <td>
        <baseBadge class="bg-purple">Badge</baseBadge>
      </td>
    </tr>
    <tr>
      <th scope="row">Button</th>
      <td>
        <baseButton>Button</baseButton>
      </td>
    </tr>
    <tr>
      <th scope="row">Modal</th>
      <td>
        <baseButton @click="onClickOpen">Modal Open</baseButton>
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
import {ref} from "@vue/reactivity";
import {useRouter} from "vue-router";

const show = ref(false);
const router = useRouter();

function onClickOpen() {
  show.value = !show.value;
}
function onClickMain() {
  router.push("/main")
}


</script>

<style lang="scss">
@import "component-ui";
</style>
