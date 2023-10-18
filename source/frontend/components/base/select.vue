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
    </div>
    <div class="select-container" id="select-01">
      <ul class="select-container-list" role="listbox">
          <li class="select-container-item">
            <button class="select-container-button" type="button" role="option">
              <span class="select-container-text">item 01</span>
            </button>
          </li>
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

