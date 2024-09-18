<template>
  <div class="ag-tooltip-header">
    현황
    <div class="tooltip" v-tooltip:top="tooltipContent">
      <span class="svg-icon">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="32"
          height="32"
          fill="currentColor"
          viewBox="0 0 32 32"
        >
          <path
            d="M17.4 24.4h-2.8v-2.8h2.8v2.8Zm-7-11.2c0-3.094 2.506-5.6 5.6-5.6s5.6 2.506 5.6 5.6c0 1.796-1.106 2.763-2.183 3.704-1.021.893-2.017 1.762-2.017 3.296h-2.8c0-2.55 1.32-3.56 2.479-4.45.91-.697 1.721-1.32 1.721-2.55 0-1.54-1.26-2.8-2.8-2.8-1.54 0-2.8 1.26-2.8 2.8h-2.8Z"
          />
          <path
            fill-rule="evenodd"
            d="M30 16c0 7.732-6.268 14-14 14S2 23.732 2 16 8.268 2 16 2s14 6.268 14 14Zm-2 0c0 6.627-5.373 12-12 12S4 22.627 4 16 9.373 4 16 4s12 5.373 12 12Z"
            clip-rule="evenodd"
          />
        </svg>
      </span>
    </div>
  </div>
</template>

<script setup>
const tooltipContent = `Queued: 수집 작업이 시작 대기 상태에 있음 <br/> Running: 현재 수집 작업이 진행 중 <br/> Success: 수집 작업이 성공적으로 완료됨 <br/> Failed: 오류가 발생하여 작업이 완료되지 않음 <br/> PartialSuccess: 일부는 성공적으로 수집되었으나, 일부는 실패함`;
</script>

<style lang="scss">
@import "./header-tooptip.scss";
</style>
