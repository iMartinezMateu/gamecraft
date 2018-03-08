import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { GroupsUserComponent } from './groups-user.component';
import { GroupsUserDetailComponent } from './groups-user-detail.component';
import { GroupsUserPopupComponent } from './groups-user-dialog.component';
import { GroupsUserDeletePopupComponent } from './groups-user-delete-dialog.component';

@Injectable()
export class GroupsUserResolvePagingParams implements Resolve<any> {

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

export const groupsUserRoute: Routes = [
    {
        path: 'groups-user',
        component: GroupsUserComponent,
        resolve: {
            'pagingParams': GroupsUserResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.groupsUser.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'groups-user/:id',
        component: GroupsUserDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.groupsUser.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const groupsUserPopupRoute: Routes = [
    {
        path: 'groups-user-new',
        component: GroupsUserPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.groupsUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'groups-user/:id/edit',
        component: GroupsUserPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.groupsUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'groups-user/:id/delete',
        component: GroupsUserDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.groupsUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
