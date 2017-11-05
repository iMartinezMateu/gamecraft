import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GamecraftgatewaySharedModule } from '../../shared';
import {
    SlackAccountService,
    SlackAccountPopupService,
    SlackAccountComponent,
    SlackAccountDetailComponent,
    SlackAccountDialogComponent,
    SlackAccountPopupComponent,
    SlackAccountDeletePopupComponent,
    SlackAccountDeleteDialogComponent,
    slackAccountRoute,
    slackAccountPopupRoute,
    SlackAccountResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...slackAccountRoute,
    ...slackAccountPopupRoute,
];

@NgModule({
    imports: [
        GamecraftgatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SlackAccountComponent,
        SlackAccountDetailComponent,
        SlackAccountDialogComponent,
        SlackAccountDeleteDialogComponent,
        SlackAccountPopupComponent,
        SlackAccountDeletePopupComponent,
    ],
    entryComponents: [
        SlackAccountComponent,
        SlackAccountDialogComponent,
        SlackAccountPopupComponent,
        SlackAccountDeleteDialogComponent,
        SlackAccountDeletePopupComponent,
    ],
    providers: [
        SlackAccountService,
        SlackAccountPopupService,
        SlackAccountResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GamecraftgatewaySlackAccountModule {}
