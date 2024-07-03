<template>
  <div class="sample" >
    <h1> 메뉴 선택 - 버튼 컴포넌트 예시</h1>
    <div class="button-container">
      <menu-search-button
        :data="single_sample_data.data"
        :selected-items="single_sample_data.selectedItems"
        label-key="name"
        value-key="id"
        title="단일 선택"
        @single-change="changeSingle"
      ></menu-search-button>
      <menu-search-button
        :data="multiple_sample_data.data"
        :selected-items="multiple_sample_data.selectedItems"
        label-key="name"
        value-key="id"
        title="다중 선택"
        :is-multi="true"
        @multiple-change="changeMultiple"
      ></menu-search-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, Ref } from "vue";
const multiple_sample_data : Ref<any> =  ref({
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

const changeMultiple : (value : any[] | {}) => void = (value) => {
  console.log("changeMultiple", value);
}

const changeSingle : (value : any[] | {}) => void = (value) => {
  console.log("changeSingle", value);
}

</script>

<style scoped>
.sample {
  width: 360px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 30px;
}

.button-container,
.tag-container {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
</style>
