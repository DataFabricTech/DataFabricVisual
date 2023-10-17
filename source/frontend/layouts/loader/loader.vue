<template>
  <div
    v-if="show"
    style="z-index: 999; position: absolute; width: 100%; height: 100%"
  >
    <svg class="loader__icon" height="56" viewBox="0 0 56 56" width="56" xmlns="http://www.w3.org/2000/svg">
      <path
        clip-rule="evenodd"
        d="M25.5 3C25.5 1.61929 26.6193 0.5 28 0.5C43.1878 0.5 55.5 12.8122 55.5 28C55.5 43.1878 43.1878 55.5 28 55.5C12.8122 55.5 0.5 43.1878 0.5 28C0.5 26.6193 1.61929 25.5 3 25.5C4.38071 25.5 5.5 26.6193 5.5 28C5.5 40.4264 15.5736 50.5 28 50.5C40.4264 50.5 50.5 40.4264 50.5 28C50.5 15.5736 40.4264 5.5 28 5.5C26.6193 5.5 25.5 4.38071 25.5 3Z"
        fill-rule="evenodd"
      />
    </svg>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import { useVfm } from "vue-final-modal";

const vfm = useVfm();

const modalId = "loader-modal";
const count = ref(0);
const show = ref(false);

onMounted(() => {
  console.log("onMounted load layout");
  window.startLoader = start;
  window.finishLoader = finish;
  console.log(window);
});

function start() {
  console.log("LOADER START - " + count.value);
  if (count.value === 0) {
    show.value = true;
  }
  count.value++;
}

function finish() {
  count.value--;
  if (count.value > 0) {
    return;
  }
  setTimeout(() => {
    show.value = false;
    count.value = 0;
    console.log("LOADER HIDE - " + count.value);
  }, 1000);
}
</script>
