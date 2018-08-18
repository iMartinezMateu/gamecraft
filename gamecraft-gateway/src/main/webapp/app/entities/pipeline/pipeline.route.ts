import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PipelineComponent } from './pipeline.component';
import { PipelineDetailComponent } from './pipeline-detail.component';
import { PipelinePopupComponent } from './pipeline-dialog.component';
import { PipelineDeletePopupComponent } from './pipeline-delete-dialog.component';

@Injectable()
export class PipelineResolvePagingParams implements Resolve<any> {

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

export const pipelineRoute: Routes = [
    {
        path: 'pipeline',
        component: PipelineComponent,
        resolve: {
            'pagingParams': PipelineResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.pipeline.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'pipeline/:id',
        component: PipelineDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.pipeline.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pipelinePopupRoute: Routes = [
    {
        path: 'pipeline-new',
        component: PipelinePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.pipeline.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pipeline/:id/edit',
        component: PipelinePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.pipeline.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pipeline/:id/delete',
        component: PipelineDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gamecraftgatewayApp.pipeline.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
