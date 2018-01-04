import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { IrcBotComponent } from './irc-bot.component';
import { IrcBotDetailComponent } from './irc-bot-detail.component';
import { IrcBotPopupComponent } from './irc-bot-dialog.component';
import { IrcBotDeletePopupComponent } from './irc-bot-delete-dialog.component';

@Injectable()
export class IrcBotResolvePagingParams implements Resolve<any> {

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

export const ircBotRoute: Routes = [
    {
        path: 'irc-bot',
        component: IrcBotComponent,
        resolve: {
            'pagingParams': IrcBotResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.ircBot.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'irc-bot/:id',
        component: IrcBotDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.ircBot.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ircBotPopupRoute: Routes = [
    {
        path: 'irc-bot-new',
        component: IrcBotPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.ircBot.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'irc-bot/:id/edit',
        component: IrcBotPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.ircBot.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'irc-bot/:id/delete',
        component: IrcBotDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.ircBot.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
