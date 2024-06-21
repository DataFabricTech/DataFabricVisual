<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Dropdown</h2>
    <p class="section-summary">
      드롭다운을 사용하면 사용자가 선택할 수 있는 그룹화된 항목 목록을 생성할 수 있습니다. 드롭다운은 일반적으로 팝오버로 나타나며 때로는 모달이나
      사이드바에도 나타날 수 있습니다. 드롭다운은 폼 내에서 사용되지 않고 독립적인 메뉴로 사용되어야 합니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Contents</h3>
      <div class="flex flex-col">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex justify-center gap-4">
              <div class="dropdown dropdown-lg" style="position: static;height: fit-content;">
                <ul class="dropdown-list">
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <span class="dropdown-text">dropdown item</span>
                    </button>
                  </li>
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <span class="dropdown-text">dropdown item</span>
                    </button>
                  </li>
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <span class="dropdown-text">dropdown item</span>
                    </button>
                  </li>
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <span class="dropdown-text">dropdown item</span>
                    </button>
                  </li>
                </ul>
              </div>
              <div class="dropdown" style="position: static;height: fit-content;">
                <ul class="dropdown-list">
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <svg-icon class="svg-icon" name="user"></svg-icon>
                      <span class="dropdown-text">dropdown item</span>
                    </button>
                  </li>
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <svg-icon class="svg-icon" name="user"></svg-icon>
                      <span class="dropdown-text">dropdown item</span>
                    </button>
                  </li>
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <svg-icon class="svg-icon" name="user"></svg-icon>
                      <span class="dropdown-text">dropdown item</span>
                    </button>
                  </li>
                  <li class="dropdown-item dropdown-item-negative">
                    <button class="dropdown-button">
                      <svg-icon class="svg-icon" name="trash"></svg-icon>
                      <span class="dropdown-text">삭제</span>
                    </button>
                  </li>
                </ul>
              </div>
              <div class="dropdown dropdown-sm" style="position: static;height: fit-content;">
                <ul class="dropdown-list">
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <span class="dropdown-text">dropdown item</span>
                    </button>
                    <button class="button button-neutral-ghost button-xs">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </li>
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <svg-icon class="svg-icon" name="user"></svg-icon>
                      <span class="dropdown-text">dropdown item</span>
                    </button>
                    <button class="button button-neutral-ghost button-xs">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </li>
                  <li class="dropdown-item">
                      <button class="dropdown-button">
                        <svg-icon class="svg-icon" name="user"></svg-icon>
                        <span class="dropdown-text">dropdown item</span>
                      </button>
                    <button class="button button-neutral-ghost button-xs">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </li>
                  <li class="dropdown-item dropdown-item-negative">
                    <button class="dropdown-button">
                      <svg-icon class="svg-icon" name="trash"></svg-icon>
                      <span class="dropdown-text">삭제</span>
                    </button>
                  </li>
                </ul>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Text / Icon</h4>
            <p class="group-summary">
              주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다. 대표적인 예시로 로그인 페이지 내 ‘로그인’ Button과 작업
              페이지 내 ‘완료’ Button으로 사용됩니다.
            </p>
          </div>
        </div>
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex justify-center gap-4">
              <div class="dropdown dropdown-lg" style="position: static;height:fit-content;">
                <ul class="dropdown-list">
                  <li class="dropdown-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-drop-3" class="checkbox-input" />
                      <label for="checkbox-drop-3" class="checkbox-label">
                        Checkbox
                        <span class="checkbox-subtext">(Optional)</span>
                      </label>
                    </div>
                  </li>
                  <li class="dropdown-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-drop-3" class="checkbox-input" />
                      <label for="checkbox-drop-3" class="checkbox-label">
                        Checkbox
                        <span class="checkbox-subtext">(Optional)</span>
                      </label>
                    </div>
                  </li>
                  <li class="dropdown-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-drop-3" class="checkbox-input" />
                      <label for="checkbox-drop-3" class="checkbox-label">
                        Checkbox
                        <span class="checkbox-subtext">(Optional)</span>
                      </label>
                    </div>
                  </li>
                  <li class="dropdown-item dropdown-item-negative">
                    <button class="dropdown-button">
                      <svg-icon class="svg-icon" name="trash"></svg-icon>
                      <span class="dropdown-text">삭제</span>
                    </button>
                  </li>
                </ul>
              </div>
              <div class="dropdown" style="position: static;height: fit-content;">
                <ul class="dropdown-list">
                  <li class="dropdown-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-drop-2" class="checkbox-input" />
                      <label for="checkbox-drop-2" class="checkbox-label">
                        Checkbox
                        <span class="checkbox-subtext">(Optional)</span>
                      </label>
                    </div>
                    <button class="button button-neutral-ghost button-xs">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </li>
                  <li class="dropdown-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-drop-2" class="checkbox-input" />
                      <label for="checkbox-drop-2" class="checkbox-label">
                        Checkbox
                        <span class="checkbox-subtext">(Optional)</span>
                      </label>
                    </div>
                    <button class="button button-neutral-ghost button-xs">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </li>
                  <li class="dropdown-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-drop-2" class="checkbox-input" />
                      <label for="checkbox-drop-2" class="checkbox-label">
                        Checkbox
                        <span class="checkbox-subtext">(Optional)</span>
                      </label>
                    </div>
                    <button class="button button-neutral-ghost button-xs">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </li>
                  <li class="dropdown-item dropdown-item-negative">
                    <button class="dropdown-button">
                      <svg-icon class="svg-icon" name="trash"></svg-icon>
                      <span class="dropdown-text">삭제</span>
                    </button>
                  </li>
                </ul>
              </div>
              <div class="dropdown dropdown-sm" style="position: static;height: fit-content;">
                <ul class="dropdown-list">
                  <li class="dropdown-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-drop-2" class="checkbox-input" />
                      <label for="checkbox-drop-2" class="checkbox-label">
                        Checkbox
                      </label>
                    </div>
                  </li>
                  <li class="dropdown-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-drop-2" class="checkbox-input" />
                      <label for="checkbox-drop-2" class="checkbox-label">
                        Checkbox
                      </label>
                    </div>
                  </li>
                  <li class="dropdown-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-drop-2" class="checkbox-input" />
                      <label for="checkbox-drop-2" class="checkbox-label">
                        Checkbox
                      </label>
                    </div>
                  </li>
                  <li class="dropdown-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-drop-2" class="checkbox-input" />
                      <label for="checkbox-drop-2" class="checkbox-label">
                        Checkbox
                      </label>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Checkbox</h4>
            <p class="group-summary">
              옵션의 단일 선택 또는 다중 선택을 포함합니다.
            </p>
          </div>
        </div>

      </div>
    </article>
  </section>
</template>

<script>
export default {
  name: "guide-dropdown"
};
</script>

<style scoped>

</style>