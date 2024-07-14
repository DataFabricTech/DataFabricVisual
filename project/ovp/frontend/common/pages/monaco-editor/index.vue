<template>
  <div style="height: 400px; width: 100%">
    <CodeBox v-model="code" lang="sql" :options="editorOptions" @editorReload="editorReload" />
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import CodeBox from "@/components/extends/code-box/CodeBox.vue";

const code = ref("SELECT 123column FROM test;\n");
const editorOptions = {
  theme: "vs-dark", // 사용하려는 테마 설정
  automaticLayout: true, // 에디터가 부모 컨테이너의 크기 변화에 따라 자동으로 크기를 조정
  calculatorG4: true // calculator.g4파일을 사용여부
};

function editorReload(value) {
  console.log("결과 : ", value);
}
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
