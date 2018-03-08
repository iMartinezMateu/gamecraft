import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { SonarInstance } from './sonar-instance.model';
import { SonarInstanceService } from './sonar-instance.service';

@Component({
    selector: 'jhi-sonar-instance-detail',
    templateUrl: './sonar-instance-detail.component.html'
})
export class SonarInstanceDetailComponent implements OnInit, OnDestroy {

    sonarInstance: SonarInstance;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private sonarInstanceService: SonarInstanceService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSonarInstances();
    }

    load(id) {
        this.sonarInstanceService.find(id).subscribe((sonarInstance) => {
            this.sonarInstance = sonarInstance;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSonarInstances() {
        this.eventSubscriber = this.eventManager.subscribe(
            'sonarInstanceListModification',
            (response) => this.load(this.sonarInstance.id)
        );
    }
}
