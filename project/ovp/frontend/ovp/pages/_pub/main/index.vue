<template>
  <div class="section-contents section-contents-grid" style="flex-wrap: wrap">
    <div class="grid-content">
      <div class="l-top-bar">
        <span>최근 탐색 데이터</span>
        <button class="button link-button-support">
          <span class="button-title">모두 보기</span>
        </button>
      </div>
      <div class="v-group">
        <resource-box></resource-box>
        <resource-box></resource-box>
        <resource-box></resource-box>
      </div>
    </div>
    <div class="grid-content">
      <div class="l-top-bar">
        <span>북마크 한 데이터</span>
        <button class="button link-button-support">
          <span class="button-title">모두 보기</span>
        </button>
      </div>
      <div class="v-group">
        <resource-box></resource-box>

      </div>
    </div>
    </div>
    <div class="grid-content"></div>
    <div class="grid-content"></div>
  </div>
</template>

<script>
import ResourceBox from "~/components/common/resource-box/resource-box.vue";

export default {
  name: "main",
  components: { ResourceBox }
};
</script>

<style scoped>
</style>