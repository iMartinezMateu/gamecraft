import { BaseEntity } from './../../shared';

export class Team implements BaseEntity {
    constructor(
        public id?: number,
        public teamName?: string,
        public teamDescription?: string,
        public teamUsers?: BaseEntity[],
    ) {
    }
}
