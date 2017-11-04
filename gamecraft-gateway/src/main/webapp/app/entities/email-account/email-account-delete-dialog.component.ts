import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { EmailAccount } from './email-account.model';
import { EmailAccountPopupService } from './email-account-popup.service';
import { EmailAccountService } from './email-account.service';

@Component({
    selector: 'jhi-email-account-delete-dialog',
    templateUrl: './email-account-delete-dialog.component.html'
})
export class EmailAccountDeleteDialogComponent {

    emailAccount: EmailAccount;

    constructor(
        private emailAccountService: EmailAccountService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.emailAccountService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'emailAccountListModification',
                content: 'Deleted an emailAccount'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-email-account-delete-popup',
    template: ''
})
export class EmailAccountDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private emailAccountPopupService: EmailAccountPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.emailAccountPopupService
                .open(EmailAccountDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
