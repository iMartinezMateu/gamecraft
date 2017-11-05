import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SlackAccount } from './slack-account.model';
import { SlackAccountPopupService } from './slack-account-popup.service';
import { SlackAccountService } from './slack-account.service';

@Component({
    selector: 'jhi-slack-account-delete-dialog',
    templateUrl: './slack-account-delete-dialog.component.html'
})
export class SlackAccountDeleteDialogComponent {

    slackAccount: SlackAccount;

    constructor(
        private slackAccountService: SlackAccountService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.slackAccountService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'slackAccountListModification',
                content: 'Deleted an slackAccount'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-slack-account-delete-popup',
    template: ''
})
export class SlackAccountDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private slackAccountPopupService: SlackAccountPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.slackAccountPopupService
                .open(SlackAccountDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
