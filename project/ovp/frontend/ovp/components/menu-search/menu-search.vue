<template>
  <div class="menu menu-search">
    <div class="menu-head">
      <div class="search-group">
        <label class="hidden-text" for="text-input-example-11">label</label>
        <input id="text-input-example-11" class="text-input" value="search button" />
        <svg-icon class="text-input-icon" name="user"></svg-icon>
        <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
          <span class="hidden-text">지우기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
        <button class="text-input-search-button button button-neutral-ghost">
          <span class="hidden-text">검색</span>
          <svg-icon class="button-icon" name="search"></svg-icon>
        </button>
      </div>
    </div>
    <ul class="menu-list">
      <li class="menu-item">
        <div class="checkbox">
          <input type="checkbox" id="checkbox-menu-1" class="checkbox-input" />
          <label for="checkbox-menu-1" class="checkbox-label">
            Checkbox1
          </label>
        </div>
      </li>
      <li class="menu-item">
        <div class="checkbox">
          <input type="checkbox" id="checkbox-menu-2" class="checkbox-input" />
          <label for="checkbox-menu-2" class="checkbox-label">
            Checkbox2
          </label>
        </div>
      </li>
      <li class="menu-item">
        <div class="checkbox">
          <input type="checkbox" id="checkbox-menu-3" class="checkbox-input" />
          <label for="checkbox-menu-3" class="checkbox-label">
            Checkbox3
          </label>
        </div>

      </li>
      <li class="menu-item">
        <div class="checkbox">
          <input type="checkbox" id="checkbox-menu-4" class="checkbox-input" />
          <label for="checkbox-menu-4" class="checkbox-label">
            Checkbox4
          </label>
        </div>
      </li>
      <li class="menu-item">
        <div class="checkbox">
          <input type="checkbox" id="checkbox-menu-5" class="checkbox-input" />
          <label for="checkbox-menu-5" class="checkbox-label">
            Checkbox5
          </label>
        </div>
      </li>
    </ul>
    <div class="menu-foot">
      <button class="button button-error-ghost">초기화</button>
      <div class="menu-foot-group">
        <button class="button button-neutral-ghost">취소</button>
        <button class="button button-primary">적용</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "list-search"
};
</script>

<style scoped>

</style>