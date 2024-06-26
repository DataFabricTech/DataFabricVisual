<template>
  <div>
    <div v-if="params.rendererKey === 'bookmark'">
      <button @click="handleClick">
        {{ params.onOffKeys[params.rendererKey].includes(params.data[params.rowId]) }}
      </button>
    </div>
    <div v-else>
      <button v-if="params.type === 'button'" @click="handleClick">
        {{ params.buttonText }}
      </button>

      <button v-else-if="params.type === 'icon'" @click="handleClick">ICON {{ params.buttonText }}</button>
    </div>
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
