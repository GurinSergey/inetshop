export class ComplexBasketStorage {
  basket: Object;
  note: string;
  count: number ;

  constructor(basket: Object, note: string, count: number) {
    this.basket = basket;
    this.note = note;
    this.count = count;
  }
}
