import { LOCAL_STORAGE } from '@ng-toolkit/universal';
import {Component, OnInit, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {MatStepper} from '@angular/material';
import {HttpRequestsService} from "../../../services/http-requests/http-requests.service";
import {PopupService} from "../../common-group/compose-message/popup.service";
import {AuthorizeConst} from "../../../globals/AuthorizeConst";
import {GlobalConst} from "../../../globals/GlobalConst";
import {ResultCode} from "../../../globals/result-code.enum";
import {AppStorage} from "../../../shared/for-storage/universal.inject";



export class SendForm {
  constructor(public guid: string, public newEmail: string, public guidEmail: string, public password: string) {
  }
}

@Component({
  selector: 'app-change-mail',
  templateUrl: './change-mail.component.html',
  styleUrls: ['./change-mail.component.css']
})
export class ChangeMailComponent implements OnInit {

  isLinear = true;
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  lastFormGroup: FormGroup;
  userErrorMatchingPass: string = 'Пароль не совпадает';
  userErrorPass: string = '';
  sendForm = new SendForm('', '', '', '');

  constructor(private fb: FormBuilder,
              private  httpRequestsService: HttpRequestsService,
              private router: Router,
              private route: ActivatedRoute,
              @Inject(LOCAL_STORAGE) private localStorage: any,
              @Inject(AppStorage) private cookies: Storage,
              private popupService: PopupService) {
    this.firstFormGroup = this.fb.group({
      'password': ['', [Validators.required, Validators.minLength(AuthorizeConst.minPassLength)]]
    });
    this.secondFormGroup = this.fb.group({
      'guid': '',
      'newEmail': ['', [Validators.required, Validators.pattern(AuthorizeConst.emailPattern)]]
    });
    this.lastFormGroup = this.fb.group({
      'guidEmail': ''
    });

  }

  ngOnInit() {
  }

  nextStep(stepper: MatStepper) {
    //matStepperNext
    stepper.next();
  }

  checkPass(stepper: MatStepper) {
    this.sendForm.password = this.firstFormGroup.controls['password'].value;
    this.httpRequestsService.checkPass(this.firstFormGroup.controls['password'].value).subscribe(
      data => this.ok(data, stepper),
      error => this.bad(error)
    );
  }

  checkEmail(stepper: MatStepper) {

    this.httpRequestsService.checkEmail(this.secondFormGroup.controls['newEmail'].value).subscribe(
      data => this.ok(data, stepper),
      error => this.bad(error)
    );
  }

  changeEmail(stepper: MatStepper) {

    /* console.log(this.secondFormGroup.value.guid);
     console.log(this.sendForm);*/

    //  this.sendForm.password = this.secondFormGroup.value.newemail;
    this.sendForm.guidEmail = this.lastFormGroup.controls['guidEmail'].value;
    console.log(this.sendForm);
    this.httpRequestsService.changeEmail(this.sendForm).subscribe(
      data => this.ok(data, stepper),
      error => this.bad(error)
    );
  }


  ok(data: any, stepper: MatStepper) {

    this.userErrorPass = '';
    if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS)) {

      if (data.resultCode === ResultCode.CONTINUE) {
        this.sendForm.guid = data.payload.guid;

        stepper.next();

      }
      else if (data.resultCode === ResultCode.COMPLETE) {
        this.sendForm.newEmail = data.payload.email;
        stepper.next();
      }

      else if (data.resultCode === ResultCode.SUCCESSFULL) {
        this.logout();
        this.getRedirect('Ваша почта успешно изменена, необходимо войти на сайт с новыйм email');

      }
      else
        this.bad(data);
    }
    else
      this.bad(data);
  }

  bad(errData: any) {

    if (errData.result === GlobalConst.RESPONSE_RESULT_UNAUTHORIZED)
      this.userErrorPass = 'Пароль не совпадает';
    else if (errData.result === GlobalConst.INTERNAL_SERVER_ERROR || errData.status === 500)
      this.userErrorPass = 'Internal Server Error';
    else if (errData.resultCode === ResultCode.NOT_FOUND) {
      this.userErrorPass = 'Пользователь не найден';
    }
    else if (errData.resultCode === ResultCode.BAD_DATA) {
      this.userErrorPass = errData.payload;
    }
    else
      this.userErrorPass = 'Произошла непредвиденная ошибка, повторите попытку позже либи обратитесь в техподдержку';
    console.log(this.userErrorPass);
    this.firstFormGroup.controls['password'].setErrors({
      'someOtherLogin': true
    });

    // router : Router ,route:ActivatedRoute,mainpage:string,title: string, text: string, timer: number
    this.popupService.getPopup(this.router, this.route, 'main-page', this.userErrorPass, '', 3);


  }

  getRedirect(text) {
    this.popupService.getPopupAndRedirect(this.router, this.route.parent, 'main-page', 'main-page', text, '', 4);
  }

  logout() {
    this.httpRequestsService.logout().subscribe((data: any) => {
      console.log('this is logout', data);
    });
    GlobalConst.TOKEN = undefined;
    this.cookies.removeItem(GlobalConst.COOKIES_TOKEN);
    localStorage.removeItem('initials');
    localStorage.removeItem('profile');
    // this.router.navigate(['/' + RouterConst.LOGIN]);

  }


}
