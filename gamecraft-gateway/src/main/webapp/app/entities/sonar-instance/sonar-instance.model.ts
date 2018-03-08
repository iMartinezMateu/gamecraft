import { BaseEntity } from './../../shared';

export class SonarInstance implements BaseEntity {
    constructor(
        public id?: number,
        public sonarInstanceName?: string,
        public sonarInstanceDescription?: string,
        public sonarInstanceRunnerPath?: string,
        public sonarInstanceEnabled?: boolean,
    ) {
        this.sonarInstanceEnabled = false;
    }
}
