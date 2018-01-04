import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IrcBot } from './irc-bot.model';
import { IrcBotPopupService } from './irc-bot-popup.service';
import { IrcBotService } from './irc-bot.service';

@Component({
    selector: 'jhi-irc-bot-delete-dialog',
    templateUrl: './irc-bot-delete-dialog.component.html'
})
export class IrcBotDeleteDialogComponent {

    ircBot: IrcBot;

    constructor(
        private ircBotService: IrcBotService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.ircBotService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'ircBotListModification',
                content: 'Deleted an ircBot'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-irc-bot-delete-popup',
    template: ''
})
export class IrcBotDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ircBotPopupService: IrcBotPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.ircBotPopupService
                .open(IrcBotDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
