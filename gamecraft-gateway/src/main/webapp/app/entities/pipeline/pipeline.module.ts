import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GamecraftgatewaySharedModule } from '../../shared';
import {
    PipelineService,
    PipelinePopupService,
    PipelineComponent,
    PipelineDetailComponent,
    PipelineDialogComponent,
    PipelinePopupComponent,
    PipelineDeletePopupComponent,
    PipelineDeleteDialogComponent,
    pipelineRoute,
    pipelinePopupRoute,
    PipelineResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...pipelineRoute,
    ...pipelinePopupRoute,
];

@NgModule({
    imports: [
        GamecraftgatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PipelineComponent,
        PipelineDetailComponent,
        PipelineDialogComponent,
        PipelineDeleteDialogComponent,
        PipelinePopupComponent,
        PipelineDeletePopupComponent,
    ],
    entryComponents: [
        PipelineComponent,
        PipelineDialogComponent,
        PipelinePopupComponent,
        PipelineDeleteDialogComponent,
        PipelineDeletePopupComponent,
    ],
    providers: [
        PipelineService,
        PipelinePopupService,
        PipelineResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GamecraftgatewayPipelineModule {}
