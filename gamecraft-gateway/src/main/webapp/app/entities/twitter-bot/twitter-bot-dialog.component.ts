import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TwitterBot } from './twitter-bot.model';
import { TwitterBotPopupService } from './twitter-bot-popup.service';
import { TwitterBotService } from './twitter-bot.service';

@Component({
    selector: 'jhi-twitter-bot-dialog',
    templateUrl: './twitter-bot-dialog.component.html'
})
export class TwitterBotDialogComponent implements OnInit {

    twitterBot: TwitterBot;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private twitterBotService: TwitterBotService,
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
        if (this.twitterBot.id !== undefined) {
            this.subscribeToSaveResponse(
                this.twitterBotService.update(this.twitterBot));
        } else {
            this.subscribeToSaveResponse(
                this.twitterBotService.create(this.twitterBot));
        }
    }

    private subscribeToSaveResponse(result: Observable<TwitterBot>) {
        result.subscribe((res: TwitterBot) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TwitterBot) {
        this.eventManager.broadcast({ name: 'twitterBotListModification', content: 'OK'});
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
    selector: 'jhi-twitter-bot-popup',
    template: ''
})
export class TwitterBotPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private twitterBotPopupService: TwitterBotPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.twitterBotPopupService
                    .open(TwitterBotDialogComponent as Component, params['id']);
            } else {
                this.twitterBotPopupService
                    .open(TwitterBotDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
