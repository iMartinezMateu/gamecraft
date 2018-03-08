import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Groups } from './groups.model';
import { GroupsPopupService } from './groups-popup.service';
import { GroupsService } from './groups.service';

@Component({
    selector: 'jhi-groups-delete-dialog',
    templateUrl: './groups-delete-dialog.component.html'
})
export class GroupsDeleteDialogComponent {

    groups: Groups;

    constructor(
        private groupsService: GroupsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.groupsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'groupsListModification',
                content: 'Deleted an groups'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-groups-delete-popup',
    template: ''
})
export class GroupsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private groupsPopupService: GroupsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.groupsPopupService
                .open(GroupsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
