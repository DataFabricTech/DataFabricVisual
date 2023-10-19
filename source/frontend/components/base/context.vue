<template>
  <div class="context">
    <ul class="context-list">
      <li class="context-item">
        <!--        현재 위치인 경우 is-selected 클래스 추가 -->
        <button class="context-button is-selected" type="button">
          <span class="context-text">선택된 상태</span>
        </button>
      </li>
      <li class="context-item">
        <button class="context-button" type="button">
          <span class="context-text">이름이 길어지면 말줄임</span>
        </button>
      </li>
      <li class="context-item">
        <button class="context-button" type="button">
          <span class="context-text">우측 아이콘 유형</span>
          <svg-icon name="string-ascending" class="svg-icon" />
        </button>
      </li>
      <li class="context-item">
        <button class="context-button" type="button">
          <span class="context-text">우측 아이콘 유형</span>
          <svg-icon name="string-descending" class="svg-icon" />
        </button>
      </li>
      <li class="context-item">
        <button class="context-button" type="button" disabled>
          <span class="context-text">비활성화 상태</span>
        </button>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">

</script>
