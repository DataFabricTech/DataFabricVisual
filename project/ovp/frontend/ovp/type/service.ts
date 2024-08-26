export interface Service {
  fullyQualifiedName: string;
  id: string;
  name: string;
  owner: Owner;
  tags: object[];
  relatedTerms: object[];
  serviceType: string;
  description: string;
  // database | storage
  type: string;
}

export interface Owner {
  id: string;
  type: string;
  name: string;
  fullyQualifiedName: string;
  displayName: string;

  isAdmin?: boolean;
}

export interface Ingestion {
  name: string;
  displayName: string;
  fullyQualifiedName: string;
  id: string;
  pipelineType: string;
  scheduleInterval: string;
}
