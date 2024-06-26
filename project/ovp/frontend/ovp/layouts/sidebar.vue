<template>
  <nav id="sidebar">
    <div class="l-sidebar">
      <ul class="sidebar-list">
        <li class="sidebar-item is-sidebar-item-selected">
          <button class="sidebar-button">
            <div class="sidebar-icon">
              <svg-icon name="explore" class="svg-icon"></svg-icon>
            </div>
            <span class="sidebar-text">탐색</span>
          </button>
        </li>
        <li class="sidebar-item">
          <button class="sidebar-button">
            <div class="sidebar-icon">
              <svg-icon name="chart-multitype" class="svg-icon"></svg-icon>
            </div>
            <span class="sidebar-text">데이터모델 생성</span>
          </button>
        </li>
        <li class="sidebar-item">
          <button class="sidebar-button">
            <div class="sidebar-icon">
              <svg-icon name="governance" class="svg-icon"></svg-icon>
            </div>
            <span class="sidebar-text">거버넌스</span>
          </button>
        </li>
      </ul>
      <ul class="sidebar-list">
        <li>
          <button class="sidebar-button">
            <div class="sidebar-icon">
              <svg-icon name="custom-tune" class="svg-icon"></svg-icon>
            </div>
            <span class="sidebar-text">관리</span>
          </button>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
export default {
  name: "sidebar"
};
</script>

<style scoped>

</style>