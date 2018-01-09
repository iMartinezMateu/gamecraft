import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TwitterBot } from './twitter-bot.model';
import { TwitterBotService } from './twitter-bot.service';

@Component({
    selector: 'jhi-twitter-bot-detail',
    templateUrl: './twitter-bot-detail.component.html'
})
export class TwitterBotDetailComponent implements OnInit, OnDestroy {

    twitterBot: TwitterBot;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private twitterBotService: TwitterBotService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTwitterBots();
    }

    load(id) {
        this.twitterBotService.find(id).subscribe((twitterBot) => {
            this.twitterBot = twitterBot;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTwitterBots() {
        this.eventSubscriber = this.eventManager.subscribe(
            'twitterBotListModification',
            (response) => this.load(this.twitterBot.id)
        );
    }
}
