interface AirflowConfig {
    scheduleInterval: string;
    startDate: string;
}

export interface Owner {
    id: string;
    type: string;
}

export interface Service {
    id: string;
    type: string;
}

export interface SourceConfig {
    config: {
    };
}

export interface IngestionPipeline {
    airflowConfig: AirflowConfig;
    loggerLevel: string;
    name: string;
    displayName: string;
    owner: Owner;
    pipelineType: string;
    service: Service;
    sourceConfig: SourceConfig;
}
