import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Team } from './team.model';
import { TeamPopupService } from './team-popup.service';
import { TeamService } from './team.service';
import { TeamUser, TeamUserService } from '../team-user';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-team-dialog',
    templateUrl: './team-dialog.component.html'
})
export class TeamDialogComponent implements OnInit {

    team: Team;
    isSaving: boolean;

    teamusers: TeamUser[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private teamService: TeamService,
        private teamUserService: TeamUserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.teamUserService.query()
            .subscribe((res: ResponseWrapper) => { this.teamusers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.team.id !== undefined) {
            this.subscribeToSaveResponse(
                this.teamService.update(this.team));
        } else {
            this.subscribeToSaveResponse(
                this.teamService.create(this.team));
        }
    }

    private subscribeToSaveResponse(result: Observable<Team>) {
        result.subscribe((res: Team) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Team) {
        this.eventManager.broadcast({ name: 'teamListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTeamUserById(index: number, item: TeamUser) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-team-popup',
    template: ''
})
export class TeamPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private teamPopupService: TeamPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.teamPopupService
                    .open(TeamDialogComponent as Component, params['id']);
            } else {
                this.teamPopupService
                    .open(TeamDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
