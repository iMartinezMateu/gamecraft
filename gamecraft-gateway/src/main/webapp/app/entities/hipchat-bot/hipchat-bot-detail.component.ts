import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { HipchatBot } from './hipchat-bot.model';
import { HipchatBotService } from './hipchat-bot.service';

@Component({
    selector: 'jhi-hipchat-bot-detail',
    templateUrl: './hipchat-bot-detail.component.html'
})
export class HipchatBotDetailComponent implements OnInit, OnDestroy {

    hipchatBot: HipchatBot;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private hipchatBotService: HipchatBotService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInHipchatBots();
    }

    load(id) {
        this.hipchatBotService.find(id).subscribe((hipchatBot) => {
            this.hipchatBot = hipchatBot;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInHipchatBots() {
        this.eventSubscriber = this.eventManager.subscribe(
            'hipchatBotListModification',
            (response) => this.load(this.hipchatBot.id)
        );
    }
}
