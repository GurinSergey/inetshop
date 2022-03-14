import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
import {HttpRequestsService} from "../../../../services/http-requests/http-requests.service";
import {GlobalConst} from "../../../../globals/GlobalConst";
import {ResultCode} from "../../../../globals/result-code.enum";


@Component({
  selector: 'app-mail-confirm',
  templateUrl: './mail-confirm.component.html',
  styleUrls: ['./mail-confirm.component.css']
})
export class MailConfirmComponent implements OnInit {
  private guid: string;
  public confirm: Boolean = false;
  public errorText = "Что-то пошло не так";
  public success = "Почта успешно подтверждена!";
  public visible = false;

  constructor(private activatedRoute: ActivatedRoute,
              private httpRequestsService: HttpRequestsService) {
  }

  //<h3>Пожалуйста подтвердите почту, перейдя по ссылке  <a href="">подтверждение</a></h3>http://localhost:8065/main-page/confirm_mail?guid=ab00fa50-9435-45ca-845f-bece409e6348-funkisl
  ngOnInit() {


    this.activatedRoute.queryParams.subscribe((params: Params) => {
      this.guid = params['guid'];




      this.httpRequestsService.mailConfirm(this.guid).subscribe(
        data => this.ok(data),
        error => this.bad(error)
      );
    });
  }


  ok(data: any) {
    this.visible = true;
    if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) && (data.resultCode === ResultCode.SUCCESSFULL)) {
      this.confirm = true;
    } else {
      this.bad(data);
    }

  }

  bad(errData: any) {
    this.confirm = false;
    this.visible = true;
    if (errData.result === GlobalConst.RESPONSE_RESULT_UNAUTHORIZED) {
      this.errorText = 'Невозможно получить доступ к ресурсу, попробуйте очистить cookies в браузере';
    } else if (errData.result === GlobalConst.INTERNAL_SERVER_ERROR || errData.status === 500) {
      this.errorText = 'Сервер не доступен, повторите попытку позже';
    } else if (errData.resultCode === ResultCode.NOT_FOUND) {
      this.errorText = 'Пользователь не найден либо почта не нуждается в подтверждении';
    }
    else if (errData.resultCode === ResultCode.BAD_DATA) {
      this.errorText = 'Запрос не прошел валидацию';
    }

  }
}
