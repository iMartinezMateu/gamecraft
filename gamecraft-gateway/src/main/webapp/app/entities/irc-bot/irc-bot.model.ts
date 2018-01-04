import { BaseEntity } from './../../shared';

export class IrcBot implements BaseEntity {
    constructor(
        public id?: number,
        public ircBotName?: string,
        public ircBotDescription?: string,
        public ircBotEnabled?: boolean,
        public ircServerAddress?: string,
        public ircServerPort?: number,
        public ircBotNickname?: string,
    ) {
        this.ircBotEnabled = false;
    }
}
