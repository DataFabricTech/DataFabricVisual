// composables/dropDownHelper.ts

export const useDropdownHelper = () => {
  const isDropdownOpen = ref(false);
  const conditions = ref<any[]>([]);

  const setHandler = (...newConditions: any[]) => {
    conditions.value = newConditions;
  };

  const handleClickOutside = (event: any) => {
    if (!isDropdownOpen.value) {
      return;
    }

    const isClickInside = conditions.value.some((condition) => {
      return condition?.contains(event.target);
    });

    isDropdownOpen.value = isClickInside ? isDropdownOpen.value : false;
  };

  onMounted(() => {
    document.addEventListener("click", handleClickOutside);
  });

  onBeforeUnmount(() => {
    document.removeEventListener("click", handleClickOutside);
  });

  return { isDropdownOpen, setHandler };
};
