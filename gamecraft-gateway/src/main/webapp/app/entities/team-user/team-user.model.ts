import { BaseEntity } from './../../shared';

export class TeamUser implements BaseEntity {
    constructor(
        public id?: number,
        public userId?: number,
        public teamId?: number,
        public teams?: BaseEntity[],
    ) {
    }
}
