import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TeamProject } from './team-project.model';
import { TeamProjectService } from './team-project.service';

@Component({
    selector: 'jhi-team-project-detail',
    templateUrl: './team-project-detail.component.html'
})
export class TeamProjectDetailComponent implements OnInit, OnDestroy {

    teamProject: TeamProject;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private teamProjectService: TeamProjectService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTeamProjects();
    }

    load(id) {
        this.teamProjectService.find(id).subscribe((teamProject) => {
            this.teamProject = teamProject;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTeamProjects() {
        this.eventSubscriber = this.eventManager.subscribe(
            'teamProjectListModification',
            (response) => this.load(this.teamProject.id)
        );
    }
}
