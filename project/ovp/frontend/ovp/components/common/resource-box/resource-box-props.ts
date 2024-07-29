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

  filters?: { [key: string]: { text: string; data: any[] } };

  ownerKey?: string;
  categoryKey?: string;
}
