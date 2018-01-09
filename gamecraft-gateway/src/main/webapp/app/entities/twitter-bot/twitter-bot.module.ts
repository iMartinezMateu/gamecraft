import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GamecraftgatewaySharedModule } from '../../shared';
import {
    TwitterBotService,
    TwitterBotPopupService,
    TwitterBotComponent,
    TwitterBotDetailComponent,
    TwitterBotDialogComponent,
    TwitterBotPopupComponent,
    TwitterBotDeletePopupComponent,
    TwitterBotDeleteDialogComponent,
    twitterBotRoute,
    twitterBotPopupRoute,
    TwitterBotResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...twitterBotRoute,
    ...twitterBotPopupRoute,
];

@NgModule({
    imports: [
        GamecraftgatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TwitterBotComponent,
        TwitterBotDetailComponent,
        TwitterBotDialogComponent,
        TwitterBotDeleteDialogComponent,
        TwitterBotPopupComponent,
        TwitterBotDeletePopupComponent,
    ],
    entryComponents: [
        TwitterBotComponent,
        TwitterBotDialogComponent,
        TwitterBotPopupComponent,
        TwitterBotDeleteDialogComponent,
        TwitterBotDeletePopupComponent,
    ],
    providers: [
        TwitterBotService,
        TwitterBotPopupService,
        TwitterBotResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GamecraftgatewayTwitterBotModule {}
