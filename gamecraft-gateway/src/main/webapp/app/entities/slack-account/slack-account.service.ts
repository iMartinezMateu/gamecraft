import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { SlackAccount } from './slack-account.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SlackAccountService {

    private resourceUrl = '/gamecraftslacknotificationmanager/api/slack-accounts';
    private resourceSearchUrl = '/gamecraftslacknotificationmanager/api/_search/slack-accounts';

    constructor(private http: Http) { }

    create(slackAccount: SlackAccount): Observable<SlackAccount> {
        const copy = this.convert(slackAccount);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(slackAccount: SlackAccount): Observable<SlackAccount> {
        const copy = this.convert(slackAccount);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<SlackAccount> {
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
     * Convert a returned JSON object to SlackAccount.
     */
    private convertItemFromServer(json: any): SlackAccount {
        const entity: SlackAccount = Object.assign(new SlackAccount(), json);
        return entity;
    }

    /**
     * Convert a SlackAccount to a JSON which can be sent to the server.
     */
    private convert(slackAccount: SlackAccount): SlackAccount {
        const copy: SlackAccount = Object.assign({}, slackAccount);
        return copy;
    }
}
