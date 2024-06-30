<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Menu</h2>
    <p class="section-summary">
      리스트는 관련 내용을 구조화하여 표시합니다. 내용의 유형은 여러 요소로 다양하게 표현될 수 있습니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Contents</h3>
      <div class="flex flex-col">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex justify-center gap-4">
              <div class="menu menu-lg">
                <div class="menu-head">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-11">label</label>
                    <input id="text-input-example-11" class="text-input text-input-lg" value="search button" />
                    <svg-icon class="text-input-icon" name="user"></svg-icon>
                    <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                    <button class="search-input-button button button-neutral-ghost">
                      <span class="hidden-text">검색</span>
                      <svg-icon class="button-icon" name="search"></svg-icon>
                    </button>
                  </div>
                </div>
                <ul class="menu-list">
                  <li class="menu-item">
                    <button class="menu-button">
                      <span class="menu-text">menu item</span>
                    </button>
                  </li>
                  <li class="menu-item">
                    <button class="menu-button">
                      <svg-icon class="svg-icon" name="home"></svg-icon>
                      <span class="menu-text">menu item</span>
                    </button>
                  </li>
                  <li class="menu-item">
                    <button class="menu-button">
                      <svg-icon class="svg-icon" name="home"></svg-icon>
                      <span class="menu-text">menu item</span>
                    </button>
                    <div class="menu-button-group">
                      <button class="button button-neutral-ghost button-sm" type="button">
                        <span class="hidden-text">복사</span>
                        <svg-icon class="button-icon" name="copy"></svg-icon>
                      </button>
                      <button class="button button-neutral-ghost button-sm" type="button">
                        <span class="hidden-text">수정</span>
                        <svg-icon class="button-icon" name="pen"></svg-icon>
                      </button>
                      <button class="button button-error-ghost button-sm" type="button">
                        <span class="hidden-text">삭제</span>
                        <svg-icon class="button-icon" name="trash"></svg-icon>
                      </button>
                    </div>
                  </li>
                  <li class="menu-item">
                    <button class="menu-button">
                      <svg-icon class="svg-icon" name="home"></svg-icon>
                      <span class="menu-text">menu item</span>
                    </button>
                    <div class="menu-button-group">
                      <button class="button button-neutral-ghost button-sm" type="button">
                        <span class="hidden-text">복사</span>
                        <svg-icon class="button-icon" name="copy"></svg-icon>
                      </button>
                      <button class="button button-neutral-ghost button-sm" type="button">
                        <span class="hidden-text">수정</span>
                        <svg-icon class="button-icon" name="pen"></svg-icon>
                      </button>
                      <button class="button button-error-ghost button-sm" type="button">
                        <span class="hidden-text">삭제</span>
                        <svg-icon class="button-icon" name="trash"></svg-icon>
                      </button>
                    </div>
                  </li>
                  <li class="menu-item">
                    <label class="hidden-text" for="text-input-modify">label</label>
                    <input id="text-input-modify" class="text-input" placeholder="placeholder" />
                    <div class="menu-button-group">
                      <button class="button button-neutral-ghost button-sm" type="button">
                        <span class="hidden-text">복사</span>
                        <svg-icon class="button-icon" name="copy"></svg-icon>
                      </button>
                      <button class="button button-neutral-ghost button-sm" type="button">
                        <span class="hidden-text">수정</span>
                        <svg-icon class="button-icon" name="pen"></svg-icon>
                      </button>
                      <button class="button button-error-ghost button-sm" type="button">
                        <span class="hidden-text">삭제</span>
                        <svg-icon class="button-icon" name="trash"></svg-icon>
                      </button>
                    </div>
                  </li>
                </ul>
              </div>
              <div class="menu">
                <div class="menu-head">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-11">label</label>
                    <input id="text-input-example-11" class="text-input" value="search button" />
                    <svg-icon class="text-input-icon" name="user"></svg-icon>
                    <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                    <button class="search-input-button button button-neutral-ghost">
                      <span class="hidden-text">검색</span>
                      <svg-icon class="button-icon" name="search"></svg-icon>
                    </button>
                  </div>
                </div>
                <ul class="menu-list">
                  <li class="menu-item">
                    <button class="menu-button">
                      <span class="menu-text">menu item</span>
                    </button>
                  </li>
                  <li class="menu-item">
                    <button class="menu-button">
                      <svg-icon class="svg-icon" name="home"></svg-icon>
                      <span class="menu-text">menu item</span>
                    </button>
                  </li>
                  <li class="menu-item">
                    <button class="menu-button">
                      <svg-icon class="svg-icon" name="home"></svg-icon>
                      <span class="menu-text">menu item</span>
                    </button>
                    <div class="menu-button-group">
                      <button class="button button-neutral-ghost button-xs" type="button">
                        <span class="hidden-text">복사</span>
                        <svg-icon class="button-icon" name="copy"></svg-icon>
                      </button>
                      <button class="button button-neutral-ghost button-xs" type="button">
                        <span class="hidden-text">수정</span>
                        <svg-icon class="button-icon" name="pen"></svg-icon>
                      </button>
                      <button class="button button-error-ghost button-xs" type="button">
                        <span class="hidden-text">삭제</span>
                        <svg-icon class="button-icon" name="trash"></svg-icon>
                      </button>
                    </div>
                  </li>
                  <li class="menu-item">
                    <button class="menu-button">
                      <svg-icon class="svg-icon" name="home"></svg-icon>
                      <span class="menu-text">menu item</span>
                    </button>
                    <div class="menu-button-group">
                      <button class="button button-neutral-ghost button-xs" type="button">
                        <span class="hidden-text">복사</span>
                        <svg-icon class="button-icon" name="copy"></svg-icon>
                      </button>
                      <button class="button button-neutral-ghost button-xs" type="button">
                        <span class="hidden-text">수정</span>
                        <svg-icon class="button-icon" name="pen"></svg-icon>
                      </button>
                      <button class="button button-error-ghost button-xs" type="button">
                        <span class="hidden-text">삭제</span>
                        <svg-icon class="button-icon" name="trash"></svg-icon>
                      </button>
                    </div>
                  </li>
                  <li class="menu-item">
                    <label class="hidden-text" for="text-input-modify">label</label>
                    <input id="text-input-modify" class="text-input text-input-sm" placeholder="placeholder" />
                    <div class="menu-button-group">
                      <button class="button button-neutral-ghost button-xs" type="button">
                        <span class="hidden-text">복사</span>
                        <svg-icon class="button-icon" name="copy"></svg-icon>
                      </button>
                      <button class="button button-neutral-ghost button-xs" type="button">
                        <span class="hidden-text">수정</span>
                        <svg-icon class="button-icon" name="pen"></svg-icon>
                      </button>
                      <button class="button button-error-ghost button-xs" type="button">
                        <span class="hidden-text">삭제</span>
                        <svg-icon class="button-icon" name="trash"></svg-icon>
                      </button>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Text / Icon</h4>
            <p class="group-summary">
              주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
            </p>
          </div>
        </div>
      </div>
      <div class="flex gap-8 mb-14">
        <figure class="section-figure">
          <div class="flex justify-center gap-4">
            <div class="menu menu-lg">
              <div class="menu-head">
                <div class="search-input">
                  <label class="hidden-text" for="text-input-example-11">label</label>
                  <input id="text-input-example-11" class="text-input text-input-lg" value="search button" />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                  <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                  <button class="search-input-button button button-neutral-ghost">
                    <span class="hidden-text">검색</span>
                    <svg-icon class="button-icon" name="search"></svg-icon>
                  </button>
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
                    <button class="button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">복사</span>
                      <svg-icon class="button-icon" name="copy"></svg-icon>
                    </button>
                    <button class="button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">수정</span>
                      <svg-icon class="button-icon" name="pen"></svg-icon>
                    </button>
                    <button class="button button-error-ghost button-sm" type="button">
                      <span class="hidden-text">삭제</span>
                      <svg-icon class="button-icon" name="trash"></svg-icon>
                    </button>
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
                  <label class="hidden-text" for="text-input-modify">label</label>
                  <input id="text-input-modify" class="text-input" placeholder="placeholder" />
                  <div class="menu-button-group">
                    <button class="button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">복사</span>
                      <svg-icon class="button-icon" name="copy"></svg-icon>
                    </button>
                    <button class="button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">수정</span>
                      <svg-icon class="button-icon" name="pen"></svg-icon>
                    </button>
                    <button class="button button-error-ghost button-sm" type="button">
                      <span class="hidden-text">삭제</span>
                      <svg-icon class="button-icon" name="trash"></svg-icon>
                    </button>
                  </div>
                </li>
              </ul>
            </div>
            <div class="menu">
              <div class="menu-head">
                <div class="search-input">
                  <label class="hidden-text" for="text-input-example-11">label</label>
                  <input id="text-input-example-11" class="text-input" value="search button" />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                  <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                  <button class="search-input-button button button-neutral-ghost">
                    <span class="hidden-text">검색</span>
                    <svg-icon class="button-icon" name="search"></svg-icon>
                  </button>
                </div>
              </div>
              <ul class="menu-list">
                <li class="menu-item">
                  <div class="checkbox checkbox-sm">
                    <input type="checkbox" id="checkbox-menu" class="checkbox-input" />
                    <label for="checkbox-menu" class="checkbox-label">
                      Checkbox
                    </label>
                  </div>
                </li>
                <li class="menu-item">
                  <div class="checkbox checkbox-sm">
                    <input type="checkbox" id="checkbox-menu" class="checkbox-input" />
                    <label for="checkbox-menu" class="checkbox-label">
                      Checkbox
                    </label>
                  </div>
                </li>
                <li class="menu-item">
                  <div class="checkbox checkbox-sm">
                    <input type="checkbox" id="checkbox-menu" class="checkbox-input" />
                    <label for="checkbox-menu" class="checkbox-label">
                      Checkbox
                    </label>
                  </div>
                  <div class="menu-button-group">
                    <button class="button button-neutral-ghost button-xs" type="button">
                      <span class="hidden-text">복사</span>
                      <svg-icon class="button-icon" name="copy"></svg-icon>
                    </button>
                    <button class="button button-neutral-ghost button-xs" type="button">
                      <span class="hidden-text">수정</span>
                      <svg-icon class="button-icon" name="pen"></svg-icon>
                    </button>
                    <button class="button button-error-ghost button-xs" type="button">
                      <span class="hidden-text">삭제</span>
                      <svg-icon class="button-icon" name="trash"></svg-icon>
                    </button>
                  </div>
                </li>
                <li class="menu-item">
                  <div class="checkbox checkbox-sm">
                    <input type="checkbox" id="checkbox-menu-2" class="checkbox-input" />
                    <label for="checkbox-menu-2" class="checkbox-label">
                      Checkbox
                      <span class="checkbox-subtext">(Optional)</span>
                    </label>
                  </div>
                </li>
                <li class="menu-item">
                  <label class="hidden-text" for="text-input-modify">label</label>
                  <input id="text-input-modify" class="text-input text-input-sm" placeholder="placeholder" />
                  <div class="menu-button-group">
                    <button class="button button-neutral-ghost button-xs" type="button">
                      <span class="hidden-text">복사</span>
                      <svg-icon class="button-icon" name="copy"></svg-icon>
                    </button>
                    <button class="button button-neutral-ghost button-xs" type="button">
                      <span class="hidden-text">수정</span>
                      <svg-icon class="button-icon" name="pen"></svg-icon>
                    </button>
                    <button class="button button-error-ghost button-xs" type="button">
                      <span class="hidden-text">삭제</span>
                      <svg-icon class="button-icon" name="trash"></svg-icon>
                    </button>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </figure>
        <div class="group-description">
          <h4 class="group-title">Checkbox</h4>
          <p class="group-summary">
            주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
          </p>
        </div>
      </div>

    </article>
  </section>
</template>

<script>
export default {
  name: "guide-menu"
};
</script>

<style scoped>

</style>