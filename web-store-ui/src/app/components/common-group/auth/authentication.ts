import { LOCAL_STORAGE } from '@ng-toolkit/universal';
import {CanActivate, Router} from '@angular/router';
import {Inject, Injectable, PLATFORM_ID} from '@angular/core';
import {UserProfile} from './profile.model';
import {GlobalConst} from '../../../globals/GlobalConst';
import {RouterConst} from '../../../globals/RouterConst';
import {AppStorage} from "../../../shared/for-storage/universal.inject";
import {isPlatformBrowser} from "@angular/common";



@Injectable()
export class Authentication implements CanActivate {

  userProfile: UserProfile = new UserProfile();


  constructor(@Inject(LOCAL_STORAGE) private localStorage: any, @Inject(AppStorage) private cookies: Storage,
              @Inject(PLATFORM_ID) private platformId: Object,
              private router: Router) {
  }

  canActivate() {
    if (((GlobalConst.TOKEN  === undefined) || GlobalConst.TOKEN.trim() === '')
      && ((this.cookies.getItem(GlobalConst.COOKIES_TOKEN) === undefined) || this.cookies.getItem(GlobalConst.COOKIES_TOKEN).trim() === '')) {
      this.router.navigate(['/' + RouterConst.MAINPAGE + '/' + RouterConst.LOGIN]);
    }
    return true;
  }

  get authenticated(): boolean {
    if (((GlobalConst.TOKEN === undefined) || GlobalConst.TOKEN.trim() === '')
      && ((this.cookies.getItem(GlobalConst.COOKIES_TOKEN) === undefined ) || this.cookies.getItem(GlobalConst.COOKIES_TOKEN).trim() === '')) {
      return false;
    }
    return true;
  }

  get getProfile(): UserProfile {
    // If authenticated, set local profile property and update login status subject
    if ((isPlatformBrowser(this.platformId)) &&  (this.authenticated) && (this.localStorage)) {
      if (JSON.parse(this.localStorage.getItem('profile'))) {
        this.userProfile = JSON.parse(this.localStorage.getItem('profile'));
      }
      else {
        //LAO костыль для удаляения печеньки если она каким-то чудом сохранилась по пути /main-page
        //так же костыль на то что без указания пути, печенька не удляется
        //Пока не ясно почему путь '/' меняется.
        //ссылка на проблему https://github.com/7leads/ngx-cookie-service/issues/5
        /****/

        this.cookies.removeItem(GlobalConst.COOKIES_TOKEN);
        this.cookies.clear();
      //  this.cookies.removeItem(GlobalConst.COOKIES_TOKEN, '/main-page');
        this.canActivate();
      }
    }

    return this.userProfile;
  }
}

@Injectable()
export class Authentication4Login implements CanActivate {

  constructor(@Inject(AppStorage) private cookies: Storage,
              private router: Router) {
  }

  canActivate() {

   if (((GlobalConst.TOKEN === undefined) || GlobalConst.TOKEN.trim() === '')
      && ((this.cookies.getItem(GlobalConst.COOKIES_TOKEN )=== undefined) || this.cookies.getItem(GlobalConst.COOKIES_TOKEN).trim() === '')) {
      return true;
    }
    this.router.navigate(['/' + RouterConst.MAINPAGE]);
  }
}
