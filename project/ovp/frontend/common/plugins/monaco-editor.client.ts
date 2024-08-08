import * as monaco from "monaco-editor";
import { defineNuxtPlugin } from "#app";

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.provide("monaco", monaco);
});
