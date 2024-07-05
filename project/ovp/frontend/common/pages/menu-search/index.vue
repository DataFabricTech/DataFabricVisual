<template>
  <div class="sample" >
    <h1> 메뉴 선택 - 버튼 컴포넌트 예시</h1>
    <div class="button-container">
      <menu-search-button
        :data="sample_data"
        :selected-items="single_sample_selected_data"
        label-key="name"
        value-key="id"
        title="단일 선택"
        @single-change="changeSingle"
      ></menu-search-button>
      <menu-search-button
        :data="sample_data"
        :selected-items="multiple_sample_selected_data"
        label-key="name"
        value-key="id"
        title="다중 선택"
        :is-multi="true"
        @multiple-change="changeMultiple"
      ></menu-search-button>
      <button class="button button-secondary" @click="onClickChangData">값 변경</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, Ref } from "vue";
const sample_data : Ref<any> =  ref([
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
      name: "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"
    },
  ]
);
const multiple_sample_selected_data: Ref<any> = ref([
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
]);

const single_sample_selected_data : Ref<any> =  ref({
  id: "2",
  name: "BBB"
});

const changeMultiple : (value : any[] | {}) => void = (value) => {
  console.log("changeMultiple", value);
}

const onClickChangData : (value : any[] | {}) => void = (value) => {
  multiple_sample_selected_data.value = [];
  console.log("onClickChangData", value);
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
