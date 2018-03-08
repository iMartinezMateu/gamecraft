import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SonarInstance } from './sonar-instance.model';
import { SonarInstancePopupService } from './sonar-instance-popup.service';
import { SonarInstanceService } from './sonar-instance.service';

@Component({
    selector: 'jhi-sonar-instance-delete-dialog',
    templateUrl: './sonar-instance-delete-dialog.component.html'
})
export class SonarInstanceDeleteDialogComponent {

    sonarInstance: SonarInstance;

    constructor(
        private sonarInstanceService: SonarInstanceService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.sonarInstanceService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'sonarInstanceListModification',
                content: 'Deleted an sonarInstance'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sonar-instance-delete-popup',
    template: ''
})
export class SonarInstanceDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private sonarInstancePopupService: SonarInstancePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.sonarInstancePopupService
                .open(SonarInstanceDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
