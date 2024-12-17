<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Select</h2>
    <p class="section-summary">
     셀렉트는 사용자가 옵션 메뉴에서 하나의 옵션 또는 다수의 옵션을 선택할 수 있게 도와줍니다. 선택한 옵션은 셀렉트 박스에 노출되어야 하며 선택 항목은 드롭다운 컴포넌트를 사용합니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Appearance</h3>
      <div class="flex flex-col">
        <div class="v-group gap-3">
          <h4 class="group-title">default</h4>
          <div class="flex gap-6 items-start">
            <div class="v-group gap-4">
              <select-box class="select-lg w-64"></select-box>
              <select-box class="select-lg w-64" disabled></select-box>
            </div>
            <select-box class="is-select-open w-64"></select-box>
            <select-box class="select-sm w-64"></select-box>
          </div>
          <h4 class="group-title">clean</h4>
          <div class="flex gap-6 items-start">
            <div class="v-group gap-4">
              <select-box class="select-clean select-lg w-64"></select-box>
              <select-box class="select-lg w-64" disabled></select-box>
            </div>
            <select-box class="select-clean is-select-open w-64"></select-box>
            <select-box class="select-sm select-clean is-select-invalid w-64"></select-box>
          </div>
          <h4 class="group-title">Select-search</h4>
          <div class="flex gap-6 items-start">
            <div class="v-group gap-8">
              <combo-box class="select-lg w-64"></combo-box>
              <combo-box class="select-lg is-select-open w-64"></combo-box>
            </div>
            <div class="v-group gap-8">
              <combo-box class=" w-64"></combo-box>
              <combo-box class="is-select-open w-64"></combo-box>
            </div>
            <div class="v-group gap-8">
              <combo-box class="select-sm w-64"></combo-box>
              <combo-box class="select-sm is-select-open w-64"></combo-box>
            </div>
          </div>
        </div>
      </div>
    </article>
  </section>
</template>

<script>
export default {
  name: "guide-select"
};
</script>

<style scoped>

</style>