import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Groups } from './groups.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class GroupsService {

    private resourceUrl = '/gamecraftgroup/api/groups';
    private resourceSearchUrl = '/gamecraftgroup/api/_search/groups';

    constructor(private http: Http) { }

    create(groups: Groups): Observable<Groups> {
        const copy = this.convert(groups);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(groups: Groups): Observable<Groups> {
        const copy = this.convert(groups);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Groups> {
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
     * Convert a returned JSON object to Groups.
     */
    private convertItemFromServer(json: any): Groups {
        const entity: Groups = Object.assign(new Groups(), json);
        return entity;
    }

    /**
     * Convert a Groups to a JSON which can be sent to the server.
     */
    private convert(groups: Groups): Groups {
        const copy: Groups = Object.assign({}, groups);
        return copy;
    }
}
