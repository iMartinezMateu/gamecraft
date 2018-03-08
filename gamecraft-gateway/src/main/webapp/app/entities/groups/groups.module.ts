import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GamecraftgatewaySharedModule } from '../../shared';
import {
    GroupsService,
    GroupsPopupService,
    GroupsComponent,
    GroupsDetailComponent,
    GroupsDialogComponent,
    GroupsPopupComponent,
    GroupsDeletePopupComponent,
    GroupsDeleteDialogComponent,
    groupsRoute,
    groupsPopupRoute,
    GroupsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...groupsRoute,
    ...groupsPopupRoute,
];

@NgModule({
    imports: [
        GamecraftgatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        GroupsComponent,
        GroupsDetailComponent,
        GroupsDialogComponent,
        GroupsDeleteDialogComponent,
        GroupsPopupComponent,
        GroupsDeletePopupComponent,
    ],
    entryComponents: [
        GroupsComponent,
        GroupsDialogComponent,
        GroupsPopupComponent,
        GroupsDeleteDialogComponent,
        GroupsDeletePopupComponent,
    ],
    providers: [
        GroupsService,
        GroupsPopupService,
        GroupsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GamecraftgatewayGroupsModule {}
