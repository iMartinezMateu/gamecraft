import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { GamecraftgatewayProjectModule } from './project/project.module';
import { GamecraftgatewayTeamModule } from './team/team.module';
import { GamecraftgatewayTeamUserModule } from './team-user/team-user.module';
import { GamecraftgatewayTeamProjectModule } from './team-project/team-project.module';
import { GamecraftgatewayEngineModule } from './engine/engine.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        GamecraftgatewayProjectModule,
        GamecraftgatewayTeamModule,
        GamecraftgatewayTeamUserModule,
        GamecraftgatewayTeamProjectModule,
        GamecraftgatewayEngineModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GamecraftgatewayEntityModule {}
