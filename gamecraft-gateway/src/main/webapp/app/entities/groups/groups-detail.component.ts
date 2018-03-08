import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Groups } from './groups.model';
import { GroupsService } from './groups.service';

@Component({
    selector: 'jhi-groups-detail',
    templateUrl: './groups-detail.component.html'
})
export class GroupsDetailComponent implements OnInit, OnDestroy {

    groups: Groups;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private groupsService: GroupsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInGroups();
    }

    load(id) {
        this.groupsService.find(id).subscribe((groups) => {
            this.groups = groups;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInGroups() {
        this.eventSubscriber = this.eventManager.subscribe(
            'groupsListModification',
            (response) => this.load(this.groups.id)
        );
    }
}
