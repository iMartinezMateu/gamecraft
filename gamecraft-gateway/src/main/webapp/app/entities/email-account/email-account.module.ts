import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GamecraftgatewaySharedModule } from '../../shared';
import {
    EmailAccountService,
    EmailAccountPopupService,
    EmailAccountComponent,
    EmailAccountDetailComponent,
    EmailAccountDialogComponent,
    EmailAccountPopupComponent,
    EmailAccountDeletePopupComponent,
    EmailAccountDeleteDialogComponent,
    emailAccountRoute,
    emailAccountPopupRoute,
    EmailAccountResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...emailAccountRoute,
    ...emailAccountPopupRoute,
];

@NgModule({
    imports: [
        GamecraftgatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        EmailAccountComponent,
        EmailAccountDetailComponent,
        EmailAccountDialogComponent,
        EmailAccountDeleteDialogComponent,
        EmailAccountPopupComponent,
        EmailAccountDeletePopupComponent,
    ],
    entryComponents: [
        EmailAccountComponent,
        EmailAccountDialogComponent,
        EmailAccountPopupComponent,
        EmailAccountDeleteDialogComponent,
        EmailAccountDeletePopupComponent,
    ],
    providers: [
        EmailAccountService,
        EmailAccountPopupService,
        EmailAccountResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GamecraftgatewayEmailAccountModule {}
