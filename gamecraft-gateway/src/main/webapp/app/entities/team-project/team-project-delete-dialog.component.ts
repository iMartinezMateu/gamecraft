import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TeamProject } from './team-project.model';
import { TeamProjectPopupService } from './team-project-popup.service';
import { TeamProjectService } from './team-project.service';

@Component({
    selector: 'jhi-team-project-delete-dialog',
    templateUrl: './team-project-delete-dialog.component.html'
})
export class TeamProjectDeleteDialogComponent {

    teamProject: TeamProject;

    constructor(
        private teamProjectService: TeamProjectService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.teamProjectService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'teamProjectListModification',
                content: 'Deleted an teamProject'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-team-project-delete-popup',
    template: ''
})
export class TeamProjectDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private teamProjectPopupService: TeamProjectPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.teamProjectPopupService
                .open(TeamProjectDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
