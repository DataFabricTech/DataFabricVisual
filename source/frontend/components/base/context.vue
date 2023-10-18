<template>
  <div class="context">
    <ul class="context-list">
      <li class="context-item">
        <button class="context-button">
          <slot name="menu-01"></slot>
        </button>
      </li>
      <li class="context-item context-item-selected">
        <button class="context-button">
          <slot name="menu-02"></slot>
        </button>
      </li>
      <li class="context-item context-item-disabled">
        <button class="context-button">
          <slot name="menu-03"></slot>
        </button>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">

</script>
