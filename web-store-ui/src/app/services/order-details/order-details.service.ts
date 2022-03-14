import {Injectable} from '@angular/core';
import {ResultCode} from '../../globals/result-code.enum';
import {GlobalConst} from '../../globals/GlobalConst';
import {HttpRequestsService} from '../http-requests/http-requests.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Order, OrderBuilder, ResponseOrder, ResponseOrderDetails} from '../../models/order.model';
import {Response} from '../../models/response';

import {OrderHistoryStateList} from '../../models/order-details.model';
import {OrderState} from '../../globals/order-state.enum';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {PopupService} from '../../components/common-group/compose-message/popup.service';
import {Observable} from "rxjs/index";
import {catchError, map} from "rxjs/internal/operators";


@Injectable()
export class OrderDetailsService {
  private order: Order;
  private orderHistoryStateList: OrderHistoryStateList[];


  constructor(private httpRequestsService: HttpRequestsService,
              private router: Router,
              private route: ActivatedRoute,
              private popupService: PopupService,
              private _http: HttpClient) {

  }

  private handleError(operation: String) {
    return (err: any) => {
      let errMsg = `error in ${operation}()`;
      console.log(`${errMsg}:`, err);
      if (err instanceof HttpErrorResponse) {
        // you could extract more info about the error if you want, e.g.:
        console.log(`status: ${err.status}, ${err.statusText}`);
        // errMsg = ...
      }
      return errMsg;
    };
  }


