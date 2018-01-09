import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TwitterBot } from './twitter-bot.model';
import { TwitterBotPopupService } from './twitter-bot-popup.service';
import { TwitterBotService } from './twitter-bot.service';

@Component({
    selector: 'jhi-twitter-bot-delete-dialog',
    templateUrl: './twitter-bot-delete-dialog.component.html'
})
export class TwitterBotDeleteDialogComponent {

    twitterBot: TwitterBot;

    constructor(
        private twitterBotService: TwitterBotService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.twitterBotService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'twitterBotListModification',
                content: 'Deleted an twitterBot'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-twitter-bot-delete-popup',
    template: ''
})
export class TwitterBotDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private twitterBotPopupService: TwitterBotPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.twitterBotPopupService
                .open(TwitterBotDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
