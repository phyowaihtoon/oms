import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRepositoryHeader, getRepositoryIdentifier } from '../repository.model';

export type EntityResponseType = HttpResponse<IRepositoryHeader>;
export type EntityArrayResponseType = HttpResponse<IRepositoryHeader[]>;

@Injectable({ providedIn: 'root' })
export class RepositoryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/repository');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(repository: IRepositoryHeader): Observable<EntityResponseType> {
    return this.http.post<IRepositoryHeader>(this.resourceUrl, repository, { observe: 'response' });
  }

  update(repository: IRepositoryHeader): Observable<EntityResponseType> {
    return this.http.put<IRepositoryHeader>(`${this.resourceUrl}/${getRepositoryIdentifier(repository) as number}`, repository, {
      observe: 'response',
    });
  }

  partialUpdate(repository: IRepositoryHeader): Observable<EntityResponseType> {
    return this.http.patch<IRepositoryHeader>(`${this.resourceUrl}/${getRepositoryIdentifier(repository) as number}`, repository, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRepositoryHeader>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRepositoryHeader[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRepositoryHeaderToCollectionIfMissing(
    repositoryHeaderCollection: IRepositoryHeader[],
    ...categoriesToCheck: (IRepositoryHeader | null | undefined)[]
  ): IRepositoryHeader[] {
    const repositoryHeader: IRepositoryHeader[] = categoriesToCheck.filter(isPresent);
    if (repositoryHeader.length > 0) {
      const collectionIdentifiers = repositoryHeaderCollection.map(categoryItem => getRepositoryIdentifier(categoryItem)!);
      const categoriesToAdd = repositoryHeader.filter(repositoryHeaderItem => {
        const repositoryHeaderIdentifier = getRepositoryIdentifier(repositoryHeaderItem);
        if (repositoryHeaderIdentifier == null || collectionIdentifiers.includes(repositoryHeaderIdentifier)) {
          return false;
        }
        collectionIdentifiers.push(repositoryHeaderIdentifier);
        return true;
      });
      return [...categoriesToAdd, ...repositoryHeaderCollection];
    }
    return repositoryHeaderCollection;
  }
}