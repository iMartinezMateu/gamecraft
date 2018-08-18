import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Pipeline } from './pipeline.model';
import { PipelinePopupService } from './pipeline-popup.service';
import { PipelineService } from './pipeline.service';

@Component({
    selector: 'jhi-pipeline-dialog',
    templateUrl: './pipeline-dialog.component.html'
})
export class PipelineDialogComponent implements OnInit {

    pipeline: Pipeline;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private pipelineService: PipelineService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.pipeline.id !== undefined) {
            this.subscribeToSaveResponse(
                this.pipelineService.update(this.pipeline));
        } else {
            this.subscribeToSaveResponse(
                this.pipelineService.create(this.pipeline));
        }
    }

    private subscribeToSaveResponse(result: Observable<Pipeline>) {
        result.subscribe((res: Pipeline) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Pipeline) {
        this.eventManager.broadcast({ name: 'pipelineListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-pipeline-popup',
    template: ''
})
export class PipelinePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pipelinePopupService: PipelinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.pipelinePopupService
                    .open(PipelineDialogComponent as Component, params['id']);
            } else {
                this.pipelinePopupService
                    .open(PipelineDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
