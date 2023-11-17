<template>
  <div class="l-header">
    <h1 class="logo">데이터 패브릭</h1>
    <nav>
      <ul class="gnb">
        <li class="gnb-item" v-for="menu in menus" :key="menu">
          <nuxt-link class="gnb-button" :to="menu.link">{{ menu.label }}</nuxt-link>
        </li>
      </ul>
    </nav>
    <div class="header-setting">
      <BaseButton class="button-normal">
        <span class="button-text">데이터 패브릭 관리</span>
      </BaseButton>
      <BaseButton class="button-primary">
        <span class="button-text">데이터 모델 생성</span>
      </BaseButton>
      <BaseButton class="button-primary">
        <span class="button-text">데이터 모델뷰 생성</span>
      </BaseButton>
    </div>
    <div class="header-management">
      <BaseButton class="button-icon">
        <svg-icon name="app-switcher" class="svg-icon" />
      </BaseButton>
      <BaseButton class="button-icon">
        <svg-icon name="setting" class="svg-icon" />
      </BaseButton>
      <BaseButton class="button-icon">
        <svg-icon name="user" class="svg-icon" />
      </BaseButton>
      <BaseButton class="button-icon">
        <svg-icon name="arrow-down" class="svg-icon" />
      </BaseButton>
    </div>
  </div>
</template>

<script setup lang="ts">
const router = useRouter();
const menus = [
  {
    key: "browse",
    label: "데이터 모델 브라우저",
    link: "/data-fabric/browse"
  },
  {
    key: "management",
    label: "연결정보 관리",
    link: "/data-fabric/management"
  },
  {
    key: "knowledgeGraph",
    label: "지식 그래프",
    link: ""
  }
];
</script>
