<template>
  <!-- 현재 위치에서 클릭시 is-open 클래스 추가 -->
  <div class="select">
    <div class="select-selector" role="combobox" aria-expanded="false" aria-haspopup="true">
      <button class="select-selector-button" type="button" title="열기">
        <slot name="title">
          <span class="select-selector-title">선택하세요</span>
        </slot>
        <span class="hidden">선택</span>
        <svg-icon class="svg-icon select-selector-icon" name="chevron-down-medium" aria-hidden="true"></svg-icon>
      </button>
      <div class="select-selector-input" style="display: none">
        <baseTextInput class="text-input" type="text" placeholder="검색어 입력"></baseTextInput>
      </div>
      <baseButton class="select-selector-close-button" style="display: none">
        <svg-icon class="svg-icon" name="chevron-up-medium" aria-hidden="true"></svg-icon>
      </baseButton>
    </div>
    <div class="select-container" id="select-01">
      <ul class="select-container-list" role="listbox">
          <li class="select-container-item">
            <baseCheckbox class="checkbox-indeterminate" role="option">전체</baseCheckbox>
          </li>
        <li class="select-container-item">
          <baseCheckbox role="option">item 01</baseCheckbox>
        </li>
          <li class="select-container-item">
            <button class="select-container-button" type="button" role="option">
              <span class="select-container-text">item 02</span>
            </button>
          </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">

</script>

