import { BaseEntity } from './../../shared';

export class Report implements BaseEntity {
    constructor(
        public id?: number,
        public pipelineId?: number,
        public reportDate?: any,
        public reportContent?: string,
    ) {
    }
}
