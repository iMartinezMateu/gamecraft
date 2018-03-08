import { BaseEntity } from './../../shared';

export const enum Role {
    'GUEST',
    'USER',
    'ADMINISTRATOR'
}

export class Groups implements BaseEntity {
    constructor(
        public id?: number,
        public groupName?: string,
        public groupDescription?: string,
        public groupRole?: Role,
        public groupEnabled?: boolean,
    ) {
        this.groupEnabled = false;
    }
}
