import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { SonarInstance } from './sonar-instance.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SonarInstanceService {

    private resourceUrl = '/gamecraftsonarmanager/api/sonar-instances';
    private resourceSearchUrl = '/gamecraftsonarmanager/api/_search/sonar-instances';

    constructor(private http: Http) { }

    create(sonarInstance: SonarInstance): Observable<SonarInstance> {
        const copy = this.convert(sonarInstance);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(sonarInstance: SonarInstance): Observable<SonarInstance> {
        const copy = this.convert(sonarInstance);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<SonarInstance> {
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
     * Convert a returned JSON object to SonarInstance.
     */
    private convertItemFromServer(json: any): SonarInstance {
        const entity: SonarInstance = Object.assign(new SonarInstance(), json);
        return entity;
    }

    /**
     * Convert a SonarInstance to a JSON which can be sent to the server.
     */
    private convert(sonarInstance: SonarInstance): SonarInstance {
        const copy: SonarInstance = Object.assign({}, sonarInstance);
        return copy;
    }
}
