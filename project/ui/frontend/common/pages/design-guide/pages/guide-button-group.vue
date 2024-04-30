<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Button Group</h2>
    <p class="section-summary">
      버튼 그룹은 관련된 작업들을 정렬과 간격 조정을 돕기 위해 여러 개를 쌓거나 수평으로 나열합니다.
    </p>
  <article class="mb-10">
    <h3 class="section-subtitle">Basic</h3>
    <div class="flex flex-col">
      <div class="flex gap-8 mb-14">
        <figure class="section-figure">
          <div class="button-group button-group-lg">
            <input type="checkbox" id="button-group" class="button-group-input" name="button-group1" checked />
            <label for="button-group" class="button-group-label"> Toggle </label>
            <input type="checkbox" id="button-group2" class="button-group-input" name="button-group1" checked />
            <label for="button-group2" class="button-group-label"> Toggle </label>
            <input type="checkbox" id="button-group9" class="button-group-input" name="button-group1" />
            <label for="button-group9" class="button-group-label"> Toggle </label>
            <input type="checkbox" id="button-group3" class="button-group-input" name="button-group1" />
            <label for="button-group3" class="button-group-label"> Toggle </label>
            <input type="checkbox" id="button-group4" class="button-group-input" name="button-group1" disabled />
            <label for="button-group4" class="button-group-label"> Disabled </label>
          </div>
          <div class="button-group">
            <input type="checkbox" id="button-groupsm" class="button-group-input" name="button-group2" checked />
            <label for="button-groupsm" class="button-group-label"> Toggle </label>
            <input type="checkbox" id="button-groupsm2" class="button-group-input" name="button-group2" />
            <label for="button-groupsm2" class="button-group-label"> Toggle </label>
            <input type="checkbox" id="button-groupsm6" class="button-group-input" name="button-group2" />
            <label for="button-groupsm6" class="button-group-label"> Toggle </label>
            <input type="checkbox" id="button-groupsm3" class="button-group-input" name="button-group2" checked />
            <label for="button-groupsm3" class="button-group-label"> Toggle </label>
          </div>
        </figure>
        <div class="group-description">
          <p class="group-summary">
            컨트롤들 중에서 주제와 관련된 세트로 여러 버튼으로 강조하는데 사용되며
            단일 선택 및 다중 선택이 가능합니다.
          </p>
        </div>
      </div>
    </div>
  </article>
  </section>
</template>

<script>
export default {
  name: "guide-button-group"
};
</script>

<style scoped>

</style>