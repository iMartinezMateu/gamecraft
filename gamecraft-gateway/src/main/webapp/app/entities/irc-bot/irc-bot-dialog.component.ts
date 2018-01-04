import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IrcBot } from './irc-bot.model';
import { IrcBotPopupService } from './irc-bot-popup.service';
import { IrcBotService } from './irc-bot.service';

@Component({
    selector: 'jhi-irc-bot-dialog',
    templateUrl: './irc-bot-dialog.component.html'
})
export class IrcBotDialogComponent implements OnInit {

    ircBot: IrcBot;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private ircBotService: IrcBotService,
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
        if (this.ircBot.id !== undefined) {
            this.subscribeToSaveResponse(
                this.ircBotService.update(this.ircBot));
        } else {
            this.subscribeToSaveResponse(
                this.ircBotService.create(this.ircBot));
        }
    }

    private subscribeToSaveResponse(result: Observable<IrcBot>) {
        result.subscribe((res: IrcBot) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: IrcBot) {
        this.eventManager.broadcast({ name: 'ircBotListModification', content: 'OK'});
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
    selector: 'jhi-irc-bot-popup',
    template: ''
})
export class IrcBotPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ircBotPopupService: IrcBotPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.ircBotPopupService
                    .open(IrcBotDialogComponent as Component, params['id']);
            } else {
                this.ircBotPopupService
                    .open(IrcBotDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
