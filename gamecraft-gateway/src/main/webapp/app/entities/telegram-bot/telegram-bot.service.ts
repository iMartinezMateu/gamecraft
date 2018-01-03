import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { TelegramBot } from './telegram-bot.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TelegramBotService {

    private resourceUrl = '/gamecrafttelegramnotificationmanager/api/telegram-bots';
    private resourceSearchUrl = '/gamecrafttelegramnotificationmanager/api/_search/telegram-bots';

    constructor(private http: Http) { }

    create(telegramBot: TelegramBot): Observable<TelegramBot> {
        const copy = this.convert(telegramBot);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(telegramBot: TelegramBot): Observable<TelegramBot> {
        const copy = this.convert(telegramBot);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<TelegramBot> {
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
     * Convert a returned JSON object to TelegramBot.
     */
    private convertItemFromServer(json: any): TelegramBot {
        const entity: TelegramBot = Object.assign(new TelegramBot(), json);
        return entity;
    }

    /**
     * Convert a TelegramBot to a JSON which can be sent to the server.
     */
    private convert(telegramBot: TelegramBot): TelegramBot {
        const copy: TelegramBot = Object.assign({}, telegramBot);
        return copy;
    }
}
