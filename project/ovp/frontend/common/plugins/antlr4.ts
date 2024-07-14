import { defineNuxtPlugin } from "#app";
import antlr4 from "antlr4";
import CalculatorLexer from "@/antlr4/generated/calculatorLexer";
import CalculatorParser from "@/antlr4/generated/calculatorParser";

export default defineNuxtPlugin((nuxtApp) => {
  console.log("ANTLR4 Plugin Loaded"); // 플러그인이 로드될 때 로그 출력
  nuxtApp.provide("antlr4", antlr4);
  nuxtApp.provide("CalculatorLexer", CalculatorLexer);
  nuxtApp.provide("CalculatorParser", CalculatorParser);
});
