<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <select-box class="select-clean select-lg"></select-box>
      <button class="button button-primary w-20">저장</button>
    </div>
  </div>
  <div class="section-contents section-contents-p-0 bg-white">
    <div class="l-split">
      <div class="work-list">
        <div class="l-top-bar">
          <span class="">선택된 데이터 모델 (0)</span>
          <button class="button button-secondary-stroke">추가</button>
        </div>
        <div class="menu menu-data">
          <div class="menu-head">
            <div class="h-group">
              <div class="search-input">
                <label class="hidden-text" for="data-menu-search">데이터 모델 검색</label>
                <input id="data-menu-search" class="text-input" value="검색어 입력" />
                <svg-icon class="text-input-icon" name="search"></svg-icon>
                <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                  <span class="hidden-text">지우기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
              </div>
              <button class="button button-neutral-ghost button-lg">
                <span class="hidden-text">리셋</span>
                <svg-icon class="svg-icon" name="reset"></svg-icon>
              </button>
            </div>
            <div class="filters">
              <div class="h-group">
                <select-box class="select-clean select-sm"></select-box>
                <select-box class="select-clean select-sm"></select-box>
              </div>
              <select-box class="select-sm"></select-box>
            </div>
          </div>
          <ul class="menu-list">
            <li class="menu-item">
              <div class="checkbox">
                <input type="checkbox" id="checkbox-menu" class="checkbox-input" />
                <label for="checkbox-menu" class="checkbox-label">
                  Checkbox
                  <span class="checkbox-subtext">(Optional)</span>
                </label>
              </div>
            </li>
            <li class="menu-item">
              <div class="checkbox">
                <input type="checkbox" id="checkbox-menu" class="checkbox-input" />
                <label for="checkbox-menu" class="checkbox-label">
                  Checkbox
                  <span class="checkbox-subtext">(Optional)</span>
                </label>
              </div>
            </li>
            <li class="menu-item">
              <div class="checkbox">
                <input type="checkbox" id="checkbox-menu" class="checkbox-input" />
                <label for="checkbox-menu" class="checkbox-label">
                  Checkbox
                  <span class="checkbox-subtext">(Optional)</span>
                </label>
              </div>
              <div class="menu-button-group">

              </div>
            </li>
            <li class="menu-item">
              <div class="checkbox">
                <input type="checkbox" id="checkbox-menu" class="checkbox-input" />
                <label for="checkbox-menu" class="checkbox-label">
                  Checkbox
                  <span class="checkbox-subtext">(Optional)</span>
                </label>
              </div>
              <div class="menu-button-group">

              </div>
            </li>
            <li class="menu-item">
              <div class="checkbox">
                <input type="checkbox" id="checkbox-menu" class="checkbox-input" />
                <label for="checkbox-menu" class="checkbox-label">
                  Checkbox
                  <span class="checkbox-subtext">(Optional)</span>
                </label>
              </div>
              <div class="menu-button-group">

              </div>
            </li>
          </ul>
        </div>
      </div>
      <div class="work-page">
        <div class="l-top-bar">
          <span class="">선택된 데이터 모델 (0)</span>
          <button class="button button-secondary-stroke">추가</button>
        </div>
        <div class="data-fusion"></div>
      </div>
    </div>
    <div class="l-split">
      <div class="work-list">

      </div>
      <div class="panel-contents">
        <div class="data-result">

        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import Header from "../layouts/header.vue";
import Sidebar from "../layouts/sidebar.vue";
import SelectBox from "../../../../common/components/extends/select-box/SelectBox.vue";


export default {
  name: "creation",
  components: { Sidebar, Header, SelectBox }
};
</script>

<style scoped>

</style>