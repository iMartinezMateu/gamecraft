import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TeamUserComponent } from './team-user.component';
import { TeamUserDetailComponent } from './team-user-detail.component';
import { TeamUserPopupComponent } from './team-user-dialog.component';
import { TeamUserDeletePopupComponent } from './team-user-delete-dialog.component';

@Injectable()
export class TeamUserResolvePagingParams implements Resolve<any> {

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

export const teamUserRoute: Routes = [
    {
        path: 'team-user',
        component: TeamUserComponent,
        resolve: {
            'pagingParams': TeamUserResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.teamUser.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'team-user/:id',
        component: TeamUserDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.teamUser.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const teamUserPopupRoute: Routes = [
    {
        path: 'team-user-new',
        component: TeamUserPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.teamUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'team-user/:id/edit',
        component: TeamUserPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.teamUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'team-user/:id/delete',
        component: TeamUserDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.teamUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
