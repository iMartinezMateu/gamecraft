import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TwitterBotComponent } from './twitter-bot.component';
import { TwitterBotDetailComponent } from './twitter-bot-detail.component';
import { TwitterBotPopupComponent } from './twitter-bot-dialog.component';
import { TwitterBotDeletePopupComponent } from './twitter-bot-delete-dialog.component';

@Injectable()
export class TwitterBotResolvePagingParams implements Resolve<any> {

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

export const twitterBotRoute: Routes = [
    {
        path: 'twitter-bot',
        component: TwitterBotComponent,
        resolve: {
            'pagingParams': TwitterBotResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.twitterBot.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'twitter-bot/:id',
        component: TwitterBotDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.twitterBot.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const twitterBotPopupRoute: Routes = [
    {
        path: 'twitter-bot-new',
        component: TwitterBotPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.twitterBot.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'twitter-bot/:id/edit',
        component: TwitterBotPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.twitterBot.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'twitter-bot/:id/delete',
        component: TwitterBotDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.twitterBot.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
