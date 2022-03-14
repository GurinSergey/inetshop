import {Component, OnDestroy, OnInit} from '@angular/core';
import {AppLocalStorageService} from '../../common-group/app-local-storage/app-local-storage.service';
import {PopupService} from '../../common-group/compose-message/popup.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ObservableCommonService} from '../../../services/observable/observable-common.service';
import {BasketService} from '../../../services/basket/basket.service';
import {Basket} from '../../../models/basket';
import {Subscription} from 'rxjs';


@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.css'],
  providers: [BasketService, PopupService]
})

export class BasketComponent implements OnInit, OnDestroy {
  deactivate = false;

  items: Basket;
  totalCnt = 0;
  totalSum = 0;

  basketSub: Subscription;

  constructor(private basketService: BasketService,
              private router: Router,
              private route: ActivatedRoute,
              private popupService: PopupService,
              private appLocalStorageService: AppLocalStorageService,
              private observableCommonService: ObservableCommonService) {
    this.basketSub = observableCommonService.anyShowBasket$.subscribe(
      eventVal => {
        if (eventVal) {
          this.getItems();
          this.getTotal();
        }
      });
  }

  ngOnInit() {
  }

  getTotal() {
    this.getBasketCnt();
    this.totalSum = this.basketService.getBasketSum();
  }

  getBasketCnt() {
    this.totalCnt = this.basketService.getBasketCnt();
    this.observableCommonService.changeCntBasket(this.totalCnt);
  }

  getItems() {
    this.items = this.basketService.getBasketData();
  }

  private timeOut() {
    this.deactivate = !this.deactivate;

    setTimeout(() => {
      this.deactivate = !this.deactivate;
    }, 1000);
  }

  changeCnt(id: number, cnt?: number) {
    this.timeOut();

    this.basketService.resetProductCnt(id, !cnt ? 1 : cnt);
    this.getItems();
    this.getTotal();
  }

  validCnt(id: number, cnt: number) {
    if (cnt < 1 || cnt > 999) {
      this.popupService.getPopup(this.router, this.route, 'main-page', 'Ошибка', 'Не корректное значение [1..999]', 3, 'alert-danger');
      this.changeCnt(id, 1);
    }
  }

  deleteItem(id: number) {
    this.items = this.basketService.deleteFromBasket(id);
    this.getItems();
    this.getTotal();
    this.observableCommonService.deleteFromBasket(id);
  }

  ngOnDestroy(): void {
    this.basketSub.unsubscribe();
  }
}
