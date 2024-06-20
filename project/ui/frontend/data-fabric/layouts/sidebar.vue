<template>
  <div id="sidebar">
    <div class="sidebar-layout">
      <ul class="sidebar-list">
        <li class="sidebar-item is-sidebar-item-selected">
          <button class="sidebar-button">
<!--            <svg-icon name="explore" class="sidebar-icon svg-icon"></svg-icon>-->
            <div class="sidebar-icon">
              <svg class="svg-icon" xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" viewBox="0 0 32 32">
                <path d="M8.667 11H2V9h6.667v2Zm0 5H2v2h6.667v-2ZM2 25h13.333v-2H2v2Z"/>
                <path fill-rule="evenodd" d="M20 20a6 6 0 1 0 0-12 6 6 0 0 0 0 12Zm0 2a8 8 0 1 0 0-16 8 8 0 0 0 0 16Z" clip-rule="evenodd"/>
                <path fill-rule="evenodd" d="m29 25.5-5.027-6.532 1.56-1.253 5.026 6.533L29 25.5Z" clip-rule="evenodd"/>
              </svg>
            </div>
            <span class="sidebar-text">탐색</span>
          </button>
        </li>
        <li class="sidebar-item">
          <button class="sidebar-button">
            <div class="sidebar-icon">
              <svg-icon name="chart-multitype" class="sidebar-icon svg-icon"></svg-icon>
            </div>
            <span class="sidebar-text">데이터모델 생성</span>
          </button>
        </li>
        <li class="sidebar-item">
          <button class="sidebar-button">
            <div class="sidebar-icon">
              <svg-icon name="governance" class="sidebar-icon svg-icon"></svg-icon>
            </div>
            <span class="sidebar-text">거버넌스</span>
          </button>
        </li>
      </ul>
      <ul class="sidebar-list">
        <li>
          <button class="sidebar-button">
            <div class="sidebar-icon">
              <svg-icon name="custom-tune" class="sidebar-icon svg-icon"></svg-icon>
            </div>
            <span class="sidebar-text">관리</span>
          </button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "sidebar"
};
</script>

<style scoped>

</style>