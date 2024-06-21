<template>
  <!-- TODO: [개발]text-input-icon은 on/off 필요 -->
  <div class="text-input-group">
    <label class="hidden-text" for="data-list-example">label</label>
    <svg-icon class="text-input-icon" name="user"></svg-icon>
    <input id="data-list-example" class="text-input" placeholder="검색어 입력" />
    <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
      <span class="hidden-text">지우기</span>
      <svg-icon class="button-icon" name="close"></svg-icon>
    </button>
    <button class="text-input-search-button button button-neutral-ghost">
      <span class="hidden-text">검색</span>
      <svg-icon class="button-icon" name="search"></svg-icon>
    </button>
  </div>
</template>

<script>
export default {
  name: "SearchInput"
};
</script>

<style scoped>

</style>