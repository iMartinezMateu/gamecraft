import { BaseEntity } from './../../shared';

export class TeamProject implements BaseEntity {
    constructor(
        public id?: number,
        public teamId?: number,
        public projectId?: number,
    ) {
    }
}
