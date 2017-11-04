import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { EmailAccountComponent } from './email-account.component';
import { EmailAccountDetailComponent } from './email-account-detail.component';
import { EmailAccountPopupComponent } from './email-account-dialog.component';
import { EmailAccountDeletePopupComponent } from './email-account-delete-dialog.component';

@Injectable()
export class EmailAccountResolvePagingParams implements Resolve<any> {

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

export const emailAccountRoute: Routes = [
    {
        path: 'email-account',
        component: EmailAccountComponent,
        resolve: {
            'pagingParams': EmailAccountResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.emailAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'email-account/:id',
        component: EmailAccountDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.emailAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const emailAccountPopupRoute: Routes = [
    {
        path: 'email-account-new',
        component: EmailAccountPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.emailAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'email-account/:id/edit',
        component: EmailAccountPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.emailAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'email-account/:id/delete',
        component: EmailAccountDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.emailAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
