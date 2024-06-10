<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Tag</h2>
    <p class="section-summary">
      태그는 대화형으로, 사용자기 제공하는 키워드 집합을 나타냅니다. 이 키워드들은 객체를 라벨링, 정리, 분류하는 데 도움이 됩니다. 사용자가 객체에 태그를 추가하거나 제거할 수 있습니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Appearance</h3>
      <div class="flex flex-col">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex justify-center gap-4">
              <div class="tag tag-primary">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-primary is-tag-selected">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-primary is-tag-disabled">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </div>
            <div class="flex justify-center gap-4">
              <div class="tag tag-primary tag-sm">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-primary tag-sm is-tag-selected">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-primary tag-sm is-tag-disabled">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Primary</h4>
            <p class="group-summary">
              주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
            </p>
          </div>
        </div>
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex justify-center gap-4">
              <div class="tag tag-primary-lighter">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-primary-lighter is-tag-selected">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </div>
            <div class="flex justify-center gap-4">
              <div class="tag tag-primary-lighter tag-sm">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-primary-lighter tag-sm is-tag-selected">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Lighter</h4>
            <p class="group-summary">
              주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
            </p>
          </div>
        </div>
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex justify-center gap-4">
              <div class="tag tag-neutral">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-neutral is-tag-selected">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </div>
            <div class="flex justify-center gap-4">
              <div class="tag tag-neutral tag-sm">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-neutral tag-sm is-tag-selected">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Neutral</h4>
            <p class="group-summary">
              주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
            </p>
          </div>
        </div>
      </div>
    </article>
  </section>
</template>

<script>
export default {
  name: "guide-tag"
};
</script>

<style scoped>

</style>