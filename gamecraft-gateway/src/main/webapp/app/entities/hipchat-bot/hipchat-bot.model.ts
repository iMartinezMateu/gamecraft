import { BaseEntity } from './../../shared';

export class HipchatBot implements BaseEntity {
    constructor(
        public id?: number,
        public hipchatBotName?: string,
        public hipchatBotDescription?: string,
        public hipchatBotToken?: string,
        public hipchatBotEnabled?: boolean,
    ) {
        this.hipchatBotEnabled = false;
    }
}
