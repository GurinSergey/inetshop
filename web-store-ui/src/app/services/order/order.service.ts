import {Injectable} from '@angular/core';
import {UserOrder} from '../../models/user-order';
import {GlobalConst} from '../../globals/GlobalConst';
import {HttpRequestsService} from '../http-requests/http-requests.service';
import {Observable} from 'rxjs';
import {OrderBuilder} from '../../models/order.model';
import {Router} from '@angular/router';
import {catchError, map} from 'rxjs/internal/operators';
import {OrderBasicList} from '../../models/order-basic';

@Injectable()
export class OrderService {

  constructor(private httpRequestsService: HttpRequestsService, private router: Router) {
  }

  getAllOrders(): Observable<UserOrder[]> {
    return this.httpRequestsService.getAllOrders().pipe(map((data: any) => {
      if (data.result !== GlobalConst.RESPONSE_RESULT_SUCCESS) {
        this.router.navigate(['/']);
        return null;
      }

      return data.payload.map((order: any) => {
        const orderBulider = new OrderBuilder();
        orderBulider.setMainInfo(order.orderId, order.orderDate, order.deliveryDate, order.sum, order.periodTime);
        orderBulider.setOther(order.phone, order.state, order.deliveryKind, order.paymentMethod, order.note, order.callBack, order.deliveryAddress);
        orderBulider.setUser(order.client.userName, order.client.personName);
        //  orderBulider.setOper(order.oper.personName, order.oper.operId);
        orderBulider.setOper(order.oper);
        return orderBulider.build();
      });

    }));
  }

  getUserOrders(offset: number = 0, limit: number = 10): Observable<OrderBasicList> {
    return this.httpRequestsService.getUserOrders(offset, limit).pipe(
      map((data: any) => {
        if (data.result !== GlobalConst.RESPONSE_RESULT_SUCCESS) {
          throw (data.payload);
        }

        const cnt = data.payload.count;
        const results = data.payload.results;
        return {
          count: cnt, ordersBasic: results.map((orderBasic: any) => {
            return orderBasic;
          })
        };
      }),
      catchError(error => {
        throw (error);
      })
    );
  }
}
