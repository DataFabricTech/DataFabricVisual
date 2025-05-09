<template>
  <h1 class="section-title">컴포넌트 contents 사용</h1>
  <div>
    <button class="button button-primary" @click="clickOne">1번째 탭 선택</button>
    <button class="button button-primary" @click="clickTwo">2번째 탭 선택</button>
    <button class="button button-primary" @click="clickThree">3번째 탭 선택</button>
    <tab
      style="width: 800px"
      class="tab-line"
      :data="tabOptions"
      :useTabContents="true"
      label-key="label"
      value-key="value"
      current-item-type="value"
      :current-item="initTabValue"
      @change="onChangeTab"
    >
      <template #[currentTab]>
        <component :is="currentComponent" />
      </template>
    </tab>
  </div>
  <div>
    ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  </div>
  <h1 class="section-title">일반 contents 사용</h1>
  <div>
    <button class="button button-primary" @click="clickOne2">1번째 탭 선택</button>
    <button class="button button-primary" @click="clickTwo2">2번째 탭 선택</button>
    <button class="button button-primary" @click="clickThree2">3번째 탭 선택</button>
    <tab
      style="width: 800px"
      class="tab-line"
      :data="tabOptions2"
      label-key="label"
      value-key="value"
      current-item-type="value"
      :current-item="initTabValue2"
      @change="onChangeTab2"
    >
      <template #one>
        <one></one>
      </template>
      <template #two>
        <two></two>
      </template>
      <template #three>
        <three></three>
      </template>
    </tab>
  </div>
  <div>
    ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  </div>
  <h1 class="section-title">단일값 배열 contents 사용</h1>
  <div>
    <button class="button button-primary" @click="clickOne3">1번째 탭 선택</button>
    <button class="button button-primary" @click="clickTwo3">2번째 탭 선택</button>
    <button class="button button-primary" @click="clickThree3">3번째 탭 선택</button>
    <tab
      style="width: 800px"
      class="tab-line"
      :data="tabOptions3"
      label-key="label"
      value-key="value"
      current-item-type="index"
      :current-item="initTabValue3"
      @change="onChangeTab3"
    >
      <template #첫번째>
        <one></one>
      </template>
      <template #두번째>
        <two></two>
      </template>
      <template #세번째>
        <three></three>
      </template>
    </tab>
  </div>
  <div>
    ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  </div>
  <h1 class="section-title">disable, hide</h1>
  <div>
    <button class="button button-primary" @click="clickCase">첫번째, 두번째 탭 hide, 세번째 네번째 탭 disable</button>
    <button class="button button-primary" @click="clickCase2">원상태로 되돌리기</button>
    <button class="button button-primary" @click="clickCase3">모든상태 해제</button>
    <tab
      style="width: 800px"
      class="tab-line"
      :data="tabOptions4"
      label-key="label"
      value-key="value"
      current-item-type="value"
      :current-item="initTabValue4"
      :disabled-list="disabledList"
      :hided-list="hidedList"
      @change="onChangeTab4"
    >
      <template #one>
        <h4 class="group-title">첫번째 탭</h4>
      </template>
      <template #two>
        <h4 class="group-title">두번째 탭</h4>
      </template>
      <template #three>
        <h4 class="group-title">세번쨰 탭</h4>
      </template>
      <template #four>
        <h4 class="group-title">네번째 탭</h4>
      </template>
      <template #five>
        <h4 class="group-title">다섯번 탭</h4>
      </template>
      <template #six>
        <h4 class="group-title">여섯번째 탭</h4>
      </template>
      <template #seven>
        <h4 class="group-title">일곱번째 탭</h4>
      </template>
    </tab>
  </div>
</template>

<script setup lang="ts">
import { ref, Fef, nextTick } from "vue";
import _ from "lodash";

import Tab from "@/components/extends/tab/Tab.vue";
import One from "./tab-contents/one.vue";
import Two from "./tab-contents/two.vue";
import Three from "./tab-contents/three.vue";

