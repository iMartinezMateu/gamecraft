import { BaseEntity } from './../../shared';

export class TelegramBot implements BaseEntity {
    constructor(
        public id?: number,
        public telegramBotName?: string,
        public telegramBotDescription?: string,
        public telegramBotToken?: string,
        public telegramBotEnabled?: boolean,
    ) {
        this.telegramBotEnabled = false;
    }
}
