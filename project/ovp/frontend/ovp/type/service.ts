export interface Service {
  fullyQualifiedName: string;
  id: string;
  name: string;
  owner: Owner;
  tags: object[];
  terms: object[];
  serviceType: string;
  description: string;
  type: "database" | "storage";
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
  pipelineState: string;
  owner: Owner;
  startDate: number;
  endDate: number;
  timestamp: number;
}

export interface ServiceData {
  description: string;
}

export interface DBServiceListData {
  owner: string | undefined;
  fqn: string;
  name: string;
  id: string;
  type: string;
  desc: string | undefined;
}
