<template>
  <button type="button" :class="[
    { 'mu-btn-primary': buttonType === 'add' },
    'mu-btn',
    'mu-btn-sm',
    'mu-btn-icon-only'
  ]" :title="buttonText" v-if="showButton" @click.stop="onClick" :id="`setup_${item.guid}`">
    <i :class="['mu-icon-svg', buttonType === 'add' ? 'add' : 'more']"></i><span>{{ buttonText }}</span>
  </button>
</template>

<script setup lang="ts">
import { useEventStore } from "~/store/event.ts";

// interface
interface Props {
  buttonType: string;
  item: any;
}

const props = defineProps<Props>();
const emit = defineEmits(["onToggleMetaDataTooltip", "onClickTableBodyButtons"]);

const eventStore = useEventStore();

const buttonText = computed(() => {
  return `카테고리 ${props.buttonType === 'add' ? '추가' : '더보기'}`;
});

const showButton = computed(() => {
  let show = false;

  if (props.buttonType === 'more') {
    // 더보기 버튼 렌더 시
    if (props.item.classifications && props.item.classifications.length > 5) {
      // 카테고리 갯수 5개 이상이면 더보기 버튼 생성
      show = true;
    }
  } else {
    // 추가 버튼 렌더 시
    show = true;
  }

  return show;
});

//method
const onClick = (e) => {
  if (props.buttonType === 'more') {
    // 더보기 버튼일 경우
    eventStore.emit('onToggleMetaDataTooltip', e, props.item, 'more');
  } else {
    // 추가 버튼일 경우
    eventStore.emit('onClickTableBodyButtons', 'category-map', props.item);
  }
};
</script>
