import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SlackAccountComponent } from './slack-account.component';
import { SlackAccountDetailComponent } from './slack-account-detail.component';
import { SlackAccountPopupComponent } from './slack-account-dialog.component';
import { SlackAccountDeletePopupComponent } from './slack-account-delete-dialog.component';

@Injectable()
export class SlackAccountResolvePagingParams implements Resolve<any> {

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

export const slackAccountRoute: Routes = [
    {
        path: 'slack-account',
        component: SlackAccountComponent,
        resolve: {
            'pagingParams': SlackAccountResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.slackAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'slack-account/:id',
        component: SlackAccountDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.slackAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const slackAccountPopupRoute: Routes = [
    {
        path: 'slack-account-new',
        component: SlackAccountPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.slackAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'slack-account/:id/edit',
        component: SlackAccountPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.slackAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'slack-account/:id/delete',
        component: SlackAccountDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.slackAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
