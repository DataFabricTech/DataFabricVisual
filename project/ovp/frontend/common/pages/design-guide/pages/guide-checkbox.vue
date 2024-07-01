<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Checkbox</h2>
    <p class="section-summary">
      체크박스는 사용자가 여러 선택 사항(0개, 1개 또는 여러 개)을 선택할 수 있는 방법을 제공하기 위해 사용됩니다. 또한, 특정 약관과 서비스에 동의하는 것을 나타내는 방법으로도 사용될 수
      있습니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Basic</h3>
      <div class="flex flex-col ">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
          <div class="flex justify-center gap-4">
            <div class="checkbox">
              <input type="checkbox" id="checkbox" class="checkbox-input" />
              <label for="checkbox" class="checkbox-label">
                오늘 하루 그만보기 <span class="checkbox-subtext">(Optional)</span></label
              >
            </div>
            <div class="checkbox">
              <input type="checkbox" id="checkbox2" class="checkbox-input" disabled />
              <label for="checkbox2" class="checkbox-label">
                오늘 하루 그만보기 <span class="checkbox-subtext">(Optional)</span></label
              >
            </div>
            <div class="checkbox checkbox-indeterminate">
              <input type="checkbox" id="checkbox3" class="checkbox-input" checked/>
              <label for="checkbox3" class="checkbox-label"> 오늘 하루 그만보기 </label>
            </div>
          </div>
          <div class="flex justify-center gap-4">
            <div class="checkbox checkbox-sm">
              <input type="checkbox" id="checkboxsm" class="checkbox-input" />
              <label for="checkboxsm" class="checkbox-label">
                오늘 하루 그만보기 <span class="checkbox-subtext">(Optional)</span></label
              >
            </div>
            <div class="checkbox checkbox-sm">
              <input type="checkbox" id="checkboxsm2" class="checkbox-input" disabled />
              <label for="checkboxsm2" class="checkbox-label">
                오늘 하루 그만보기 <span class="checkbox-subtext">(Optional)</span></label
              >
            </div>
            <div class="checkbox checkbox-indeterminate checkbox-sm">
              <input type="checkbox" id="checkboxsm3" class="checkbox-input" checked/>
              <label for="checkboxsm3" class="checkbox-label"> 오늘 하루 그만보기 </label>
            </div>
          </div>
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
      <h3 class="section-subtitle">Button</h3>
      <div class="flex flex-col ">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex justify-center gap-4">
              <div class="checkbox-button">
                <input type="checkbox" id="checkbox-btn-01" class="checkbox-button-input" />
                <label for="checkbox-btn-01" class="checkbox-button-label">checkbox button</label>
              </div>
              <div class="checkbox-button">
                <input type="checkbox" id="checkbox-btn-02" class="checkbox-button-input" disabled/>
                <label for="checkbox-btn-02" class="checkbox-button-label">checkbox button</label>
              </div>
              <div class="checkbox-button">
                <input type="checkbox" id="checkbox-btn-03" class="checkbox-button-input" checked/>
                <label for="checkbox-btn-03" class="checkbox-button-label">
                <svg-icon class="svg-icon" name="user"></svg-icon>
                  checkbox button
                </label>
              </div>
            </div>
            <div class="flex justify-center gap-4">
              <div class="checkbox-button checkbox-button-sm">
                <input type="checkbox" id="checkbox-btn-sm-01" class="checkbox-button-input" />
                <label for="checkbox-btn-sm-01" class="checkbox-button-label">checkbox button</label>
              </div>
              <div class="checkbox-button checkbox-button-sm">
                <input type="checkbox" id="checkbox-btn-sm-02" class="checkbox-button-input" disabled/>
                <label for="checkbox-btn-sm-02" class="checkbox-button-label">checkbox button</label>
              </div>
              <div class="checkbox-button checkbox-button-sm">
                <input type="checkbox" id="checkbox-btn-sm-03" class="checkbox-button-input" checked/>
                <label for="checkbox-btn-sm-03" class="checkbox-button-label">
                  <svg-icon class="svg-icon" name="user"></svg-icon>
                  checkbox button
                </label>
              </div>
            </div>
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
  name: "guide-checkbox"
};
</script>

<style scoped>

</style>