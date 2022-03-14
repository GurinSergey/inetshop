import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {HttpRequestsService} from "../../../services/http-requests/http-requests.service";
import {GlobalConst} from "../../../globals/GlobalConst";
import {FormGroup, Validators, FormBuilder, FormControl} from '@angular/forms';
import {AuthorizeConst} from '../../../globals/AuthorizeConst';
import {ResultCode} from "../../../globals/result-code.enum";
import {PopupService} from "../../common-group/compose-message/popup.service";
import {RoutingStateService} from "../../../routing-state.service";

@Component({
  selector: 'app-registry',
  templateUrl: './registry.component.html',
  styleUrls: ['./registry.component.css']
})
export class RegistryComponent implements OnInit {
  deactivate: boolean = false;
  myForm: FormGroup;

  previousRoute: string;
  haveError = false;
  errorRegistry: string;

  /*Web-client block of errors*/
  userEmailError: string = "Почтовый адрес не соответствует формату";
  userNameError: string = "Имя обязательно к заполнению";
  userPassErrorLength: string = "Длинна пароля меньше " + AuthorizeConst.minPassLength + " символов";
  userErrorMatchingPass: string = "Пароль не совпадает";

  /*Server block of errors*/
  userEmailServerError: string;
  userNameServerError: string;
  userPassErrorServerLength: string;
  userErrorServerMatchingPass: string;

  constructor(private httpRequest: HttpRequestsService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private routingState: RoutingStateService,
              private location: Location,
              private builder: FormBuilder,
              private popupService: PopupService) {
    this.myForm = builder.group({
        'userEmail': ['', [Validators.required, Validators.pattern(AuthorizeConst.emailPattern)]],
        'userName': ['', Validators.required],
        'userPass': ['', [Validators.required, Validators.minLength(AuthorizeConst.minPassLength)]],
        'userPassConf': ['', [Validators.required, Validators.minLength(AuthorizeConst.minPassLength)]]
      }, {validator: this.matchingPasswords('userPass', 'userPassConf')}
    );
  }

  matchingPasswords(value1: string, value2: string) {
    return (group: FormGroup): { [key: string]: boolean } => {
      let v1 = group.controls[value1];
      let v2 = group.controls[value2];

      return v1.value !== v2.value ? {mismatchedPasswords: true} : null;
    }
  }

  submit() {
    this.deactivate = !this.deactivate;

    const credentials = {
      userName: this.myForm.controls['userEmail'].value,
      password: this.myForm.controls['userPass'].value,
      confirmPassword: this.myForm.controls['userPassConf'].value,
      personName: this.myForm.controls['userName'].value
    };

    this.httpRequest.registry(credentials).subscribe(
      data => this.handleData_register(data),
      error => this.handleError_registry(error));

    this.deactivate = !this.deactivate;
  }

  ngOnInit() {
    this.previousRoute = this.routingState.getPreviousUrl();
    console.log(this.previousRoute);

  }

  goBack(): void {
    //  this.location.back();
    this.popupService.getPopupAndRedirect(this.router, this.activatedRoute, 'main-page', this.previousRoute,
      'test', `test:`, 5);
  }


  handleData_register(data: any) {
    this.myForm.controls.userPass.reset();
    this.myForm.controls.userPassConf.reset();


    if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) && (data.resultCode === ResultCode.SUCCESSFULL)) {
      this.popupService.getPopupAndRedirect(this.router, this.activatedRoute, 'main-page', this.previousRoute,
        'Регистрация прошла успешно!', `На вашу почту  ${data.payload.userName} отправлено письмо для подтверждения учетных данных`, 5);

    } else {
      this.handleError_registry(data);
    }
  }

  handleError_registry(errData: any) {
    let errorText: string = '';

    if (errData.result === GlobalConst.RESPONSE_RESULT_UNAUTHORIZED)
      errorText = 'Невозможно получить доступ к ресурсу, попробуйте очистить cookies в браузере';
    else if (errData.result === GlobalConst.INTERNAL_SERVER_ERROR || errData.status === 500)
      errorText = 'Internal Server Error';
    else if (errData.resultCode === ResultCode.BAD_DATA) {
      console.log(ResultCode.BAD_DATA);
      this.haveError = true;
      this.parseError(errData.payload);
      errorText = 'некорректные данные';
    }

    this.popupService.getPopup(this.router, this.activatedRoute, 'main-page',
      'Произошла ошибка: ', errorText, 5);
    console.log('login error', errData);
  }


  private parseError(errData: any) {
    for (let errField of errData.fieldErrors) {
      switch (errField.field) {
        case 'userName': {
          this.userEmailServerError = errField.message + ' ';
          break;
        }
        case 'personName': {
          this.userNameServerError = errField.message + ' ';
          break;
        }
        case 'password': {
          this.userPassErrorServerLength = errField.message + ' ';
          break;
        }
        case 'confirmPassword': {
          this.userErrorServerMatchingPass = errField.message + ' ';
          break;
        }
        default: {
          //statements;
          break;
        }
      }
    }
  }

}
