import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {ProductList} from '../../../models/product-list';
import {ProductService} from '../../../services/product/product.service';
import {ObservableZhdunService} from '../../../services/observable/observable-zhdun.service';
import {map} from "rxjs/internal/operators";
import {GlobalConst} from "../../../globals/GlobalConst";
import {makeStateKey, TransferState} from "@angular/platform-browser";
import {Observable} from "rxjs/Rx";

const RESULT_KEY = makeStateKey<any>('main');

@Injectable()
export class MainPageResolver implements Resolve<ProductList> {
  result: any;

  constructor(private productService: ProductService,
              private transferState: TransferState,
              private zhdun: ObservableZhdunService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {
    const found = this.transferState.hasKey(RESULT_KEY);
    let id = route.params['id'];
    if (found) {
      const res = Observable.of(this.transferState.get<ProductList>(RESULT_KEY, null));
      this.transferState.remove(RESULT_KEY);
      return res;
    } else {
      this.transferState.onSerialize(RESULT_KEY, () => this.result);
      this.zhdun.on();
      return this.productService.findAll(id, 0, GlobalConst.PRODUCT_lIST_LIMIT).pipe(map(data => {
        let productList = new ProductList(data.products, data.count);

        this.zhdun.off();
        return productList;
      })).do(result => this.result = result);
    }

    //   this.zhdun.on();

    /*  return this.productService.findAll(0, GlobalConst.PRODUCT_lIST_LIMIT).pipe(map(data => {
        let productList = new ProductList(data.products, data.count);

        this.zhdun.off();
        return productList;
      }));*/
  }
}
