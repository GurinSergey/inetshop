export class OrderBasic {
  orderId: number;
  orderDate: Date;
  sum: number;
  orderState: String;
  photoUrls: String[];
  flag: Boolean = false;

  constructor(orderId: number, orderDate: Date, sum: number, orderState: String, photoUrls: String[], flag: Boolean) {
    this.orderId = orderId;
    this.orderDate = orderDate;
    this.sum = sum;
    this.orderState = orderState;
    this.photoUrls = photoUrls;
    this.flag = flag;
  }
}

export class OrderBasicList {
  ordersBasic: OrderBasic[];
  count: number;

  constructor(ordersBasic: OrderBasic[] = [], count: number) {
    this.ordersBasic = ordersBasic;
    this.count = count;
  }
}
