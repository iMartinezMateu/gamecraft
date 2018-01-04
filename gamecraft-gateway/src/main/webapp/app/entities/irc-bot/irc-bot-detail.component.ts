import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { IrcBot } from './irc-bot.model';
import { IrcBotService } from './irc-bot.service';

@Component({
    selector: 'jhi-irc-bot-detail',
    templateUrl: './irc-bot-detail.component.html'
})
export class IrcBotDetailComponent implements OnInit, OnDestroy {

    ircBot: IrcBot;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private ircBotService: IrcBotService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInIrcBots();
    }

    load(id) {
        this.ircBotService.find(id).subscribe((ircBot) => {
            this.ircBot = ircBot;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInIrcBots() {
        this.eventSubscriber = this.eventManager.subscribe(
            'ircBotListModification',
            (response) => this.load(this.ircBot.id)
        );
    }
}
