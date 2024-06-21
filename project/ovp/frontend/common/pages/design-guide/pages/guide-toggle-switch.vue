<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Toggle Switch</h2>
    <p class="section-summary">
      토글 스위치는 두 상태 사이를 전환할 수 있는 스위치 모양 UI 요소입니다. 사용자가 스위치를 끄거나 켜면 해당 기능이 활성화되거나 비활성화됩니다.    </p>
  <article class="mb-10">
    <h3 class="section-subtitle">Switch</h3>
    <div class="flex flex-col gap-5">
      <div class="v-group gap-3">
        <div class="h-group gap-4">
          <div class="switch">
            <input type="checkbox" id="sw" class="switch-input" />
            <label for="sw" class="switch-label">
              Label
              <div class="switch-control"></div>
            </label>
          </div>
          <div class="switch">
            <input type="checkbox" id="sw2" class="switch-input" checked />
            <label for="sw2" class="switch-label">
              Label
              <div class="switch-control"></div>
            </label>
          </div>
          <div class="switch">
            <input type="checkbox" id="sw3" class="switch-input" disabled />
            <label for="sw3" class="switch-label">
              Disabled
              <div class="switch-control"></div>
            </label>
          </div>
        </div>
      </div>
    </div>
  </article>
  </section>
</template>

<script>
export default {
  name: "guide-toggle-switch"
};
</script>

<style scoped>

</style>