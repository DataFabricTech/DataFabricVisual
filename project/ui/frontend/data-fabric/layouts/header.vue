<template>
  <header id="header">
    <div class="header-layout">
      <div class="header-search">
        <h1 class="logo">
          <a href="#" class="logo-link" title="메인 이동">
            Open VDAP Portal
            <span class="hidden-text">logo</span>
          </a>
        </h1>
        <div class="text-input-group">
          <label class="hidden-text" for="data-list-example">label</label>
          <input id="data-list-example" class="text-input" placeholder="검색어 입력" />
          <svg-icon class="text-input-icon" name="user"></svg-icon>
          <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
            <span class="hidden-text">지우기</span>
            <svg-icon class="button-icon" name="close"></svg-icon>
          </button>
          <button class="text-input-search-button button button-neutral-ghost">
            <span class="hidden-text">검색</span>
            <svg-icon class="button-icon" name="search"></svg-icon>
          </button>
        </div>
      </div>
      <div class="profile">
          <span class="profile-avatar">
            <img class="profile-img" src="" alt="프로필 이미지">
          </span>
        <div class="profile-text">root</div>
        <button class="button button-sm button-neutral-ghost">
          <svg-icon name="chevron-down-medium"></svg-icon>
          <span class="hidden-text">내 메뉴</span>
        </button>
        <div class="dropdown" style="top: 40px;right: 16px;">
          <ul class="dropdown-list">
            <li class="dropdown-item">
              <button class="dropdown-button">
                <span class="dropdown-text">내 정보</span>
              </button>
            </li>
            <li class="dropdown-item">
              <button class="dropdown-button">
                <span class="dropdown-text">나의 데이터 현황</span>
              </button>
            </li>
            <li class="dropdown-item">
              <button class="dropdown-button">
                <span class="dropdown-text">나의 조직</span>
              </button>
            </li>
            <li class="dropdown-item">
              <button class="dropdown-button">
                <span class="dropdown-text">알림</span>
              </button>
            </li>
            <li class="dropdown-item dropdown-item-line">
              <button class="dropdown-button">
                <span class="dropdown-text">로그아웃</span>
              </button>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
export default {
  name: "header"
};
</script>

<style scoped>

</style>