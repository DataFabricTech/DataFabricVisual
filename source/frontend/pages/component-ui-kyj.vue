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
          <div class="step">
            <div class="step-item">
              <div class="step-icon">
                <span class="step-icon-text">
                  1
                </span>
              </div>
              <div class="step-title">
                <span class="step-title-text">
                  step
                </span>
              </div>
            </div>
            <div class="step-item is-current">
              <div class="step-icon">
              <span class="step-icon-text">
                2
              </span>
              </div>
              <div class="step-title">
                <span class="step-title-text">
                  step
                </span>
              </div>
            </div>
            <div class="step-item">
              <div class="step-icon">
                <span class="step-icon-text">
                  3
                </span>
              </div>
              <div class="step-title">
                <span class="step-title-text">
                  step
                </span>
              </div>
            </div>
            <div class="step-item">
              <div class="step-icon">
                <span class="step-icon-text">
                  4
                </span>
              </div>
              <div class="step-title">
                <span class="step-title-text">
                  step
                </span>
              </div>
            </div>
          </div>
        </div>
      </td>
    </tr>
    <tr>
      <th scope="row">Select</th>
    </tr>
    <tr>
      <td>
        <div class="h-group gap-4">
          <div class="v-group">
            <strong class="w-[180px] font-light text-[14px]">size: lg</strong>
            <baseSelect class="select-lg w-[100%]"></baseSelect>
          </div>
          <div class="v-group">
            <strong class="w-[140px] font-light text-[14px]">size: md</strong>
            <baseSelect class="select w-[100%]"></baseSelect>
          </div>
          <div class="v-group">
            <strong class="w-[120px] font-light text-[14px]">size: sm</strong>
            <baseSelect class="select-sm w-[100%]"></baseSelect>
          </div>
        </div>
      </td>
    </tr>
    <tr>
      <th scope="row">toggle button</th>
    </tr>
    <tr>
      <td>
        <div class="v-group gap-4">
          <div class="h-group">
            <strong class="w-[100px] font-light text-[14px]">size: lg</strong>
            <baseToggleButton class="toggle-button-lg"></baseToggleButton>
          </div>
          <div class="h-group">
            <strong class="w-[100px] font-light text-[14px]">size: md</strong>
            <baseToggleButton></baseToggleButton>
          </div>
          <div class="h-group">
            <strong class="w-[100px] font-light text-[14px]">size: sm</strong>
            <baseToggleButton class="toggle-button-sm"></baseToggleButton>
          </div>
        </div>
      </td>
    </tr>
    <tr>
      <th scope="row">table</th>
    </tr>
    <tr>
      <td>
        <div class="v-group gap-4">
          <div class="h-group">
            <strong class="w-[100px] font-light text-[14px]">type: coulumn</strong>
            <baseTableColumn></baseTableColumn>
          </div>
          <div class="h-group">
            <strong class="w-[100px] font-light text-[14px]">type: row</strong>
            <baseTableRow></baseTableRow>
          </div>
        </div>
      </td>
    </tr>
    </tbody>
  </table>
</div>
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
