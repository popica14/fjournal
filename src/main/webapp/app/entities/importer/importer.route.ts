import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IImporter, Importer } from 'app/shared/model/importer.model';
import { ImporterService } from './importer.service';
import { ImporterComponent } from './importer.component';
import { ImporterDetailComponent } from './importer-detail.component';
import { ImporterUpdateComponent } from './importer-update.component';

@Injectable({ providedIn: 'root' })
export class ImporterResolve implements Resolve<IImporter> {
  constructor(private service: ImporterService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IImporter> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((importer: HttpResponse<Importer>) => {
          if (importer.body) {
            return of(importer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Importer());
  }
}

export const importerRoute: Routes = [
  {
    path: '',
    component: ImporterComponent,
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'foodJournalApp.importer.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ImporterDetailComponent,
    resolve: {
      importer: ImporterResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'foodJournalApp.importer.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ImporterUpdateComponent,
    resolve: {
      importer: ImporterResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'foodJournalApp.importer.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ImporterUpdateComponent,
    resolve: {
      importer: ImporterResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'foodJournalApp.importer.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
