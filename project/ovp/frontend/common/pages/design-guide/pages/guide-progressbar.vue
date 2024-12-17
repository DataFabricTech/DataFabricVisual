<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Progress Bar</h2>
    <p class="section-summary">
      프로그래스바는 작업 프로세스의 진행률을 표시합니다. 작업의 수행 비율을 표시해야 하는 경우 사용합니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Type</h3>
      <div class="flex flex-col">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex flex-col justify-center gap-5">
              <div class="progressbar progressbar-sm">
                <div class="progressbar-gage" style="width: 70%;" title="70%">
                </div>
              </div>
              <progress id="bar-gage-02" class="progressbar" value="20" max="100">
                <strong>20%</strong>
              </progress>
<!--              <div class='progressbar-group'><span class='progressbar-text'>${val}%</span><progress id='bar-gage-02' class='progressbar progressbar-sm progressbar-major' value=${val} max='100'></progress></div>-->
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
            <div class="flex flex-col justify-center gap-5">
              <div class="progressbar progressbar-critical progressbar-sm">
                <div class="progressbar-gage" style="width: 70%;" title="70%">
                </div>
              </div>
              <progress id="bar-gage-02" class="progressbar progressbar-critical" value="20" max="100">
                <strong>20%</strong>
              </progress>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Critical</h4>
            <p class="group-summary">
              주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
            </p>
          </div>
        </div>
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex flex-col justify-center gap-5">
              <div class="progressbar progressbar-major progressbar-sm">
                <div class="progressbar-gage" style="width: 70%;" title="70%">
                </div>
              </div>
              <progress id="bar-gage-02" class="progressbar progressbar-major" value="20" max="100">
                <strong>20%</strong>
              </progress>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Major</h4>
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
  name: "guide-progress"
};
</script>

<style scoped>

</style>