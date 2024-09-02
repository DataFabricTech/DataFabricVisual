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

  const getTrinoQuery = (queryFilter: QueryFilter) => {
    // query 구현을 backend 에서 하려니까 코드가 너무 복잡해져서 front 에 해서 넘겨서 처리.
    const trinoFilter: QueryFilter = {
      query: {
        bool: {
          must: [{ bool: { should: [{ term: { serviceType: "trino" } }] } }],
        },
      },
    };
    const trinoMustArray = trinoFilter.query.bool.must;
    queryFilter.query.bool.must = _.concat(
      queryFilter.query.bool.must,
      trinoMustArray,
    );
    return queryFilter;
  };

  return { setQueryFilterByDepth, getTrinoQuery };
};
