<template>
  <!--      기본: normal, 정보: info, 성공: success, 경고: warning, 에러: error (icon도 동일명)-->
  <div v-if="!isClose" :class="'notification-' + currentMessage.theme" class="notification">
    <div class="notification-icon">
      <svg-icon :name="currentMessage.theme as string" class="svg-icon"></svg-icon>
    </div>
    <div class="notification-content">
      <div v-html="currentMessage.message" />
    </div>
    <base-button v-if="currentMessage.useClose" class="button-icon button-link button-sm" title="닫기" @click="onClose">
      <span>닫기</span>
      <svg-icon class="svg-icon" name="close"></svg-icon>
    </base-button>
    <base-button :disabled="currentIndex === 0" class="button-icon button-link button-sm" @click="onPrev">
      <span> &lt; </span>
    </base-button>
    <base-button
      :disabled="currentIndex === Object.keys(props.messages).length - 1"
      class="button-icon button-link button-sm"
      @click="onNext"
    >
      <span> &gt; </span>
    </base-button>
    <base-button @click="onRefresh">@</base-button>
  </div>
</template>

<script lang="ts" setup>
import type { PropType } from "vue";
import { computed, ref } from "vue";
import type { INotificationProp } from "./notification";

const props = defineProps({
  messages: {
    type: Array as PropType<INotificationProp[]>,
    default: () => []
  }
});

const currentIndex = ref(0);

const currentMessage = computed(() => {
  const message = props.messages[currentIndex.value];
  if (instanceOfNotificationProp(message)) {
    return props.messages[currentIndex.value];
  }

  return {
    theme: "normal",
    useClose: true,
    link: "",
    message: ""
  };
});

const isClose = ref(false);

const emits = defineEmits<{
  (e: "refresh"): void;
}>();

function onPrev() {
  currentIndex.value--;
  if (currentIndex.value < 0) {
    currentIndex.value = 0;
  }
}
function onNext() {
  currentIndex.value++;
  const length = Object.keys(props.messages).length;
  if (currentIndex.value === length) {
    currentIndex.value = length - 1;
  }
}
function onClose() {
  isClose.value = true;
}
function onRefresh() {
  emits("refresh");
}

function instanceOfNotificationProp(data: any): data is INotificationProp {
  return data;
}
</script>
