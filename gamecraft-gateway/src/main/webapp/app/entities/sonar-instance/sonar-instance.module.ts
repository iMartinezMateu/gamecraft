import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GamecraftgatewaySharedModule } from '../../shared';
import {
    SonarInstanceService,
    SonarInstancePopupService,
    SonarInstanceComponent,
    SonarInstanceDetailComponent,
    SonarInstanceDialogComponent,
    SonarInstancePopupComponent,
    SonarInstanceDeletePopupComponent,
    SonarInstanceDeleteDialogComponent,
    sonarInstanceRoute,
    sonarInstancePopupRoute,
    SonarInstanceResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...sonarInstanceRoute,
    ...sonarInstancePopupRoute,
];

@NgModule({
    imports: [
        GamecraftgatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SonarInstanceComponent,
        SonarInstanceDetailComponent,
        SonarInstanceDialogComponent,
        SonarInstanceDeleteDialogComponent,
        SonarInstancePopupComponent,
        SonarInstanceDeletePopupComponent,
    ],
    entryComponents: [
        SonarInstanceComponent,
        SonarInstanceDialogComponent,
        SonarInstancePopupComponent,
        SonarInstanceDeleteDialogComponent,
        SonarInstanceDeletePopupComponent,
    ],
    providers: [
        SonarInstanceService,
        SonarInstancePopupService,
        SonarInstanceResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GamecraftgatewaySonarInstanceModule {}
