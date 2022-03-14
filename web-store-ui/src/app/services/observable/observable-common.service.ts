import {Injectable} from '@angular/core';
import {BehaviorSubject, Subject} from 'rxjs/index';


@Injectable()
export class ObservableCommonService {

  private cntBasket = new Subject<any>();
  anyCntBasket$ = this.cntBasket.asObservable();

  private inBasket = new Subject<any>();
  anyAddToBasket$ = this.inBasket.asObservable();

  private fromBasket = new Subject<any>();
  anyDeleteFromBasket$ = this.fromBasket.asObservable();

  private cntCompare = new Subject<any>();
  anyCntCompare$ = this.cntCompare.asObservable();

  private inCompare = new Subject<any>();
  anyAddToCompare$ = this.inCompare.asObservable();

  private fromCompare = new Subject<any>();
  anyDeleteFromCompare$ = this.fromCompare.asObservable();

  private ordersParentSource = new BehaviorSubject<any>('');
  orderParent$ = this.ordersParentSource.asObservable();

  private ordersChildSource = new BehaviorSubject<any>('');
  orderDetailsChild$ = this.ordersChildSource.asObservable();

  private userParentSource = new BehaviorSubject<any>('');
  userParent$ = this.userParentSource.asObservable();

  private userChildSource = new BehaviorSubject<any>('');
  userChild$ = this.userChildSource.asObservable();

  private showBasket = new BehaviorSubject<any>('');
  anyShowBasket$ = this.showBasket.asObservable();

  private filterData = new Subject<any>();
  anyFilterData$ = this.filterData.asObservable();

  changeCntBasket(eventVal: any) {
    this.cntBasket.next(eventVal);
  }

  addToBasket(eventVal: any) {
    this.inBasket.next(eventVal);
  }

  deleteFromBasket(eventVal: any) {
    this.fromBasket.next(eventVal);
  }

  changeCntCompare(eventVal: any) {
    this.cntCompare.next(eventVal);
  }

  addToCompare(eventVal: any) {
    this.inCompare.next(eventVal);
  }

  deleteFromCompare(eventVal: any) {
    this.fromCompare.next(eventVal);
  }

  sendOrderToChild(order: any) {
    this.ordersParentSource.next(order);
  }

  sendDetailsToParent(orderDetails: any) {
    this.ordersChildSource.next(orderDetails);
  }

  eventInParentToChild(eventVal: string) {
    this.userParentSource.next(eventVal);
  }

  eventInChildToParent(eventVal: string) {
    this.userChildSource.next(eventVal);
  }

  pushShowBasket(eventVal: boolean) {
    this.showBasket.next(eventVal);
  }

  pushFilterData(eventVal: String) {
    this.filterData.next(eventVal);
  }
}
