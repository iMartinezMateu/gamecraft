import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { EmailAccount } from './email-account.model';
import { EmailAccountPopupService } from './email-account-popup.service';
import { EmailAccountService } from './email-account.service';

@Component({
    selector: 'jhi-email-account-dialog',
    templateUrl: './email-account-dialog.component.html'
})
export class EmailAccountDialogComponent implements OnInit {

    emailAccount: EmailAccount;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private emailAccountService: EmailAccountService,
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
        if (this.emailAccount.id !== undefined) {
            this.subscribeToSaveResponse(
                this.emailAccountService.update(this.emailAccount));
        } else {
            this.subscribeToSaveResponse(
                this.emailAccountService.create(this.emailAccount));
        }
    }

    private subscribeToSaveResponse(result: Observable<EmailAccount>) {
        result.subscribe((res: EmailAccount) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: EmailAccount) {
        this.eventManager.broadcast({ name: 'emailAccountListModification', content: 'OK'});
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
    selector: 'jhi-email-account-popup',
    template: ''
})
export class EmailAccountPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private emailAccountPopupService: EmailAccountPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.emailAccountPopupService
                    .open(EmailAccountDialogComponent as Component, params['id']);
            } else {
                this.emailAccountPopupService
                    .open(EmailAccountDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
