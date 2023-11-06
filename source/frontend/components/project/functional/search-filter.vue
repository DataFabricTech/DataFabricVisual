<template>
  <div class="search-filter">
    <div class="search-filter-body">
      <form action="" class="form flex-row items-center">
        <div class="form-list">
          <div class="form-item">
            <div class="form-label">
              <strong class="form-title">검색어</strong>
            </div>
            <div class="form-content">
              <BaseSelect class="select-lg"></BaseSelect>
              <label for="01" class="hidden-text">검색어</label>
              <BaseTextInput id="01" class="max-w-[522px] text-input-lg"></BaseTextInput>
            </div>
          </div>
          <div class="form-item">
            <div class="form-label">
              <strong class="form-title">기간 선택</strong>
            </div>
            <div class="form-content">
              <BaseSelect class="select-lg"></BaseSelect>
              <DatePicker
                v-model:modelValue="args.dateRange"
                :type="args.rangeType"
                class="date-picker">
              </DatePicker>
            </div>
          </div>
          <div class="form-item">
            <div class="form-label">
              <strong class="form-title">연결정보</strong>
            </div>
            <div class="form-content">
              <BaseSelect class="select-lg"></BaseSelect>
            </div>
          </div>
          <div class="form-item">
            <div class="form-label">
              <strong class="form-title">도메인</strong>
            </div>
            <div class="form-content">
              <BaseSelect class="select-lg"></BaseSelect>
            </div>
          </div>
          <div class="form-item">
            <div class="form-label">
              <strong class="form-title">데이터모델</strong>
            </div>
            <div class="form-content">
              <BaseSelect class="select-lg"></BaseSelect>
              <BaseSelect class="select-lg"></BaseSelect>
              <BaseSelect class="select-lg"></BaseSelect>
            </div>
          </div>
        </div>
        <div class="form-action">
          <BaseButton class=" button-lg button-primary">
            <span class="button-text">검색</span>
          </BaseButton>
          <BaseButton class=" button-lg button-negative">
            <span class="button-text">초기화</span>
          </BaseButton>
        </div>
      </form>
    </div>
    <div class="search-filter-footer">
      <div class="h-group gap-[16px]">
        <BaseBadge class="bg-marker-cyan">HDFS</BaseBadge>
        <BaseBadge class="bg-marker-purple">공간</BaseBadge>
      </div>
      <BaseButton class="button-ghost">
        <span class="button-text">상세닫기</span>
      </BaseButton>
    </div>
  </div>
</template>

<script setup lang="ts">

import DatePicker from "/components/common/date-picker/date-picker.vue";


const meta: Meta<typeof DatePicker> = {
  component: DatePicker,
};

let args = {
  date: "2023-11-22",
  dateRange: ["2023-11-10", "2023-11-22"],
  // storybook 에 object 형식으로 뜸
  dateType: "time",
  rangeType: "date"
};

</script>
