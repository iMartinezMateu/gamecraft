import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { TeamProject } from './team-project.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TeamProjectService {

    private resourceUrl = '/gamecraftteam/api/team-projects';
    private resourceSearchUrl = '/gamecraftteam/api/_search/team-projects';

    constructor(private http: Http) { }

    create(teamProject: TeamProject): Observable<TeamProject> {
        const copy = this.convert(teamProject);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(teamProject: TeamProject): Observable<TeamProject> {
        const copy = this.convert(teamProject);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<TeamProject> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to TeamProject.
     */
    private convertItemFromServer(json: any): TeamProject {
        const entity: TeamProject = Object.assign(new TeamProject(), json);
        return entity;
    }

    /**
     * Convert a TeamProject to a JSON which can be sent to the server.
     */
    private convert(teamProject: TeamProject): TeamProject {
        const copy: TeamProject = Object.assign({}, teamProject);
        return copy;
    }
}
