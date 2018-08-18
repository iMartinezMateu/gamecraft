import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { GamecraftgatewayProjectModule } from './project/project.module';
import { GamecraftgatewayTeamModule } from './team/team.module';
import { GamecraftgatewayTeamUserModule } from './team-user/team-user.module';
import { GamecraftgatewayTeamProjectModule } from './team-project/team-project.module';
import { GamecraftgatewayEngineModule } from './engine/engine.module';
import { GamecraftgatewayEmailAccountModule } from './email-account/email-account.module';
import { GamecraftgatewaySlackAccountModule } from './slack-account/slack-account.module';
import { GamecraftgatewayTelegramBotModule } from './telegram-bot/telegram-bot.module';
import { GamecraftgatewayIrcBotModule } from './irc-bot/irc-bot.module';
import { GamecraftgatewayTwitterBotModule } from './twitter-bot/twitter-bot.module';
import { GamecraftgatewayHipchatBotModule } from './hipchat-bot/hipchat-bot.module';
import { GamecraftgatewaySonarInstanceModule } from './sonar-instance/sonar-instance.module';
import { GamecraftgatewayGroupsModule } from './groups/groups.module';
import { GamecraftgatewayGroupsUserModule } from './groups-user/groups-user.module';
import { GamecraftgatewayPipelineModule } from './pipeline/pipeline.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        GamecraftgatewayProjectModule,
        GamecraftgatewayTeamModule,
        GamecraftgatewayTeamUserModule,
        GamecraftgatewayTeamProjectModule,
        GamecraftgatewayEngineModule,
        GamecraftgatewayEmailAccountModule,
        GamecraftgatewaySlackAccountModule,
        GamecraftgatewayTelegramBotModule,
        GamecraftgatewayIrcBotModule,
        GamecraftgatewayTwitterBotModule,
        GamecraftgatewayHipchatBotModule,
        GamecraftgatewaySonarInstanceModule,
        GamecraftgatewayGroupsModule,
        GamecraftgatewayGroupsUserModule,
        GamecraftgatewayPipelineModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GamecraftgatewayEntityModule {}
