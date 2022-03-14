import {Product} from './product.model';
import {User, UserRole} from './user.model';
import {Order} from './order.model';

export class OrderDetails {
  id: number;
  price: number;
  quantity: number;
  totalPrice: number;
  orderId: number;
  product: Product;
  isLock:boolean;
  isCanceled:boolean;


  /*LAO без комментариев зачем это нужно*/

  /*constructor(obj?: OrderDetails) {
    for (const prop in obj) {
      if (obj.hasOwnProperty(prop)) {
        this[prop] = obj[prop];
      }
    }
  }*/

  constructor(id: number, orderId: number, price: number, totalPrice: number, quantity: number, product: Product,isLock,isCanceled) {
    this.id = id;
    this.orderId = orderId;
    this.price = price;
    this.totalPrice = totalPrice;
    this.quantity = quantity;
    this.product = product;
  }


}

export class OrderHistoryStateList {
  linkId: number;
  order: Order;
  orderHistoryState: OrderHistoryState;
  stateDate: Date;
  oper: User;
}

export class OrderHistoryState {
  stateId: number;
  stateName: string;
  stateNote: string;
  stateCode: string;
}
