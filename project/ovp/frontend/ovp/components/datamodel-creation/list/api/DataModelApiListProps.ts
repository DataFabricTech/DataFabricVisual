import { DataModelListProps } from "~/components/datamodel-creation/list/base/DataModelListProps";

export interface DataModelApiListProps extends DataModelListProps {
  selectedItems: any[];
  useSort?: boolean;
  useInfinite?: boolean;
  useLiveSearch?: boolean;
}
