<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Breadcrumb</h2>
    <p class="section-summary">
      브레드크럼은 사이트나 앱에서 사용자의 위치를 보여주는데 사용되는 네비게이션 시스템입니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Default</h3>
      <div class="flex flex-col align-center">
        <div class="flex gap-8 mb-12">
          <figure class="section-figure items-center">
            <Breadcrumb></Breadcrumb>
          </figure>
          <div class="group-description">
            <p class="group-summary">
              주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
            </p>
          </div>
        </div>
      </div>
    </article>
    <article class="mb-10">
      <h3 class="section-subtitle">Slash</h3>
      <div class="flex flex-col align-center">
        <div class="flex gap-8 mb-12">
          <figure class="section-figure items-center">
            <Breadcrumb class="breadcrumb-slash"></Breadcrumb>
          </figure>
          <div class="group-description">
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
  name: "guide-breadcrumb"
};
</script>

<style scoped>

</style>