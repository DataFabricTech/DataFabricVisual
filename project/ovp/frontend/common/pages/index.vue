<template>
  <div>
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
      <h2 class="section-title">icongraphy</h2>
      <div class="h-group gap-4">
        <svg-icon v-for="index in 5" :key="index" class="svg-icon icon-home" name="home"></svg-icon>
        <svg-icon class="svg-icon" name="trash"></svg-icon>
      </div>
    </section>

    <!-- 개발 완료 목록 -->
    <section class="guide-section">
      <h2 class="section-title">개발 UI</h2>
      <article class="mb-10">
        <div class="flex flex-col gap-5">
          <div class="v-group gap-3 w-72">
            <h4 class="group-title">Alert</h4>
            <nuxt-link :to="'/components/alert'" class="button button-primary button-lg">
              <span class="button-text">Alert 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Select</h4>
            <nuxt-link :to="'/selectbox'" class="button button-primary button-lg">
              <span class="button-text">Select 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Checkbox List</h4>
            <nuxt-link :to="'/checkboxList'" class="button button-primary button-lg">
              <span class="button-text">Checkbox List 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Combobox</h4>
            <nuxt-link :to="'/combobox'" class="button button-primary button-lg">
              <span class="button-text">Combobox 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Step & Tab</h4>
            <nuxt-link :to="'/components/navigation'" class="button button-primary button-lg">
              <span class="button-text">Step / Tab 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">ag-grid</h4>
            <nuxt-link :to="'/ag-grid'" class="button button-primary button-lg">
              <span class="button-text">ag-grid 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Today</h4>
            <nuxt-link :to="'/dayjs'" class="button button-primary button-lg">
              <span class="button-text">Today 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">DatePicker</h4>
            <nuxt-link :to="'/vue-datepicker-next'" class="button button-primary button-lg">
              <span class="button-text">datepicker 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Highcharts</h4>
            <nuxt-link :to="'/highcharts'" class="button button-primary button-lg">
              <span class="button-text">highcharts 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Modal</h4>
            <nuxt-link :to="'/vue-final-modal'" class="button button-primary button-lg">
              <span class="button-text">modal 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Menu Search</h4>
            <nuxt-link :to="'/menu-search'" class="button button-primary button-lg">
              <span class="button-text">menu-search 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Menu Search</h4>
            <nuxt-link :to="'/editable-group'" class="button button-primary button-lg">
              <span class="button-text">editable-group 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Search Input</h4>
            <nuxt-link :to="'/search-input'" class="button button-primary button-lg">
              <span class="button-text">search-input 예시 이동</span>
            </nuxt-link>
          </div>
        </div>
      </article>
    </section>
  </div>
</template>

<script lang="ts" setup>
import { shallowRef } from "vue";
// 1. 컴포넌트 등록
// ex) 컴포넌트명 AgGrid / 컴포넌트명 규칙: 폴더명 ag-grid -> AgGrid, `-` 로 연결된 부분은 대문자로 연결
import AgGrid from "~/pages/ag-grid/index.vue";
import Dayjs from "~/pages/dayjs/index.vue";
import Highcharts from "~/pages/highcharts/index.vue";
import Locale from "~/pages/locale/index.vue";
import VueDatepickerNext from "~/pages/vue-datepicker-next/index.vue";
import VueFinalModal from "~/pages/vue-final-modal/index.vue";
import Notification from "../components/extends/notification/Notification.vue";
import Breadcrumb from "../components/base/breadcrumb/Breadcrumb.vue";
import SelectBox from "../components/extends/select-box/SelectBox.vue";

const currentComponent = shallowRef(Dayjs);

// 2. 컴포넌트를 객체에 저장
const components: any = {
  AgGrid,
  Dayjs,
  Highcharts,
  Locale,
  SelectBox,
  VueDatepickerNext,
  VueFinalModal,
  Notification,
  Breadcrumb
};

function clickMenu(menu: string) {
  currentComponent.value = components[menu];
}

function checkCurrentPage(item: number): void {
  console.log("Emit currentPageNumber: ", item);
}

const isFirstCheckedEvent: boolean = true;

const options: { [key: string]: string | number }[] = [
  {
    label: "option1",
    value: "option11"
  },
  {
    label: "option2",
    value: "option22"
  },
  {
    label: "option3",
    value: "option33"
  }
];

const selectedItem = "";

function selectItem(item: string | number) {
  console.log(item);
}
</script>

<style lang="css" scoped></style>
