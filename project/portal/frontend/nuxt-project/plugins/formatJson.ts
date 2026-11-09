export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.provide('jsonPretty', (value) => {
    if (typeof value === 'object' && value !== null) {
      // 들여쓰기 2칸으로 pretty print
      return JSON.stringify(value);
    }
    return String(value);
  });
});
