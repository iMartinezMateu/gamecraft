import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { TwitterBot } from './twitter-bot.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TwitterBotService {

    private resourceUrl = '/gamecrafttwitternotificationmanager/api/twitter-bots';
    private resourceSearchUrl = '/gamecrafttwitternotificationmanager/api/_search/twitter-bots';

    constructor(private http: Http) { }

    create(twitterBot: TwitterBot): Observable<TwitterBot> {
        const copy = this.convert(twitterBot);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(twitterBot: TwitterBot): Observable<TwitterBot> {
        const copy = this.convert(twitterBot);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<TwitterBot> {
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
     * Convert a returned JSON object to TwitterBot.
     */
    private convertItemFromServer(json: any): TwitterBot {
        const entity: TwitterBot = Object.assign(new TwitterBot(), json);
        return entity;
    }

    /**
     * Convert a TwitterBot to a JSON which can be sent to the server.
     */
    private convert(twitterBot: TwitterBot): TwitterBot {
        const copy: TwitterBot = Object.assign({}, twitterBot);
        return copy;
    }
}
