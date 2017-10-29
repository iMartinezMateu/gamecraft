import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TeamProject } from './team-project.model';
import { TeamProjectPopupService } from './team-project-popup.service';
import { TeamProjectService } from './team-project.service';

@Component({
    selector: 'jhi-team-project-dialog',
    templateUrl: './team-project-dialog.component.html'
})
export class TeamProjectDialogComponent implements OnInit {

    teamProject: TeamProject;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private teamProjectService: TeamProjectService,
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
        if (this.teamProject.id !== undefined) {
            this.subscribeToSaveResponse(
                this.teamProjectService.update(this.teamProject));
        } else {
            this.subscribeToSaveResponse(
                this.teamProjectService.create(this.teamProject));
        }
    }

    private subscribeToSaveResponse(result: Observable<TeamProject>) {
        result.subscribe((res: TeamProject) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TeamProject) {
        this.eventManager.broadcast({ name: 'teamProjectListModification', content: 'OK'});
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
    selector: 'jhi-team-project-popup',
    template: ''
})
export class TeamProjectPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private teamProjectPopupService: TeamProjectPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.teamProjectPopupService
                    .open(TeamProjectDialogComponent as Component, params['id']);
            } else {
                this.teamProjectPopupService
                    .open(TeamProjectDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
