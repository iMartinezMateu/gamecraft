import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { HipchatBot } from './hipchat-bot.model';
import { HipchatBotPopupService } from './hipchat-bot-popup.service';
import { HipchatBotService } from './hipchat-bot.service';

@Component({
    selector: 'jhi-hipchat-bot-dialog',
    templateUrl: './hipchat-bot-dialog.component.html'
})
export class HipchatBotDialogComponent implements OnInit {

    hipchatBot: HipchatBot;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private hipchatBotService: HipchatBotService,
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
        if (this.hipchatBot.id !== undefined) {
            this.subscribeToSaveResponse(
                this.hipchatBotService.update(this.hipchatBot));
        } else {
            this.subscribeToSaveResponse(
                this.hipchatBotService.create(this.hipchatBot));
        }
    }

    private subscribeToSaveResponse(result: Observable<HipchatBot>) {
        result.subscribe((res: HipchatBot) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: HipchatBot) {
        this.eventManager.broadcast({ name: 'hipchatBotListModification', content: 'OK'});
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
    selector: 'jhi-hipchat-bot-popup',
    template: ''
})
export class HipchatBotPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private hipchatBotPopupService: HipchatBotPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.hipchatBotPopupService
                    .open(HipchatBotDialogComponent as Component, params['id']);
            } else {
                this.hipchatBotPopupService
                    .open(HipchatBotDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
