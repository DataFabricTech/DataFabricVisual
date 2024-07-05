import type {
  DataModel,
  ResourceBoxCommonProps,
} from "./resource-box-common-props";

export interface ResourceBoxListProps extends ResourceBoxCommonProps {
  dataList: DataModel[];

  useListCheckbox?: boolean;
  isBoxSelectedStyle: boolean;
}
