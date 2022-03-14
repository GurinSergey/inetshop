import {ProductEntry} from './product-entry';

export class BasketProduct extends ProductEntry {
  cnt: number;

  constructor(p: ProductEntry, cnt: number) {
    super(p.id, p.title, p.code, p.price, p.url_image);
    this.cnt = cnt;
    this.latIdent = p.latIdent;
    this.description = p.description;
    this.isInBasketBox = p.isInBasketBox;
  }
}

export class Basket {
  productList: BasketProduct[];

  constructor(productList: BasketProduct[] = []) {
    this.productList = productList;
  }
}
