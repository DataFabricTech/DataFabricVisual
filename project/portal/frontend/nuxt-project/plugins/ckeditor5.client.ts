import CKEditor from "@ckeditor/ckeditor5-vue";
// import ClassicEditor from "@ckeditor/ckeditor5-build-classic";
import Editor from "@/plugins/lib/CustomEditor5";
import { CkeditorTranslation_ko } from "@/plugins/lib/CKEditorTranslator"

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.use(CKEditor);

  // 한글 번역
  CkeditorTranslation_ko()

  return {
    provide: {
      ckeditor5: {
        customEditor5: Editor
      },
    },
  };
});
