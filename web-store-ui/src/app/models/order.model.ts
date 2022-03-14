import {OrderDetails} from "./order-details.model";
import {ResultCode} from "../globals/result-code.enum";
import {Response} from "./response";

export class OrderBuilder {
  private order: Order;

  constructor() {
    this.order = new Order();
  }

  setMainInfo(orderId: number, orderDate: Date, deliveryDate: string, sum: number, periodTime: string): OrderBuilder {
    this.order.orderId = orderId;
    this.order.orderDate = orderDate;
    this.order.deliveryDate = deliveryDate;
    this.order.sum = sum;
    this.order.periodTime = periodTime;
    return this;
  }


  setOther(phone: string, state: number, deliveryKind: number, paymentMethod: number, note: string, callBack: string, deliveryAddress: string): OrderBuilder {
    this.order.phone = phone;
    this.order.state = state;
    this.order.deliveryKind = deliveryKind;
    this.order.paymentMethod = paymentMethod;
    this.order.note = note;
    this.order.callBack = callBack;
    this.order.deliveryAddress = deliveryAddress;
    return this;
  }


  setOper(/*personName: string, operId: number*/ data: any): OrderBuilder {
    this.order.operPersonName = null;
    this.order.operId = null;

    if (data != null) {
      this.order.operPersonName = data.personName;
      this.order.operId = data.operId;
    }
    return this;
  }

  setUser(userName: string, personName: string): OrderBuilder {
    this.order.clientUserName = userName;
    this.order.clientPersonName = personName;
    return this;
  }

  setMinimalInfoResponse(orderId: number, state: number, personName: string,) {
    this.order.orderId = orderId;
    this.order.state = state;
    this.order.operPersonName = personName;
    return this;
  }

  build(): Order {
    return this.order;
  }

}


export class Order {
  orderId: number;
  orderDate: Date;
  clientUserName: string;
  clientPersonName: string;
  deliveryDate: string;
  phone: string;
  sum: number;
  state: number;
  deliveryKind: number;
  paymentMethod: number;
  note: string;
  callBack: string;
  deliveryAddress: string;
  periodTime: string;
  operPersonName: string;
  operId: number;
  customerName: string;
}

export const payMethods = [
  {code: 'CASH', text: 'Наличными', icon: 'https://png.icons8.com/cotton/1600/cash-.png'},
  {
    code: 'ELECTRONIC',
    text: 'Карта',
    icon: 'https://cdn2.iconfinder.com/data/icons/flat-seo-web-ikooni/128/flat_seo2-21-512.png'
  }
];

export const periodTimes = [
  {code: 'P09_12', text: '09:00-12:00'},
  {code: 'P12_15', text: '12:00-15:00'},
  {code: 'P15_18', text: '15:00-18:00'},
  {code: 'P18_20', text: '18:00-20:00'},
  {code: 'P09_20', text: '09:00-20:00'}];

export const stateSelect = [
  {code: 2, codeLat: "CONFIRM", text: "Заказ подтвержден"},
  {code: 3, codeLat: "WAIT", text: "Заказ сформирован и ожидает отгрузки"},
  {code: 4, codeLat: "SEND_ORDER", text: "Заказ отправлен"},
  {code: 5, codeLat: "RECEIVED", text: "Товар получен"},
  {code: 6, codeLat: "CLOSED", text: "Заказ закрыт"},
  {code: 7, codeLat: "REJECTED", text: "Заказ отменен"},
  {code: 12, codeLat: "CHECK_REST", text: "Проверить остатки"}];


export class ResponseOrderDetails {
  resultCode: ResultCode;
  orderDetails: OrderDetails[];

  constructor(resultCode: ResultCode, orderDetails: OrderDetails[]) {
    this.resultCode = resultCode;
    this.orderDetails = orderDetails;
  }
}

export class ResponseOrder {
  resultCode: ResultCode;
  order: Order;
  response: Response[];

  constructor(resultCode: ResultCode, order: Order, response: Response[] = null) {
    this.resultCode = resultCode;
    this.order = order;
    this.response = response;
  }
}



