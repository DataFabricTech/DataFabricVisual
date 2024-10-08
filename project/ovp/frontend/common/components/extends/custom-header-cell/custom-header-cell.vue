<!-- CustomHeader.vue -->
<template>
  <div @contextmenu.prevent="onRightClick">
    {{ params.displayName }}
  </div>
</template>

<script setup lang="ts">
import { defineProps } from "vue";
import { useClipboard } from "@vueuse/core";
import VueContextMenu from "@imengyu/vue3-context-menu";

const currentText = ref("");
const { copy } = useClipboard({
  source: currentText,
  legacy: true
});

const props = defineProps<{
  params: any;
}>();

const onRightClick = (event: MouseEvent) => {
  event.preventDefault();
  VueContextMenu.showContextMenu({
    x: event.x,
    y: event.y,
    items: [
      {
        label: "복사 (컬럼 이름)",
        onClick: () => {
          copy(`${props.params.fqn}.${props.params.displayName}`);
        }
      },
      {
        label: "복사 (모든 컬럼 이름)",
        onClick: () => {
          copy(props.params.gridColumnDefs.map((fieldObj) => `${props.params.fqn}.${fieldObj.field}`).join(", "));
        }
      }
    ]
  });
};
</script>

<style scoped></style>
