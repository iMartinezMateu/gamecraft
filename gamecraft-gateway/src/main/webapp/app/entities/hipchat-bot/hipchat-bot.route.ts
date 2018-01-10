import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { HipchatBotComponent } from './hipchat-bot.component';
import { HipchatBotDetailComponent } from './hipchat-bot-detail.component';
import { HipchatBotPopupComponent } from './hipchat-bot-dialog.component';
import { HipchatBotDeletePopupComponent } from './hipchat-bot-delete-dialog.component';

@Injectable()
export class HipchatBotResolvePagingParams implements Resolve<any> {

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

export const hipchatBotRoute: Routes = [
    {
        path: 'hipchat-bot',
        component: HipchatBotComponent,
        resolve: {
            'pagingParams': HipchatBotResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.hipchatBot.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'hipchat-bot/:id',
        component: HipchatBotDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.hipchatBot.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const hipchatBotPopupRoute: Routes = [
    {
        path: 'hipchat-bot-new',
        component: HipchatBotPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.hipchatBot.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'hipchat-bot/:id/edit',
        component: HipchatBotPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.hipchatBot.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'hipchat-bot/:id/delete',
        component: HipchatBotDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.hipchatBot.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
