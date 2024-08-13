import { useLoading } from "@/composables/loadingHelper";

export default defineNuxtPlugin((nuxtApp) => {
  const loading = useLoading() as any;

  nuxtApp.vueApp.use(useLoading);

  return {
    provide: {
      loading,
    },
  };
});
