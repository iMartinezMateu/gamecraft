import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Report } from './report.model';
import { ReportPopupService } from './report-popup.service';
import { ReportService } from './report.service';

@Component({
    selector: 'jhi-report-dialog',
    templateUrl: './report-dialog.component.html'
})
export class ReportDialogComponent implements OnInit {

    report: Report;
    isSaving: boolean;
    reportDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private reportService: ReportService,
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
        if (this.report.id !== undefined) {
            this.subscribeToSaveResponse(
                this.reportService.update(this.report));
        } else {
            this.subscribeToSaveResponse(
                this.reportService.create(this.report));
        }
    }

    private subscribeToSaveResponse(result: Observable<Report>) {
        result.subscribe((res: Report) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Report) {
        this.eventManager.broadcast({ name: 'reportListModification', content: 'OK'});
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
    selector: 'jhi-report-popup',
    template: ''
})
export class ReportPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reportPopupService: ReportPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.reportPopupService
                    .open(ReportDialogComponent as Component, params['id']);
            } else {
                this.reportPopupService
                    .open(ReportDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
