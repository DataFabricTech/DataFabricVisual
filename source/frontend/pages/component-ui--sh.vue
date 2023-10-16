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
          <baseBadge class="bg-marker-gray">Badge</baseBadge>
        </div>
      </td>
    </tr>
    <tr>
      <th scope="row">tag</th>
    </tr>
    <tr>
      <td>
        <div class="flex gap-2">
          <baseTag class="">단속위치</baseTag>
        </div>
      </td>
    </tr>
    <tr>
      <th scope="row">Button</th>
    </tr>
    <tr>
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
        <p style="width: 200px">read-only</p>
      </td>
    </tr>
    <tr>
      <td>
        <div class="flex gap-2">
          <baseTextinput id="input-ex-01" class="text-input-lg" style="width: 200px" placeholder="입력해주세요."></baseTextinput>
          <label for="input-ex-01" class="label-hidden">lable</label>
          <baseTextinput style="width: 200px" disabled value="입력할 수 없습니다."></baseTextinput>
          <baseTextinput class="text-input-sm" style="width: 200px" readonly value="read only"></baseTextinput>
        </div>
      </td>
    </tr>
    <tr>
      <th scope="row">Radio</th>
    </tr>
    <tr>
      <td class="col-sub">
        <p style="width: 200px">default</p>
        <p style="width: 200px">checked</p>
        <p style="width: 200px">disabled</p>
      </td>
    </tr>
    <tr>
      <td>
        <div class="flex gap-2">
          <baseRadio class="radio-lg" style="width: 200px" id="radio-ex-01">radio</baseRadio>
          <baseRadio style="width: 200px" id="radio-ex-02" checked>Radio</baseRadio>
          <baseRadio class="radio-sm" style="width: 200px" id="radio-ex-03" disabled>라디오</baseRadio>
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
