<template>
  <client-only>
    <div class="radio-box">
      <span v-for="(option, index) in radioGroup" :key="index">
        <input
          type="radio"
          :id="option.id"
          name="radioGroup"
          :value="option.value"
          @change="changeGroup(option)"
          :disabled="option.disabled"
          :checked="option.checked"
        />
        <label :for="option.id">{{ option[labelKey] }}</label>
      </span>
    </div>
  </client-only>
</template>

<script setup lang="ts">
// import { useI18n } from "vue-i18n";
// import messages from "./index.json";
//
// const { t } = useI18n({
//   messages
// });

import { RadioGroupProps } from "~/components/extends/radio-group/RadioGroupProps";
import { RadioGroupComposition } from "~/components/extends/radio-group/RadioGroupComposition";

const props = withDefaults(defineProps<RadioGroupProps>(), {
  data: () => [],
  labelKey: "label",
  valueKey: "value",
  name: "radioGroup",
  checkedItem: "",
  disabledList: () => [],
  isFirstCheckedEvent: true,
  radioGroup: () => []
});

// TODO : props.name 값이 없을 경우 즉, 사용자가 주지 않았을 경우 UUID로 생성하여 Default값으로 줄 것.

const emit = defineEmits<{ (e: "change", item: string | number): void }>();
function onChange(value: string | number) {
  emit("change", value);
}
const { radioGroup, changeGroup } = RadioGroupComposition(props, onChange);
</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
