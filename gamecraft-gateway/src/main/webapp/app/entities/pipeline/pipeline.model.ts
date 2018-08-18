import { BaseEntity } from './../../shared';

export const enum PipelineRepositoryType {
    'GITHUB',
    'BITBUCKET'
}

export const enum PipelineSchedule {
    'WEBHOOK',
    'CRON'
}

export class Pipeline implements BaseEntity {
    constructor(
        public id?: number,
        public pipelineName?: string,
        public pipelineDescription?: string,
        public pipelineRepositoryType?: PipelineRepositoryType,
        public pipelineRepositoryAddress?: string,
        public pipelineRepositoryUsername?: string,
        public pipelineRepositoryPassword?: string,
        public pipelineRepositoryBranch?: string,
        public pipelineEngineId?: number,
        public pipelineNotificatorId?: number,
        public pipelineFtpAddress?: string,
        public pipelineFtpUsername?: string,
        public pipelineFtpPassword?: string,
        public pipelineSchedule?: PipelineSchedule,
        public pipelineScheduleDetails?: string,
    ) {
    }
}
