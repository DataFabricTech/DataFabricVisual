export interface Glossary {
  id: string;
  name: string;
  displayName: string;
  description: string;
  fullyQualifiedName: string;
  owner: Owner;
  //TODO response type 정의
  tags: object;
}
export interface Term {
  id: string;
  displayName: string;
  description: string;
  fullyQualifiedName: string;
  name: string;
}
export interface Activity {
  createdBy: string;
  headerMessage: string;
  id: string;
  message: string;
  updatedAt: string;
  updatedBy: string;
}
export interface Owner {
  id: string;
  type: string;
  name: string;
  fullyQualifiedName: string;
  displayName: string;
}
export interface JsonPatchOperation {
  op: string;
  path: string;
  value?: string | object;
}
