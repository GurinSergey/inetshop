import {AfterViewChecked, AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {DatePipe, Location} from "@angular/common";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";
import {User} from "../../../models/user.model";
import {HttpRequestsService} from "../../../services/http-requests/http-requests.service";
import {SettingsService} from "../../../settings-service.service";
import {PopupService} from "../../common-group/compose-message/popup.service";
import {GlobalConst} from "../../../globals/GlobalConst";
import {ResultCode} from "../../../globals/result-code.enum";
import {PopupStyle} from "../../../globals/popup-style.enum";
import {Subscription} from "rxjs/Rx";
import {RouterConst} from "../../../globals/RouterConst";


@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit{
  panelOpenState: boolean = false;
  userForm: FormGroup;
  user: User;
  deactivate: boolean = false;
  btnTitle: string = "подтвердить почту";
  stext: string = "Почта не подтверждена, при нажатии вам будет отправлено письмо на почту для ее подтверждения";
  tooltipText: string = this.stext;
  bColor = "accent";
  datePipe = new DatePipe(this.settingsService.getLanguage());
  confirm = false;

  personalPanelOpen:boolean = false;
  @ViewChild('personalMenu') personalMenu: ElementRef;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private httpRequestsService: HttpRequestsService,
              private location: Location,
              private fb: FormBuilder,
              private settingsService: SettingsService,
              private popupService: PopupService) {
    this.createForm();
  }

  ngOnInit() {
    this.httpRequestsService.getProfile().subscribe((data: any) => {
      this.user = data.payload;
      if (this.user.mailConfirm) {
        this.confirm = true;
      }
      this.rebuildForm();
    });

    this.route.children.map(item => {
      if(item.snapshot.routeConfig.path == RouterConst.USERFAVORITE)
        this.openItemPersonalMenu(3);
    });
  }

  openItemPersonalMenu(index){
    this.personalPanelOpen = true;
    let item = this.personalMenu.nativeElement.getElementsByClassName('nav-link');
    for (var i = 0; i < item.length; i++)
      item[i].classList.remove('active');

    item[index - 1].classList.add('active');
  }

  createForm() {
    this.userForm = this.fb.group({
      userName: [{value: '', disabled: true}],
      personName: '',
      birthdate: '',
      sex: '',
      mailConfirm: '',
      state: ''
    });
  }

  rebuildForm() {
    this.userForm.reset({
      userName: this.user.userName,
      personName: this.user.personName,
      birthdate: this.user.birthdate, //this.datePipe.transform(this.user.birthdate, 'yyyy-MM-dd'),
      sex: this.user.sex,
      mailConfirm: this.user.mailConfirm,
      state: this.user.state
    });

  }

  prepareSaveUser(): User {
    const formModel = this.userForm.value;
    const saveUser: User = {
      id: this.user.id,
      userName: formModel.userName as string,
      personName: formModel.personName as string,
      birthdate: this.datePipe.transform(formModel.birthdate, 'yyyy-MM-dd'),
      sex: formModel.sex as string,
      mailConfirm: null,
      state: null,
      userRoles: null
    };

    return saveUser;
  }

  onSubmit() {
    let user_ = this.prepareSaveUser();
    this.httpRequestsService.updateUserProfile(user_).subscribe((data: any) => {
      this.user = data.payload;
      this.rebuildForm();
    });

  }

  hadlerForm() {
    this.deactivate = !this.deactivate;
    this.httpRequestsService.sendConfirmEmail().subscribe(
      (data: any) => {
        if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) && (data.resultCode === ResultCode.SUCCESSFULL)) {
          this.btnTitle = this.btnTitle == "подтвердить почту" ? "письмо отправлено" : "подтвердить почту";
          this.popupService.getPopup(this.router, this.route, 'main-page', 'Письмо отправлено', "Ожидайте письмо в течении <b>5-10</b> минут", 10);
          this.bColor = "primary";
          this.runTimer();
        }
        else {
          this.deactivate = !this.deactivate;
          this.popupService.getPopup(this.router, this.route, 'main-page', 'Письмо не отправлено', "Повторите попытку позже", 10, PopupStyle.DANGER);
        }
      },
      error => {
        this.deactivate = !this.deactivate;
        this.popupService.getPopup(this.router, this.route, 'main-page', 'Письмо не отправлено', "Повторите попытку позже", 10, PopupStyle.DANGER);
      }
    );
  }

  cancel() {
    this.rebuildForm();
    this.hadlerForm();
  }

  runTimer() {
    setTimeout(() => {
      this.deactivate = !this.deactivate;
      this.btnTitle = "подтвердить почту";
      this.tooltipText = this.stext;
      this.bColor = "accent";
    }, 30000);
  }


}

