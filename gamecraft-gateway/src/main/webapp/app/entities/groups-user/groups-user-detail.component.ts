import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { GroupsUser } from './groups-user.model';
import { GroupsUserService } from './groups-user.service';

@Component({
    selector: 'jhi-groups-user-detail',
    templateUrl: './groups-user-detail.component.html'
})
export class GroupsUserDetailComponent implements OnInit, OnDestroy {

    groupsUser: GroupsUser;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private groupsUserService: GroupsUserService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInGroupsUsers();
    }

    load(id) {
        this.groupsUserService.find(id).subscribe((groupsUser) => {
            this.groupsUser = groupsUser;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInGroupsUsers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'groupsUserListModification',
            (response) => this.load(this.groupsUser.id)
        );
    }
}
