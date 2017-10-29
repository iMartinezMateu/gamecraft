import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TeamUser } from './team-user.model';
import { TeamUserService } from './team-user.service';

@Component({
    selector: 'jhi-team-user-detail',
    templateUrl: './team-user-detail.component.html'
})
export class TeamUserDetailComponent implements OnInit, OnDestroy {

    teamUser: TeamUser;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private teamUserService: TeamUserService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTeamUsers();
    }

    load(id) {
        this.teamUserService.find(id).subscribe((teamUser) => {
            this.teamUser = teamUser;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTeamUsers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'teamUserListModification',
            (response) => this.load(this.teamUser.id)
        );
    }
}
