import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {OrderBasicList} from '../../../../models/order-basic';
import {OrderService} from '../../../../services/order/order.service';
import {GlobalConst} from '../../../../globals/GlobalConst';
import {ObservableZhdunService} from "../../../../services/observable/observable-zhdun.service";
import {map} from "rxjs/internal/operators";


@Injectable()
export class UserOrdersResolver implements Resolve<[OrderBasicList]> {
  orderBasicList: OrderBasicList;

  constructor(private orderService: OrderService,
              private zhdun: ObservableZhdunService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {
    this.zhdun.on();

    return this.orderService.getUserOrders(0, GlobalConst.PERSONAL_AREA_LIMIT).pipe(map(data => {
      this.orderBasicList = data;

      this.zhdun.off();
      return this.orderBasicList;
    }));
  }
}
