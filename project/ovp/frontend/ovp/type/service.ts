export interface Service {
  fullyQualifiedName: string;
  id: string;
  name: string;
  owner: Owner;
  tags: object[];
  relatedTerms: object[];
  serviceType: string;
}

export interface Owner {
  id: string;
  type: string;
  name: string;
  fullyQualifiedName: string;
  displayName: string;
}
