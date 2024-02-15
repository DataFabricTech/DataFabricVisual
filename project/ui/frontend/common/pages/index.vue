<template>
  <div>
    <h1>Fabric-design</h1>
    <section class="guide-section">
      <h2 class="section-title">color system</h2>
      <ol class="test">
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
      </ol>
    </section>
    <section class="guide-section">
      <h2 class="section-title">shadows</h2>
      <ol class="test shadows">
        <li>shadow-sm</li>
        <li>shadow-md</li>
        <li>shadow-lg</li>
        <li>shadow-xl</li>
      </ol>
    </section>
    <section class="guide-section">
      <h2 class="section-title">typography</h2>
      <div class="">
        <p class="guide-typo" v-for="index in 14" :key="index">
          데이터패브릭에서 제작한 모든 서비스와 문서에는 Pretendard 폰트를 사용합니다.
        </p>
      </div>
    </section>
    <section class="guide-section">
      <h2 class="section-title">icongraphy</h2>
      <div class="h-group">
        <svg-icon name="home" v-for="index in 5" :key="index" class="svg-icon icon-home"></svg-icon>
        <svg-icon class="svg-icon icon-trash" name="trash"></svg-icon>
      </div>
    </section>
    <section class="guide-section">
      <h2 class="section-title">components</h2>
      <div class="link-buttons">
        <!-- 3. 메뉴 버튼 추가 @click="clickMenu('컴포넌트명')" -->
        <button @click="clickMenu('Dayjs')">Dayjs</button>
        <button @click="clickMenu('Highcharts')">HighCharts</button>
        <button @click="clickMenu('VueFinalModal')">Vue Final Modal</button>
        <button @click="clickMenu('AgGrid')">Ag-Grid</button>
        <button @click="clickMenu('VueDatepickerNext')">Vue Datepicker Nuxt</button>
        <button @click="clickMenu('Locale')">Locale</button>
      </div>
      <div></div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { shallowRef } from "vue";
// 1. 컴포넌트 등록
// ex) 컴포넌트명 AgGrid / 컴포넌트명 규칙: 폴더명 ag-grid -> AgGrid, `-` 로 연결된 부분은 대문자로 연결
import AgGrid from "~/pages/ag-grid/index.vue";
import Dayjs from "~/pages/dayjs/index.vue";
import Highcharts from "~/pages/highcharts/index.vue";
import Locale from "~/pages/locale/index.vue";
import VueDatepickerNext from "~/pages/vue-datepicker-next/index.vue";
import VueFinalModal from "~/pages/vue-final-modal/index.vue";

const currentComponent = shallowRef(Dayjs);

// 2. 컴포넌트를 객체에 저장
const components: any = {
  AgGrid,
  Dayjs,
  Highcharts,
  Locale,
  VueDatepickerNext,
  VueFinalModal
};

function clickMenu(menu: string) {
  currentComponent.value = components[menu];
}
</script>

<style lang="css" scoped></style>
