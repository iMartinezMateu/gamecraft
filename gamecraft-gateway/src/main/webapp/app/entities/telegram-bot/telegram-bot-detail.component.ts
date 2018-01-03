import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TelegramBot } from './telegram-bot.model';
import { TelegramBotService } from './telegram-bot.service';

@Component({
    selector: 'jhi-telegram-bot-detail',
    templateUrl: './telegram-bot-detail.component.html'
})
export class TelegramBotDetailComponent implements OnInit, OnDestroy {

    telegramBot: TelegramBot;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private telegramBotService: TelegramBotService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTelegramBots();
    }

    load(id) {
        this.telegramBotService.find(id).subscribe((telegramBot) => {
            this.telegramBot = telegramBot;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTelegramBots() {
        this.eventSubscriber = this.eventManager.subscribe(
            'telegramBotListModification',
            (response) => this.load(this.telegramBot.id)
        );
    }
}
