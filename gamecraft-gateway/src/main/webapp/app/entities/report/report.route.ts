import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ReportComponent } from './report.component';
import { ReportDetailComponent } from './report-detail.component';
import { ReportPopupComponent } from './report-dialog.component';
import { ReportDeletePopupComponent } from './report-delete-dialog.component';

@Injectable()
export class ReportResolvePagingParams implements Resolve<any> {

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

export const reportRoute: Routes = [
    {
        path: 'report',
        component: ReportComponent,
        resolve: {
            'pagingParams': ReportResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.report.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'report/:id',
        component: ReportDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.report.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reportPopupRoute: Routes = [
    {
        path: 'report-new',
        component: ReportPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.report.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'report/:id/edit',
        component: ReportPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.report.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'report/:id/delete',
        component: ReportDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.report.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
