<template>
  <div class="sample">
    <div class="select select-clean">
      <div v-for="(item, key) in multiple_sample_data.selectedItems" :key="key">
        <span> {{item.name}} </span>
      </div>
      <button class="select-button button-lg" style="width: 360px" @click="onClickOpenMenuSearch">
        <span class="select-button-title">다중 카테고리</span>
        <div class="badge badge-primary-lighter">
          <p class="badge-text">10</p>
        </div>
        <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
      </button>
      <menu-search
        v-on-click-outside="() => multiple_sample_data.isShow = false"
        v-show="multiple_sample_data.isShow"
        :data="multiple_sample_data.data"
        :selected-items="multiple_sample_data.selectedItems"
        :is-multi="true"
        label-key="name"
        value-key="id"
        @cancel="onClickOpenMenuSearch"
        @multiple-change="changeMultiple"
      ></menu-search>
    </div>

    <div class="select select-clean">
      <button class="select-button button-lg" style="width: 360px" @click="onClickOpenMenuSearch_2">
        <span class="select-button-title">단일 카테고리</span>
        <div class="badge badge-primary-lighter">
          <p class="badge-text">10</p>
        </div>
        <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
      </button>
      <menu-search
        v-on-click-outside="() => single_sample_data.isShow = false"
        v-show="single_sample_data.isShow"
        :data="single_sample_data.data"
        :selected-items="single_sample_data.selectedItems"
        label-key="name"
        value-key="id"
        @cancel="onClickOpenMenuSearch_2"
        @single-change="changeSingle"
      ></menu-search>
    </div>
  </div>
</template>

<script setup lang="ts">
import { vOnClickOutside } from "@vueuse/components";
import { ref, Ref } from "vue";
const multiple_sample_data : Ref<any> =  ref({
  isShow: false,
  data: [
    {
      id: "1",
      name: "AAA"
    },
    {
      id: "2",
      name: "BBB"
    },
    {
      id: "3",
      name: "CCC"
    },
    {
      id: "4",
      name: "DDD"
    },
  ],
  selectedItems: [
    {
      id: "1",
      name: "AAA"
    },
    {
      id: "2",
      name: "BBB"
    },
    {
      id: "3",
      name: "CCC"
    },
  ]
});
const single_sample_data : Ref<any> =  ref({
  isShow: false,
  data: [
    {
      id: "1",
      name: "AAA"
    },
    {
      id: "2",
      name: "BBB"
    },
    {
      id: "3",
      name: "CCC"
    },
    {
      id: "4",
      name: "DDD"
    },
  ],
  selectedItems: {
    id: "2",
    name: "BBB"
  },
});

const onClickOpenMenuSearch: () => void = () => {
  multiple_sample_data.value.isShow = !multiple_sample_data.value.isShow;
};
const changeMultiple : (value : any[] | {}) => void = (value) => {
  console.log("changeMultiple", value);
  onClickOpenMenuSearch();
}

const onClickOpenMenuSearch_2: () => void = () => {
  single_sample_data.value.isShow = !single_sample_data.value.isShow;
};
const changeSingle : (value : any[] | {}) => void = (value) => {
  console.log("changeSingle", value);
  onClickOpenMenuSearch_2();
}

</script>

<style scoped>
.sample {
  display: flex;
  gap: 8px;
  padding: 30px;
}
</style>
