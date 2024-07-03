<template>
  <div>
    <CodeBox v-model="code" lang="sql" :options="editorOptions" />
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import CodeBox from "@/components/extends/code-box/CodeBox.vue";

const code = ref("SELECT * FROM test;");
const editorOptions = {
  theme: "vs-dark", // 사용하려는 테마 설정
  automaticLayout: true
};
</script>
<style scoped>
/* Monaco Editor 컨테이너의 높이와 너비를 설정 */
div {
  height: 400px;
  width: 100%;
}

/* Monaco Editor 자체의 스타일을 정의 */
.vs-dark {
  background-color: #1e1e1e;
  color: #d4d4d4;
}
</style>
