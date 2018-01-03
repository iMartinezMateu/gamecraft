import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TelegramBotComponent } from './telegram-bot.component';
import { TelegramBotDetailComponent } from './telegram-bot-detail.component';
import { TelegramBotPopupComponent } from './telegram-bot-dialog.component';
import { TelegramBotDeletePopupComponent } from './telegram-bot-delete-dialog.component';

@Injectable()
export class TelegramBotResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const telegramBotRoute: Routes = [
    {
        path: 'telegram-bot',
        component: TelegramBotComponent,
        resolve: {
            'pagingParams': TelegramBotResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.telegramBot.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'telegram-bot/:id',
        component: TelegramBotDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.telegramBot.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const telegramBotPopupRoute: Routes = [
    {
        path: 'telegram-bot-new',
        component: TelegramBotPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.telegramBot.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'telegram-bot/:id/edit',
        component: TelegramBotPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.telegramBot.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'telegram-bot/:id/delete',
        component: TelegramBotDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.telegramBot.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
