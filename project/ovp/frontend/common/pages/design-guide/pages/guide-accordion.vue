<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Accordion</h2>
    <p class="section-summary">
      아코디언은 관련 콘텐츠를 축소 및 확장할 수 있는 수직 적층 구조 목록입니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Type</h3>
      <div class="flex flex-col">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure items-center">
            <Accordion></Accordion>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Normal</h4>
            <p class="group-summary">
              기본적인 상태 알림에 사용합니다.
            </p>
          </div>
        </div>
        <div class="flex gap-8 mb-14">
          <figure class="section-figure items-center">
          </figure>
          <div class="group-description">
            <h4 class="group-title">Info</h4>
            <p class="group-summary">
              normal타입보다 중요한 정보를 알릴 때 사용됩니다.
            </p>
          </div>
        </div>
      </div>
    </article>
  </section>
</template>


<script>

export default {
  name: "guide-accordion"
};
</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
