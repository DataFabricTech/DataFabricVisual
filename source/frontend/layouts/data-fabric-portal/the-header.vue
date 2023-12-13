<template>
    <div class="l-header">
      <h1 class="logo">
        데이터 패브릭
      </h1>
      <div class="header-management">
        <BaseButton class="button-icon" @click="toggleLnb">
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
        <div class="lnb" v-if="showLnb">
          <div class="lnb-top-bar">
            <span class="lnb-title">서비스 바로가기</span>
            <baseButton class="button-link button-icon" @click="closeLnb">
              <svg-icon name="close" class="svg-icon" />
            </baseButton>
          </div>
          <ul class="lnb-list">
            <li class="lnb-item">
              <a class="lnb-link" href="#">데이터 패브릭</a>
            </li>
            <li class="lnb-item">
              <a class="lnb-link" href="#">IRIS 서비스</a>
            </li>
            <li class="lnb-item">
              <a class="lnb-link" href="#">분석 시각화</a>
            </li>
            <li class="lnb-item">
              <a class="lnb-link" href="#">Playground</a>
            </li>
            <li class="lnb-item">
              <a class="lnb-link" href="#">GitHub</a>
            </li>
            <li class="lnb-item">
              <a class="lnb-link" href="#">Jupyter</a>
            </li>
            <li class="lnb-item">
              <a class="lnb-link" href="#">OPEN API</a>
            </li>
            <li class="lnb-item">
              <a class="lnb-link" href="#">MYCloud</a>
            </li>
            <li class="lnb-item">
              <a class="lnb-link" href="#">분석시각화</a>
            </li>
          </ul>
        </div>
      </div>
    </div>
</template>

<script lang="ts">
export default {
  data() {
    return {
      showLnb: false
    };
  },
  methods: {
    toggleLnb() {
      this.showLnb = !this.showLnb;
    },
    closeLnb() {
      this.showLnb = false;
    }
  }
};
</script>

