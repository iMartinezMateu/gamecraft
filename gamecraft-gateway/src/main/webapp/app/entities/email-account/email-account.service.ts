import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { EmailAccount } from './email-account.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class EmailAccountService {

    private resourceUrl = '/gamecraftemailnotificationmanager/api/email-accounts';
    private resourceSearchUrl = '/gamecraftemailnotificationmanager/api/_search/email-accounts';

    constructor(private http: Http) { }

    create(emailAccount: EmailAccount): Observable<EmailAccount> {
        const copy = this.convert(emailAccount);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(emailAccount: EmailAccount): Observable<EmailAccount> {
        const copy = this.convert(emailAccount);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<EmailAccount> {
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
     * Convert a returned JSON object to EmailAccount.
     */
    private convertItemFromServer(json: any): EmailAccount {
        const entity: EmailAccount = Object.assign(new EmailAccount(), json);
        return entity;
    }

    /**
     * Convert a EmailAccount to a JSON which can be sent to the server.
     */
    private convert(emailAccount: EmailAccount): EmailAccount {
        const copy: EmailAccount = Object.assign({}, emailAccount);
        return copy;
    }
}
