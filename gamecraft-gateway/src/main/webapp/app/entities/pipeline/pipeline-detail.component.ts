import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Pipeline } from './pipeline.model';
import { PipelineService } from './pipeline.service';

@Component({
    selector: 'jhi-pipeline-detail',
    templateUrl: './pipeline-detail.component.html'
})
export class PipelineDetailComponent implements OnInit, OnDestroy {

    pipeline: Pipeline;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private pipelineService: PipelineService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPipelines();
    }

    load(id) {
        this.pipelineService.find(id).subscribe((pipeline) => {
            this.pipeline = pipeline;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPipelines() {
        this.eventSubscriber = this.eventManager.subscribe(
            'pipelineListModification',
            (response) => this.load(this.pipeline.id)
        );
    }
}
