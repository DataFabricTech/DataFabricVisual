<template>
  <MonacoEditor v-model="computedEditorValue" :lang="lang" :options="options" @load="onEditorLoad" />
</template>

<script lang="ts" setup>
import { ref, defineProps, defineEmits, computed } from "vue";
import antlr4 from "antlr4";
import CalculatorLexer from "@/antlr4/generated/calculatorLexer";
import CalculatorParser from "@/antlr4/generated/calculatorParser";
import { editor } from "monaco-editor";

const props = defineProps({
  modelValue: {
    type: String,
    required: true
  },
  lang: {
    type: String,
    default: "sql"
  },
  options: {
    type: Object,
    default: () => ({
      value: "",
      automaticLayout: true,
      calculatorG4: true
    })
  }
});

const changedValue = ref(""); // 에디터의 변경된 값을 저장할 변수

const emit = defineEmits<{
  (event: "editorReload", value: string): void;
}>();

const computedEditorValue = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    changedValue.value = value;
  }
});

const validateQuery = (query: string) => {
  try {
    const chars = new antlr4.InputStream(query);
    const lexer = new CalculatorLexer(chars);
    const tokens = new antlr4.CommonTokenStream(lexer);
    const parser = new CalculatorParser(tokens);

    parser.buildParseTrees = true;
    const tree = parser.query();
    // 구문 분석 중 오류가 발생했는지 확인
    if (parser._syntaxErrors > 0) {
      return false;
    }
    return true;
  } catch (error) {
    return false;
  }
};

const validateSelectedText = (text: string) => {
  // 쿼리 구분 기준 (예를 들어 세미콜론)으로 나누기
  const queries = text
    .split(";")
    .map((query) => query.trim())
    .filter((query) => query.length > 0);

  for (const query of queries) {
    if (!validateQuery(query)) {
      return false;
    }
  }
  return true;
};

const onEditorLoad = (editorInstance: editor.IStandaloneCodeEditor) => {
  editorInstance.onDidBlurEditorWidget(() => {
    const selection = editorInstance.getSelection();
    let currentValue = editorInstance.getValue();

    if (selection && !selection.isEmpty()) {
      currentValue = editorInstance.getModel().getValueInRange(selection);
    }

    const isValid = validateSelectedText(currentValue);
    if (isValid) {
      emit("editorReload", currentValue);
    } else {
      emit("editorReload", "");
    }
  });
};
</script>

<style scoped>
div {
  height: 400px;
  width: 100%;
}

.vs-dark {
  background-color: #1e1e1e;
  color: #d4d4d4;
}
</style>
