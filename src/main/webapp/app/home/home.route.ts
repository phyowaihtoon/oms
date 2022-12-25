import { Route } from '@angular/router';
import { UserAuthorityResolveService } from 'app/login/user-authority-resolve.service';

import { HomeComponent } from './home.component';

export const HOME_ROUTE: Route = {
  path: '',
  component: HomeComponent,
  data: {
    pageTitle: 'home.title',
  },
};
