<template>
  <div class="list">
    <div class="list-head">
      <slot name="title"></slot>
    </div>
    <div class="list-body">
      <slot name="options">
        <div class="list-options">
          <baseCheckbox id="list-check-04">전체</baseCheckbox>
          <div class="list-options-count">
            <strong class="list-options-count-selected">0</strong>
            <span>/</span>
            <span class="list-options-count-all">
              38
            </span>
          </div>
        </div>
      </slot>
      <ul class="list-list">
        <li class="list-item">
          <baseCheckbox id="list-check-01">default</baseCheckbox>
        </li>
        <li class="list-item is-disabled">
          <baseCheckbox id="list-check-02" disabled>default</baseCheckbox>
        </li>
        <li class="list-item">
          <baseCheckbox id="list-check-03">default</baseCheckbox>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">

</script>
