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
          <div class="h-group">
            <baseButton class="button-primary button-lg">
              <span class="button-text">Button</span>
            </baseButton>
            <baseButton class="button-link">
              <span class="button-text">Button</span>
            </baseButton>
            <baseButton class="button-sm">
              <span class="button-text">Button</span>
            </baseButton>
            <baseButton title="사용자 관리" class="button-icon button-negative button-lg">
              <svg-icon name="user" class="svg-icon" />
              <span class="hidden">사용자 관리</span>
            </baseButton>
            <baseButton title="사용자 관리" class="button-icon button-normal">
              <svg-icon name="user" class="svg-icon" />
              <span class="hidden">사용자 관리</span>
            </baseButton>
            <baseButton title="사용자 관리" class="button-icon button-secondary button-sm">
              <svg-icon name="user" class="svg-icon" />
              <span class="hidden">사용자 관리</span>
            </baseButton>
          </div>
        </td>
      </tr>
      <tr>
        <th scope="row">Modal</th>
        <td>
          <baseButton @click="onClickOpen">
            <span class="button-text">Modal Open</span>
          </baseButton>
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
