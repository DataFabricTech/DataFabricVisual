<template>
  <div class="loader-overlay">
    <div class="loader" role="alert">
      <svg-icon class="loader-icon animate-spin" name="loader"/>
    </div>
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
