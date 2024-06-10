<template>
  <section class="guide-section">
    <h2 class="section-title">Text Input</h2>
    <article class="mb-10">
      <div class="flex flex-col gap-5">
        <div class="v-group gap-3">
          <h4 class="group-title">default</h4>
          <div class="flex gap-6 items-start">
            <div class="v-group gap-4">
              <label class="hidden-text" for="text-input-example">label</label>
              <input id="text-input-example" class="text-input text-input-lg" placeholder="placeholder" />
              <label class="hidden-text" for="text-input-example-3">label</label>
              <input id="text-input-example-3" class="text-input text-input-lg" placeholder="placeholder" disabled />
              <div class="text-input-group">
                <label class="hidden-text" for="text-input-example-4">label</label>
                <input id="text-input-example-4" class="text-input text-input-lg" placeholder="prefix icon" />
                <svg-icon class="text-input-icon" name="user"></svg-icon>
              </div>
              <div class="text-input-group">
                <label class="hidden-text" for="text-input-example-4">label</label>
                <input id="text-input-example-4" class="text-input text-input-lg" placeholder="button" />
                <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
                  <span class="hidden-text">지우기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="text-input-group">
                <label class="hidden-text" for="text-input-example-4">label</label>
                <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                <svg-icon class="text-input-icon" name="user"></svg-icon>
                <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
                  <span class="hidden-text">지우기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
              </div>
            </div>
            <div class="v-group gap-5">
              <label class="hidden-text" for="text-input-example">label</label>
              <input id="text-input-example" class="text-input" placeholder="placeholder" />
              <label class="hidden-text" for="text-input-example-3">label</label>
              <input id="text-input-example-3" class="text-input" placeholder="placeholder" disabled />
              <div class="text-input-group">
                <label class="hidden-text" for="text-input-example-4">label</label>
                <input id="text-input-example-4" class="text-input" placeholder="prefix icon" />
                <svg-icon class="text-input-icon" name="user"></svg-icon>
              </div>
              <div class="text-input-group">
                <label class="hidden-text" for="text-input-example-5">label</label>
                <input id="text-input-example-5" class="text-input" placeholder="button" />
                <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
                  <span class="hidden-text">지우기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="text-input-group">
                <label class="hidden-text" for="text-input-example-6">label</label>
                <input id="text-input-example-6" class="text-input" value="prefix icon + button" />
                <svg-icon class="text-input-icon" name="user"></svg-icon>
                <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
                  <span class="hidden-text">지우기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
              </div>
            </div>
            <div class="v-group gap-6">
              <label class="hidden-text" for="text-input-example">label</label>
              <input id="text-input-example" class="text-input text-input-sm" placeholder="placeholder" />
              <label class="hidden-text" for="text-input-example-7">label</label>
              <input id="text-input-example-7" class="text-input text-input-sm" placeholder="placeholder" disabled />
              <div class="text-input-group">
                <label class="hidden-text" for="text-input-example-8">label</label>
                <input id="text-input-example-8" class="text-input text-input-sm" placeholder="prefix icon" />
                <svg-icon class="text-input-icon" name="user"></svg-icon>
              </div>
              <div class="text-input-group">
                <label class="hidden-text" for="text-input-example-9">label</label>
                <input id="text-input-example-9" class="text-input text-input-sm" placeholder="button" />
                <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
                  <span class="hidden-text">지우기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="text-input-group ">
                <label class="hidden-text" for="text-input-example-10">label</label>
                <input id="text-input-example-10" class="text-input text-input-sm" value="prefix icon + button" />
                <svg-icon class="text-input-icon" name="user"></svg-icon>
                <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
                  <span class="hidden-text">지우기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
              </div>
            </div>
          </div>
          <h4 class="group-title">search button</h4>
          <div class="flex gap-6 items-start">
            <div class="v-group gap-4">
              <div class="text-input-group">
                <label class="hidden-text" for="text-input-example-11">label</label>
                <input id="text-input-example-11" class="text-input text-input-lg" value="search button" />
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
            <div class="v-group gap-4">
              <div class="text-input-group">
                <label class="hidden-text" for="text-input-example-11">label</label>
                <input id="text-input-example-11" class="text-input" placeholder="search button" />
                <svg-icon class="text-input-icon" name="user"></svg-icon>
                <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
                  <span class="hidden-text">지우기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
                <button class="text-input-search-button button button-neutral-ghost">
                  <span class="hidden-text">검색</span>
                  <svg-icon class="button-icon" name="search"></svg-icon>
                </button>
              </div>
            </div>
            <div class="v-group gap-4">
              <div class="text-input-group ">
                <label class="hidden-text" for="text-input-example-11">label</label>
                <input id="text-input-example-11" class="text-input text-input-sm" value="search button" />
                <svg-icon class="text-input-icon" name="user"></svg-icon>
                <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
                  <span class="hidden-text">지우기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
                <button class="text-input-search-button button button-neutral-ghost">
                  <span class="hidden-text">검색</span>
                  <svg-icon class="button-icon" name="search"></svg-icon>
                </button>
              </div>
            </div>
          </div>
          <h4 class="group-title">password</h4>
          <div class="flex gap-6 items-start">
            <div class="v-group gap-4">
              <div class="text-input-group">
                <label class="hidden-text" for="text-input-example-11">label</label>
                <input
                  type="password"
                  id="text-input-example-11"
                  class="text-input text-input-lg"
                  placeholder="password"
                />
                <svg-icon class="text-input-icon" name="lock-locked"></svg-icon>
                <button class="text-input-action-button button button-neutral-ghost button-md" type="button">
                  <span class="hidden-text">패스워드 보기</span>
                  <svg-icon class="button-icon" name="eye"></svg-icon>
                </button>
              </div>
            </div>
            <div class="v-group gap-4">
              <div class="text-input-group">
                <label class="hidden-text" for="text-input-example-11">label</label>
                <input
                  type="password"
                  id="text-input-example-11"
                  class="text-input"
                  placeholder="password"
                  required
                />
                <svg-icon class="text-input-icon" name="lock-locked"></svg-icon>
                <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
                  <span class="hidden-text">패스워드 보기</span>
                  <svg-icon class="button-icon" name="eye"></svg-icon>
                </button>
              </div>
            </div>
            <div class="v-group gap-4">
              <div class="text-input-group">
                <label class="hidden-text" for="text-input-example-11">label</label>
                <input
                  type="password"
                  id="text-input-example-11"
                  class="text-input text-input-sm"
                  placeholder="password"
                />
                <svg-icon class="text-input-icon" name="lock-locked"></svg-icon>
                <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
                  <span class="hidden-text">패스워드 보기</span>
                  <svg-icon class="button-icon" name="eye"></svg-icon>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </article>
  </section>
</template>

<script>
export default {
  name: "guide-text-input"
};
</script>

<style scoped>

</style>