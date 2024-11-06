<template>
  <div class="inner-search" id="parent-div" ref="parentDiv">
    <div class="mu-input-icon">
      <input
        type="text"
        :class="inputClass"
        :placeholder="props.placeholder"
        :disabled="props.disabled"
        :value="selectedOption.description"
        @input="onChangeInput"
        @focus="toggleOptionList(true)"
        @blur="toggleOptionList(false)"
        @keypress.enter="onEnterKeyUp"
      />
      <button
        type="button"
        class="mu-btn mu-btn-delete"
        v-show="showDeleteButton"
        @click="onClickDelete"
      >
        <i class="mu-icon-svg item-delete"></i>
        <span>삭제</span>
      </button>
    </div>
    <div
      class="option-box slim-scroll"
      v-show="showOptions"
      :style="`width: ${width}px;`"
    >
      <ul class="mu-list">
        <li v-if="noResult" :style="optionItemStyle">
          <div>검색 결과가 없습니다.</div>
        </li>
        <li
          v-else
          v-for="(option, index) in optionList"
          :key="index"
          :value="option.value"
          :style="optionItemStyle"
          class="option-item"
          @mousedown="setSelectedOption(option)"
        >
          <div v-if="!props.descOnly" class="item-container">
            <p class="item-value">{{ option.description }}</p>&nbsp;
            <p class="item-desc">({{ option.name }})</p>
          </div>
          <div v-else>
            {{ option.description }}
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";
import { onMounted, ref, computed, nextTick } from 'vue';

const emit = defineEmits(["setData"]);
const props = defineProps([
  "value",
  "placeholder",
  "options",
  "disabled",
  "setData",
  "descOnly", // true일 경우 옵션의 description만 표시
]);

const parentDiv = ref<HTMLElement | null>(null);

const selectedOption = ref({
  // list 선택값 제어
  description: ObjectUtils.getPropertyValue(props.value, "description", ""),
  value: ObjectUtils.getPropertyValue(props.value, "value", ""),
});
const optionList = ref(props.options); // 옵션 목록
const showOptions = ref(false); // 옵션 목록 렌더 여부 제어 flag
const width = ref(0); // 옵션 목록 영역 width
const noResult = ref(false); // 옵션 검색 결과 유무 flag
const isInvalid = ref(false); // input 입력값 유효성 여부 flag

onMounted(() => {
  // 옵션 목록 div에 대하여 width: inherit 속성이 유효하지 않아 아래와 같이 처리
  // const parent = document.querySelector("#parent-div");
  // width.value = parent.clientWidth;  
  nextTick(() => {
    if (parentDiv.value) {
      width.value = parentDiv.value.clientWidth;
    } else {
      console.error("#parent-div 요소를 찾을 수 없습니다.");
    }
  });
});

// 입력란 내 x 버튼 렌더 여부 계산
// 입력란 비활성화 상태에서는 버튼 렌더하지 않음
const showDeleteButton = computed(() =>
  props.disabled ? false : selectedOption.value.description
);
// 카테고리 입력란 class 목록 계산
const inputClass = computed(() => {
  const classNames = ["mu-input"];

  const { description } = selectedOption.value;
  if (description) {
    // 입력값이 있을 때 그 입력값이 유효하면 테두리 색상 파란색으로, 유효하지 않으면 붉은색으로 처리
    const className = isInvalid.value ? "error" : "success";
    classNames.push(className);
  }

  return classNames.join(" ");
});

// 옵션 목록 아이템 스타일 계산
const optionItemStyle = () => {
  const height = props.descOnly ? "30px" : "55px";
  return `display: flex; align-items: center; padding-left: 10px; height: ${height}`;
};

// Input 값 변경 이벤트 핸들러
const onChangeInput = (e) => {
  const { target: { value } } = e;    
  let localIsInvalid = false;

  const localSelectedOption = JSON.parse(JSON.stringify(selectedOption.value));
  localSelectedOption.description = value;

  // 입력값에 따라 옵션 목록 필터 처리
  setOptionList(value);

  const matched = props.options.find((el) => el.description === value);
  if (!matched) {
    localIsInvalid = true;

    // 부모 컴포넌트의 validation 통과하지 않도록 value값 초기화
    if (localSelectedOption.value) {      
      localSelectedOption.value = "" ;
    }
  }

  isInvalid.value = localIsInvalid;

  selectedOption.value = localSelectedOption;
  emit("setData", JSON.parse(JSON.stringify(localSelectedOption)));
};

// 옵션 목록 변경 이벤트 핸들러
const setOptionList = (newValue) => {
  const localOptionList = props.options.filter((el) => {
    const { description, value } = el;

    return description.includes(newValue) ||
        value.toUpperCase().includes(newValue.toUpperCase());    
  });

  optionList.value = localOptionList;
  noResult.value = newValue && localOptionList.length === 0;
};

// 옵션 선택 이벤트 핸들러
const setSelectedOption = (option) => {
  const selected = JSON.parse(JSON.stringify(option));
  selectedOption.value = selected;
  emit("setData", selected);

  if (isInvalid.value) {
    isInvalid.value = false;
  }

  toggleOptionList(false);
};

// 입력란 내 x 버튼 클릭 이벤트 핸들러
const onClickDelete = () => {
  const option = {
    description: "",
    value: "",
  };

  setOptionList("");
  setSelectedOption(option);
};

// 옵션 목록 화면 렌더 여부 처리
const toggleOptionList = (toggle) => {
  showOptions.value = toggle;
};

// 입력된 값을 토대로 옵션 목록 like 필터 처리
const filterFunction = (item) => {
  return (
    item.description.includes(selectedOption.value.description) ||
    item.value.toUpperCase().includes(selectedOption.value.description.toUpperCase())
  );
};

// 입력란 내 엔터키 입력 이벤트 핸들러
const onEnterKeyUp = () => {
  const filtered = props.options.filter(filterFunction);
  const { description } = selectedOption.value;

  if (description && filtered.length === 1) {
    setSelectedOption(filtered[0]);
  }
};
</script>

<style lang="scss" scoped>
.option-box {
  position: fixed;
  z-index: 100;
  width: inherit;
  max-width: inherit;
  max-height: 250px;
  background-color: #ffffff;
  border: 1px solid #d6dce2;
  overflow-y: auto;
  border-radius: 3px;
  margin-top: 2px;
}

.option-item {
  cursor: pointer;

  & .item-value {
    font-weight: 700;
    color: #000000;
  }

  & .item-desc {
    font-weight: 400;
  }
}

.option-item:hover {
  background-color: #e5f3fe;
}

.item-container {
  display: flex; /* 좌우 배치를 위한 flexbox 설정 */
  align-items: center; /* 내용의 수직 정렬 (중앙 정렬) */
}
</style>
