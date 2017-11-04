import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { EmailAccount } from './email-account.model';
import { EmailAccountService } from './email-account.service';

@Component({
    selector: 'jhi-email-account-detail',
    templateUrl: './email-account-detail.component.html'
})
export class EmailAccountDetailComponent implements OnInit, OnDestroy {

    emailAccount: EmailAccount;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private emailAccountService: EmailAccountService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEmailAccounts();
    }

    load(id) {
        this.emailAccountService.find(id).subscribe((emailAccount) => {
            this.emailAccount = emailAccount;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEmailAccounts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'emailAccountListModification',
            (response) => this.load(this.emailAccount.id)
        );
    }
}
