import { BaseEntity } from './../../shared';

export class Engine implements BaseEntity {
    constructor(
        public id?: number,
        public engineName?: string,
        public engineDescription?: string,
        public engineCompilerPath?: string,
        public engineCompilerArguments?: string,
    ) {
    }
}
