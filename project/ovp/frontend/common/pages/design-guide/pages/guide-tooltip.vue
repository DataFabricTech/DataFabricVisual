<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">tooltip</h2>
    <p class="section-summary">툴팁은 사용자가 요소에 마우스를 올리거나 초점을 맞췄을 때 나타나는 짧은 설명이나 정보를 제공하는 작은 팝업 컴포넌트입니다.</p>
    <article class="mb-10">
      <h3 class="section-subtitle">Tooltip</h3>
      <div class="flex flex-col">
        <div class="flex gap-8 mb-14">
          <div class="v-group gap-4">
            <div class="tooltip" v-tooltip:top.secondary="`Icon Tooltip Sample!`">
              <svg-icon class="svg-icon" name="info" style="color: #005acd"></svg-icon>
            </div>
            <p class="tooltip" v-tooltip="`Tooltip Sample`">Tooltip Sample</p>
            <p class="tooltip" v-tooltip:top="`My name is ${name}`">Tooltip Sample - Top</p>
            <p class="tooltip"
              v-tooltip:bottom.secondary="
                'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Sunt, voluptatem. '
              "
            >
              Tooltip Sample - Secondary
            </p>
            <p class="tooltip" v-tooltip:left.accent="`Tooltip Accent?`">Tooltip Sample - Accent</p>
            <p class="tooltip" v-tooltip:right.primary="`Tooltip Primary`">Tooltip Sample - Primary</p>
            <tooltip position="bottom">
              <template v-slot:text>
                <p>Point at me and find out the secret</p>
              </template>
              <template v-slot:tooltip>
                <strong>Watermelon is a berry</strong>
              </template>
            </tooltip>
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
