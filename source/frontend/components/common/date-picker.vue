<template>
  <div>
    <Picker v-model:value="picked" />
  </div>
</template>

<script lang="ts" setup>
import Picker from 'vue-datepicker-next';
import 'vue-datepicker-next/index.css';
import { ref } from "vue";
import dayjs from "dayjs";

const picked = ref(dayjs('2023-08-08'));
</script>
