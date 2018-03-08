import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SonarInstanceComponent } from './sonar-instance.component';
import { SonarInstanceDetailComponent } from './sonar-instance-detail.component';
import { SonarInstancePopupComponent } from './sonar-instance-dialog.component';
import { SonarInstanceDeletePopupComponent } from './sonar-instance-delete-dialog.component';

@Injectable()
export class SonarInstanceResolvePagingParams implements Resolve<any> {

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

export const sonarInstanceRoute: Routes = [
    {
        path: 'sonar-instance',
        component: SonarInstanceComponent,
        resolve: {
            'pagingParams': SonarInstanceResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.sonarInstance.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sonar-instance/:id',
        component: SonarInstanceDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.sonarInstance.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sonarInstancePopupRoute: Routes = [
    {
        path: 'sonar-instance-new',
        component: SonarInstancePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.sonarInstance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sonar-instance/:id/edit',
        component: SonarInstancePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.sonarInstance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sonar-instance/:id/delete',
        component: SonarInstanceDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.sonarInstance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
