import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { GroupsUser } from './groups-user.model';
import { GroupsUserPopupService } from './groups-user-popup.service';
import { GroupsUserService } from './groups-user.service';

@Component({
    selector: 'jhi-groups-user-delete-dialog',
    templateUrl: './groups-user-delete-dialog.component.html'
})
export class GroupsUserDeleteDialogComponent {

    groupsUser: GroupsUser;

    constructor(
        private groupsUserService: GroupsUserService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.groupsUserService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'groupsUserListModification',
                content: 'Deleted an groupsUser'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-groups-user-delete-popup',
    template: ''
})
export class GroupsUserDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private groupsUserPopupService: GroupsUserPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.groupsUserPopupService
                .open(GroupsUserDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
