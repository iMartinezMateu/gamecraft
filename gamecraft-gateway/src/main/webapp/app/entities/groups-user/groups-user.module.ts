import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GamecraftgatewaySharedModule } from '../../shared';
import {
    GroupsUserService,
    GroupsUserPopupService,
    GroupsUserComponent,
    GroupsUserDetailComponent,
    GroupsUserDialogComponent,
    GroupsUserPopupComponent,
    GroupsUserDeletePopupComponent,
    GroupsUserDeleteDialogComponent,
    groupsUserRoute,
    groupsUserPopupRoute,
    GroupsUserResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...groupsUserRoute,
    ...groupsUserPopupRoute,
];

@NgModule({
    imports: [
        GamecraftgatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        GroupsUserComponent,
        GroupsUserDetailComponent,
        GroupsUserDialogComponent,
        GroupsUserDeleteDialogComponent,
        GroupsUserPopupComponent,
        GroupsUserDeletePopupComponent,
    ],
    entryComponents: [
        GroupsUserComponent,
        GroupsUserDialogComponent,
        GroupsUserPopupComponent,
        GroupsUserDeleteDialogComponent,
        GroupsUserDeletePopupComponent,
    ],
    providers: [
        GroupsUserService,
        GroupsUserPopupService,
        GroupsUserResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GamecraftgatewayGroupsUserModule {}
