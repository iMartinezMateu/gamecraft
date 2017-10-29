import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GamecraftgatewaySharedModule } from '../../shared';
import {
    TeamUserService,
    TeamUserPopupService,
    TeamUserComponent,
    TeamUserDetailComponent,
    TeamUserDialogComponent,
    TeamUserPopupComponent,
    TeamUserDeletePopupComponent,
    TeamUserDeleteDialogComponent,
    teamUserRoute,
    teamUserPopupRoute,
    TeamUserResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...teamUserRoute,
    ...teamUserPopupRoute,
];

@NgModule({
    imports: [
        GamecraftgatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TeamUserComponent,
        TeamUserDetailComponent,
        TeamUserDialogComponent,
        TeamUserDeleteDialogComponent,
        TeamUserPopupComponent,
        TeamUserDeletePopupComponent,
    ],
    entryComponents: [
        TeamUserComponent,
        TeamUserDialogComponent,
        TeamUserPopupComponent,
        TeamUserDeleteDialogComponent,
        TeamUserDeletePopupComponent,
    ],
    providers: [
        TeamUserService,
        TeamUserPopupService,
        TeamUserResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GamecraftgatewayTeamUserModule {}
