import { BaseEntity } from './../../shared';

export class TwitterBot implements BaseEntity {
    constructor(
        public id?: number,
        public twitterBotName?: string,
        public twitterBotDescription?: string,
        public twitterBotConsumerKey?: string,
        public twitterBotConsumerSecret?: string,
        public twitterBotAccessToken?: string,
        public twitterBotAccessTokenSecret?: string,
        public twitterBotEnabled?: boolean,
    ) {
        this.twitterBotEnabled = false;
    }
}
