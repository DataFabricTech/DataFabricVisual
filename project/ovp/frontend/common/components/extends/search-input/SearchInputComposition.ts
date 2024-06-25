import type { SearchInputProps } from "./SearchInputProps";
import { ref } from "vue";

interface SearchInputComposition extends SearchInputProps {
  inputValue: Ref<string>;

  clearSearchValue(): void;
}

export function SearchInputComposition(props: SearchInputProps): SearchInputComposition {
  // DATA
  const inputValue: Ref<string> = ref<string>("");

  // METHODS
  const clearSearchValue = () => {
    inputValue.value = "";
  };

  return { ...props, inputValue, clearSearchValue };
}
