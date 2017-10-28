import { BaseEntity } from './../../shared';

export class Project implements BaseEntity {
    constructor(
        public id?: number,
        public projectName?: string,
        public projectDescription?: string,
        public projectWebsite?: string,
        public projectLogo?: string,
        public projectArchived?: boolean,
    ) {
        this.projectArchived = false;
    }
}
