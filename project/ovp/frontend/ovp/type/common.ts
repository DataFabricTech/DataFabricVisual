export interface JsonPatchOperation {
  op: string;
  path: string;
  value?: string | object;
}
export interface Tag {
  [key: string]: string;
}
