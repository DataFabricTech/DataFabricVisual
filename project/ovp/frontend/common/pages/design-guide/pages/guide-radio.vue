<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Radio</h2>
    <p class="section-summary">
      라디오는 여러 선택지 중에서 사용자가 한 가지만 선택할 수 있도록 합니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Basic</h3>
      <div class="flex flex-col">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex justify-center gap-4">
              <div class="radio">
                <input type="radio" id="radio" class="radio-input" name="radio1" checked />
                <label for="radio" class="radio-label">
                  직접 선택 <span class="radio-subtext">(Optional)</span></label
                >
              </div>
              <div class="radio">
                <input type="radio" id="radio2" class="radio-input" disabled name="radio1" />
                <label for="radio2" class="radio-label">
                  직접 선택 <span class="radio-subtext">(Optional)</span></label
                >
              </div>
              <div class="radio">
                <input type="radio" id="radio3" class="radio-input" name="radio1" />
                <label for="radio3" class="radio-label">직접 선택</label>
              </div>
            </div>
            <div class="flex justify-center gap-4">
              <div class="radio radio-sm">
                <input type="radio" id="radiosm" class="radio-input" name="radio2" checked />
                <label for="radiosm" class="radio-label">
                  직접 선택 <span class="radio-subtext">(Optional)</span></label
                >
              </div>
              <div class="radio radio-sm">
                <input type="radio" id="radiosm2" class="radio-input" disabled name="radio2" />
                <label for="radiosm2" class="radio-label">
                  직접 선택<span class="radio-subtext">(Optional)</span></label
                >
              </div>
              <div class="radio radio-sm">
                <input type="radio" id="radiosm3" class="radio-input" name="radio2" />
                <label for="radiosm3" class="radio-label">직접 선택</label>
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
  name: "guide-radio"
};
</script>

<style scoped>

</style>