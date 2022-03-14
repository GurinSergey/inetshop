import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthorizeConst} from "../../../globals/AuthorizeConst";
import {HttpRequestsService} from "../../../services/http-requests/http-requests.service";
import {ActivatedRoute, Router} from "@angular/router";
import {GlobalConst} from "../../../globals/GlobalConst";
import {PopupService} from "../../common-group/compose-message/popup.service";
import {ResultCode} from "../../../globals/result-code.enum";

@Component({
  selector: 'app-forgot-pass',
  templateUrl: './forgot-pass.component.html',
  styleUrls: ['./forgot-pass.component.css']
})
export class ForgotPassComponent implements OnInit {
  userForm: FormGroup;
  email: string;

  constructor(private fb: FormBuilder,
              private httpRequestService: HttpRequestsService,
              private route: ActivatedRoute,
              private router: Router,
              private popupService:PopupService) {

    this.createForm();
  }

  ngOnInit() {

  }

  createForm() {
    this.userForm = this.fb.group({
      userName: ''
    });
  }

  submit() {
    this.email = this.userForm.value.userName;

    this.httpRequestService.forgotPass(this.email).subscribe(
      data => this.ok(data),
      error => this.bad(error)
    )
  }

  ok(data: any) {
    if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS)&&(data.resultCode === ResultCode.SUCCESSFULL))
      this.popupService.getPopup(this.router, this.route, 'main-page',`На вашу почту отправлена ссылка для изменение пароля`, ``, 5);
    else
      this.bad(data);
  }

  bad(errData: any) {

    let errorText: string = '';

    if (errData.result === GlobalConst.RESPONSE_RESULT_UNAUTHORIZED)
      errorText = 'Невозможно получить доступ к ресурсу, попробуйте очистить cookies в браузере';
    else if (errData.result === GlobalConst.INTERNAL_SERVER_ERROR || errData.status === 500)
      errorText = 'Internal Server Error';
    else if (errData.resultCode === ResultCode.NOT_FOUND)
      errorText = 'Пользователь в списке восстановления не найден';
    else if (errData.resultCode === ResultCode.BAD_DATA)
      errorText = errData.payload;

    this.popupService.getPopup(this.router, this.route, 'main-page',
      `Ошибка: `,
      `  ${errorText}`, 5);


  }


}


