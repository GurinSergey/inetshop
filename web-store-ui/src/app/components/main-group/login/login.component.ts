import { LOCAL_STORAGE } from '@ng-toolkit/universal';
import {Component, OnInit, Inject} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {FormGroup, Validators, FormBuilder} from '@angular/forms';
import {AuthorizeConst} from '../../../globals/AuthorizeConst';
import {HttpRequestsService} from '../../../services/http-requests/http-requests.service';
import {GlobalConst} from '../../../globals/GlobalConst';
import {AppLocalStorageService} from "../../common-group/app-local-storage/app-local-storage.service";
import {AppStorage} from "../../../shared/for-storage/universal.inject";
import {RouterConst} from "../../../globals/RouterConst";
import {FavoriteService} from "../../../services/favorite/favorite.service";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  deactivate = false;
  myForm: FormGroup;
  userNameError = 'Имя пользователя не соответствует шаблону';
  userPassError: string = 'Длинна пароля меньше ' + AuthorizeConst.minPassLength + ' символов';

  authorized = true;
  userServerError: string;
  initials: string;

  submitAttempt = false;

  constructor(@Inject(LOCAL_STORAGE) private localStorage: any, private httpRequest: HttpRequestsService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              @Inject(AppStorage) private cookies: Storage,
              private location: Location,
              private fBuilder: FormBuilder,
              private appLocalStorage: AppLocalStorageService,
              private favoriteService: FavoriteService) {
    this.myForm = fBuilder.group({
      'userName': ['', [Validators.pattern(AuthorizeConst.emailPattern)]],
      'userPass': ['', [Validators.minLength(AuthorizeConst.minPassLength)]],
    });
  }

  private setToken(securityToken: string) {
    GlobalConst.TOKEN = securityToken;
    this.cookies.setItem(GlobalConst.COOKIES_TOKEN, securityToken);
    //this.cookies.setItem(GlobalConst.COOKIES_TOKEN, securityToken);
  }

  private _setSession(authResult, profile) {
    // Save session data and update login status subject
    this.localStorage.setItem('profile', JSON.stringify(profile));
  }

  ok(data: any) {
    this.authorized = true;
    this.initials = data.payload.personName;
    this.localStorage.setItem('initials', this.initials);
    this._setSession(data.result, data.payload);
    this.setToken(data.securityToken);
    this.appLocalStorage.customSync();
    //this.router.navigate(['/' + RouterConst.MAINPAGE],{relativeTo: this.activatedRoute});
//console.log(this.activatedRoute);
    /*VDN Favorite list sync*/
    this.favoriteService.syncFavoriteList();
  }

  bad(data: any) {
    console.log('login error', data);

    this.authorized = false;
    this.userServerError = /*data*/ 'Пользователь или пароль не верен';
    this.myForm.controls.userPass.reset();
  }

  submit() {
    this.deactivate = !this.deactivate;

    this.submitAttempt = true;

    if (this.myForm.invalid) {
      this.deactivate = !this.deactivate;
      return;
    }

    const credentials = {
      userName: this.myForm.controls['userName'].value,
      password: this.myForm.controls['userPass'].value
    };

    this.httpRequest.login(credentials).subscribe(
      (data: any) => {
        this.deactivate = !this.deactivate;
        if (data.result === GlobalConst.INTERNAL_SERVER_ERROR || data.status === 500) {
          console.log('login error', 'Internal Server Error');
        } else if (data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) {
          this.ok(data);
        } else if (data.result === GlobalConst.RESPONSE_RESULT_UNAUTHORIZED) {
          this.bad(data.payload);
        } else {
          this.bad(data.result);
        }
      },
      error => {
        this.deactivate = !this.deactivate;
        this.bad(error);
      });
  }

  ngOnInit() {
  }

  getRegistry() {

   // console.log(this.activatedRoute.snapshot);
    this.router.navigate(['registry'], {relativeTo: this.activatedRoute});

    /* this.router.navigate(['registry'], {
       queryParams: {
         returnUrl:urlprimary
       },
       relativeTo: this.activatedRoute
     })*/

  }

  resetForm() {
    this.userServerError = '';
    this.myForm.controls['userName'].setValue('');
    this.myForm.controls['userPass'].setValue('');
    this.submitAttempt = false;
  }
}
