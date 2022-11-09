import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { IUserAuthority } from './userauthority.model';
import { UserAuthorityService } from './userauthority.service';

@Injectable({
  providedIn: 'root',
})
export class UserAuthorityResolveService implements Resolve<IUserAuthority> {
  constructor(protected userAuthorityService: UserAuthorityService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IUserAuthority> | Observable<never> {
    const menuCode = route.data['menuCode'];
    const userAuthority = this.userAuthorityService.retrieveUserAuthority();
    if (userAuthority?.menuGroups) {
      const menuGroups = userAuthority.menuGroups;
      for (const menuGroup of menuGroups) {
        const subMenus = menuGroup.subMenuItems;
        const selectedMenu = subMenus?.find(item => item.menuItem?.menuCode === menuCode);
        if (selectedMenu) {
          userAuthority.activeMenu = selectedMenu;
          return of(userAuthority);
        }
      }
    }

    return EMPTY;
  }
}
