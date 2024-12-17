// composables/queryHelpers.ts

import { ref } from "vue";
import _ from "lodash";

export interface QueryFilter {
  query: {
    bool: {
      must: any[];
    };
  };
}

interface BoolObj {
  bool: {
    should: any[];
  };
}

interface SingleKeyObj {
  [key: string]: Ref<string>;
}

interface KeyObj {
  term: SingleKeyObj;
}

export const useQueryHelpers = () => {
  const setQueryFilterByDepth = (key: string, value: any) => {
    const setTermObj: Ref<any[]> = ref<any[]>([]);
    const setBoolObj: Ref<BoolObj> = ref<BoolObj>({
      bool: { should: [] },
    });

    for (const item of value) {
      const setKeyObj: Ref<KeyObj> = ref<KeyObj>({
        term: { [`${key}`]: ref(item) },
      });

      setTermObj.value.push(setKeyObj.value);
    }
    setBoolObj.value.bool.should = setTermObj.value;
    return setBoolObj.value;
  };

  return { setQueryFilterByDepth };
};
