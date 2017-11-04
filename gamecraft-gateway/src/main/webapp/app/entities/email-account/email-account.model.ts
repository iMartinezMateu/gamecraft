import { BaseEntity } from './../../shared';

export class EmailAccount implements BaseEntity {
    constructor(
        public id?: number,
        public emailAccountName?: string,
        public emailSmtpServer?: string,
        public emailSmtpUsername?: string,
        public emailSmtpPassword?: string,
        public emailSmtpUseSSL?: boolean,
        public emailSmtpPort?: number,
        public emailAccountDescription?: string,
        public emailAccountEnabled?: boolean,
    ) {
        this.emailSmtpUseSSL = false;
        this.emailAccountEnabled = false;
    }
}
