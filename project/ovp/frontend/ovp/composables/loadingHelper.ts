// composables/loadingHelper.ts

export const useLoading = () => {
  const isLoading = useState<boolean>("isLoading", () => false);
  const count = useState<number>("count", () => 0);

  const start = () => {
    isLoading.value = true;
    count.value++;
  };

  const stop = () => {
    count.value--;

    setTimeout(() => {
      if (count.value < 1) {
        isLoading.value = false;
      }
    }, 10);
  };

  return { isLoading, start, stop };
};