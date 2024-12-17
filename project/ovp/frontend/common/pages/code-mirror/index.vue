<template>
  <client-only>
    <code-mirror ref="cm" class="codemirror-container" v-model="value" :lang="lang" basic gutter wrap @change="changeCode"></code-mirror>
  </client-only>
</template>

<script lang="ts" setup>
import { ref, UnwrapRef } from 'vue';
import CodeMirror from 'vue-codemirror6';
import { LanguageSupport } from '@codemirror/language';
import {sql} from "@codemirror/lang-sql";

const cm: Ref<InstanceType<typeof CodeMirror> | undefined> = ref();
const lang: LanguageSupport = sql();
const value:Ref<UnwrapRef<string>> = ref('Cozy lummox gives smart squid who asks for job pen.');

function changeCode(event) {
  console.log(event);
}

</script>

<style>
.codemirror-container {
  height: 400px; /* 에디터의 높이 설정 */
}
</style>
