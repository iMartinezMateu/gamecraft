import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SlackAccount } from './slack-account.model';
import { SlackAccountPopupService } from './slack-account-popup.service';
import { SlackAccountService } from './slack-account.service';

@Component({
    selector: 'jhi-slack-account-dialog',
    templateUrl: './slack-account-dialog.component.html'
})
export class SlackAccountDialogComponent implements OnInit {

    slackAccount: SlackAccount;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private slackAccountService: SlackAccountService,
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
        if (this.slackAccount.id !== undefined) {
            this.subscribeToSaveResponse(
                this.slackAccountService.update(this.slackAccount));
        } else {
            this.subscribeToSaveResponse(
                this.slackAccountService.create(this.slackAccount));
        }
    }

    private subscribeToSaveResponse(result: Observable<SlackAccount>) {
        result.subscribe((res: SlackAccount) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: SlackAccount) {
        this.eventManager.broadcast({ name: 'slackAccountListModification', content: 'OK'});
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
    selector: 'jhi-slack-account-popup',
    template: ''
})
export class SlackAccountPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private slackAccountPopupService: SlackAccountPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.slackAccountPopupService
                    .open(SlackAccountDialogComponent as Component, params['id']);
            } else {
                this.slackAccountPopupService
                    .open(SlackAccountDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
