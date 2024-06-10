<template>
  <div class="step-container" style="width:450px">
    <div class="step">
      <!--  TODO: [개발] 작성완료후 다음으로 넘어갈시 해당 페이지는 is-step-item-complete 클래스 추가 -->
      <div class="step-item" v-for="(item, index) in data"
           @click="clickStep(index)" :class="{ 'is-step-item-selected': changeCurrentStepClass(index) }">
        <button class="step-button">
          <p class="step-number">1</p>
          <svg-icon class="step-icon" name="checkmark"></svg-icon>
        </button>
        <span class="step-title">{{ item[labelKey] }}</span>
      </div>
    </div>

    <slot :name="data[currentIndex][valueKey]"></slot>
    <div v-if="showBtn">
      <button @click="move(currentIndex - 1)" :disabled="currentIndex === 0">
        이전
      </button
      >
      <button
        @click="move(currentIndex + 1)"
        :disabled="currentIndex + 1 >= data.length"
      >
        다음
      </button>
    </div>
  </div>

</template>

<script lang="ts" setup>
import { StepProps } from "@/components/extends/step/StepProps";
import { StepComposition } from "@/components/extends/step/StepComposition";

const INDEX = "index";

const props = withDefaults(defineProps<StepProps>(), {
  data: () => [],
  labelKey: "label",
  valueKey: "value",
  iconKey: "icon",
  currentItem: 0,
  currentItemType: INDEX,
  showBtn: true,
  comparison: "equal"
});

const emit = defineEmits<{ (e: "change", item: number | string): void }>();
const onChange = (value: string | number): void => {
  emit("change", value);
};
const {
  data,
  labelKey,
  valueKey,
  showBtn,
  currentIndex,
  clickStep,
  move,
  changeCurrentStepClass
} = StepComposition(props, onChange);

</script>

<style>

</style>
