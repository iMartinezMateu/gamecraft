import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TeamUser } from './team-user.model';
import { TeamUserPopupService } from './team-user-popup.service';
import { TeamUserService } from './team-user.service';
import { Team, TeamService } from '../team';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-team-user-dialog',
    templateUrl: './team-user-dialog.component.html'
})
export class TeamUserDialogComponent implements OnInit {

    teamUser: TeamUser;
    isSaving: boolean;

    teams: Team[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private teamUserService: TeamUserService,
        private teamService: TeamService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.teamService.query()
            .subscribe((res: ResponseWrapper) => { this.teams = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.teamUser.id !== undefined) {
            this.subscribeToSaveResponse(
                this.teamUserService.update(this.teamUser));
        } else {
            this.subscribeToSaveResponse(
                this.teamUserService.create(this.teamUser));
        }
    }

    private subscribeToSaveResponse(result: Observable<TeamUser>) {
        result.subscribe((res: TeamUser) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TeamUser) {
        this.eventManager.broadcast({ name: 'teamUserListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTeamById(index: number, item: Team) {
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
    selector: 'jhi-team-user-popup',
    template: ''
})
export class TeamUserPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private teamUserPopupService: TeamUserPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.teamUserPopupService
                    .open(TeamUserDialogComponent as Component, params['id']);
            } else {
                this.teamUserPopupService
                    .open(TeamUserDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
