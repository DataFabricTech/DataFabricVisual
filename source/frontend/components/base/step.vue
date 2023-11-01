<template>
  <div class="step">
    <div class="step-item" v-for="item in state.stepList"
         :key="item.id" :class="[item.isCurrent ? 'is-current':'']">
      <div class="step-icon">
        <span class="step-icon-text">
          {{ item.num }}
        </span>
      </div>
      <div class="step-title">
        <span class="step-title-text">
          {{ item.title }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

const state = reactive({
  stepList: [
    {
      id: 1,
      num: 1,
      title: "저장소 유형 선택",
      isCurrent: true
    },
    {
      id: 2,
      num: 2,
      title: "기본 정보 입력",
      isCurrent: false
    },
    {
      id: 3,
      num: 3,
      title: "상세 정보 입력",
      isCurrent: false
    },
    {
      id: 4,
      num: 4,
      title: "추가 정보 입력",
      isCurrent: false
    }
  ]
});


</script>
