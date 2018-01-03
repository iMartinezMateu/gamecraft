import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TelegramBot } from './telegram-bot.model';
import { TelegramBotPopupService } from './telegram-bot-popup.service';
import { TelegramBotService } from './telegram-bot.service';

@Component({
    selector: 'jhi-telegram-bot-delete-dialog',
    templateUrl: './telegram-bot-delete-dialog.component.html'
})
export class TelegramBotDeleteDialogComponent {

    telegramBot: TelegramBot;

    constructor(
        private telegramBotService: TelegramBotService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.telegramBotService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'telegramBotListModification',
                content: 'Deleted an telegramBot'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-telegram-bot-delete-popup',
    template: ''
})
export class TelegramBotDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private telegramBotPopupService: TelegramBotPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.telegramBotPopupService
                .open(TelegramBotDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
