<template>
  <form action="" class="form">
    <h4 class="form-subject">가로유형 폼 타이틀</h4>
    <div class="form-list">
      <div class="form-item">
        <div class="form-label">
          <strong class="form-title">label</strong>
        </div>
        <div class="form-content">
          <BaseSelect class="select-lg"></BaseSelect>
          <label for="01" class="hidden-text">검색어</label>
          <BaseTextInput id="01" class="w-[522px] text-input-lg"></BaseTextInput>
        </div>
      </div>
      <div class="form-item">
        <div class="form-label">
          <strong class="form-title">label</strong>
        </div>
        <div class="form-content">
          <BaseRadio class="radio-lg" id="radio-form-01">riado-01</BaseRadio>
          <BaseRadio class="radio-lg" id="radio-form-01">riado-02</BaseRadio>
        </div>
      </div>
      <div class="form-item">
        <div class="form-label">
          <strong class="form-title">label</strong>
        </div>
        <div class="form-content">
          <BaseCheckbox class="checkbox-lg" id="check-form-01">체크해주세요.</BaseCheckbox>
        </div>
      </div>
    </div>
  </form>
</template>
