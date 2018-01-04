import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GamecraftgatewaySharedModule } from '../../shared';
import {
    IrcBotService,
    IrcBotPopupService,
    IrcBotComponent,
    IrcBotDetailComponent,
    IrcBotDialogComponent,
    IrcBotPopupComponent,
    IrcBotDeletePopupComponent,
    IrcBotDeleteDialogComponent,
    ircBotRoute,
    ircBotPopupRoute,
    IrcBotResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...ircBotRoute,
    ...ircBotPopupRoute,
];

@NgModule({
    imports: [
        GamecraftgatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        IrcBotComponent,
        IrcBotDetailComponent,
        IrcBotDialogComponent,
        IrcBotDeleteDialogComponent,
        IrcBotPopupComponent,
        IrcBotDeletePopupComponent,
    ],
    entryComponents: [
        IrcBotComponent,
        IrcBotDialogComponent,
        IrcBotPopupComponent,
        IrcBotDeleteDialogComponent,
        IrcBotDeletePopupComponent,
    ],
    providers: [
        IrcBotService,
        IrcBotPopupService,
        IrcBotResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GamecraftgatewayIrcBotModule {}
