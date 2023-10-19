<template>
  <tab-bar></tab-bar>
  <alert></alert>
  <p>composables test : {{ sampleData }}</p>
</template>

<script lang="ts" setup>
import { sample } from "../../composables/sample";
import TabBar from "../../components/project/tab-bar.vue";
import Alert from "../../components/common/alert.vue";

const sampleData = sample();
</script>
