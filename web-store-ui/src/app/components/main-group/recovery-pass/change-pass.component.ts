import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MatStepper} from '@angular/material';
import {ActivatedRoute, Router} from '@angular/router';
import {SendForm} from './recovery-pass.component';
import {HttpRequestsService} from '../../../services/http-requests/http-requests.service';
import {PopupService} from '../../common-group/compose-message/popup.service';
import {AuthorizeConst} from '../../../globals/AuthorizeConst';
import {GlobalConst} from '../../../globals/GlobalConst';
import {ResultCode} from '../../../globals/result-code.enum';

/**
 * @title Stepper overview
 */
@Component({
  selector: 'change-pass-component',
  templateUrl: './change-pass.component.html'
})
export class ChangePassComponent {
  isLinear = true;
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  userErrorMatchingPass: string = 'Пароль не совпадает';
  userErrorPass: string = '';
  sendForm = new SendForm('', '', '');

  constructor(private fb: FormBuilder,
              private  httpRequestsService: HttpRequestsService,
              private router: Router,
              private route: ActivatedRoute,
              private popupService: PopupService) {
    this.firstFormGroup = this.fb.group({
      'password': ['', [Validators.required, Validators.minLength(AuthorizeConst.minPassLength)]]
    });
    this.secondFormGroup = this.fb.group({
      'guid': '',
      'newPassword': ['', [Validators.required, Validators.minLength(AuthorizeConst.minPassLength)]],
      'confirmPassword': ['', [Validators.required, Validators.minLength(AuthorizeConst.minPassLength)]]
    }, {validator: this.matchingPasswords('newPassword', 'confirmPassword')});


  }

  ngOnInit() {
  }


  matchingPasswords(value1: string, value2: string) {
    return (group: FormGroup): { [key: string]: boolean } => {
      let v1 = group.controls[value1];
      let v2 = group.controls[value2];
      return v1.value !== v2.value ? {mismatchedPasswords: true} : null;
    };
  }

  nextStep(stepper: MatStepper) {
    //matStepperNext
    stepper.next();
  }

  checkPass(stepper: MatStepper) {
    this.httpRequestsService.checkPass(this.firstFormGroup.controls['password'].value).subscribe(
      data => this.ok(data, stepper),
      error => this.bad(error)
    );
  }

  changePass(stepper: MatStepper) {

    console.log(this.secondFormGroup.value.guid);
    console.log(this.sendForm);

    this.sendForm.password = this.secondFormGroup.value.newPassword;
    this.sendForm.confirmPassword = this.secondFormGroup.value.confirmPassword;

    this.httpRequestsService.changePass(this.sendForm).subscribe(
      data => this.ok(data, stepper),
      error => this.bad(error)
    );
  }


  ok(data: any, stepper: MatStepper) {

    this.userErrorPass = '';
    if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS)) {
      if ((data.resultCode === ResultCode.SUCCESSFULL) || (data.resultCode === ResultCode.CONTINUE)) {

        if (data.resultCode === ResultCode.CONTINUE) {
          this.sendForm.guid = data.payload.guid;
          stepper.next();

        }
        if (data.resultCode === ResultCode.SUCCESSFULL) {
          this.getRedirect();

        }
        // stepper.next();
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

  getRedirect() {

    /*  this.router.navigate([{
        outlets: {
          popup: ['compose', {
            details: 'test',
            message: 'test'
          }],
          primary:'userprofile'
        }
            }],  {relativeTo: this.route.parent.parent});
  */
    this.popupService.getPopupAndRedirect(this.router, this.route.parent, 'main-page', 'userprofile', 'пароль изменен', '', 4);
  }

}
