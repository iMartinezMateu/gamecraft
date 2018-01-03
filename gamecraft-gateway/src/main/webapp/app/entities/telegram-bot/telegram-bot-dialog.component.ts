import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TelegramBot } from './telegram-bot.model';
import { TelegramBotPopupService } from './telegram-bot-popup.service';
import { TelegramBotService } from './telegram-bot.service';

@Component({
    selector: 'jhi-telegram-bot-dialog',
    templateUrl: './telegram-bot-dialog.component.html'
})
export class TelegramBotDialogComponent implements OnInit {

    telegramBot: TelegramBot;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private telegramBotService: TelegramBotService,
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
        if (this.telegramBot.id !== undefined) {
            this.subscribeToSaveResponse(
                this.telegramBotService.update(this.telegramBot));
        } else {
            this.subscribeToSaveResponse(
                this.telegramBotService.create(this.telegramBot));
        }
    }

    private subscribeToSaveResponse(result: Observable<TelegramBot>) {
        result.subscribe((res: TelegramBot) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TelegramBot) {
        this.eventManager.broadcast({ name: 'telegramBotListModification', content: 'OK'});
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
    selector: 'jhi-telegram-bot-popup',
    template: ''
})
export class TelegramBotPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private telegramBotPopupService: TelegramBotPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.telegramBotPopupService
                    .open(TelegramBotDialogComponent as Component, params['id']);
            } else {
                this.telegramBotPopupService
                    .open(TelegramBotDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
