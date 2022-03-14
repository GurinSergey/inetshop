import {Injectable} from '@angular/core';
import {ActivatedRoute, ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot} from '@angular/router';
import {ComplexBasketStorage} from '../../common-group/app-local-storage/basket-storage.model';
import {AppLocalStorageService} from '../../common-group/app-local-storage/app-local-storage.service';
import {HttpRequestsService} from '../../../services/http-requests/http-requests.service';
import {GlobalConst} from '../../../globals/GlobalConst';
import {catchError, map} from 'rxjs/internal/operators';
import {PopupService} from '../../common-group/compose-message/popup.service';
import {PopupStyle} from '../../../globals/popup-style.enum';
import {ObservableZhdunService} from '../../../services/observable/observable-zhdun.service';
import {BasketService} from '../../../services/basket/basket.service';
import {ProductService} from '../../../services/product/product.service';
import {Basket, BasketProduct} from '../../../models/basket';

@Injectable()
export class CheckoutResolver implements Resolve<Basket> {
  basket: Basket;

  constructor(private appLocalStorageService: AppLocalStorageService,
              private httpRequestsService: HttpRequestsService,
              private router: Router,
              private route: ActivatedRoute,
              private popupService: PopupService,
              private basketService: BasketService,
              private productService: ProductService,
              private zhdun: ObservableZhdunService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {
    this.zhdun.on();

    let complexBasketStorage: ComplexBasketStorage = new ComplexBasketStorage({}, 'empty basket', 0);

    if (this.appLocalStorageService.exist('ComplexBasketStorage')) {
      complexBasketStorage = this.appLocalStorageService.get('ComplexBasketStorage');
    }

    this.zhdun.on();
    return this.httpRequestsService.synchronizeBasket(complexBasketStorage).pipe(map((data: any) => {
        this.basket = new Basket();
        if (data.result !== GlobalConst.RESPONSE_RESULT_SUCCESS) {
          this.popupService.getPopup(this.router, this.route, 'main-page', 'Ошибка', 'Что-то пошло не так:(', 5, PopupStyle.DANGER);

          this.zhdun.off();

          return this.basket;
        }

        data.payload.map(p => {
          this.basket.productList.push(new BasketProduct(this.productService.getEntry(p), p.cnt));
        });

        this.zhdun.off();
        return this.basket;
      }
    ));
  }
}
