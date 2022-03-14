import {Injectable} from '@angular/core';
import {Basket, BasketProduct} from '../../models/basket';
import {AppLocalStorageService} from '../../components/common-group/app-local-storage/app-local-storage.service';
import {ComplexBasketStorage} from '../../components/common-group/app-local-storage/basket-storage.model';

@Injectable()
export class BasketService {
  basket: Basket;
  private productList: BasketProduct[];

  constructor(private appLocalStorage: AppLocalStorageService) {
    this.basket = new Basket();
  }

  getBasketData(): Basket {
    this.basket = new Basket();
    if (this.appLocalStorage.exist('ComplexBasketStorage')) {
      this.basket.productList = this.appLocalStorage.get('ComplexBasketStorage').basket.productList;
    }

    return this.basket;
  }

  addToBasket(basketProduct: BasketProduct): Basket {
    this.productList = [];
    if (this.appLocalStorage.exist('ComplexBasketStorage')) {
      this.productList = this.appLocalStorage.get('ComplexBasketStorage').basket.productList;
    }
    if (this.isExistsProduct(basketProduct.id)) {
      return;
    }

    this.productList.push(basketProduct);
    this.basket.productList = this.productList;

    this.appLocalStorage.set('ComplexBasketStorage', new ComplexBasketStorage(this.basket, 'client -> addToBasket', this.getAllProductsCnt(this.productList)));

    return this.basket;
  }

  deleteFromBasket(id: number): Basket {
    if (id === null) {
      alert('deleteFromBasket -> \'id\' is empty');
      return;
    }

    this.productList = this.getBasketData().productList;
    const index = this.productList.findIndex(product => product.id === id);
    this.productList.splice(index, 1);
    this.basket.productList = this.productList;

    this.appLocalStorage.set('ComplexBasketStorage', new ComplexBasketStorage(this.basket, 'client -> deleteFromBasket', this.getAllProductsCnt(this.productList)));

    return this.basket;
  }

  removeBasket() {
    this.productList = [];
    this.appLocalStorage.set('ComplexBasketStorage', new ComplexBasketStorage(new Basket(), 'client -> removeBasket', 0));
  }

  isExistsProduct(id: number): boolean {
    if (id === null) {
      alert('isExistProduct -> \'id\' is empty');
      return;
    }

    return this.getBasketData().productList.find(product => product.id === id) != null;
  }

  getBasketCnt(): number {
    let total: number = 0;

    this.getBasketData().productList.forEach(product => total += parseFloat(product.cnt.toString()));

    return total;
  }

  private getAllProductsCnt(productList: BasketProduct[]): number {
    let total: number = 0;

    productList.forEach(product => total += product.cnt);

    return total;
  }

  getBasketSum(): number {
    let sum: number = 0;

    this.getBasketData().productList.forEach(product => sum += product.price * product.cnt);

    return sum;
  }

  resetProductCnt(id: number, cnt: number) {
    if (id === null) {
      alert('resetProductCnt -> \'id\' is empty');
      return;
    }

    this.productList = this.getBasketData().productList;
    this.productList[this.productList.findIndex(product => product.id === id)].cnt = cnt;
    this.basket.productList = this.productList;

    this.appLocalStorage.set('ComplexBasketStorage', new ComplexBasketStorage(this.basket, 'client -> resetProductCnt', this.getAllProductsCnt(this.productList)));

    return this.basket;
  }
}
