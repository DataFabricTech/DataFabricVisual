export interface JsonPatchOperation {
  op: string;
  path: string;
  value?: string | object;
}
export interface Tag {
  // description: string;
  // displayName: string;
  // labelType: string;
  // name: string;
  // source: string;
  // state: string;
  // style: object;
  // tagFQN: string;
  [key: string]: string | object;
}
