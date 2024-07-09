export interface DataModel {
  id: string | number;
  serviceIcon: string;
  depth: string[];
  firModelNm: string;
  modelNm: string;
  modelDesc: string;
  owner: { [key: string]: string };
  category: { [key: string]: string };
}

export interface ResourceBoxCommonProps {
  usePrvBtn?: boolean;
  showOwner?: boolean;
  showCategory?: boolean;
  useFirModelNm?: boolean;
  useDataNmLink?: boolean;

  class?: any;
}
