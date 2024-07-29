<template>
  <div class="sample-container">
    <div class="sample">
      <h1>메뉴 선택 - Tag 컴포넌트 예시</h1>
      <menu-search-tag
        :data="tag_sample_data"
        :selected-items="tag_selected_sample_data"
        label-key="name"
        value-key="id"
        :is-multi="true"
        title="값을 선택하세요"
        @multiple-change="changeTag"
      ></menu-search-tag>
    </div>
    <div class="sample">
      <h1>메뉴 선택 - 버튼 컴포넌트 예시</h1>
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
          :data="multiple_sample_data2"
          :selected-items="multiple_sample_selected_data"
          label-key="name"
          value-key="id"
          title="다중 선택"
          :is-multi="true"
          @multiple-change="changeMultiple"
        ></menu-search-button>
        <button class="button button-secondary" @click="onClickChangData">값 변경</button>
        <br />
        <menu-search-button
          :data="multiple_sample_data2"
          :selected-items="[]"
          label-key="name"
          value-key="id"
          title="버튼 클릭 이벤트"
          :is-multi="true"
          @open="onOpenMenuSearch"
          @multiple-change="changeMultiple"
        ></menu-search-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, Ref } from "vue";
const sample_data: Ref<any> = ref([
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
  }
]);
const multiple_sample_data2: Ref<any> = ref([
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
  }
]);
const multiple_sample_new_data = [
  {
    id: "1",
    name: "QQQ"
  },
  {
    id: "2",
    name: "WWW"
  },
  {
    id: "3",
    name: "EEE"
  },
  {
    id: "4",
    name: "RRR"
  }
];
const tag_sample_data = [
  {
    id: "1",
    name: "ZZZ"
  },
  {
    id: "2",
    name: "XXX"
  },
  {
    id: "3",
    name: "CCC"
  },
  {
    id: "4",
    name: "VVV"
  }
];
const tag_selected_sample_data: Ref<any> = ref([
  {
    id: "1",
    name: "ZZZ"
  }
]);
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
  }
]);

const single_sample_selected_data: Ref<any> = ref({
  id: "2",
  name: "BBB"
});

const changeMultiple: (value: any[] | {}) => void = (value) => {
  console.log("changeMultiple", value);
};

const onClickChangData: (value: any[] | {}) => void = (value) => {
  multiple_sample_selected_data.value = [];
  console.log("onClickChangData", value);
};
const onOpenMenuSearch: () => void = () => {
  multiple_sample_data2.value = multiple_sample_new_data;
  console.log("onClickChangData", multiple_sample_data2);
};

const changeSingle: (value: any[] | {}) => void = (value) => {
  console.log("changeSingle", value);
};
const changeTag: (value: any[] | {}) => void = (value) => {
  console.log("changeTag", value);
};
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

.sample-container {
  display: flex;
  flex-direction: row;
  gap: 16px;
}
</style>
