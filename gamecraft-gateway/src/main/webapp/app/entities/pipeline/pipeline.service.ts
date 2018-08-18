import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Pipeline } from './pipeline.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PipelineService {

    private resourceUrl = '/gamecraftpipelineexecutor/api/pipelines';
    private resourceSearchUrl = '/gamecraftpipelineexecutor/api/_search/pipelines';

    constructor(private http: Http) { }

    create(pipeline: Pipeline): Observable<Pipeline> {
        const copy = this.convert(pipeline);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(pipeline: Pipeline): Observable<Pipeline> {
        const copy = this.convert(pipeline);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Pipeline> {
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
     * Convert a returned JSON object to Pipeline.
     */
    private convertItemFromServer(json: any): Pipeline {
        const entity: Pipeline = Object.assign(new Pipeline(), json);
        return entity;
    }

    /**
     * Convert a Pipeline to a JSON which can be sent to the server.
     */
    private convert(pipeline: Pipeline): Pipeline {
        const copy: Pipeline = Object.assign({}, pipeline);
        return copy;
    }
}
