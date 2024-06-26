<template>
  <div v-if="params.type === 'onOff'">
    <button @click="handleClick">
      <fa v-if="params.onOffKeys.includes(params.data[params.rowId])" :icon="params.icon.on" />
      <fa v-else :icon="params.icon.off" />
    </button>
  </div>
  <div v-else>
    <button
      v-if="params.type === 'button'"
      class="button button-primary-lighter button-lg"
      type="button"
      @click="handleClick"
    >
      <span class="button-title">{{ params.buttonText }}</span>
    </button>

    <button v-else-if="params.type === 'icon'" @click="handleClick">
      <fa :icon="params.icon" />
    </button>
  </div>
</template>

<script setup>
const props = defineProps({
  params: {
    type: Object
  }
});
const params = props.params;

const handleClick = () => {
  if (params.onClickRenderer) {
    params.onClickRenderer(params);
  }
};
</script>
