<template>
  <dl class="data-status">
    <dt class="data-status-label">
      <svg-icon :name= "props.icon" class="svg-icon"/>
      <span class="data-status-name">
        {{ props.title }}
      </span>
    </dt>
    <dd class="data-status-num">
      {{ props.statusValue }}
    </dd>
  </dl>
</template>

<script setup lang="ts">

  const props = defineProps({
    icon: {
      type: String,
      default: 'data'
    },
    title: {
      type: String
    },
    statusValue: {
      type: String
    },
  })
</script>
