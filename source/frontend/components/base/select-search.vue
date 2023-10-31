<template>
  <!-- 현재 위치에서 클릭시 is-open 클래스 추가 -->
  <div class="select">
    <div class="select-selector" role="combobox" aria-expanded="false" aria-haspopup="true">
<!-- TODO:[개발] 1. default는 select-selector-button 스타일 display:block / select-selector-input, select-selector-close-button 스타일 display:none;
                2.text-input 전환시 select-selector-button 스타일 display:none; 위 두개는 display:block처리
                3. select-lg 일 경우 baseTextInput도 text-input-lg / select-sm 일 경우 text-input-sm 적용 필요-->
      <button class="select-selector-button" style="display: none;" type="button" title="열기">
        <slot name="title">
          <span class="select-selector-title">선택하세요</span>
        </slot>
        <span class="hidden">선택</span>
        <svg-icon class="svg-icon select-selector-icon" name="chevron-down-medium" aria-hidden="true"></svg-icon>
      </button>
      <div class="select-selector-input">
        <baseTextInput class="text-input" type="text" placeholder="검색어 입력"></baseTextInput>
      </div>
      <baseButton class="button-icon button-link select-selector-close-button">
        <svg-icon class="svg-icon" name="chevron-up-medium" aria-hidden="true"></svg-icon>
      </baseButton>
    </div>
    <div class="select-container" id="select-01">
      <ul class="select-container-list" role="listbox">
        <li class="select-container-item">
          <button class="select-container-button" type="button" role="option">
            <span class="select-container-text">item 01</span>
          </button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">

</script>

