import {AfterContentChecked, Component, ElementRef, Input, OnDestroy, OnInit, ViewChild} from "@angular/core";
import {FilterValueEntry} from "../../../models/filter-entry";
import {AppLocalStorageService} from "../../common-group/app-local-storage/app-local-storage.service";
import {ProductService} from "../../../services/product/product.service";
import {Product} from "../../../models/product";
import {ComplexWatchStorage} from "../../common-group/app-local-storage/watch-storage.model";
import {Observable, Subscription} from "rxjs/index";
import {ProductListComponent} from "../product-list/product-list.component";
import {ProductFilterService} from "../../../services/product-filter/product-filter.service";
import {FilterListComponent} from "../filter-list/filter-list.component";
import {CatalogService} from "../../../services/catalog/catalog.service";

@Component({
  selector: 'app-section-list',
  templateUrl: './section-list.component.html',
  styleUrls: ['./section-list.component.css']
})

export class SectionListComponent implements OnInit, AfterContentChecked {
  @ViewChild(ProductListComponent) productListComponent: ProductListComponent;
  @ViewChild(FilterListComponent) filterListComponent: FilterListComponent;

  list_view = false;
  tile_list = true;
  offset: number;
  limit: number;
  count = 5;
  progressBar: boolean;
  selectedMatOption = 'novelty';
  sort:string;

  filterListOptions: Observable<FilterValueEntry[]>;
  _catalogName:string;

  @ViewChild('sortSelect') sortSelect: ElementRef;

  constructor(


    private filterService: ProductFilterService,
    private catalogService: CatalogService) {
    this.filterListOptions = this.filterService.valueChips;
    this.catalogService.catalogName.subscribe(data => {
      this._catalogName = data;
    });
  }

  ngAfterContentChecked() {
    let cdk = document.getElementsByClassName("mat-select-panel")[0];
    if(!cdk) return;

    let select = this.sortSelect.nativeElement.getElementsByClassName("select")[0].getBoundingClientRect();
    cdk.parentElement.style.top = select.bottom + 15 + 'px';
    cdk.parentElement.style.transform = "none";
  }

  ngOnInit() {
    this.progressBar = false;
    //this.appLocalStorage.customSync(); // LAO обновим инфу по истории просмотров
  }

  onChangeProgress(val: boolean) {
    this.progressBar = val;
  }

  toogle_view() {
    this.list_view = !this.list_view;
    this.tile_list = !this.tile_list;
  }

  changeSort(value) {
    if(value)
      this.sort = value;

    switch (this.sort) {
      case 'price_up': {
        this.productListComponent.changeSort('ASC', 'price');
        break;
      }
      case 'price_down': {
        this.productListComponent.changeSort('DESC', 'price');
        break;
      }
      case 'rating': {
        this.productListComponent.changeSort('DESC', 'ps.total_rating');
        break;
      }
      case 'popular': {
        this.productListComponent.changeSort('DESC', 'ps.total_comments');
        break;
      }
      case 'auction': {
        this.productListComponent.changeSort();
        break;
      }
      case 'novelty': {
        this.productListComponent.changeSort('DESC', 'id');
        break;
      }
      default: {
        this.productListComponent.changeSort();
        break;
      }
    }
  }

  removeFilterItem(id) {
    this.filterListComponent.unCheckedValueFilter(id);
  }
}
