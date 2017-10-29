import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TeamProjectComponent } from './team-project.component';
import { TeamProjectDetailComponent } from './team-project-detail.component';
import { TeamProjectPopupComponent } from './team-project-dialog.component';
import { TeamProjectDeletePopupComponent } from './team-project-delete-dialog.component';

@Injectable()
export class TeamProjectResolvePagingParams implements Resolve<any> {

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

export const teamProjectRoute: Routes = [
    {
        path: 'team-project',
        component: TeamProjectComponent,
        resolve: {
            'pagingParams': TeamProjectResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.teamProject.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'team-project/:id',
        component: TeamProjectDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.teamProject.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const teamProjectPopupRoute: Routes = [
    {
        path: 'team-project-new',
        component: TeamProjectPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.teamProject.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'team-project/:id/edit',
        component: TeamProjectPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.teamProject.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'team-project/:id/delete',
        component: TeamProjectDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.teamProject.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
