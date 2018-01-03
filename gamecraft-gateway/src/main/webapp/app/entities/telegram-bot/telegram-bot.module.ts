import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GamecraftgatewaySharedModule } from '../../shared';
import {
    TelegramBotService,
    TelegramBotPopupService,
    TelegramBotComponent,
    TelegramBotDetailComponent,
    TelegramBotDialogComponent,
    TelegramBotPopupComponent,
    TelegramBotDeletePopupComponent,
    TelegramBotDeleteDialogComponent,
    telegramBotRoute,
    telegramBotPopupRoute,
    TelegramBotResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...telegramBotRoute,
    ...telegramBotPopupRoute,
];

@NgModule({
    imports: [
        GamecraftgatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TelegramBotComponent,
        TelegramBotDetailComponent,
        TelegramBotDialogComponent,
        TelegramBotDeleteDialogComponent,
        TelegramBotPopupComponent,
        TelegramBotDeletePopupComponent,
    ],
    entryComponents: [
        TelegramBotComponent,
        TelegramBotDialogComponent,
        TelegramBotPopupComponent,
        TelegramBotDeleteDialogComponent,
        TelegramBotDeletePopupComponent,
    ],
    providers: [
        TelegramBotService,
        TelegramBotPopupService,
        TelegramBotResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GamecraftgatewayTelegramBotModule {}
