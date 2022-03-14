export class UserOrder {
  fio: string;
  mobile: string;
  email: string;
  deliveryKind: number;
  address: string;
  callBack: boolean;
  note: string;
  paymentMethod: number;

  constructor(fio: string, mobile: string, email: string, deliveryKind: number, address: string, callBack: boolean, note: string, paymentMethod: number) {
    this.fio = fio;
    this.mobile = mobile;
    this.email = email;
    this.deliveryKind = deliveryKind;
    this.address = address;
    this.callBack = callBack;
    this.note = note;
    this.paymentMethod = paymentMethod;
  }
}
