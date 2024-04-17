<template>
  <div class="step" style="width:350px">
    <div
      v-for="(item, index) in data"
      @click="clickStep(index)"
      class="step-item"
    >
      <div class="step-icon">

      </div>
      <span class="step-title" :class="{ 'current-step': changeCurrentStepClass(index) }">
          {{ item[labelKey] }}
        </span>
    </div>
  </div>


  <slot :name="data[currentIndex][valueKey]"></slot>
    <div v-if="showBtn">
      <button @click="move(currentIndex - 1)" :disabled="currentIndex === 0">
        이전</button
      ><button
      @click="move(currentIndex + 1)"
      :disabled="currentIndex + 1 >= data.length"
    >
      다음
    </button>
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
})

const emit = defineEmits<{ (e: "change", item: number | string): void }>();
const onChange = (value: string | number): void => {
  emit("change", value)
}
const { data, labelKey, valueKey, showBtn, currentIndex, clickStep, move, changeCurrentStepClass } = StepComposition(props, onChange)

</script>

<style>
.step-container {
  display: inline-block;
  margin-right: 30px;
}

.flex-item {
  display: inline-block;
  padding: 30px;
  border: 1px solid #ccc;
  border-radius: 50%; /* 둥근 테두리를 만듭니다. */
  cursor: pointer;
  transition: background-color 0.3s ease; /* 배경 색상 전환에 애니메이션을 추가합니다. */
}
.current-step {
  color: orange; /* 현재 스텝에 대한 글자 색상을 여기에 지정 */
}
</style>
