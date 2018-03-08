import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { GroupsUser } from './groups-user.model';
import { GroupsUserPopupService } from './groups-user-popup.service';
import { GroupsUserService } from './groups-user.service';

@Component({
    selector: 'jhi-groups-user-dialog',
    templateUrl: './groups-user-dialog.component.html'
})
export class GroupsUserDialogComponent implements OnInit {

    groupsUser: GroupsUser;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private groupsUserService: GroupsUserService,
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
        if (this.groupsUser.id !== undefined) {
            this.subscribeToSaveResponse(
                this.groupsUserService.update(this.groupsUser));
        } else {
            this.subscribeToSaveResponse(
                this.groupsUserService.create(this.groupsUser));
        }
    }

    private subscribeToSaveResponse(result: Observable<GroupsUser>) {
        result.subscribe((res: GroupsUser) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: GroupsUser) {
        this.eventManager.broadcast({ name: 'groupsUserListModification', content: 'OK'});
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
    selector: 'jhi-groups-user-popup',
    template: ''
})
export class GroupsUserPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private groupsUserPopupService: GroupsUserPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.groupsUserPopupService
                    .open(GroupsUserDialogComponent as Component, params['id']);
            } else {
                this.groupsUserPopupService
                    .open(GroupsUserDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
