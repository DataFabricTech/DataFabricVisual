<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Tab</h2>
    <p class="section-summary">
      탭은 서로 다른 화면을 쉽게 전환하는데 사용합니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Default</h3>
      <div class="flex flex-col ">
        <div class="flex flex-col gap-8 mb-14">
          <figure class="section-figure">
            <Tab
              :data="tabOptions2"
              :label-key="'label'"
              :value-key="'value'"
              :current-item="currentTab2"
              :current-item-type="'index'"
              @change="changeTab2"
            ></Tab>
          </figure>
          <div class="group-description">
            <p class="group-summary">
              주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
            </p>
          </div>
        </div>
      </div>
    </article>
    <article class="mb-10">
      <h3 class="section-subtitle">Line</h3>
      <div class="flex flex-col ">
        <div class="flex flex-col gap-8 mb-14">
          <figure class="section-figure">
            <Tab
              class="tab-line"
              style="width: 100%"
              :data="tabOptions2"
              :label-key="'label'"
              :value-key="'value'"
              :current-item="currentTab2"
              :current-item-type="'index'"
              @change="changeTab2"
            ></Tab>
          </figure>
          <div class="group-description">
            <p class="group-summary">
              주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
            </p>
          </div>
        </div>
      </div>
    </article>
  </section>

</template>

<script setup lang="ts">
import Tab from "~/components/extends/tab/Tab.vue";
import { ref } from "vue";

const tabOptions2 = [
  { label: "tab", value: 1 },
  { label: "tab", value: 2 },
  { label: "tab", value: 3 },
  { label: "tab", value: 4 },
  { label: "tab", value: 5 },
  { label: "tab", value: 6 },
  { label: "tab", value: 7 },
  { label: "tab", value: 8 }
];
const currentTab2 = ref();
currentTab2.value = 1;

function changeTab2(item: number | string) {
  currentTab2.value = item;
}
</script>

<style scoped></style>
