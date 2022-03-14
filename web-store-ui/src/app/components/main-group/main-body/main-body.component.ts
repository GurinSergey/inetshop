import {Component, OnDestroy, OnInit} from '@angular/core';
import {ComplexWatchStorage} from "../../common-group/app-local-storage/watch-storage.model";
import {Product} from "../../../models/product";
import {Subscription} from "rxjs/index";
import {AppLocalStorageService} from "../../common-group/app-local-storage/app-local-storage.service";
import {ProductService} from "../../../services/product/product.service";
import {GlobalConst} from "../../../globals/GlobalConst";
import {SliderEntry} from "../../../models/slider-entry";
import {map} from "rxjs/internal/operators";

@Component({
  selector: 'app-main-body',
  templateUrl: './main-body.component.html',
  styleUrls: ['./main-body.component.css']
})
export class MainBodyComponent implements OnInit, OnDestroy {
  prevViewedItems = 'Просмотренные ранее товары';
  bestSellingProducts = 'Самые покупаемые товары';

  watchListSub: Subscription;
  itemsWatch: Product[] = [];

  sliderList: SliderEntry[] = [];

  constructor(private appLocalStorage: AppLocalStorageService,
              private productService: ProductService) {
    this.watchListSub = this.appLocalStorage.watchProductList.subscribe((data: ComplexWatchStorage) => {
      if (data.watchHistory) {
        this.itemsWatch = data.watchHistory.productList.map((product: any) => {
          return this.productService.getProductEntry(product);
        }).reverse();
      }
    });
  }

  ngOnInit(): void {
    this.getItemsForSlider();
/*
    this.sliderList.push(new SliderEntry('item', 'item', 'item', 'item', 'http://localhost:8065/assets/images/products/1/1.jpg'));
    this.sliderList.push(new SliderEntry('item', 'item', 'item', 'item', 'http://localhost:8065/assets/images/products/3/1.jpg'));
    this.sliderList.push(new SliderEntry('item', 'item', 'item', 'item', 'http://localhost:8065/assets/images/products/4/1.jpg'));
    this.sliderList.push(new SliderEntry('item', 'item', 'item', 'item', 'http://localhost:8065/assets/images/products/5/1.jpg'));
    this.sliderList.push(new SliderEntry('item', 'item', 'item', 'item', 'http://localhost:8065/assets/images/products/6/1.jpg'));
    this.sliderList.push(new SliderEntry('item', 'item', 'item', 'item', 'http://localhost:8065/assets/images/products/7/1.jpg'));
    this.sliderList.push(new SliderEntry('item', 'item', 'item', 'item', 'http://localhost:8065/assets/images/products/8/1.jpg'));
    this.sliderList.push(new SliderEntry('item', 'item', 'item', 'item', 'http://localhost:8065/assets/images/products/9/1.jpg'));
    */
  }

  private getItemsForSlider() {
    this.productService.getTheBestSellingProducts(GlobalConst.SLIDER_PRODUCT_LIMIT).subscribe(data => {
      if (data) {
        this.sliderList = data;
      }
    });
  }

  ngOnDestroy(): void {
    this.watchListSub.unsubscribe();
  }
}
