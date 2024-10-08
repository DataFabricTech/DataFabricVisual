<template>
  <category-graph />
<!--  <network-diagram />-->
  <!-- 모델 리스트 -->
  <div class="visual-model-list">
    <div class="visual-model-list-head">
      <div class="breadcrumb">
        <ul class="breadcrumb-list">
          <li class="breadcrumb-item">
            <span class="breadcrumb-link">1depth</span>
          </li>
          <li class="breadcrumb-item is-breadcrumb-selected">
            <span class="breadcrumb-link">데이터 모델</span>
          </li>
        </ul>
      </div>
      <button class="search-input-action-button button button-neutral-ghost button-sm" type="button"
              @click="closeModellist">
        <span class="hidden-text">지우기</span>
        <svg-icon class="button-icon" name="close"></svg-icon>
      </button>
    </div>
    <div class="p-3 h-full">
      <strong>총 <em class="primary">68건</em></strong>
      <div class="menu menu-data menu-lg">
        <ul class="menu-list">
          <li class="menu-item">
            <div class="checkbox">
              <input type="checkbox" id="checkbox-menu-01" class="checkbox-input" checked />
              <label for="checkbox-menu-01" class="checkbox-label">
                <span class="hidden-text">label</span>
              </label>
            </div>
            <button class="menu-button">
              <div class="type-img type-img-oracle"></div>
              <span class="menu-text">데이터 모델</span>
              <span class="menu-subtext">(소유자)</span>
            </button>
            <div class="menu-button-group">
              <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
              <button class="button button-neutral-ghost button-sm">
                <span class="hidden-text">북마크</span>
                <svg-icon class="svg-icon secondary" name="tag-fill"></svg-icon>
              </button>
            </div>
          </li>
          <li class="menu-item" v-for="menu in 26" :key="menu">
            <div class="checkbox">
              <input type="checkbox" id="checkbox-menu-02" class="checkbox-input" />
              <label for="checkbox-menu-02" class="checkbox-label">
                <span class="hidden-text">label</span>
              </label>
            </div>
            <button class="menu-button">
              <div class="type-img type-img-oracle"></div>
              <span class="menu-text">데이터 모델</span>
              <span class="menu-subtext">(소유자)</span>
            </button>
            <div class="menu-button-group">
              <button class="button button-neutral-ghost button-sm">
                <span class="hidden-text">북마크</span>
                <svg-icon class="svg-icon" name="tag"></svg-icon>
              </button>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import CategoryGraph from "./graph/category-graph.vue";
import NetworkDiagram from "./graph/network-diagram.vue";
</script>

<style lang="scss" scoped></style>
