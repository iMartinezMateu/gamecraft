import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { GroupsComponent } from './groups.component';
import { GroupsDetailComponent } from './groups-detail.component';
import { GroupsPopupComponent } from './groups-dialog.component';
import { GroupsDeletePopupComponent } from './groups-delete-dialog.component';

@Injectable()
export class GroupsResolvePagingParams implements Resolve<any> {

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

export const groupsRoute: Routes = [
    {
        path: 'groups',
        component: GroupsComponent,
        resolve: {
            'pagingParams': GroupsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.groups.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'groups/:id',
        component: GroupsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.groups.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const groupsPopupRoute: Routes = [
    {
        path: 'groups-new',
        component: GroupsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.groups.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'groups/:id/edit',
        component: GroupsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.groups.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'groups/:id/delete',
        component: GroupsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.groups.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
