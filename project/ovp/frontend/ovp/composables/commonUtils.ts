// composables/commonUtils.ts

export const useCommonUtils = () => {
  const getPwdIconName = ({
    inputType,
    isVisible,
  }: {
    inputType?: string;
    isVisible?: boolean;
  }) => {
    return inputType === "text" || isVisible ? "eye" : "eye-hide";
  };

  return { getPwdIconName };
};
