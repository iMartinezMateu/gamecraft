import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { SlackAccount } from './slack-account.model';
import { SlackAccountService } from './slack-account.service';

@Component({
    selector: 'jhi-slack-account-detail',
    templateUrl: './slack-account-detail.component.html'
})
export class SlackAccountDetailComponent implements OnInit, OnDestroy {

    slackAccount: SlackAccount;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private slackAccountService: SlackAccountService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSlackAccounts();
    }

    load(id) {
        this.slackAccountService.find(id).subscribe((slackAccount) => {
            this.slackAccount = slackAccount;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSlackAccounts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'slackAccountListModification',
            (response) => this.load(this.slackAccount.id)
        );
    }
}
