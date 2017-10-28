import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GamecraftgatewaySharedModule } from '../../shared';
import {
    ProjectService,
    ProjectPopupService,
    ProjectComponent,
    ProjectDetailComponent,
    ProjectDialogComponent,
    ProjectPopupComponent,
    ProjectDeletePopupComponent,
    ProjectDeleteDialogComponent,
    projectRoute,
    projectPopupRoute,
    ProjectResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...projectRoute,
    ...projectPopupRoute,
];

@NgModule({
    imports: [
        GamecraftgatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProjectComponent,
        ProjectDetailComponent,
        ProjectDialogComponent,
        ProjectDeleteDialogComponent,
        ProjectPopupComponent,
        ProjectDeletePopupComponent,
    ],
    entryComponents: [
        ProjectComponent,
        ProjectDialogComponent,
        ProjectPopupComponent,
        ProjectDeleteDialogComponent,
        ProjectDeletePopupComponent,
    ],
    providers: [
        ProjectService,
        ProjectPopupService,
        ProjectResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GamecraftgatewayProjectModule {}
