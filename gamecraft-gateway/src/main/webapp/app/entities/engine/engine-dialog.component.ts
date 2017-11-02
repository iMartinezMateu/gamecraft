import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Engine } from './engine.model';
import { EnginePopupService } from './engine-popup.service';
import { EngineService } from './engine.service';

@Component({
    selector: 'jhi-engine-dialog',
    templateUrl: './engine-dialog.component.html'
})
export class EngineDialogComponent implements OnInit {

    engine: Engine;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private engineService: EngineService,
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
        if (this.engine.id !== undefined) {
            this.subscribeToSaveResponse(
                this.engineService.update(this.engine));
        } else {
            this.subscribeToSaveResponse(
                this.engineService.create(this.engine));
        }
    }

    private subscribeToSaveResponse(result: Observable<Engine>) {
        result.subscribe((res: Engine) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Engine) {
        this.eventManager.broadcast({ name: 'engineListModification', content: 'OK'});
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
    selector: 'jhi-engine-popup',
    template: ''
})
export class EnginePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private enginePopupService: EnginePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.enginePopupService
                    .open(EngineDialogComponent as Component, params['id']);
            } else {
                this.enginePopupService
                    .open(EngineDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
