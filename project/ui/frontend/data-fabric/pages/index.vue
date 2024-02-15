<template>
  <section>
    <h2>DATA Fabric</h2>
    <base-button>{{ t("test") }}</base-button>
    {{ gt("common") }}
  </section>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import messages from "./index.json";

import BaseButton from "@base/button/button.vue";

const { t } = useI18n({
  messages
});

</script>

<style lang="scss" scoped>

</style>
