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