const initTabValue: Ref<String> = ref("one");
const initTabValue2: Ref<String> = ref("one");
const initTabValue3: Ref<number> = ref(0);
const initTabValue4: Ref<String> = ref("one");

const currentTab: Ref<String> = ref("one");
const currentComponent: Component = shallowRef(One);

const disabledList: Ref<string[] | number[]> = ref(["four", "seven"]);
const hidedList: Ref<string[] | number[]> = ref(["five", "six"]);

const tabOptions = [
  { label: "첫번째", value: "one", component: One },
  { label: "두번째", value: "two", component: Two },
  { label: "세번쨰", value: "three", component: Three }
];

const tabOptions2 = [
  { label: "첫번째", value: "one" },
  { label: "두번째", value: "two" },
  { label: "세번쨰", value: "three" }
];

const tabOptions3 = ["첫번째", "두번째", "세번째"];

const tabOptions4 = [
  { label: "첫번째", value: "one" },
  { label: "두번째", value: "two" },
  { label: "세번쨰", value: "three" },
  { label: "네번째", value: "four" },
  { label: "다섯번째", value: "five" },
  { label: "여섯번째", value: "six" },
  { label: "일곱번째", value: "seven" }
];

function onChangeTab(tab: number | string) {
  console.log("선택된 탭: ", tab);
  currentTab.value = tab;
  currentComponent.value = _.find(tabOptions, ["value", tab])?.component;
}

function onChangeTab2(tab: number | string) {
  console.log("선택된 탭: ", tab);
}

function onChangeTab3(tab: number | string) {
  console.log("선택된 탭: ", tab);
}

function onChangeTab4(tab: number | string) {
  console.log("선택된 탭: ", tab);
}

const clickOne = async () => {
  initTabValue.value = "";
  // NOTE: setTimeout을 사용해야하는 이유
  // 첫번째 문제: initTabValue가 초기값이 one인 상태에서 다른 탭을 마우스로 클릭해서 선택한 경우 탭 상태는
  await nextTick();
  initTabValue.value = "one";
  currentTab.value = "one";
  currentComponent.value = _.find(tabOptions, ["value", "one"])?.component;
};

const clickTwo = async () => {
  initTabValue.value = "";
  await nextTick();
  initTabValue.value = "two";
  currentTab.value = "two";
  currentComponent.value = _.find(tabOptions, ["value", "two"])?.component;
};

const clickThree = async () => {
  initTabValue.value = "";
  await nextTick();
  initTabValue.value = "three";
  currentTab.value = "three";
  currentComponent.value = _.find(tabOptions, ["value", "three"])?.component;
};

const clickOne2 = async () => {
  initTabValue2.value = "";
  await nextTick();
  initTabValue2.value = "one";
};

const clickTwo2 = async () => {
  initTabValue2.value = "";
  await nextTick();
  initTabValue2.value = "two";
};

const clickThree2 = async () => {
  initTabValue2.value = "";
  await nextTick();
  initTabValue2.value = "three";
};

const clickOne3 = async () => {
  initTabValue3.value = -1;
  await nextTick();
  initTabValue3.value = 0;
};

const clickTwo3 = async () => {
  initTabValue3.value = -1;
  await nextTick();
  initTabValue3.value = 1;
};

const clickThree3 = async () => {
  initTabValue3.value = -1;
  await nextTick();
  initTabValue3.value = 2;
};

const clickCase = () => {
  hidedList.value = ["one", "two"];
  disabledList.value = ["three", "four"];
};

const clickCase2 = () => {
  hidedList.value = ["five", "six"];
  disabledList.value = ["four", "seven"];
};
const clickCase3 = () => {
  hidedList.value = [];
  disabledList.value = [];
};
</script>

<style scoped></style>
<script setup lang="ts"></script>
