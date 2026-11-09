<template>
  <div class="inner-search" id="parent-div" ref="parentDiv">
    <div class="mu-input-icon w260">
      <input type="text" :class="inputClass" placeholder="태그를 입력하세요." :disabled="props.disabled"
        v-model="selectedOption.tag_name" @input="onChangeInput" @focus="toggleOptionList(true)"
        @blur="toggleOptionList(false)" />
      <button type="button" class="mu-btn mu-btn-delete" v-show="showDeleteButton" @click="onClickDelete">
        <i class="mu-icon-svg item-delete"></i>
        <span>삭제</span>
      </button>
    </div>
    <div class="option-box slim-scroll" v-show="showOptions" :style="`width: ${width}px;`">
      <ul class="mu-list">
        <li v-if="noResult" :style="optionItemStyle">
          <div>검색 결과가 없습니다.</div>
        </li>
        <li v-else v-for="(option, index) in optionList" :key="index" :value="option.tag_name" :style="optionItemStyle"
          class="option-item" @mousedown="setSelectedOption(option)">
          <div class="item-container">
            <p class="item-value">{{ option.tag_name }}</p>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed, nextTick, watch } from 'vue';

const emit = defineEmits(["setData"]);
const props = defineProps([
  "options",
  "disabled",
  "setData"
]);

const parentDiv = ref<HTMLElement | null>(null);

const selectedOption = ref({
  tag_name: ""
});
const optionList = ref(props.options); // 옵션 목록
const showOptions = ref(false); // 옵션 목록 렌더 여부 제어 flag
const width = ref(0); // 옵션 목록 영역 width
const noResult = ref(false); // 옵션 검색 결과 유무 flag
const isInvalid = ref(false); // input 입력값 유효성 여부 flag

onMounted(() => {
  // 옵션 목록 div에 대하여 width: inherit 속성이 유효하지 않아 아래와 같이 처리
  nextTick(() => {
    if (parentDiv.value) {
      width.value = parentDiv.value.clientWidth;
    } else {
      console.error("#parent-div 요소를 찾을 수 없습니다.");
    }
  });

  onClickDelete();
});

// 입력란 내 x 버튼 렌더 여부 계산
// 입력란 비활성화 상태에서는 버튼 렌더하지 않음
const showDeleteButton = computed(() =>
  props.disabled ? false : selectedOption.value.tag_name
);
// 입력란 class 목록 계산
const inputClass = computed(() => {
  const classNames = ["mu-input"];
  return classNames.join(" ");
});

watch(() => selectedOption.value.tag_name, (newVal, oldVal) => {
  const krEnOnly = /^[ㄱ-ㅎ가-힣]*$/;
  if (!krEnOnly.test(newVal)) {
    selectedOption.value.tag_name = newVal.replace(/[^ㄱ-ㅎ가-힣a-zA-Z0-9]/g, '');
  }
})

// 옵션 목록 아이템 스타일 계산
const optionItemStyle = () => {
  const height = "30px";
  return `display: flex; align-items: center; padding-left: 10px; height: ${height}`;
};

// Input 값 변경 이벤트 핸들러
const onChangeInput = (e) => {
  const { target: { value } } = e;
  let localIsInvalid = false;

  const localSelectedOption = JSON.parse(JSON.stringify(selectedOption.value));
  localSelectedOption.tag_name = value;

  // 입력값에 따라 옵션 목록 필터 처리
  setOptionList(value);

  const matched = props.options.find((el) => el.tag_name === value);
  if (!matched) {
    localIsInvalid = true;
  }

  isInvalid.value = localIsInvalid;

  selectedOption.value = localSelectedOption;
  if (localIsInvalid) {
    emit("setData", JSON.parse(JSON.stringify(localSelectedOption)));
  }
};

// 옵션 목록 변경 이벤트 핸들러
const setOptionList = (newValue) => {
  const localOptionList = props.options.filter((el) => {
    const { tag_name } = el;

    return tag_name.includes(newValue);
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
    tag_name: ""
  };

  noResult.value = false;
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
    item.tag_name.includes(selectedOption.value.tag_name)
  );
};

watch(
  () => props.options,
  (newVal, oldVal) => {
    onClickDelete()
  }
);
</script>

<style lang="scss" scoped>
.option-box {
  position: absolute;
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
  display: flex;
  /* 좌우 배치를 위한 flexbox 설정 */
  align-items: center;
  /* 내용의 수직 정렬 (중앙 정렬) */
}
</style>
