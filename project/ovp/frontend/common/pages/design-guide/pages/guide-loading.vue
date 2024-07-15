<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Loading Indicator</h2>
    <p class="section-summary">
      로더는 데이터를 검색하거나 화면이 느리게 뜨는 경우 사용합니다.
      사용자에게 프로세스가 진행중임을 알리는데에 도움이 됩니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Type</h3>
      <div class="flex flex-col">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex gap-12 items-center justify-center">
              <Loading class="loader-lg"></Loading>
              <Loading></Loading>
              <Loading class="loader-sm"></Loading>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Default</h4>
            <p class="group-summary">
              주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
            </p>
          </div>
        </div>
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex gap-12 items-center justify-center">
              <Loading class="loader-simple loader-lg"></Loading>
              <Loading class="loader-simple"></Loading>
              <Loading class="loader-simple loader-sm"></Loading>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Simple</h4>
            <p class="group-summary">
              주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
            </p>
          </div>
        </div>
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex gap-2 items-center justify-center">
              <button class="button button-lg button-primary">
                <Loading class="loader-simple loader-xs" :hideText="true"></Loading>
                <span class="button-text">button</span>
              </button>
              <button class="button button-lg button-primary-stroke">
                <Loading class="loader-simple loader-xs" :hideText="true"></Loading>
                <span class="button-text">button</span>
              </button>
              <button class="button button-lg button-primary-lighter">
                <Loading class="loader-simple loader-xs" :hideText="true"></Loading>
                <span class="button-text">button</span>
              </button>
              <button class="button button-lg button-primary-ghost">
                <Loading class="loader-simple loader-xs" :hideText="true"></Loading>
                <span class="button-text">button</span>
              </button>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">button</h4>
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
  name: "guide-loading"
};
</script>

<style scoped>

</style>