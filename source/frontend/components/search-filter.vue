<template>
  <div class="search-filter">
    <div class="search-filter-body">
      <form action="" class="form">
        <ul class="form-list">
          <li class="form-item">
            <div class="form-label">
              <strong class="form-title">검색어</strong>
            </div>
            <div class="form-content">
              <BaseSelect class="select-lg"></BaseSelect>
              <label for="01" class="hidden">검색어</label>
              <BaseTextInput id="01" class="w-[522px] text-input-lg"></BaseTextInput>
            </div>
          </li>
          <li class="form-item">
            <div class="form-label">
              <strong class="form-title">기간 선택</strong>
            </div>
            <div class="form-content">
              <BaseSelect class="select-lg"></BaseSelect>
              <!--            TODO: [퍼블리싱] datepicker 추가 예정-->
            </div>
          </li>
          <li class="form-item">
            <div class="form-label">
              <strong class="form-title">연결정보</strong>
            </div>
            <div class="form-content">
              <BaseSelect class="select-lg"></BaseSelect>
            </div>
          </li>
          <li class="form-item">
            <div class="form-label">
              <strong class="form-title">도메인</strong>
            </div>
            <div class="form-content">
              <BaseSelect class="select-lg"></BaseSelect>
            </div>
          </li>
          <li class="form-item">
            <div class="form-label">
              <strong class="form-title">데이터모델</strong>
            </div>
            <div class="form-content">
              <BaseSelect class="select-lg"></BaseSelect>
              <BaseSelect class="select-lg"></BaseSelect>
              <BaseSelect class="select-lg"></BaseSelect>
            </div>
          </li>
        </ul>
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
</script>
