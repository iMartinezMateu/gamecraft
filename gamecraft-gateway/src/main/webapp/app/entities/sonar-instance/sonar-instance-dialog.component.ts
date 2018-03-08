import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SonarInstance } from './sonar-instance.model';
import { SonarInstancePopupService } from './sonar-instance-popup.service';
import { SonarInstanceService } from './sonar-instance.service';

@Component({
    selector: 'jhi-sonar-instance-dialog',
    templateUrl: './sonar-instance-dialog.component.html'
})
export class SonarInstanceDialogComponent implements OnInit {

    sonarInstance: SonarInstance;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private sonarInstanceService: SonarInstanceService,
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
        if (this.sonarInstance.id !== undefined) {
            this.subscribeToSaveResponse(
                this.sonarInstanceService.update(this.sonarInstance));
        } else {
            this.subscribeToSaveResponse(
                this.sonarInstanceService.create(this.sonarInstance));
        }
    }

    private subscribeToSaveResponse(result: Observable<SonarInstance>) {
        result.subscribe((res: SonarInstance) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: SonarInstance) {
        this.eventManager.broadcast({ name: 'sonarInstanceListModification', content: 'OK'});
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
    selector: 'jhi-sonar-instance-popup',
    template: ''
})
export class SonarInstancePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private sonarInstancePopupService: SonarInstancePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.sonarInstancePopupService
                    .open(SonarInstanceDialogComponent as Component, params['id']);
            } else {
                this.sonarInstancePopupService
                    .open(SonarInstanceDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