  getAllStatesByOrderId(id: number | string): Observable<OrderHistoryStateList[]> {
    return this.httpRequestsService.getOrderStatesById(id)/*.take(1)*/.pipe(map((data: any) => {
      if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) && (
          (data.resultCode === ResultCode.SUCCESSFULL))) {
        this.orderHistoryStateList = data.payload;
        return this.orderHistoryStateList;
      }

      this.router.navigate(['/']);
      return null;
    }));
  }


  getProtocolById(id: number | string): Observable<any> {

    return this.httpRequestsService.getProtocolById(id)/*.take(1)*/.pipe(map((data: any) => {
      if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) && (
          (data.resultCode === ResultCode.SUCCESSFULL))) {
        console.log(data);
        return data.payload;
      }

      this.router.navigate(['/']);
      return null;
    }));
  }

  findDetailsByOrderId_Admin(id: number | string): Observable<ResponseOrderDetails> {
    return this.httpRequestsService.getOrderDetailsById_Admin(id)/*.take(1)*/.pipe(map((data: any) => {
      if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) && (
          (data.resultCode === ResultCode.SUCCESSFULL) ||
          (data.resultCode === ResultCode.READ_ONLY) ||
          (data.resultCode === ResultCode.CONTINUE))) {
        console.log(data);
        return new ResponseOrderDetails(data.resultCode, data.payload);
      }

      this.router.navigate(['/']);
      return null;
    }));
  }

  findDetailsByOrderId_User(id: number | string): Observable<ResponseOrderDetails> {
    return this.httpRequestsService.getOrderDetailsById_User(id)/*.take(1)*/.pipe(map((data: any) => {
      if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) && (
          (data.resultCode === ResultCode.SUCCESSFULL) ||
          (data.resultCode === ResultCode.READ_ONLY) ||
          (data.resultCode === ResultCode.CONTINUE))) {
        console.log(data);
        return new ResponseOrderDetails(data.resultCode, data.payload);
      }

      this.router.navigate(['/']);
      return null;
    }));
  }

  updateOrder(order: Order): Observable<Order> {

    return this.httpRequestsService.updateOrder(order)/*.take(1)*/.pipe(map((data: any) => {
      if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) && (data.resultCode === ResultCode.SUCCESSFULL)) {

        return this.extracted(data.payload);

      } else {
        this.router.navigate(['/']);
        return null;
      }
    }));
  }


  updateStateOrderMass(orders: any, state: OrderState): Observable<ResponseOrder[]> {
    return this.httpRequestsService.updateStateOrderMass(orders, state)/*.take(1)*/.pipe(map((data: any) => {
      console.log(data);

      let responseList = data.payload;

      // return responseList.map(function (responseOrder: any) {
      //   return new ResponseOrder(responseOrder.resultCode, OrderDetailsService.extracteds(responseOrder.payload));
      // });

      return responseList.map((responseOrder: any) => {
        return new ResponseOrder(responseOrder.resultCode, this.extracted(responseOrder.payload));
      });

    }, catchError(this.handleError('getData'))));
  }

  getListReserveByOrderId(id: number|string):Observable<any> {
    return this.httpRequestsService.getAllReserveByOrderDetailId(id)/*.take(1)*/.pipe(map((data: any) => {
      let reserveList = data.payload;
      return reserveList;
    }, catchError(this.handleError('getData'))));
  }

  updateStateOrder(order: Order, state: OrderState): Observable<ResponseOrder> {

    return this.httpRequestsService.updateStateOrder(order, state)/*.take(1)*/.pipe(map((data: any) => {
      if (data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) {
        if ((data.resultCode == ResultCode.CONTINUE) || (data.resultCode == ResultCode.SUCCESSFULL)) {
          this.ok(data.resultCode);
          return new ResponseOrder(data.resultCode, this.extracted(data.payload));
        } else if (data.resultCode == ResultCode.NO_REST) {
          let r = new ResponseOrder(data.resultCode,
            this.extractedResponseDetails(data.payload),
            data.payload.responseOrderDetailsList);
          console.log("r");
          console.log(r);
          return new ResponseOrder(data.resultCode,
            this.extractedResponseDetails(data.payload),
            data.payload.responseOrderDetailsList);// data;
        } else  if ((data.resultCode == ResultCode.READ_ONLY)||(data.resultCode == ResultCode.STATE_FORBIDDEN)){
          let resp = new Response();
          resp.resultCode = data.resultCode;
          resp.payload = data.payload;
          return new ResponseOrder(data.resultCode,null,[resp]);
        }

      } else {
        this.bad(data);
      }
    }, catchError(this.handleError('getData'))));
  }

  extracted(data: any) {
    let orderBulider = new OrderBuilder();
    orderBulider.setMainInfo(data.orderId, data.orderDate, data.deliveryDate, data.sum, data.periodTime);
    orderBulider.setOther(data.phone, data.state, data.deliveryKind, data.paymentMethod, data.note, data.callBack, data.deliveryAddress);
    orderBulider.setUser(data.client.userName, data.client.personName);
    //  orderBulider.setOper(data.oper.personName, data.oper.operId);
    orderBulider.setOper(data.oper);
    return orderBulider.build();
  }

  extractedResponseDetails(data: any) {
    let orderBulider = new OrderBuilder();
    orderBulider.setMinimalInfoResponse(data.orderId, data.state, data.operPersonName);
    return orderBulider.build();
  }


  ok(data: ResultCode) {

    if ((data === ResultCode.READ_ONLY)) {
      this.popupService.getPopup(this.router, this.route, 'admin', `Заказ  обрабатывается другим менеджером`, ``, 5);
    }
  }

  bad(errData: any) {
    let errorText: string = '';
    if (errData.result === GlobalConst.RESPONSE_RESULT_UNAUTHORIZED) {
      errorText = 'Невозможно получить доступ к ресурсу, попробуйте очистить cookies в браузере';
    } else if (errData.result === GlobalConst.INTERNAL_SERVER_ERROR || errData.status === 500) {
      errorText = 'Internal Server Error 500, попробуйте повторить попытку позже';
    } else if (errData.resultCode === ResultCode.NOT_FOUND) {
      errorText = 'Заказа не найден';
    }

    this.popupService.getPopup(this.router, this.route, 'admin',
      `Заказ не изменен. Ошибка: `,
      `  ${errorText}`, 5);
  }

}
