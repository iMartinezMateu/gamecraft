import { BaseEntity } from './../../shared';

export class GroupsUser implements BaseEntity {
    constructor(
        public id?: number,
        public groupId?: number,
        public userId?: number,
    ) {
    }
}
