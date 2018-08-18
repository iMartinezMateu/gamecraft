import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Pipeline } from './pipeline.model';
import { PipelinePopupService } from './pipeline-popup.service';
import { PipelineService } from './pipeline.service';

@Component({
    selector: 'jhi-pipeline-delete-dialog',
    templateUrl: './pipeline-delete-dialog.component.html'
})
export class PipelineDeleteDialogComponent {

    pipeline: Pipeline;

    constructor(
        private pipelineService: PipelineService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pipelineService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'pipelineListModification',
                content: 'Deleted an pipeline'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pipeline-delete-popup',
    template: ''
})
export class PipelineDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pipelinePopupService: PipelinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.pipelinePopupService
                .open(PipelineDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
