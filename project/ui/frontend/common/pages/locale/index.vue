<template>
  <div>
    <h1>Locale</h1>
    <div>{{ t("showDate") }}</div>
    <div>{{ $t("common") }}</div>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import messages from "./index.json";

const { t } = useI18n({
  messages
});
</script>

<style lang="scss" scoped></style>
