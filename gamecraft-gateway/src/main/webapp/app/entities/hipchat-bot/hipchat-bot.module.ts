import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GamecraftgatewaySharedModule } from '../../shared';
import {
    HipchatBotService,
    HipchatBotPopupService,
    HipchatBotComponent,
    HipchatBotDetailComponent,
    HipchatBotDialogComponent,
    HipchatBotPopupComponent,
    HipchatBotDeletePopupComponent,
    HipchatBotDeleteDialogComponent,
    hipchatBotRoute,
    hipchatBotPopupRoute,
    HipchatBotResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...hipchatBotRoute,
    ...hipchatBotPopupRoute,
];

@NgModule({
    imports: [
        GamecraftgatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        HipchatBotComponent,
        HipchatBotDetailComponent,
        HipchatBotDialogComponent,
        HipchatBotDeleteDialogComponent,
        HipchatBotPopupComponent,
        HipchatBotDeletePopupComponent,
    ],
    entryComponents: [
        HipchatBotComponent,
        HipchatBotDialogComponent,
        HipchatBotPopupComponent,
        HipchatBotDeleteDialogComponent,
        HipchatBotDeletePopupComponent,
    ],
    providers: [
        HipchatBotService,
        HipchatBotPopupService,
        HipchatBotResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GamecraftgatewayHipchatBotModule {}
