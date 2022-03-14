import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Route, Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";
import {HttpRequestsService} from "../../../services/http-requests/http-requests.service";
import {GlobalConst} from "../../../globals/GlobalConst";
import {PopupService} from "../../common-group/compose-message/popup.service";
import {ResultCode} from "../../../globals/result-code.enum";


export class SendForm {
  constructor(public guid: string, public password: string, public confirmPassword: string) {
  }
}
//http://localhost:8065/main-page/recoverpass/588c335b-0a57-4110-8c4a-a4a509ae4e32-1.0

@Component({
  selector: 'app-recovery-pass',
  templateUrl: './recovery-pass.component.html',
  styleUrls: ['./recovery-pass.component.css']
})
export class RecoveryPassComponent implements OnInit {
//recoverpass?guid

  userForm: FormGroup;
  sendForm = new SendForm('', '', '');


  constructor(private route: ActivatedRoute,
              private router: Router,
              private fb: FormBuilder,
              private httpRequestService: HttpRequestsService,
              private popupService: PopupService) {
    this.createForm();
  }

  ngOnInit() {
    this.sendForm.guid = this.route.snapshot.paramMap.get('guid');
  }


  createForm() {
    this.userForm = this.fb.group({
      guid: this.sendForm.guid,
      password: '',
      confirmPassword: ''

    });
  }


  submit() {

    this.sendForm.password = this.userForm.value.password;
    this.sendForm.confirmPassword = this.userForm.value.confirmPassword;

    this.httpRequestService.changePass(this.sendForm).subscribe(
      data => this.ok(data),
      error => this.bad(error)
    )
  }

  ok(data: any) {
    if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS)&&(data.resultCode === ResultCode.SUCCESSFULL))
        this.popupService.getPopupAndRedirect(this.router, this.route, 'main-page',null ,`Пароль изменен.`, ``, 5);
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
