import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { IrcBot } from './irc-bot.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class IrcBotService {

    private resourceUrl = '/gamecraftircnotificationmanager/api/irc-bots';
    private resourceSearchUrl = '/gamecraftircnotificationmanager/api/_search/irc-bots';

    constructor(private http: Http) { }

    create(ircBot: IrcBot): Observable<IrcBot> {
        const copy = this.convert(ircBot);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(ircBot: IrcBot): Observable<IrcBot> {
        const copy = this.convert(ircBot);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<IrcBot> {
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
     * Convert a returned JSON object to IrcBot.
     */
    private convertItemFromServer(json: any): IrcBot {
        const entity: IrcBot = Object.assign(new IrcBot(), json);
        return entity;
    }

    /**
     * Convert a IrcBot to a JSON which can be sent to the server.
     */
    private convert(ircBot: IrcBot): IrcBot {
        const copy: IrcBot = Object.assign({}, ircBot);
        return copy;
    }
}
