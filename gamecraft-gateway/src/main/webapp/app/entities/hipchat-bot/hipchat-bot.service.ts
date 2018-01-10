import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { HipchatBot } from './hipchat-bot.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class HipchatBotService {

    private resourceUrl = '/gamecrafthipchatnotificationmanager/api/hipchat-bots';
    private resourceSearchUrl = '/gamecrafthipchatnotificationmanager/api/_search/hipchat-bots';

    constructor(private http: Http) { }

    create(hipchatBot: HipchatBot): Observable<HipchatBot> {
        const copy = this.convert(hipchatBot);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(hipchatBot: HipchatBot): Observable<HipchatBot> {
        const copy = this.convert(hipchatBot);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<HipchatBot> {
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
     * Convert a returned JSON object to HipchatBot.
     */
    private convertItemFromServer(json: any): HipchatBot {
        const entity: HipchatBot = Object.assign(new HipchatBot(), json);
        return entity;
    }

    /**
     * Convert a HipchatBot to a JSON which can be sent to the server.
     */
    private convert(hipchatBot: HipchatBot): HipchatBot {
        const copy: HipchatBot = Object.assign({}, hipchatBot);
        return copy;
    }
}
