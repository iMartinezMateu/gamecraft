import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Report } from './report.model';
import { ReportService } from './report.service';

@Component({
    selector: 'jhi-report-detail',
    templateUrl: './report-detail.component.html'
})
export class ReportDetailComponent implements OnInit, OnDestroy {

    report: Report;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private reportService: ReportService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInReports();
    }

    load(id) {
        this.reportService.find(id).subscribe((report) => {
            this.report = report;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInReports() {
        this.eventSubscriber = this.eventManager.subscribe(
            'reportListModification',
            (response) => this.load(this.report.id)
        );
    }
}
