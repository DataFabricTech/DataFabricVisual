<template>
  <nav id="sidebar">
    <div class="l-sidebar">
      <ul class="sidebar-list">
        <li class="sidebar-item is-sidebar-item-selected">
          <nuxt-link :to="'/portal/main'" class="sidebar-button">
            <div class="sidebar-icon">
              <svg-icon name="explore" class="svg-icon"></svg-icon>
            </div>
            <span class="sidebar-text">탐색</span>
          </nuxt-link>
        </li>
        <li class="sidebar-item">
          <nuxt-link :to="''" class="sidebar-button">
            <div class="sidebar-icon">
              <svg-icon name="chart-multitype" class="svg-icon"></svg-icon>
            </div>
            <span class="sidebar-text">데이터모델 생성</span>
          </nuxt-link>
        </li>
        <li class="sidebar-item">
          <nuxt-link :to="''" class="sidebar-button">
            <div class="sidebar-icon">
              <svg-icon name="governance" class="svg-icon"></svg-icon>
            </div>
            <span class="sidebar-text">거버넌스</span>
          </nuxt-link>
        </li>
      </ul>
      <ul class="sidebar-list">
        <li>
          <nuxt-link :to="''" class="sidebar-button">
            <div class="sidebar-icon">
              <svg-icon name="custom-tune" class="svg-icon"></svg-icon>
            </div>
            <span class="sidebar-text">관리</span>
          </nuxt-link>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
export default {
  name: "sidebar",
};
</script>

<style scoped></style>
