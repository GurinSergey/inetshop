import {Component, OnInit, ViewChild} from '@angular/core';
import {FormGroup, FormControl, Validators, FormBuilder} from '@angular/forms';
import {OrderService} from '../../../services/order/order.service';
import {UserOrder} from '../../../models/user-order';
import {PopupService} from '../../common-group/compose-message/popup.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ResultCode} from '../../../globals/result-code.enum';
import {GlobalConst} from '../../../globals/GlobalConst';
import {AppLocalStorageService} from '../../common-group/app-local-storage/app-local-storage.service';
import {Authentication} from '../../common-group/auth/authentication';
import {ProductService} from '../../../services/product/product.service';
import {ObservableCommonService} from '../../../services/observable/observable-common.service';
import {BasketService} from '../../../services/basket/basket.service';
import {HttpRequestsService} from '../../../services/http-requests/http-requests.service';
import {Basket} from '../../../models/basket';
import {PopupStyle} from '../../../globals/popup-style.enum';
import {catchError, map} from 'rxjs/internal/operators';
import {BranchNova, CityNova} from '../../../models/city-nova.model';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css'],
  providers: [BasketService, ProductService, HttpRequestsService, PopupService]
})

export class CheckoutComponent implements OnInit {
  ourAddress = 'г. Киев, ул. Соломенская, 5';
  deliveryKind = 0;
  callBack = true;
  paymentMethod = 0;

  auth = false;

  items: Basket;
  totalCnt = 0;
  totalSum = 0;

  cityList: CityNova[];
  branchNova: BranchNova[] = [];

  isLoadingCities = false;
  isLoadingBranch = false;

  nameError = 'Не указано или не верно указано ФИО';
  emailError = 'Не указан или не верно указан Email';
  phoneError = 'Не указан или не верно указан телефон';
  cityError = 'Не указан или не верно указан город';
  addressError = 'Не указан или не верно указан адресс';

  private phoneMask = GlobalConst.PHONE_MASK;

  public checkoutForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private basketService: BasketService,
              private orderService: OrderService,
              private router: Router,
              private route: ActivatedRoute,
              private popupService: PopupService,
              private appLocalStorageService: AppLocalStorageService,
              private authentication: Authentication,
              private observableCommonService: ObservableCommonService,
              private httpRequestsService: HttpRequestsService) {
    this.createFormGroup();
    this.getAllCity();
  }

  ngOnInit() {
    this.route.data
      .subscribe((data: { basket: Basket }) => {
        this.items = data.basket;
        this.totalCnt = this.basketService.getBasketCnt();
        this.totalSum = this.calcTotalSum();
      });
  }

  submit() {
    if (this.checkoutForm.valid) {
      this.addOrder();
    }
  }

  private addOrder() {
    const order = new UserOrder(this.checkoutForm.get('userName').value,
      this.checkoutForm.get('userPhone').value,
      this.checkoutForm.get('userEmail').value,
      this.deliveryKind,
      (this.deliveryKind === 0 ? this.ourAddress :
        this.cityList.filter(city => city.cityRef === this.checkoutForm.get('deliveryCity').value)[0].cityDescription
        + ', ' +
        this.checkoutForm.get('deliveryAddress').value),
      this.callBack,
      this.checkoutForm.get('orderNote').value,
      this.paymentMethod
    );

    this.httpRequestsService.addOrder(order).subscribe((data: any) => {
      if (data.result !== GlobalConst.RESPONSE_RESULT_SUCCESS) {
        this.popupService.getPopup(this.router, this.route, 'main-page',
          'Внимание!', data.payload, 50, PopupStyle.DANGER);
        return;
      }

      this.popupService.getPopupAndRedirect(this.router, this.route, 'main-page', 'main-page',
        'Спасибо, Ваш заказ №' + data.payload + ' принят.', 'В ближайшее время с Вами свяжется наш менеджер.', 50, 'alert-success');
      this.checkoutForm.reset();

      this.syncBasket();
    });
  }

  private syncBasket() {
    this.items.productList.forEach(item => {
      this.observableCommonService.deleteFromBasket(item.id);
    });

    this.basketService.removeBasket();
    this.appLocalStorageService.remove('ComplexBasketStorage');

    this.observableCommonService.changeCntBasket(0);
  }

  private calcTotalSum() {
    let total = 0;
    this.items.productList.forEach(item => {
      total += item.price * item.cnt;
    });
    return total;
  }

  private createFormGroup() {
    let name = null;
    let email = null;

    if (this.authentication.authenticated) {
      this.auth = true;

      name = this.authentication.userProfile.personName;
      email = this.authentication.userProfile.userName;
    }

    this.checkoutForm = this.formBuilder.group({
      'userName': [name, [Validators.required]],
      'userPhone': [null, [Validators.required, Validators.pattern('[(][0-9]{3}[)][ ][0-9]{3}[-][0-9]{4}')]],
      'userEmail': [email, [Validators.required, Validators.pattern('[a-zA-Z0-9_.]+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}')]],
      'orderNote': [null, []],
      'isUserAuth': [this.auth]
    });
  }

  private addFormControl() {
    this.checkoutForm.addControl('deliveryCity', new FormControl(null, Validators.required));
    this.checkoutForm.addControl('deliveryAddress', new FormControl(null, Validators.required));
  }

  private removeFormControl() {
    this.checkoutForm.removeControl('deliveryCity');
    this.checkoutForm.removeControl('deliveryAddress');

    this.branchNova = [];
  }

  onSelectionChange() {
    !this.checkoutForm.contains('deliveryCity') ? this.addFormControl() : this.removeFormControl();
  }

  getAllCity() {
    this.isLoadingCities = true;

    this.httpRequestsService.getAllNovaCity().pipe(
      map((data: any) => {
        return data;
      }),
      catchError(error => {
        throw (error);
      })
    ).subscribe(result => {
      setTimeout(() => {
        this.cityList = result.payload;
        this.isLoadingCities = false;
      }, 1000);
    });
  }

  getBranchNovaByRef(cityRef: any) {
    this.isLoadingBranch = true;

    this.httpRequestsService.getAllByCity(cityRef).pipe(
      map((data: any) => {
        return data;
      }),
      catchError(error => {
        throw (error);
      })
    ).subscribe(result => {
      setTimeout(() => {
        this.branchNova = result.payload;
        this.isLoadingBranch = false;
      }, 1000);
    });
  }
}
