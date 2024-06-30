<template>
  <section>
    <h2>DATA Fabric</h2>
    {{ gt("common") }}

    <button
      class="button button-secondary"
      type="button"
      id="button1"
      @click="onClickSampleAPI()"
    >
      API 연동
    </button>
    {{result1}}

    <button
      class="button button-secondary"
      type="button"
      id=""button1
      @click="onClickErrorAPI()"
    >
      API 연동 - error 처리
    </button>
    {{result2}}
  </section>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import messages from "./index.json";

const { t } = useI18n({
  messages
});
const result1 = ref("");
const result2 = ref("");

const { $api } = useNuxtApp();
const onClickSampleAPI = async () => {
  result1.value = await $api(`/api/sample`);
};

const onClickErrorAPI = async () => {
  result2.value = await $api(`/api/sample/errorMessage/anno`);
};
</script>

<style lang="scss" scoped>

</style>
