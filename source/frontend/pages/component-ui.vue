<template>
<div class="layout-guide">
  <div class="layout-guide-title">
    <h1>Fabric UI Components</h1>
  </div>
  <table class="guide-table">
    <tbody>
    <tr>
      <th scope="row">Badge</th>
    </tr>
    <tr>
      <td>
        <div class="flex gap-2">
          <baseBadge class="bg-marker-purple">Badge</baseBadge>
          <baseBadge class="bg-marker-cyan">Badge</baseBadge>
          <baseBadge class="bg-marker-red">Badge</baseBadge>
          <baseBadge class="bg-marker-yellow">Badge</baseBadge>
        </div>
      </td>
    </tr>
    <tr>
      <th scope="row">Button</th>
    </tr>
    <tr>
      <td>
        <baseButton>Button</baseButton>
      </td>
    </tr>
    <tr>
      <th scope="row">Modal</th>
    </tr>
    <tr>
      <td>
        <baseButton @click="onClickOpen">Modal Open</baseButton>
      </td>
    </tr>
    <tr>
      <th scope="row">Text-input</th>
    </tr>
    <tr>
      <td class="col-sub">
        <p style="width: 200px">default</p>
        <p style="width: 200px">disabled</p>
      </td>
    </tr>
    <tr>
      <td>
        <div class="flex gap-2">
          <baseTextinput style="width: 200px" placeholder="입력해주세요."></baseTextinput>
          <baseTextinput style="width: 200px" disabled value="입력할 수 없습니다."></baseTextinput>
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
