import type {
  DataModel,
  ResourceBoxCommonProps,
} from "./resource-box-common-props";

export interface ResourceBoxProps extends ResourceBoxCommonProps {
  dataObj: DataModel;

  partKey?: string;
  useEditButtons?: boolean;
  isEditMode?: boolean;
  editable?: boolean;
  isChecked?: boolean;
  selectedModelList?: any[];

  filters?: { [key: string]: { text: string; data: any[] } };
  userList?: any[];
  categoryList?: any[];

  ownerKey?: string;
  categoryKey?: string;
}
