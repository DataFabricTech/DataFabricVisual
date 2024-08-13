export interface JsonPatchOperation {
  op: string;
  path: string;
  value?: string | object;
}
export interface Tag {
  description: string;
  displayName: string;
  labelType: string;
  name: string;
  source: string;
  state: string;
  style: object;
  tagFQN: string;
}

interface Column {
  name: string;
  dataType: string;
  desc: string;
  constraint:
    | "PRIMARY_KEY"
    | "FOREIGN_KEY"
    | "UNIQUE"
    | "SORT_KEY"
    | "DIST_KEY"
    | "NULL"
    | "NOT_NULL";
}

export interface PreviewData {
  modelType: "structured" | "unstructured";
  modelInfo: {
    model: {
      name: string;
      desc: string;
      tableType?: string;
      cnt: number;
      ext?: string;
    };
    columns?: Column[];
    details?: string;
    url?: string;
  };
  tags: object[];
  glossaries: object[];
}
