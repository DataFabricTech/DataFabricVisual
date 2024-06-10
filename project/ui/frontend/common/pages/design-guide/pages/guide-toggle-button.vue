<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Toggle Button</h2>
    <p class="section-summary">
      라디오는 여러 선택지 중에서 사용자가 한 가지만 선택할 수 있도록 합니다.
    </p>
  <article class="mb-10">
    <h3 class="section-subtitle">Toggle</h3>
    <div class="flex flex-col gap-5">
      <div class="v-group gap-3">
        <h4 class="group-title">Basic</h4>
        <div class="h-group gap-4">
          <div class="v-group gap-3">
            <div class="toggle toggle-lg">
              <input type="radio" id="toggle" class="toggle-input" name="toggle1" checked />
              <label for="toggle" class="toggle-label">
                <svg-icon class="toggle-icon" name="trash"></svg-icon>
                Toggle
              </label>
              <input type="radio" id="toggle2" class="toggle-input" name="toggle1" />
              <label for="toggle2" class="toggle-label">
                <svg-icon class="toggle-icon" name="trash"></svg-icon>
                Toggle
              </label>
              <input type="radio" id="toggle3" class="toggle-input" name="toggle1" />
              <label for="toggle3" class="toggle-label">
                <svg-icon class="toggle-icon" name="trash"></svg-icon>
                Toggle
              </label>
              <input type="radio" id="toggle4" class="toggle-input" name="toggle1" disabled />
              <label for="toggle4" class="toggle-label">
                <svg-icon class="toggle-icon" name="trash"></svg-icon>
                Disabled
              </label>
            </div>
            <div class="toggle">
              <input type="radio" id="togglesm" class="toggle-input" name="toggle2" checked />
              <label for="togglesm" class="toggle-label">
                <svg-icon class="toggle-icon" name="trash"></svg-icon>
                Toggle
              </label>
              <input type="radio" id="togglesm2" class="toggle-input" name="toggle2" />
              <label for="togglesm2" class="toggle-label">
                <svg-icon class="toggle-icon" name="trash"></svg-icon>
                Toggle
              </label>
              <input type="radio" id="togglesm3" class="toggle-input" name="toggle2" />
              <label for="togglesm3" class="toggle-label">
                <svg-icon class="toggle-icon" name="trash"></svg-icon>
                Toggle
              </label>
            </div>
          </div>
        </div>
        <h4 class="group-title">Primary</h4>
        <div class="h-group gap-4">
          <div class="v-group gap-3">
            <div class="toggle toggle-lg toggle-primary">
              <input type="radio" id="toggleprimary" class="toggle-input" name="toggle3" checked />
              <label for="toggleprimary" class="toggle-label">
                <svg-icon class="toggle-icon" name="trash"></svg-icon>
                Toggle
              </label>
              <input type="radio" id="toggleprimary2" class="toggle-input" name="toggle3" />
              <label for="toggleprimary2" class="toggle-label">
                <svg-icon class="toggle-icon" name="trash"></svg-icon>
                Toggle
              </label>
              <input type="radio" id="toggleprimary3" class="toggle-input" name="toggle3" />
              <label for="toggleprimary3" class="toggle-label">
                <svg-icon class="toggle-icon" name="trash"></svg-icon>
                Toggle
              </label>
              <input type="radio" id="toggleprimary4" class="toggle-input" name="toggle3" disabled />
              <label for="toggleprimary4" class="toggle-label">
                <svg-icon class="toggle-icon" name="trash"></svg-icon>
                Disabled
              </label>
            </div>
            <div class="toggle toggle-primary">
              <input type="radio" id="toggleprimarysm" class="toggle-input" name="toggle4" checked />
              <label for="toggleprimarysm" class="toggle-label">
                <svg-icon class="toggle-icon" name="trash"></svg-icon>
                Toggle
              </label>
              <input type="radio" id="toggleprimarysm2" class="toggle-input" name="toggle4" />
              <label for="toggleprimarysm2" class="toggle-label">
                <svg-icon class="toggle-icon" name="trash"></svg-icon>
                Toggle
              </label>
              <input type="radio" id="toggleprimarysm3" class="toggle-input" name="toggle4" />
              <label for="toggleprimarysm3" class="toggle-label">
                <svg-icon class="toggle-icon" name="trash"></svg-icon>
                Toggle
              </label>
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
  name: "guide-toggle-button"
};
</script>

<style scoped>

</style>