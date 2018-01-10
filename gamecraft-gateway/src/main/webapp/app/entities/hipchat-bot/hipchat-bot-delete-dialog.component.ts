import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { HipchatBot } from './hipchat-bot.model';
import { HipchatBotPopupService } from './hipchat-bot-popup.service';
import { HipchatBotService } from './hipchat-bot.service';

@Component({
    selector: 'jhi-hipchat-bot-delete-dialog',
    templateUrl: './hipchat-bot-delete-dialog.component.html'
})
export class HipchatBotDeleteDialogComponent {

    hipchatBot: HipchatBot;

    constructor(
        private hipchatBotService: HipchatBotService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.hipchatBotService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'hipchatBotListModification',
                content: 'Deleted an hipchatBot'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-hipchat-bot-delete-popup',
    template: ''
})
export class HipchatBotDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private hipchatBotPopupService: HipchatBotPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.hipchatBotPopupService
                .open(HipchatBotDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
