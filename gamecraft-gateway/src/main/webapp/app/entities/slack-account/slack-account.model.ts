import { BaseEntity } from './../../shared';

export class SlackAccount implements BaseEntity {
    constructor(
        public id?: number,
        public slackAccountName?: string,
        public slackAccountDescription?: string,
        public slackAccountToken?: string,
        public slackAccountEnabled?: boolean,
    ) {
        this.slackAccountEnabled = false;
    }
}
