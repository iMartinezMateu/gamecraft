import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Groups } from './groups.model';
import { GroupsPopupService } from './groups-popup.service';
import { GroupsService } from './groups.service';

@Component({
    selector: 'jhi-groups-dialog',
    templateUrl: './groups-dialog.component.html'
})
export class GroupsDialogComponent implements OnInit {

    groups: Groups;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private groupsService: GroupsService,
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
        if (this.groups.id !== undefined) {
            this.subscribeToSaveResponse(
                this.groupsService.update(this.groups));
        } else {
            this.subscribeToSaveResponse(
                this.groupsService.create(this.groups));
        }
    }

    private subscribeToSaveResponse(result: Observable<Groups>) {
        result.subscribe((res: Groups) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Groups) {
        this.eventManager.broadcast({ name: 'groupsListModification', content: 'OK'});
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
    selector: 'jhi-groups-popup',
    template: ''
})
export class GroupsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private groupsPopupService: GroupsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.groupsPopupService
                    .open(GroupsDialogComponent as Component, params['id']);
            } else {
                this.groupsPopupService
                    .open(GroupsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
