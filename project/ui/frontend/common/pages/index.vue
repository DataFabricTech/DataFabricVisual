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
      <h2 class="section-title">icongraphy</h2>
      <div class="h-group gap-4">
        <svg-icon v-for="index in 5" :key="index" class="svg-icon icon-home" name="home"></svg-icon>
        <svg-icon class="svg-icon" name="trash"></svg-icon>
      </div>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Button</h2>
      <article class="mb-10">
        <h3 class="section-subtitle">Basic</h3>
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
                <span class="button-title">Disabled</span>
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
                <span class="button-title">Disabled</span>
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
                <span class="button-title">Disabled</span>
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
        <h3 class="section-subtitle">Icon</h3>
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
        <h3 class="section-subtitle">Link</h3>
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
                <span class="button-title">Disabled</span>
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
                <span class="button-title">Disabled</span>
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
                <span class="button-title">Disabled</span>
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
                <span class="button-title">Disabled</span>
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
              </button>
            </div>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Alert</h2>
      <article class="mb-10">
        <div class="v-group gap-5">
          <h4 class="group-title">Default</h4>
          <Alert></Alert>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Notification</h2>
      <article class="mb-10">
        <h3 class="section-subtitle">Notification</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">Default</h4>
            <div class="h-group gap-4">
              <div class="notification">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
              <div class="notification notification-sm">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
            </div>
            <h4 class="group-title">Info</h4>
            <div class="h-group gap-4">
              <div class="notification notification-info">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
              <div class="notification notification-sm notification-info">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
            </div>
            <h4 class="group-title">Warning</h4>
            <div class="h-group gap-4">
              <div class="notification notification-warning">
                <svg-icon class="notification-icon" name="warning"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
              <div class="notification notification-sm notification-warning">
                <svg-icon class="notification-icon" name="warning"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
            </div>
            <h4 class="group-title">Info</h4>
            <div class="h-group gap-4">
              <div class="notification notification-success">
                <svg-icon class="notification-icon" name="success"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
              <div class="notification notification-sm notification-success">
                <svg-icon class="notification-icon" name="success"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
            </div>
            <h4 class="group-title">Error</h4>
            <div class="h-group gap-4">
              <div class="notification notification-error">
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
              <div class="notification notification-sm notification-error">
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
            </div>
          </div>
        </div>
      </article>
      <article class="mb-10">
        <h3 class="section-subtitle">Notification Box</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">Default</h4>
            <Notification></Notification>
            <h4 class="group-title">Info 아이콘</h4>
            <Notification :notification-type="'info'"></Notification>
            <h4 class="group-title">Warning 아이콘</h4>
            <Notification :notification-type="'warning'"></Notification>
            <h4 class="group-title">Sucess 아이콘</h4>
            <Notification :notification-type="'success'"></Notification>
            <h4 class="group-title">Error 아이콘</h4>
            <Notification :notification-type="'error'"></Notification>
            <h4 class="group-title">Info</h4>
            <Notification class="notification-box-info"></Notification>
            <h4 class="group-title">Warning</h4>
            <Notification class="notification-box-warning"></Notification>
            <h4 class="group-title">Info</h4>
            <Notification class="notification-box-success"></Notification>
            <h4 class="group-title">Error</h4>
            <Notification class="notification-box-error"></Notification>
          </div>
        </div>
      </article>
      <article class="mb-10">
        <h3 class="section-subtitle">Notification Box Extends</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">Default</h4>
            <Notification :isExtends="true" class="w-96"></Notification>
            <h4 class="group-title">Info 아이콘</h4>
            <Notification :isExtends="true" :notification-type="'info'" class="w-96"></Notification>
            <h4 class="group-title">Warning 아이콘</h4>
            <Notification :isExtends="true" :notification-type="'warning'" class="w-96"></Notification>
            <h4 class="group-title">Success 아이콘</h4>
            <Notification :isExtends="true" :notification-type="'success'" class="w-96"></Notification>
            <h4 class="group-title">Error 아이콘</h4>
            <Notification :isExtends="true" :notification-type="'error'" class="w-96"></Notification>
            <h4 class="group-title">Info</h4>
            <Notification :isExtends="true" class="w-96 notification-box-info"></Notification>
            <h4 class="group-title">Warning</h4>
            <Notification :isExtends="true" class="w-96 notification-box-warning"></Notification>
            <h4 class="group-title">Info</h4>
            <Notification :isExtends="true" class="w-96 notification-box-success"></Notification>
            <h4 class="group-title">Error</h4>
            <Notification :isExtends="true" class="w-96 notification-box-error"></Notification>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Checkbox</h2>
      <article class="mb-10">
        <h3 class="section-subtitle">Basic</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <div class="element-group">
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
                <input type="checkbox" id="checkbox3" class="checkbox-input" />
                <label for="checkbox3" class="checkbox-label"> 오늘 하루 그만보기 </label>
              </div>
            </div>
            <div class="element-group">
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
                <input type="checkbox" id="checkboxsm3" class="checkbox-input" />
                <label for="checkboxsm3" class="checkbox-label"> 오늘 하루 그만보기 </label>
              </div>
            </div>
          </div>
        </div>
      </article>
      <article class="mb-10">
        <h3 class="section-subtitle">Switch</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <div class="h-group gap-4">
              <div class="switch">
                <input type="checkbox" id="sw" class="switch-input" />
                <label for="sw" class="switch-label">
                  Label
                  <div class="switch-control"></div>
                </label>
              </div>
              <div class="switch">
                <input type="checkbox" id="sw2" class="switch-input" checked />
                <label for="sw2" class="switch-label">
                  Label
                  <div class="switch-control"></div>
                </label>
              </div>
              <div class="switch">
                <input type="checkbox" id="sw3" class="switch-input" disabled />
                <label for="sw3" class="switch-label">
                  Disabled
                  <div class="switch-control"></div>
                </label>
              </div>
            </div>
          </div>
        </div>
      </article>
      <article class="mb-10">
        <h3 class="section-subtitle">Button-Group</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">Basic</h4>
            <div class="h-group gap-4">
              <div class="v-group gap-3">
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
              </div>
            </div>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Radio</h2>
      <article class="mb-10">
        <h3 class="section-subtitle">Basic</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <div class="element-group">
              <div class="radio">
                <input type="radio" id="radio" class="radio-input" name="radio1" checked />
                <label for="radio" class="radio-label">
                  오늘하루 그만보기 <span class="radio-subtext">(Optional)</span></label
                >
              </div>
              <div class="radio">
                <input type="radio" id="radio2" class="radio-input" disabled name="radio1" />
                <label for="radio2" class="radio-label">
                  오늘 하루 그만보기 <span class="radio-subtext">(Optional)</span></label
                >
              </div>
              <div class="radio">
                <input type="radio" id="radio3" class="radio-input" name="radio1" />
                <label for="radio3" class="radio-label"> 오늘 하루 그만보기 </label>
              </div>
            </div>
            <div class="element-group">
              <div class="radio radio-sm">
                <input type="radio" id="radiosm" class="radio-input" name="radio2" checked />
                <label for="radiosm" class="radio-label">
                  오늘 하루 그만보기 <span class="radio-subtext">(Optional)</span></label
                >
              </div>
              <div class="radio radio-sm">
                <input type="radio" id="radiosm2" class="radio-input" disabled name="radio2" />
                <label for="radiosm2" class="radio-label">
                  오늘 하루 그만보기 <span class="radio-subtext">(Optional)</span></label
                >
              </div>
              <div class="radio radio-sm">
                <input type="radio" id="radiosm3" class="radio-input" name="radio2" />
                <label for="radiosm3" class="radio-label"> 오늘 하루 그만보기 </label>
              </div>
            </div>
          </div>
        </div>
      </article>
      <article class="mb-10">
        <h3 class="section-subtitle">Toggle</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">Basic</h4>
            <div class="h-group gap-4">
              <div class="v-group gap-3">
                <div class="toggle toggle-lg">
                  <input type="radio" id="toggle" class="toggle-input" name="toggle1" checked />
                  <label for="toggle" class="toggle-label">
                    <svg-icon class="toggle-icon" name="trash"></svg-icon>
                    Toggle
                  </label>
                  <input type="radio" id="toggle2" class="toggle-input" name="toggle1" />
                  <label for="toggle2" class="toggle-label">
                    <svg-icon class="toggle-icon" name="trash"></svg-icon>
                    Toggle
                  </label>
                  <input type="radio" id="toggle3" class="toggle-input" name="toggle1" />
                  <label for="toggle3" class="toggle-label">
                    <svg-icon class="toggle-icon" name="trash"></svg-icon>
                    Toggle
                  </label>
                  <input type="radio" id="toggle4" class="toggle-input" name="toggle1" disabled />
                  <label for="toggle4" class="toggle-label">
                    <svg-icon class="toggle-icon" name="trash"></svg-icon>
                    Disabled
                  </label>
                </div>
                <div class="toggle">
                  <input type="radio" id="togglesm" class="toggle-input" name="toggle2" checked />
                  <label for="togglesm" class="toggle-label">
                    <svg-icon class="toggle-icon" name="trash"></svg-icon>
                    Toggle
                  </label>
                  <input type="radio" id="togglesm2" class="toggle-input" name="toggle2" />
                  <label for="togglesm2" class="toggle-label">
                    <svg-icon class="toggle-icon" name="trash"></svg-icon>
                    Toggle
                  </label>
                  <input type="radio" id="togglesm3" class="toggle-input" name="toggle2" />
                  <label for="togglesm3" class="toggle-label">
                    <svg-icon class="toggle-icon" name="trash"></svg-icon>
                    Toggle
                  </label>
                </div>
              </div>
            </div>
            <h4 class="group-title">Primary</h4>
            <div class="h-group gap-4">
              <div class="v-group gap-3">
                <div class="toggle toggle-lg toggle-primary">
                  <input type="radio" id="toggleprimary" class="toggle-input" name="toggle3" checked />
                  <label for="toggleprimary" class="toggle-label">
                    <svg-icon class="toggle-icon" name="trash"></svg-icon>
                    Toggle
                  </label>
                  <input type="radio" id="toggleprimary2" class="toggle-input" name="toggle3" />
                  <label for="toggleprimary2" class="toggle-label">
                    <svg-icon class="toggle-icon" name="trash"></svg-icon>
                    Toggle
                  </label>
                  <input type="radio" id="toggleprimary3" class="toggle-input" name="toggle3" />
                  <label for="toggleprimary3" class="toggle-label">
                    <svg-icon class="toggle-icon" name="trash"></svg-icon>
                    Toggle
                  </label>
                  <input type="radio" id="toggleprimary4" class="toggle-input" name="toggle3" disabled />
                  <label for="toggleprimary4" class="toggle-label">
                    <svg-icon class="toggle-icon" name="trash"></svg-icon>
                    Disabled
                  </label>
                </div>
                <div class="toggle toggle-primary">
                  <input type="radio" id="toggleprimarysm" class="toggle-input" name="toggle4" checked />
                  <label for="toggleprimarysm" class="toggle-label">
                    <svg-icon class="toggle-icon" name="trash"></svg-icon>
                    Toggle
                  </label>
                  <input type="radio" id="toggleprimarysm2" class="toggle-input" name="toggle4" />
                  <label for="toggleprimarysm2" class="toggle-label">
                    <svg-icon class="toggle-icon" name="trash"></svg-icon>
                    Toggle
                  </label>
                  <input type="radio" id="toggleprimarysm3" class="toggle-input" name="toggle4" />
                  <label for="toggleprimarysm3" class="toggle-label">
                    <svg-icon class="toggle-icon" name="trash"></svg-icon>
                    Toggle
                  </label>
                </div>
              </div>
            </div>
          </div>
        </div>
      </article>
      <article class="mb-10">
        <h3 class="section-subtitle">Button-Group</h3>
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">Basic</h4>
            <div class="h-group gap-4">
              <div class="v-group gap-3">
                <div class="button-group button-group-lg">
                  <input
                    type="radio"
                    id="button-groupprimary"
                    class="button-group-input"
                    name="button-group3"
                    checked
                  />
                  <label for="button-groupprimary" class="button-group-label"> Toggle </label>
                  <input type="radio" id="button-groupprimary2" class="button-group-input" name="button-group3" />
                  <label for="button-groupprimary2" class="button-group-label"> Toggle </label>
                  <input type="radio" id="button-groupprimary8" class="button-group-input" name="button-group3" />
                  <label for="button-groupprimary8" class="button-group-label"> Toggle </label>
                  <input type="radio" id="button-groupprimary3" class="button-group-input" name="button-group3" />
                  <label for="button-groupprimary3" class="button-group-label"> Toggle </label>
                  <input
                    type="radio"
                    id="button-groupprimary4"
                    class="button-group-input"
                    name="button-group3"
                    disabled
                  />
                  <label for="button-groupprimary4" class="button-group-label"> Disabled </label>
                </div>
                <div class="button-group">
                  <input
                    type="radio"
                    id="button-groupprimarysm"
                    class="button-group-input"
                    name="button-group4"
                    checked
                  />
                  <label for="button-groupprimarysm" class="button-group-label"> Toggle </label>
                  <input
                    type="radio"
                    id="button-groupprimarysm6"
                    class="button-group-input"
                    name="button-group4"
                    checked
                  />
                  <label for="button-groupprimarysm6" class="button-group-label"> Toggle </label>
                  <input type="radio" id="button-groupprimarysm2" class="button-group-input" name="button-group4" />
                  <label for="button-groupprimarysm2" class="button-group-label"> Toggle </label>
                  <input type="radio" id="button-groupprimarysm3" class="button-group-input" name="button-group4" />
                  <label for="button-groupprimarysm3" class="button-group-label"> Toggle </label>
                </div>
              </div>
            </div>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Pagination</h2>
      <article class="mb-10">
        <h3 class="section-subtitle">Basic Pagination</h3>
        <div class="flex flex-col align-center gap-5">
          <Pagination :totalCount="60" :perPage="10" :currentPageNumber="4" @change="checkCurrentPage"></Pagination>
        </div>
      </article>
      <article class="mb-10">
        <h3 class="section-subtitle">Numerous Pagination</h3>
        <div class="flex flex-col align-center gap-5">
          <Pagination :totalCount="100" :perPage="10" :currentPageNumber="1" @change="checkCurrentPage"></Pagination>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Badge</h2>
      <article class="mb-10">
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">filled</h4>
            <div class="h-group gap-4">
              <div class="badge badge-red">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-yellow">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-green">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-blue">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-purple">
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-gray">
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-primary">
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-secondary">
                <p class="badge-text">badge</p>
              </div>
            </div>
            <div class="h-group gap-4">
              <div class="badge badge-red">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-yellow">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-green">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-blue">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-purple">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-gray">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-primary">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-secondary">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
            </div>
            <h4 class="group-title">lighter</h4>
            <div class="h-group gap-4">
              <div class="badge badge-red-lighter">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
                <p class="badge-text">badge</p>
                <span class="hidden-text">아이콘 뱃지 설명</span>
              </div>
              <div class="badge badge-yellow-lighter">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-green-lighter">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-blue-lighter">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-purple-lighter">
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-gray-lighter">
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-primary-lighter">
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-secondary-lighter">
                <p class="badge-text">badge</p>
              </div>
            </div>
            <div class="h-group gap-4">
              <div class="badge badge-red-lighter">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-yellow-lighter">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-green-lighter">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-blue-lighter">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-purple-lighter">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-gray-lighter">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-primary-lighter">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-secondary-lighter">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
            </div>
            <h4 class="group-title">stroke</h4>
            <div class="h-group gap-4">
              <div class="badge badge-red-stroke">
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-yellow-stroke">
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-green-stroke">
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-blue-stroke">
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-purple-stroke">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-gray-stroke">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-primary-stroke">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
                <p class="badge-text">badge</p>
              </div>
              <div class="badge badge-secondary-stroke">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
                <p class="badge-text">badge</p>
              </div>
            </div>
            <div class="h-group gap-4">
              <div class="badge badge-red-stroke">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-yellow-stroke">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-green-stroke">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-blue-stroke">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-purple-stroke">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-gray-stroke">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-primary-stroke">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
              <div class="badge badge-secondary-stroke">
                <svg-icon class="badge-icon" name="folder"></svg-icon>
              </div>
            </div>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Tag</h2>
      <article class="mb-10">
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">Primary</h4>
            <div class="h-group gap-4">
              <div class="tag tag-primary">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-primary is-tag-selected">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-primary is-tag-disabled">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </div>
            <div class="h-group gap-4">
              <div class="tag tag-primary tag-sm">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-primary tag-sm is-tag-selected">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-primary tag-sm is-tag-disabled">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </div>
            <h4 class="group-title">Lighter</h4>
            <div class="h-group gap-4">
              <div class="tag tag-primary-lighter">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-primary-lighter is-tag-selected">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </div>
            <div class="h-group gap-4">
              <div class="tag tag-primary-lighter tag-sm">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-primary-lighter tag-sm is-tag-selected">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </div>
            <h4 class="group-title">Neutral</h4>
            <div class="h-group gap-4">
              <div class="tag tag-neutral">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-neutral is-tag-selected">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </div>
            <div class="h-group gap-4">
              <div class="tag tag-neutral tag-sm">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="tag tag-neutral tag-sm is-tag-selected">
                <a class="tag-link">TAG</a>
                <button class="tag-delete-button">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </div>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Step</h2>
      <article class="mb-10">
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">default</h4>
            <div class="step" style="width:450px">
              <div class="step-section is-step-section-complete">
                <div class="step-item">
                  <button class="step-button">
                    <div class="step-circle">
                      <p class="step-number">1</p>
                      <svg-icon class="step-icon" name="checkmark"></svg-icon>
                    </div>
                  </button>
                  <span class="step-title">step</span>
                </div>
              </div>
              <div class="step-section is-step-section-complete">
                <div class="step-item">
                  <button class="step-button">
                    <div class="step-circle">
                      <p class="step-number">2</p>
                      <svg-icon class="step-icon" name="checkmark"></svg-icon>
                    </div>
                  </button>
                  <span class="step-title">step</span>
                </div>
              </div>
              <div class="step-section">
                <div class="step-item is-step-item-selected">
                  <button class="step-button">
                    <div class="step-circle">
                      <p class="step-number">3</p>
                      <svg-icon class="step-icon" name="checkmark"></svg-icon>
                    </div>
                  </button>
                  <span class="step-title">stepstep</span>
                </div>
              </div>
              <div class="step-section">
                <div class="step-item">
                  <button class="step-button">
                    <div class="step-circle">
                      <p class="step-number">4</p>
                      <svg-icon class="step-icon" name="checkmark"></svg-icon>
                    </div>
                  </button>
                  <span class="step-title">step</span>
                </div>
              </div>
            </div>
            <h4 class="group-title">dotted</h4>
            <div class="step step-dotted" style="width:450px">
              <div class="step-section is-step-section-complete">
                <div class="step-item">
                  <button class="step-button">
                    <div class="step-circle">
                      <p class="step-number">1</p>
                      <svg-icon class="step-icon" name="checkmark"></svg-icon>
                    </div>
                  </button>
                  <span class="step-title">step</span>
                </div>
              </div>
              <div class="step-section is-step-section-complete">
                <div class="step-item">
                  <button class="step-button">
                    <div class="step-circle">
                      <p class="step-number">2</p>
                      <svg-icon class="step-icon" name="checkmark"></svg-icon>
                    </div>
                  </button>
                  <span class="step-title">step</span>
                </div>
              </div>
              <div class="step-section">
                <div class="step-item is-step-item-selected">
                  <button class="step-button">
                    <div class="step-circle">
                      <p class="step-number">3</p>
                      <svg-icon class="step-icon" name="checkmark"></svg-icon>
                    </div>
                  </button>
                  <span class="step-title">stepstep</span>
                </div>
              </div>
              <div class="step-section">
                <div class="step-item">
                  <button class="step-button">
                    <div class="step-circle">
                      <p class="step-number">4</p>
                      <svg-icon class="step-icon" name="checkmark"></svg-icon>
                    </div>
                  </button>
                  <span class="step-title">step</span>
                </div>
              </div>
            </div>
            <h4 class="group-title">Vertical</h4>
            <div class="h-group gap-4">
              <div class="step step-vertical" style="height:450px">
                <div class="step-section is-step-section-complete">
                  <div class="step-item">
                    <button class="step-button">
                      <div class="step-circle">
                        <p class="step-number">1</p>
                        <svg-icon class="step-icon" name="checkmark"></svg-icon>
                      </div>
                    </button>
                    <span class="step-title">step</span>
                  </div>
                </div>
                <div class="step-section is-step-section-complete">
                  <div class="step-item">
                    <button class="step-button">
                      <div class="step-circle">
                        <p class="step-number">2</p>
                        <svg-icon class="step-icon" name="checkmark"></svg-icon>
                      </div>
                    </button>
                    <span class="step-title">step</span>
                  </div>
                </div>
                <div class="step-section">
                  <div class="step-item is-step-item-selected">
                    <button class="step-button">
                      <div class="step-circle">
                        <p class="step-number">3</p>
                        <svg-icon class="step-icon" name="checkmark"></svg-icon>
                      </div>
                    </button>
                    <span class="step-title">stepstep</span>
                  </div>
                </div>
                <div class="step-section">
                  <div class="step-item">
                    <button class="step-button">
                      <div class="step-circle">
                        <p class="step-number">4</p>
                        <svg-icon class="step-icon" name="checkmark"></svg-icon>
                      </div>
                    </button>
                    <span class="step-title">step</span>
                  </div>
                </div>
              </div>
              <div class="step step-vertical step-dotted" style="height:450px">
                <div class="step-section is-step-section-complete">
                  <div class="step-item">
                    <button class="step-button">
                      <div class="step-circle">
                        <p class="step-number">1</p>
                        <svg-icon class="step-icon" name="checkmark"></svg-icon>
                      </div>
                    </button>
                    <span class="step-title">step</span>
                  </div>
                </div>
                <div class="step-section is-step-section-complete">
                  <div class="step-item">
                    <button class="step-button">
                      <div class="step-circle">
                        <p class="step-number">2</p>
                        <svg-icon class="step-icon" name="checkmark"></svg-icon>
                      </div>
                    </button>
                    <span class="step-title">step</span>
                  </div>
                </div>
                <div class="step-section">
                  <div class="step-item is-step-item-selected">
                    <button class="step-button">
                      <div class="step-circle">
                        <p class="step-number">3</p>
                        <svg-icon class="step-icon" name="checkmark"></svg-icon>
                      </div>
                    </button>
                    <span class="step-title">stepstep</span>
                  </div>
                </div>
                <div class="step-section">
                  <div class="step-item">
                    <button class="step-button">
                      <div class="step-circle">
                        <p class="step-number">4</p>
                        <svg-icon class="step-icon" name="checkmark"></svg-icon>
                      </div>
                    </button>
                    <span class="step-title">step</span>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Breadcrumb</h2>
      <article class="mb-10">
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <Breadcrumb></Breadcrumb>
            <Breadcrumb class="breadcrumb-slash"></Breadcrumb>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Text Input</h2>
      <article class="mb-10">
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">default</h4>
            <div class="flex gap-6 items-start">
              <div class="v-group gap-4">
                <label class="hidden-text" for="text-input-example">label</label>
                <input id="text-input-example" class="text-input text-input-lg" placeholder="placeholder" />
                <label class="hidden-text" for="text-input-example-3">label</label>
                <input id="text-input-example-3" class="text-input text-input-lg" placeholder="placeholder" disabled />
                <div class="text-input-group">
                  <label class="hidden-text" for="text-input-example-4">label</label>
                  <input id="text-input-example-4" class="text-input text-input-lg" placeholder="prefix icon" />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                </div>
                <div class="text-input-group">
                  <label class="hidden-text" for="text-input-example-4">label</label>
                  <input id="text-input-example-4" class="text-input text-input-lg" placeholder="button" />
                  <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="text-input-group">
                  <label class="hidden-text" for="text-input-example-4">label</label>
                  <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                  <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
              </div>
              <div class="v-group gap-5">
                <label class="hidden-text" for="text-input-example">label</label>
                <input id="text-input-example" class="text-input" placeholder="placeholder" />
                <label class="hidden-text" for="text-input-example-3">label</label>
                <input id="text-input-example-3" class="text-input" placeholder="placeholder" disabled />
                <div class="text-input-group">
                  <label class="hidden-text" for="text-input-example-4">label</label>
                  <input id="text-input-example-4" class="text-input" placeholder="prefix icon" />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                </div>
                <div class="text-input-group">
                  <label class="hidden-text" for="text-input-example-5">label</label>
                  <input id="text-input-example-5" class="text-input" placeholder="button" />
                  <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="text-input-group">
                  <label class="hidden-text" for="text-input-example-6">label</label>
                  <input id="text-input-example-6" class="text-input" value="prefix icon + button" />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                  <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
              </div>
              <div class="v-group gap-6">
                <label class="hidden-text" for="text-input-example">label</label>
                <input id="text-input-example" class="text-input text-input-sm" placeholder="placeholder" />
                <label class="hidden-text" for="text-input-example-7">label</label>
                <input id="text-input-example-7" class="text-input text-input-sm" placeholder="placeholder" disabled />
                <div class="text-input-group">
                  <label class="hidden-text" for="text-input-example-8">label</label>
                  <input id="text-input-example-8" class="text-input text-input-sm" placeholder="prefix icon" />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                </div>
                <div class="text-input-group">
                  <label class="hidden-text" for="text-input-example-9">label</label>
                  <input id="text-input-example-9" class="text-input text-input-sm" placeholder="button" />
                  <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="text-input-group ">
                  <label class="hidden-text" for="text-input-example-10">label</label>
                  <input id="text-input-example-10" class="text-input text-input-sm" value="prefix icon + button" />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                  <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
              </div>
            </div>
            <h4 class="group-title">search button</h4>
            <div class="flex gap-6 items-start">
              <div class="v-group gap-4">
                <div class="text-input-group">
                  <label class="hidden-text" for="text-input-example-11">label</label>
                  <input id="text-input-example-11" class="text-input text-input-lg" value="search button" />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                  <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                  <button class="text-input-search-button button button-neutral-ghost">
                    <span class="hidden-text">검색</span>
                    <svg-icon class="button-icon" name="search"></svg-icon>
                  </button>
                </div>
              </div>
              <div class="v-group gap-4">
                <div class="text-input-group">
                  <label class="hidden-text" for="text-input-example-11">label</label>
                  <input id="text-input-example-11" class="text-input" placeholder="search button" />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                  <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                  <button class="text-input-search-button button button-neutral-ghost">
                    <span class="hidden-text">검색</span>
                    <svg-icon class="button-icon" name="search"></svg-icon>
                  </button>
                </div>
              </div>
              <div class="v-group gap-4">
                <div class="text-input-group ">
                  <label class="hidden-text" for="text-input-example-11">label</label>
                  <input id="text-input-example-11" class="text-input text-input-sm" value="search button" />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                  <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                  <button class="text-input-search-button button button-neutral-ghost">
                    <span class="hidden-text">검색</span>
                    <svg-icon class="button-icon" name="search"></svg-icon>
                  </button>
                </div>
              </div>
            </div>
            <h4 class="group-title">password</h4>
            <div class="flex gap-6 items-start">
              <div class="v-group gap-4">
                <div class="text-input-group">
                  <label class="hidden-text" for="text-input-example-11">label</label>
                  <input
                    type="password"
                    id="text-input-example-11"
                    class="text-input text-input-lg"
                    placeholder="password"
                  />
                  <svg-icon class="text-input-icon" name="lock-locked"></svg-icon>
                  <button class="text-input-action-button button button-neutral-ghost button-md" type="button">
                    <span class="hidden-text">패스워드 보기</span>
                    <svg-icon class="button-icon" name="eye"></svg-icon>
                  </button>
                </div>
              </div>
              <div class="v-group gap-4">
                <div class="text-input-group">
                  <label class="hidden-text" for="text-input-example-11">label</label>
                  <input
                    type="password"
                    id="text-input-example-11"
                    class="text-input"
                    placeholder="password"
                    required
                  />
                  <svg-icon class="text-input-icon" name="lock-locked"></svg-icon>
                  <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
                    <span class="hidden-text">패스워드 보기</span>
                    <svg-icon class="button-icon" name="eye"></svg-icon>
                  </button>
                </div>
              </div>
              <div class="v-group gap-4">
                <div class="text-input-group">
                  <label class="hidden-text" for="text-input-example-11">label</label>
                  <input
                    type="password"
                    id="text-input-example-11"
                    class="text-input text-input-sm"
                    placeholder="password"
                  />
                  <svg-icon class="text-input-icon" name="lock-locked"></svg-icon>
                  <button class="text-input-action-button button button-neutral-ghost button-xs" type="button">
                    <span class="hidden-text">패스워드 보기</span>
                    <svg-icon class="button-icon" name="eye"></svg-icon>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Textarea</h2>
      <article class="mb-10">
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3 w-72">
            <h4 class="group-title">default</h4>
            <textarea class="textarea" cols="33"></textarea>
            <textarea class="textarea" placeholder="placeholder"></textarea>
            <textarea class="textarea" readonly>readonly style</textarea>
            <textarea class="textarea" disabled>disabled style</textarea>
            <textarea class="textarea is-textarea-fixed">resize none</textarea>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Select</h2>
      <article class="mb-10">
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3">
            <h4 class="group-title">default</h4>
            <div class="flex gap-6 items-start">
              <div class="v-group gap-4">
                <select-box class="select-lg w-64"></select-box>
                <select-box class="select-lg w-64" disabled></select-box>
              </div>
              <select-box class="is-select-open w-64"></select-box>
              <select-box class="select-sm w-64"></select-box>
            </div>
            <h4 class="group-title">clean</h4>
            <div class="flex gap-6 items-start">
              <div class="v-group gap-4">
                <select-box class="select-clean select-lg w-64"></select-box>
                <select-box class="select-lg w-64" disabled></select-box>
              </div>
              <select-box class="select-clean is-select-open w-64"></select-box>
              <select-box class="select-sm select-clean is-select-invalid w-64"></select-box>
            </div>
            <h4 class="group-title">Select-search</h4>
            <div class="flex gap-6 items-start">
              <div class="v-group gap-8">
                <combo-box class="select-lg w-64"></combo-box>
                <combo-box class="select-lg is-select-open w-64"></combo-box>
              </div>
              <div class="v-group gap-8">
                <combo-box class=" w-64"></combo-box>
                <combo-box class="is-select-open w-64"></combo-box>
              </div>
              <div class="v-group gap-8">
                <combo-box class="select-sm w-64"></combo-box>
                <combo-box class="select-sm is-select-open w-64"></combo-box>
              </div>
            </div>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Dropdown</h2>
      <article class="mb-10">
        <div class="flex flex-col align-center gap-5">
          <div class="v-group">
            <button class="button button-lg button-neutral-ghost">
              <svg-icon class="button-icon" name="kebab-menu"></svg-icon>
            </button>
            <div class="h-group gap-5">
              <div class="dropdown dropdown-lg" style="position: static">
                <ul class="dropdown-list" style="position: static">
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <span class="dropdown-text">dropdown item</span>
                    </button>
                  </li>
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <svg-icon class="svg-icon" name="user"></svg-icon>
                      <span class="dropdown-text">dropdown item</span>
                    </button>
                  </li>
                  <li class="dropdown-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-drop" class="checkbox-input" />
                      <label for="checkbox-drop" class="checkbox-label">
                        Checkbox
                        <span class="checkbox-subtext">(Optional)</span>
                      </label>
                    </div>
                    <button class="button button-neutral-ghost button-xs">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </li>
                  <li class="dropdown-item dropdown-item-negative">
                    <button class="dropdown-button">
                      <svg-icon class="svg-icon" name="trash"></svg-icon>
                      <span class="dropdown-text">삭제</span>
                    </button>
                  </li>
                </ul>
              </div>
              <div class="dropdown" style="position: static">
                <ul class="dropdown-list" style="position: static">
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <span class="dropdown-text">dropdown item</span>
                    </button>
                  </li>
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <svg-icon class="svg-icon" name="user"></svg-icon>
                      <span class="dropdown-text">dropdown item</span>
                    </button>
                  </li>
                  <li class="dropdown-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-drop-2" class="checkbox-input" />
                      <label for="checkbox-drop-2" class="checkbox-label">
                        Checkbox
                        <span class="checkbox-subtext">(Optional)</span>
                      </label>
                    </div>
                    <button class="button button-neutral-ghost button-xs">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </li>
                  <li class="dropdown-item dropdown-item-negative">
                    <button class="dropdown-button">
                      <svg-icon class="svg-icon" name="trash"></svg-icon>
                      <span class="dropdown-text">삭제</span>
                    </button>
                  </li>
                </ul>
              </div>
              <div class="dropdown dropdown-sm" style="position: static">
                <ul class="dropdown-list">
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <span class="dropdown-text">dropdown item</span>
                    </button>
                  </li>
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <svg-icon class="svg-icon" name="user"></svg-icon>
                      <span class="dropdown-text">dropdown item</span>
                    </button>
                  </li>
                  <li class="dropdown-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-drop-3" class="checkbox-input" />
                      <label for="checkbox-drop-3" class="checkbox-label">
                        Checkbox
                        <span class="checkbox-subtext">(Optional)</span>
                      </label>
                    </div>
                    <button class="button button-neutral-ghost button-xs">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </li>
                  <li class="dropdown-item dropdown-item-negative">
                    <button class="dropdown-button">
                      <svg-icon class="svg-icon" name="trash"></svg-icon>
                      <span class="dropdown-text">삭제</span>
                    </button>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Menu</h2>
      <article class="mb-10">
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-6">
            <div class="menu menu-lg">
              <div class="menu-search">
                <div class="text-input-group">
                  <label class="hidden-text" for="text-input-example-11">label</label>
                  <input id="text-input-example-11" class="text-input text-input-lg" value="search button" />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                  <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                  <button class="text-input-search-button button button-neutral-ghost">
                    <span class="hidden-text">검색</span>
                    <svg-icon class="button-icon" name="search"></svg-icon>
                  </button>
                </div>
              </div>
              <ul class="menu-list">
                <li class="menu-item">
                  <button class="menu-button">
                    <span class="menu-text">menu item</span>
                  </button>
                </li>
                <li class="menu-item">
                  <button class="menu-button">
                    <svg-icon class="svg-icon" name="home"></svg-icon>
                    <span class="menu-text">menu item</span>
                  </button>
                </li>
                <li class="menu-item">
                  <button class="menu-button">
                    <svg-icon class="svg-icon" name="home"></svg-icon>
                    <span class="menu-text">menu item</span>
                  </button>
                  <div class="menu-button-group">
                    <button class="button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">복사</span>
                      <svg-icon class="button-icon" name="copy"></svg-icon>
                    </button>
                    <button class="button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">수정</span>
                      <svg-icon class="button-icon" name="pen"></svg-icon>
                    </button>
                    <button class="button button-error-ghost button-sm" type="button">
                      <span class="hidden-text">삭제</span>
                      <svg-icon class="button-icon" name="trash"></svg-icon>
                    </button>
                  </div>
                </li>
                <li class="menu-item">
                  <div class="checkbox">
                    <input type="checkbox" id="checkbox-menu" class="checkbox-input" />
                    <label for="checkbox-menu" class="checkbox-label">
                      Checkbox
                      <span class="checkbox-subtext">(Optional)</span>
                    </label>
                  </div>
                </li>
                <li class="menu-item">
                  <label class="hidden-text" for="text-input-modify">label</label>
                  <input id="text-input-modify" class="text-input" placeholder="placeholder" />
                  <div class="menu-button-group">
                    <button class="button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">복사</span>
                      <svg-icon class="button-icon" name="copy"></svg-icon>
                    </button>
                    <button class="button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">수정</span>
                      <svg-icon class="button-icon" name="pen"></svg-icon>
                    </button>
                    <button class="button button-error-ghost button-sm" type="button">
                      <span class="hidden-text">삭제</span>
                      <svg-icon class="button-icon" name="trash"></svg-icon>
                    </button>
                  </div>
                </li>
              </ul>
            </div>
            <div class="menu">
              <div class="menu-search">
                <div class="text-input-group">
                  <label class="hidden-text" for="text-input-example-11">label</label>
                  <input id="text-input-example-11" class="text-input" value="search button" />
                  <svg-icon class="text-input-icon" name="user"></svg-icon>
                  <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                  <button class="text-input-search-button button button-neutral-ghost">
                    <span class="hidden-text">검색</span>
                    <svg-icon class="button-icon" name="search"></svg-icon>
                  </button>
                </div>
              </div>
              <ul class="menu-list">
                <li class="menu-item">
                  <button class="menu-button">
                    <span class="menu-text">menu item</span>
                  </button>
                </li>
                <li class="menu-item">
                  <button class="menu-button">
                    <svg-icon class="svg-icon" name="home"></svg-icon>
                    <span class="menu-text">menu item</span>
                  </button>
                </li>
                <li class="menu-item">
                  <button class="menu-button">
                    <svg-icon class="svg-icon" name="home"></svg-icon>
                    <span class="menu-text">menu item</span>
                  </button>
                  <div class="menu-button-group">
                    <button class="button button-neutral-ghost button-xs" type="button">
                      <span class="hidden-text">복사</span>
                      <svg-icon class="button-icon" name="copy"></svg-icon>
                    </button>
                    <button class="button button-neutral-ghost button-xs" type="button">
                      <span class="hidden-text">수정</span>
                      <svg-icon class="button-icon" name="pen"></svg-icon>
                    </button>
                    <button class="button button-error-ghost button-xs" type="button">
                      <span class="hidden-text">삭제</span>
                      <svg-icon class="button-icon" name="trash"></svg-icon>
                    </button>
                  </div>
                </li>
                <li class="menu-item">
                  <div class="checkbox checkbox-sm">
                    <input type="checkbox" id="checkbox-menu-2" class="checkbox-input" />
                    <label for="checkbox-menu-2" class="checkbox-label">
                      Checkbox
                      <span class="checkbox-subtext">(Optional)</span>
                    </label>
                  </div>
                </li>
                <li class="menu-item">
                  <label class="hidden-text" for="text-input-modify">label</label>
                  <input id="text-input-modify" class="text-input text-input-sm" placeholder="placeholder" />
                  <div class="menu-button-group">
                    <button class="button button-neutral-ghost button-xs" type="button">
                      <span class="hidden-text">복사</span>
                      <svg-icon class="button-icon" name="copy"></svg-icon>
                    </button>
                    <button class="button button-neutral-ghost button-xs" type="button">
                      <span class="hidden-text">수정</span>
                      <svg-icon class="button-icon" name="pen"></svg-icon>
                    </button>
                    <button class="button button-error-ghost button-xs" type="button">
                      <span class="hidden-text">삭제</span>
                      <svg-icon class="button-icon" name="trash"></svg-icon>
                    </button>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Modal</h2>
      <article class="mb-10">
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-6">
            <h4 class="group-title">default</h4>
            <div class="modal modal-padding-16" style="width:350px">
              <div class="modal-head">
                <div class="modal-head-text">
                  <span class="modal-head-title">insert modal title here</span>
                  <span class="modal-head-subtitle">Sub title</span>
                </div>
                <button class="button link-button button-sm" type="button">
                  <span class="hidden-text">닫기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="modal-body">
                <input class="text-input text-input-lg w-full" />
              </div>
              <div class="modal-foot">
                <div class="modal-foot-group">
                  <button class="button button-ghost button-lg">취소</button>
                  <button class="button button-primary button-lg">확인</button>
                </div>
              </div>
            </div>
            <div class="modal modal-padding-48" style="width:480px">
              <div class="modal-head">
                <div class="modal-head-text">
                  <span class="modal-head-title">insert modal title heredd</span>
                  <span class="modal-head-subtitle">Sub title</span>
                </div>
                <button class="button link-button button-sm" type="button">
                  <span class="hidden-text">닫기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="modal-body">
                <input class="text-input text-input-lg w-full" />
              </div>
              <div class="modal-foot">
                <button class="button button-ghost button-lg">취소</button>
                <button class="button button-primary button-lg">확인</button>
              </div>
            </div>
            <div class="modal modal-padding-32" style="width:620px">
              <div class="modal-head">
                <div class="modal-head-text">
                  <span class="modal-head-title">insert modal title here</span>
                  <span class="modal-head-subtitle">Sub title</span>
                </div>
                <button class="button link-button button-sm" type="button">
                  <span class="hidden-text">닫기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="modal-body">
                <input class="text-input text-input-lg w-full" />
              </div>
              <div class="modal-foot">
                <div class="checkbox">
                  <input type="checkbox" id="today" class="checkbox-input" />
                  <label for="today" class="checkbox-label">
                    오늘 하루 그만보기 <span class="checkbox-subtext">(Optional)</span>
                  </label>
                </div>
                <div class="modal-foot-group">
                  <button class="button button-ghost button-lg">취소</button>
                  <button class="button button-primary button-lg">확인</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Table</h2>
      <article class="mb-10">
        <div class="v-group gap-3">
          <div class="v-group gap-3">
            <h4 class="group-title">Horizontal</h4>
            <table>
              <colgroup>
                <col style="width: 15%" />
                <col style="width: 35%" />
                <col style="width: 15%" />
                <col style="width: 35%" />
              </colgroup>
              <tr>
                <th>Header Cell</th>
                <td colspan="3">Row Cell</td>
              </tr>
              <tr>
                <th>Header Cell</th>
                <td>
                  There are many variations of passages of Lorem Ipsum available, but the majority have suffered
                  alteration in some form, by injected humour
                </td>
                <th>Header Cell</th>
                <td>
                  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's
                  standard dummy text ever since the 1500s is simply dummy text of the printing and typesetting
                  industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s is simply dummy
                  text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text
                  ever since thstandard dummy text ever since the 1500s
                </td>
              </tr>
              <tr>
                <th>Header Cell</th>
                <td>Row Cell</td>
                <th>Header Cell</th>
                <td>Row Cell</td>
              </tr>
              <tr>
                <th>Header Cell</th>
                <td>Row Cell</td>
                <th>Header Cell</th>
                <td>Row Cell</td>
              </tr>
            </table>
            <h4 class="group-title">Vertical</h4>
            <table>
              <colgroup>
                <col style="width: 25%" />
                <col style="width: 25%" />
                <col style="width: 25%" />
                <col style="width: 25%" />
              </colgroup>
              <tr>
                <th>Header Cell</th>
                <th>Header Cell</th>
                <th>Header Cell</th>
                <th>Header Cell</th>
              </tr>
              <tr>
                <td>
                  There are many variations of passages of Lorem Ipsum available, but the majority have suffered
                  alteration in some form, by injected humour
                </td>
                <td>
                  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's
                  standard dummy text ever since the 1500s is simply dummy text of the printing and typesetting
                  industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s is simply dummy
                  text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text
                  ever since thstandard dummy text ever since the 1500s
                </td>
                <td>Row Cell</td>
                <td>Row Cell</td>
              </tr>
            </table>
          </div>
        </div>
      </article>
    </section>
    <section class="guide-section">
      <h2 class="section-title">Form</h2>
      <article class="mb-10">
        <h3 class="section-subtitle">Vertical</h3>
        <div class="flex flex-col gap-5">
          <div class="form form-lg w-[400px]">
            <div class="form-head">
              <p3 class="form-title">Form title</p3>
            </div>
            <div class="form-body">
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail flex flex-col">
                  <div class="text-input-group">
                    <label class="hidden-text" for="text-input-example-4">label</label>
                    <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                    <svg-icon class="text-input-icon" name="user"></svg-icon>
                    <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail">
                  <div class="element-group">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox" class="checkbox-input" />
                      <label for="checkbox" class="checkbox-label"> label</label>
                    </div>
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox2" class="checkbox-input" disabled />
                      <label for="checkbox2" class="checkbox-label"> label</label>
                    </div>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail">
                  <textarea class="textarea" rows="5"></textarea>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="form form-vertical w-[400px]">
            <div class="form-head">
              <p3 class="form-title">Form title</p3>
            </div>
            <div class="form-body">
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail flex flex-col">
                  <div class="text-input-group">
                    <label class="hidden-text" for="text-input-example-4">label</label>
                    <input id="text-input-example-4" class="text-input" value="prefix icon + button" />
                    <svg-icon class="text-input-icon" name="user"></svg-icon>
                    <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail">
                  <div class="element-group">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox" class="checkbox-input" />
                      <label for="checkbox" class="checkbox-label"> label</label>
                    </div>
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox2" class="checkbox-input" disabled />
                      <label for="checkbox2" class="checkbox-label"> label</label>
                    </div>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail">
                  <textarea class="textarea" rows="5"></textarea>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="form form-vertical form-sm w-[400px]">
            <div class="form-head">
              <p3 class="form-title">Form title</p3>
            </div>
            <div class="form-body">
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail flex flex-col">
                  <div class="text-input-group">
                    <label class="hidden-text" for="text-input-example-4">label</label>
                    <input id="text-input-example-4" class="text-input text-input-sm" value="prefix icon + button" />
                    <svg-icon class="text-input-icon" name="user"></svg-icon>
                    <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail">
                  <div class="element-group">
                    <div class="checkbox checkbox-sm">
                      <input type="checkbox" id="checkbox" class="checkbox-input" />
                      <label for="checkbox" class="checkbox-label"> label</label>
                    </div>
                    <div class="checkbox checkbox-sm">
                      <input type="checkbox" id="checkbox2" class="checkbox-input" disabled />
                      <label for="checkbox2" class="checkbox-label"> label</label>
                    </div>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail">
                  <textarea class="textarea" rows="5"></textarea>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </article>
      <article class="mb-10">
        <h3 class="section-subtitle">Horizontal</h3>
        <div class="flex flex-col gap-5">
          <div class="form form-horizontal form-lg w-[538px]">
            <div class="form-head">
              <p3 class="form-title">Form title</p3>
            </div>
            <div class="form-body">
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail flex flex-col">
                  <div class="text-input-group">
                    <label class="hidden-text" for="text-input-example-4">label</label>
                    <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                    <svg-icon class="text-input-icon" name="user"></svg-icon>
                    <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail">
                  <div class="element-group">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox" class="checkbox-input" />
                      <label for="checkbox" class="checkbox-label"> label</label>
                    </div>
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox2" class="checkbox-input" disabled />
                      <label for="checkbox2" class="checkbox-label"> label</label>
                    </div>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail">
                  <textarea class="textarea" rows="5"></textarea>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="form form-horizontal w-[538px]">
            <div class="form-head">
              <p3 class="form-title">Form title</p3>
            </div>
            <div class="form-body">
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail flex flex-col">
                  <div class="text-input-group">
                    <label class="hidden-text" for="text-input-example-4">label</label>
                    <input id="text-input-example-4" class="text-input" value="prefix icon + button" />
                    <svg-icon class="text-input-icon" name="user"></svg-icon>
                    <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail">
                  <div class="element-group">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox" class="checkbox-input" />
                      <label for="checkbox" class="checkbox-label"> label</label>
                    </div>
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox2" class="checkbox-input" disabled />
                      <label for="checkbox2" class="checkbox-label"> label</label>
                    </div>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail">
                  <textarea class="textarea" rows="5"></textarea>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="form form-horizontal form-sm w-[538px]">
            <div class="form-head">
              <p3 class="form-title">Form title</p3>
            </div>
            <div class="form-body">
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail flex flex-col">
                  <div class="text-input-group">
                    <label class="hidden-text" for="text-input-example-4">label</label>
                    <input id="text-input-example-4" class="text-input text-input-sm" value="prefix icon + button" />
                    <svg-icon class="text-input-icon" name="user"></svg-icon>
                    <button class="text-input-action-button button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail">
                  <div class="element-group">
                    <div class="checkbox checkbox-sm">
                      <input type="checkbox" id="checkbox" class="checkbox-input" />
                      <label for="checkbox" class="checkbox-label"> label</label>
                    </div>
                    <div class="checkbox checkbox-sm">
                      <input type="checkbox" id="checkbox2" class="checkbox-input" disabled />
                      <label for="checkbox2" class="checkbox-label"> label</label>
                    </div>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label">
                  label
                  <span class="required">*</span>
                  <span class="sub-label">(sublabel)</span>
                </label>
                <div class="form-detail">
                  <textarea class="textarea" rows="5"></textarea>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </article>
    </section>
    <!-- 개발 완료 목록 -->
    <section class="guide-section">
      <h2 class="section-title">개발 UI</h2>
      <article class="mb-10">
        <div class="flex flex-col align-center gap-5">
          <div class="v-group gap-3 w-72">
            <h4 class="group-title">Select</h4>
            <nuxt-link :to="'/selectbox'" class="button button-primary button-lg">
              <span class="button-text">Select 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Checkbox List</h4>
            <nuxt-link :to="'/checkboxList'" class="button button-primary button-lg">
              <span class="button-text">Checkbox List 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Combobox</h4>
            <nuxt-link :to="'/combobox'" class="button button-primary button-lg">
              <span class="button-text">Combobox 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Step & Tab</h4>
            <nuxt-link :to="'/components/navigation'" class="button button-primary button-lg">
              <span class="button-text">Step / Tab 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">ag-grid</h4>
            <nuxt-link :to="'/ag-grid'" class="button button-primary button-lg">
              <span class="button-text">ag-grid 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Today</h4>
            <nuxt-link :to="'/dayjs'" class="button button-primary button-lg">
              <span class="button-text">Today 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">DatePicker</h4>
            <nuxt-link :to="'/vue-datepicker-next'" class="button button-primary button-lg">
              <span class="button-text">datepicker 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Highcharts</h4>
            <nuxt-link :to="'/highcharts'" class="button button-primary button-lg">
              <span class="button-text">highcharts 예시 이동</span>
            </nuxt-link>
            <h4 class="group-title">Modal</h4>
            <nuxt-link :to="'/vue-final-modal'" class="button button-primary button-lg">
              <span class="button-text">modal 예시 이동</span>
            </nuxt-link>
          </div>
        </div>
      </article>
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
import Breadcrumb from "../components/base/breadcrumb/Breadcrumb.vue";
import SelectBox from "../components/extends/select-box/SelectBox.vue";


const currentComponent = shallowRef(Dayjs);

// 2. 컴포넌트를 객체에 저장
const components: any = {
  AgGrid,
  Dayjs,
  Highcharts,
  Locale,
  SelectBox,
  VueDatepickerNext,
  VueFinalModal,
  Notification,
  Breadcrumb
};

function clickMenu(menu: string) {
  currentComponent.value = components[menu];
}

function checkCurrentPage(item: number): void {
  console.log("Emit currentPageNumber: ", item);
}

const isFirstCheckedEvent: boolean = true;

const options: { [key: string]: string | number }[] = [
  {
    label: "option1",
    value: "option11"
  },
  {
    label: "option2",
    value: "option22"
  },
  {
    label: "option3",
    value: "option33"
  }
];

const selectedItem = "";

function selectItem(item: string | number) {
  console.log(item);
}
</script>

<style lang="css" scoped></style>
