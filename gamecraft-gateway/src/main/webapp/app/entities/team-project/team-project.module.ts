import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GamecraftgatewaySharedModule } from '../../shared';
import {
    TeamProjectService,
    TeamProjectPopupService,
    TeamProjectComponent,
    TeamProjectDetailComponent,
    TeamProjectDialogComponent,
    TeamProjectPopupComponent,
    TeamProjectDeletePopupComponent,
    TeamProjectDeleteDialogComponent,
    teamProjectRoute,
    teamProjectPopupRoute,
    TeamProjectResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...teamProjectRoute,
    ...teamProjectPopupRoute,
];

@NgModule({
    imports: [
        GamecraftgatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TeamProjectComponent,
        TeamProjectDetailComponent,
        TeamProjectDialogComponent,
        TeamProjectDeleteDialogComponent,
        TeamProjectPopupComponent,
        TeamProjectDeletePopupComponent,
    ],
    entryComponents: [
        TeamProjectComponent,
        TeamProjectDialogComponent,
        TeamProjectPopupComponent,
        TeamProjectDeleteDialogComponent,
        TeamProjectDeletePopupComponent,
    ],
    providers: [
        TeamProjectService,
        TeamProjectPopupService,
        TeamProjectResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GamecraftgatewayTeamProjectModule {}
