import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TeamUser } from './team-user.model';
import { TeamUserPopupService } from './team-user-popup.service';
import { TeamUserService } from './team-user.service';

@Component({
    selector: 'jhi-team-user-delete-dialog',
    templateUrl: './team-user-delete-dialog.component.html'
})
export class TeamUserDeleteDialogComponent {

    teamUser: TeamUser;

    constructor(
        private teamUserService: TeamUserService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.teamUserService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'teamUserListModification',
                content: 'Deleted an teamUser'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-team-user-delete-popup',
    template: ''
})
export class TeamUserDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private teamUserPopupService: TeamUserPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.teamUserPopupService
                .open(TeamUserDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
