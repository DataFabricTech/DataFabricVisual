<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">tooltip</h2>
    <p class="section-summary">툴팁은 사용자가 요소에 마우스를 올리거나 초점을 맞췄을 때 나타나는 짧은 설명이나 정보를 제공하는 작은 팝업 컴포넌트입니다.</p>
    <article class="mb-10">
      <h3 class="section-subtitle">Primary</h3>
      <div class="flex flex-col">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex justify-center gap-6">
              <div class="tooltip" v-tooltip:top="`top tooltip`">
                <svg-icon class="svg-icon" name="help-outline"></svg-icon>
              </div>
              <div class="tooltip" v-tooltip:right="`right tooltip`">
                <svg-icon class="svg-icon" name="help-outline"></svg-icon>
              </div>
              <div class="tooltip" v-tooltip:bottom="`bottom tooltip`">
                <svg-icon class="svg-icon" name="help-outline"></svg-icon>
              </div>
              <div class="tooltip" v-tooltip:left="`left tooltip`">
                <svg-icon class="svg-icon" name="help-outline"></svg-icon>
              </div>
            </div>
            <div class="flex justify-center gap-6">
              <div class="tooltip" v-tooltip:top="`top tooltip`">
                <p>Text Hover</p>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <p class="group-summary">
              Primary tooltip은 디폴트 스타일입니다. tooltip은 기본 top, right, bottom, left에서 나타나며 아이콘 또는 텍스트에 hover시 나타날 수 있습니다.
            </p>
          </div>
        </div>
      </div>
    </article>
    <article class="mb-10">
      <h3 class="section-subtitle">Light</h3>
      <div class="flex flex-col">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex justify-center gap-6">
              <div class="tooltip tooltip-light" v-tooltip:top="`top tooltip`">
                <svg-icon class="svg-icon" name="help-outline"></svg-icon>
              </div>
              <div class="tooltip tooltip-light" v-tooltip:right="`right tooltip`">
                <svg-icon class="svg-icon" name="help-outline"></svg-icon>
              </div>
              <div class="tooltip tooltip-light" v-tooltip:bottom="`bottom tooltip`">
                <svg-icon class="svg-icon" name="help-outline"></svg-icon>
              </div>
              <div class="tooltip tooltip-light" v-tooltip:left="`left tooltip`">
                <svg-icon class="svg-icon" name="help-outline"></svg-icon>
              </div>
            </div>
            <div class="flex justify-center gap-6">
              <div class="tooltip tooltip-light" v-tooltip:top="`top tooltip`">
                <p>Text Hover</p>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <p class="group-summary">
              밝은 색상의 light tooltip입니다.
            </p>
          </div>
        </div>
      </div>
    </article>
  </section>
</template>

<script setup lang="ts">
const name = "모비젠";
onMounted(() => {});
</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
