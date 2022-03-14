import {Injectable} from '@angular/core';

import {Router} from "@angular/router";
import {HttpRequestsService} from '../../../services/http-requests/http-requests.service';
import {ComplexBasketStorage} from './basket-storage.model';
import {ComplexWatchStorage, WatchHistory} from "./watch-storage.model";
import {BehaviorSubject} from "rxjs";
import {ProductBuilder} from "../../../models/product.model";




@Injectable()
export class AppLocalStorageService {
  _storage_service;
  note;
  watchProductList = new BehaviorSubject<any>(new ComplexWatchStorage(null, "empty", 0));

  constructor(private httpService: HttpRequestsService,
              private router: Router) {
    this._storage_service = null;
    this.note = "_storage_service";
    this.initialize();
  }


  initialize() {
    if (localStorage) {
      try {
        localStorage.setItem('_tmptest', 'tmpval');
        if (localStorage === Object(window.localStorage)) {
          this._storage_service = localStorage;
        }
        localStorage.removeItem('_tmptest');
      } catch (e) {
        console.log("local storage not support: " + e);
      }
    }

  }

  available(): boolean {
    //LAO why - method with boolean signature not guaranteed to return boolean #5696
    return Boolean(this._storage_service);
  }

  set(key, value) {
    if (this.available()) {
      this._storage_service[key] = JSON.stringify(value);
    }
  }

  get(key) {
    var result = null;
    if (this.available() && this._storage_service.hasOwnProperty(key) && this._storage_service[key] != undefined) {
      try {
        result = JSON.parse(this._storage_service[key]);
      } catch (e) {
      }
    }
    return result;
  }


  remove(key) {
    if (this.available()) {
      delete this._storage_service[key]
    }
  }

  exist(key): boolean {
    //LAO why - method with boolean signature not guaranteed to return boolean #5696
    return Boolean(this.available() && this._storage_service.hasOwnProperty(key));
  }

  clear() {
    this.available() && this._storage_service.clear();
  }

  synchronize() {
    let complexBasketStorage: ComplexBasketStorage;
    complexBasketStorage = new ComplexBasketStorage({}, 'empty basket', 0);

    if (this.exist("ComplexBasketStorage")) {
      complexBasketStorage = this.get("ComplexBasketStorage");
    }

    this.synchronizeBasket(complexBasketStorage);
  }

  private synchronizeBasket(localStorge: any) {
    this.httpService.synchronizeBasket(localStorge).subscribe(
      data => this.set("ComplexBasketStorage", data),
      error => console.log(error)
    );
  }


  synchronizeWatchHistory(localStorage: any) {
    this.httpService.synchronizeWatch(localStorage).subscribe((data: any) => {
      let complexWatchStorage: ComplexWatchStorage = data.payload;
      let watchHistory_: WatchHistory = {  // = new WatchHistory();
        productList: data.payload.watchHistory.productList.map((data: any) => {
          return new ProductBuilder().setFields(
            data.id,
            data.title,
            data.code,
            data.producer,
            data.description,
            data.template,
            data.price,
            data.visible,
            data.isExists,
            data.photos)
            .setAdvancedFields(data.statistics)
            .build();

        })
      };
      complexWatchStorage.watchHistory = watchHistory_;
      this.set("ComplexWatchStorage", complexWatchStorage);
      this.watchProductList.next(complexWatchStorage);

    });
  }


// LAO Серега переделал логику synchronize, теперь не могу его использовать, сделал свой
  customSync() {
    let complexWatchStorage: ComplexWatchStorage;
    complexWatchStorage = new ComplexWatchStorage(null, 'empty watch', 0);

    if (this.exist("ComplexWatchStorage")) {
      complexWatchStorage = this.get("ComplexWatchStorage");
    }

    this.synchronizeWatchHistory(complexWatchStorage);
  }


}
