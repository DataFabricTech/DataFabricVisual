export interface DataModel {
  id: string | number;
  serviceIcon: string;
  depth: string[];
  firModelNm: string;
  modelNm: string;
  modelDesc: string;
  owner: string;
  category: string;
}

export interface ResourceBoxCommonProps {
  usePrvBtn?: boolean;
  showOwner?: boolean;
  showCategory?: boolean;
  useFirModelNm?: boolean;
  useDataNmLink?: boolean;

  class?: any;
}
