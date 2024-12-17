export interface DataModel {
  type: string;
  id: string | number;
  serviceIcon: string;
  depth: string[];
  firModelNm: string;
  modelNm: string;
  modelDesc: string;
  owner: string;
  category: string;

  [key: string]: string | number | string[];
}

export interface ResourceBoxCommonProps {
  usePrvBtn?: boolean;
  showOwner?: boolean;
  showCategory?: boolean;
  useFirModelNm?: boolean;
  useDataNmLink?: boolean;
  useListCheckbox?: boolean;
  useContextBox?: boolean;

  class?: any;
}
