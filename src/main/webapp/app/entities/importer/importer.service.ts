import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IImporter } from 'app/shared/model/importer.model';

type EntityResponseType = HttpResponse<IImporter>;
type EntityArrayResponseType = HttpResponse<IImporter[]>;

@Injectable({ providedIn: 'root' })
export class ImporterService {
  public resourceUrl = SERVER_API_URL + 'api/importers';

  constructor(protected http: HttpClient) {}

  create(importer: IImporter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(importer);
    return this.http
      .post<IImporter>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(importer: IImporter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(importer);
    return this.http
      .put<IImporter>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IImporter>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IImporter[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(importer: IImporter): IImporter {
    const copy: IImporter = Object.assign({}, importer, {
      importDate: importer.importDate && importer.importDate.isValid() ? importer.importDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.importDate = res.body.importDate ? moment(res.body.importDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((importer: IImporter) => {
        importer.importDate = importer.importDate ? moment(importer.importDate) : undefined;
      });
    }
    return res;
  }
}
