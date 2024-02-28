<template>
  <div>
    <h1 class="text-3xl font-bold underline">Fabric-design</h1>
    <section class="guide-section">
      <h2 class="section-title">color system</h2>
      <ol class="test">
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
      </ol>
    </section>
    <section class="guide-section">
      <h2 class="section-title">shadows</h2>
      <ol class="test shadows">
        <li>shadow-sm</li>
        <li>shadow-md</li>
        <li>shadow-lg</li>
        <li>shadow-xl</li>
      </ol>
    </section>
    <section class="guide-section">
      <h2 class="section-title">typography</h2>
      <div class="">
        <p v-for="index in 14" :key="index" class="guide-typo">
          데이터패브릭에서 제작한 모든 서비스와 문서에는 Pretendard 폰트를 사용합니다.
        </p>
      </div>
    </section>
    <section class="guide-section">
      <h2 class="section-title">icongraphy</h2>
      <div class="h-group gap-4">
        <svg-icon v-for="index in 5" :key="index" class="svg-icon icon-home" name="home"></svg-icon>
        <svg-icon class="svg-icon icon-trash" name="trash"></svg-icon>
      </div>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Button</h2>
      <article class="mb-10">
        <h3 class="section-subtitle">Button</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">Primary</h4>
            <div class="h-group gap-4">
              <button class="button button-primary button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-primary-stroke button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-primary-lighter button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-primary-ghost button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-primary-ghost button-lg" disabled type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-primary" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-primary-stroke" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-primary-lighter" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-primary-ghost" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-primary-ghost" disabled type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-primary button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-primary-stroke button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-primary-lighter button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-primary-ghost button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-primary-ghost button-sm" disabled type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
          </div>
          <div class="v-group gap-3">
            <h4 class="group-title">Secondary</h4>
            <div class="h-group gap-4">
              <button class="button button-secondary button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-secondary-stroke button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-secondary-lighter button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-secondary-ghost button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-secondary" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-secondary-stroke" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-secondary-lighter" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-secondary-ghost" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-secondary button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-secondary-stroke button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-secondary-lighter button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-secondary-ghost button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
          </div>
          <div class="v-group gap-3">
            <h4 class="group-title">Error</h4>
            <div class="h-group gap-4">
              <button class="button button-error button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-error-stroke button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-error-lighter button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-error-ghost button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-error" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-error-stroke" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-error-lighter" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-error-ghost" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-error button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-error-stroke button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-error-lighter button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-error-ghost button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
          </div>
          <div class="v-group gap-3">
            <h4 class="group-title">Neutral</h4>
            <div class="h-group gap-4">
              <button class="button button-neutral button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-neutral-stroke button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-neutral-lighter button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-neutral-ghost button-lg" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-neutral" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-neutral-stroke" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-neutral-lighter" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-neutral-ghost" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-neutral button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-neutral-stroke button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-neutral-lighter button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="button button-neutral-ghost button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
          </div>
        </div>
      </article>
      <article class="mb-10">
        <h3 class="section-subtitle">Icon Button</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">Primary</h4>
            <div class="h-group gap-4">
              <button class="button button-primary button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-stroke button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-lighter button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-ghost button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-ghost button-xl" disabled type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-primary button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-stroke button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-lighter button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-ghost button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-ghost button-lg" disabled type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-primary" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-stroke" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-lighter" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-ghost" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-ghost" disabled type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-primary button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-stroke button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-lighter button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-ghost button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-ghost button-sm" disabled type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-primary button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-stroke button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-lighter button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-ghost button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-primary-ghost button-xs" disabled type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
          </div>
          <div class="v-group gap-3">
            <h4 class="group-title">Secondary</h4>
            <div class="h-group gap-4">
              <button class="button button-secondary button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-secondary-stroke button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-secondary-lighter button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-secondary-ghost button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-secondary button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-secondary-stroke button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-secondary-lighter button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-secondary-ghost button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-secondary" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-secondary-stroke" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-secondary-lighter" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-secondary-ghost" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-secondary button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-secondary-stroke button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-secondary-lighter button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-secondary-ghost button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-secondary button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-secondary-stroke button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-secondary-lighter button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-secondary-ghost button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
          </div>
          <div class="v-group gap-3">
            <h4 class="group-title">Error</h4>
            <div class="h-group gap-4">
              <button class="button button-error button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-error-stroke button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-error-lighter button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-error-ghost button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-error button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-error-stroke button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-error-lighter button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-error-ghost button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-error" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-error-stroke" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-error-lighter" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-error-ghost" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-error button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-error-stroke button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-error-lighter button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-error-ghost button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-error button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-error-stroke button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-error-lighter button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-error-ghost button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
          </div>
          <div class="v-group gap-3">
            <h4 class="group-title">Neutral</h4>
            <div class="h-group gap-4">
              <button class="button button-neutral button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-neutral-stroke button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-neutral-lighter button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-neutral-ghost button-xl" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-neutral button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-neutral-stroke button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-neutral-lighter button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-neutral-ghost button-lg" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-neutral" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-neutral-stroke" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-neutral-lighter" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-neutral-ghost" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-neutral button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-neutral-stroke button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-neutral-lighter button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-neutral-ghost button-sm" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="button button-neutral button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-neutral-stroke button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-neutral-lighter button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
              <button class="button button-neutral-ghost button-xs" type="button">
                <span class="hidden-text">휴지통으로 이동</span>
                <svg-icon class="button-icon" name="trash"></svg-icon>
              </button>
            </div>
          </div>
        </div>
      </article>
      <article class="mb-10">
        <h3 class="section-subtitle">Link Button</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">Default</h4>
            <div class="h-group gap-4">
              <button class="link-button" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-black" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-support" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-error" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-error" disabled type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="link-button link-button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-black link-button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-support link-button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-error link-button-sm" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-error link-button-sm" disabled type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
          </div>
          <div class="v-group gap-3">
            <h4 class="group-title">Underline</h4>
            <div class="h-group gap-4">
              <button class="link-button link-button-underline" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-black link-button-underline" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-support link-button-underline" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-error link-button-underline" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-error link-button-underline" disabled type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
            <div class="h-group gap-4">
              <button class="link-button link-button-sm link-button-underline" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-black link-button-sm link-button-underline" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-support link-button-sm link-button-underline" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-error link-button-sm link-button-underline" type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
              <button class="link-button link-button-error link-button-sm link-button-underline" disabled type="button">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="button-title">Button</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Notification</h2>
      <article class="mb-10">
        <h3 class="section-subtitle">Inline Notification</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">Default</h4>
            <div class="h-group gap-4">
              <InlineNotification></InlineNotification>
            </div>
            <h4 class="group-title">Info</h4>
            <div class="h-group gap-4">
              <InlineNotification class="inline-notification-info"></InlineNotification>
            </div>
            <h4 class="group-title">Warning</h4>
            <div class="h-group gap-4">
              <InlineNotification class="inline-notification-warning"></InlineNotification>
            </div>
            <h4 class="group-title">Info</h4>
            <div class="h-group gap-4">
              <InlineNotification class="inline-notification-success"></InlineNotification>
            </div>
            <h4 class="group-title">Error</h4>
            <div class="h-group gap-4">
              <InlineNotification class="inline-notification-error"></InlineNotification>
            </div>
          </div>
        </div>
      </article>
      <article class="mb-10">
        <h3 class="section-subtitle">Basic Notification</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">Default</h4>
            <div class="h-group gap-4">
              <Notification></Notification>
            </div>
            <h4 class="group-title">Default - success 아이콘</h4>
            <div class="h-group gap-4">
              <Notification :notification-type="'info'"></Notification>
            </div>
            <h4 class="group-title">Default - success 아이콘</h4>
            <div class="h-group gap-4">
              <Notification :notification-type="'warning'"></Notification>
            </div>
            <h4 class="group-title">Default - success 아이콘</h4>
            <div class="h-group gap-4">
              <Notification :notification-type="'success'"></Notification>
            </div>
            <h4 class="group-title">Default - success 아이콘</h4>
            <div class="h-group gap-4">
              <Notification :notification-type="'error'"></Notification>
            </div>
            <h4 class="group-title">Info</h4>
            <div class="h-group gap-4">
              <Notification class="notification-info"></Notification>
            </div>
            <h4 class="group-title">Warning</h4>
            <div class="h-group gap-4">
              <Notification class="notification-warning"></Notification>
            </div>
            <h4 class="group-title">Info</h4>
            <div class="h-group gap-4">
              <Notification class="notification-success"></Notification>
            </div>
            <h4 class="group-title">Error</h4>
            <div class="h-group gap-4">
              <Notification class="notification-error"></Notification>
            </div>
          </div>
        </div>
      </article>
      <article class="mb-10">
        <h3 class="section-subtitle">Extends Notification</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">Default</h4>
            <div class="h-group gap-4">
              <Notification :isExtends="true" class="w-96"></Notification>
            </div>
            <h4 class="group-title">Default - success 아이콘</h4>
            <div class="h-group gap-4">
              <Notification :isExtends="true" :notification-type="'primary'" class="w-96"></Notification>
            </div>
            <h4 class="group-title">Info</h4>
            <div class="h-group gap-4">
              <Notification :isExtends="true" class="w-96 notification-info"></Notification>
            </div>
            <h4 class="group-title">Warning</h4>
            <div class="h-group gap-4">
              <Notification :isExtends="true" class="w-96 notification-warning"></Notification>
            </div>
            <h4 class="group-title">Info</h4>
            <div class="h-group gap-4">
              <Notification :isExtends="true" class="w-96 notification-success"></Notification>
            </div>
            <h4 class="group-title">Error</h4>
            <div class="h-group gap-4">
              <Notification :isExtends="true" class="w-96 notification-error"></Notification>
            </div>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Checkbox</h2>
      <article class="mb-10">
        <h3 class="section-subtitle">Basic Checkbox</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <div class="h-group gap-4">
              <div class="checkbox">
                <input type="checkbox" id="checkbox" class="checkbox-input" />
                <label for="checkbox" class="checkbox-label"> 오늘 하루 그만보기
                  <span class="checkbox-subtext">(Optional)</span></label>
              </div>
              <div class="checkbox">
                <input type="checkbox" id="checkbox2" class="checkbox-input" disabled/>
                <label for="checkbox2" class="checkbox-label"> 오늘 하루 그만보기
                  <span class="checkbox-subtext">(Optional)</span></label>
              </div>
              <div class="checkbox checkbox-indeterminate">
                <input type="checkbox" id="checkbox3" class="checkbox-input" />
                <label for="checkbox3" class="checkbox-label"> 오늘 하루 그만보기 </label>
              </div>
            </div>
            <div class="h-group gap-4">
              <div class="checkbox checkbox-sm">
                <input type="checkbox" id="checkboxsm" class="checkbox-input" />
                <label for="checkboxsm" class="checkbox-label"> 오늘 하루 그만보기
                  <span class="checkbox-subtext">(Optional)</span></label>
              </div>
              <div class="checkbox checkbox-sm">
                <input type="checkbox" id="checkboxsm2" class="checkbox-input" disabled/>
                <label for="checkboxsm2" class="checkbox-label"> 오늘 하루 그만보기
                  <span class="checkbox-subtext">(Optional)</span></label>
              </div>
              <div class="checkbox checkbox-indeterminate checkbox-sm">
                <input type="checkbox" id="checkboxsm3" class="checkbox-input" />
                <label for="checkboxsm3" class="checkbox-label"> 오늘 하루 그만보기 </label>
              </div>
            </div>
          </div>
        </div>
      </article>
      <article class="mb-10">
        <h3 class="section-subtitle">Toggle Checkbox</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <div class="h-group gap-4">
              <div class="checkbox">
                <input type="checkbox" id="checkbox" class="checkbox-input" />
                <label for="checkbox" class="checkbox-label"> 오늘 하루 그만보기
                  <span class="checkbox-subtext">(Optional)</span></label>
              </div>
              <div class="checkbox">
                <input type="checkbox" id="checkbox2" class="checkbox-input" disabled/>
                <label for="checkbox2" class="checkbox-label"> 오늘 하루 그만보기
                  <span class="checkbox-subtext">(Optional)</span></label>
              </div>
              <div class="checkbox checkbox-indeterminate">
                <input type="checkbox" id="checkbox3" class="checkbox-input" />
                <label for="checkbox3" class="checkbox-label"> 오늘 하루 그만보기 </label>
              </div>
            </div>
            <div class="h-group gap-4">
              <div class="checkbox checkbox-sm">
                <input type="checkbox" id="checkboxsm" class="checkbox-input" />
                <label for="checkboxsm" class="checkbox-label"> 오늘 하루 그만보기
                  <span class="checkbox-subtext">(Optional)</span></label>
              </div>
              <div class="checkbox checkbox-sm">
                <input type="checkbox" id="checkboxsm2" class="checkbox-input" disabled/>
                <label for="checkboxsm2" class="checkbox-label"> 오늘 하루 그만보기
                  <span class="checkbox-subtext">(Optional)</span></label>
              </div>
              <div class="checkbox checkbox-indeterminate checkbox-sm">
                <input type="checkbox" id="checkboxsm3" class="checkbox-input" />
                <label for="checkboxsm3" class="checkbox-label"> 오늘 하루 그만보기 </label>
              </div>
            </div>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Radio</h2>
      <article class="mb-10">
        <h3 class="section-subtitle">Basic Radio</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <div class="h-group gap-4">
              <div class="radio">
                <input type="radio" id="radio" class="radio-input" />
                <label for="radio" class="radio-label"> 오늘 하루 그만보기
                  <span class="radio-subtext">(Optional)</span></label>
              </div>
              <div class="radio">
                <input type="radio" id="radio2" class="radio-input" disabled/>
                <label for="radio2" class="radio-label"> 오늘 하루 그만보기
                  <span class="radio-subtext">(Optional)</span></label>
              </div>
              <div class="radio radio-indeterminate">
                <input type="radio" id="radio3" class="radio-input" />
                <label for="radio3" class="radio-label"> 오늘 하루 그만보기 </label>
              </div>
            </div>
            <div class="h-group gap-4">
              <div class="radio radio-sm">
                <input type="radio" id="radiosm" class="radio-input" />
                <label for="radiosm" class="radio-label"> 오늘 하루 그만보기
                  <span class="radio-subtext">(Optional)</span></label>
              </div>
              <div class="radio radio-sm">
                <input type="radio" id="radiosm2" class="radio-input" disabled/>
                <label for="radiosm2" class="radio-label"> 오늘 하루 그만보기
                  <span class="radio-subtext">(Optional)</span></label>
              </div>
              <div class="radio radio-indeterminate radio-sm">
                <input type="radio" id="radiosm3" class="radio-input" />
                <label for="radiosm3" class="radio-label"> 오늘 하루 그만보기 </label>
              </div>
            </div>
          </div>
        </div>
      </article>
      <article class="mb-10">
        <h3 class="section-subtitle">Toggle Radio</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <div class="h-group gap-4">
              <div class="radio">
                <input type="radio" id="radio" class="radio-input" />
                <label for="radio" class="radio-label"> 오늘 하루 그만보기
                  <span class="radio-subtext">(Optional)</span></label>
              </div>
              <div class="radio">
                <input type="radio" id="radio2" class="radio-input" disabled/>
                <label for="radio2" class="radio-label"> 오늘 하루 그만보기
                  <span class="radio-subtext">(Optional)</span></label>
              </div>
              <div class="radio radio-indeterminate">
                <input type="radio" id="radio3" class="radio-input" />
                <label for="radio3" class="radio-label"> 오늘 하루 그만보기 </label>
              </div>
            </div>
            <div class="h-group gap-4">
              <div class="radio radio-sm">
                <input type="radio" id="radiosm" class="radio-input" />
                <label for="radiosm" class="radio-label"> 오늘 하루 그만보기
                  <span class="radio-subtext">(Optional)</span></label>
              </div>
              <div class="radio radio-sm">
                <input type="radio" id="radiosm2" class="radio-input" disabled/>
                <label for="radiosm2" class="radio-label"> 오늘 하루 그만보기
                  <span class="radio-subtext">(Optional)</span></label>
              </div>
              <div class="radio radio-indeterminate radio-sm">
                <input type="radio" id="radiosm3" class="radio-input" />
                <label for="radiosm3" class="radio-label"> 오늘 하루 그만보기 </label>
              </div>
            </div>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">components</h2>
      <div class="link-buttons">
        <!-- 3. 메뉴 버튼 추가 @click="clickMenu('컴포넌트명')" -->
        <button @click="clickMenu('Dayjs')">Dayjs</button>
        <button @click="clickMenu('Highcharts')">HighCharts</button>
        <button @click="clickMenu('VueFinalModal')">Vue Final Modal</button>
        <button @click="clickMenu('AgGrid')">Ag-Grid</button>
        <button @click="clickMenu('VueDatepickerNext')">Vue Datepicker Nuxt</button>
        <button @click="clickMenu('Locale')">Locale</button>
      </div>
      <div></div>
    </section>
  </div>
</template>

<script lang="ts" setup>
import { shallowRef } from "vue";
// 1. 컴포넌트 등록
// ex) 컴포넌트명 AgGrid / 컴포넌트명 규칙: 폴더명 ag-grid -> AgGrid, `-` 로 연결된 부분은 대문자로 연결
import AgGrid from "~/pages/ag-grid/index.vue";
import Dayjs from "~/pages/dayjs/index.vue";
import Highcharts from "~/pages/highcharts/index.vue";
import Locale from "~/pages/locale/index.vue";
import VueDatepickerNext from "~/pages/vue-datepicker-next/index.vue";
import VueFinalModal from "~/pages/vue-final-modal/index.vue";
import Notification from "../components/extends/notification/Notification.vue";
import InlineNotification from "../components/extends/inline-notification/InlineNotification.vue";

const currentComponent = shallowRef(Dayjs);

// 2. 컴포넌트를 객체에 저장
const components: any = {
  AgGrid,
  Dayjs,
  Highcharts,
  Locale,
  VueDatepickerNext,
  VueFinalModal,
  Notification,
  InlineNotification
};

function clickMenu(menu: string) {
  currentComponent.value = components[menu];
}
</script>

<style lang="css" scoped></style>
