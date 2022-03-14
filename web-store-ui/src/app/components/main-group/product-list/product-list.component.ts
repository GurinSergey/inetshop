import {Component, OnInit, Input, Output, EventEmitter, OnDestroy} from '@angular/core';
import {ProductEntry} from '../../../models/product-entry';
import {BasketService} from '../../../services/basket/basket.service';
import {ProductService} from '../../../services/product/product.service';
import {CompareService} from '../../../services/compare/compare.service';
import {ObservableZhdunService} from '../../../services/observable/observable-zhdun.service';
import {ActivatedRoute} from '@angular/router';
import {ProductList} from '../../../models/product-list';
import {ObservableCommonService} from '../../../services/observable/observable-common.service';
import {GlobalConst} from '../../../globals/GlobalConst';
import {Product} from '../../../models/product';
import {ProductSortService} from '../../../services/product-sort/product-sort.service';
import {CommentService} from '../../../services/comment/comment.service';
import {CompareProduct} from '../../../models/compare';
import {BasketProduct} from "../../../models/basket";
import {FavoriteService} from "../../../services/favorite/favorite.service";
import {Authentication} from "../../common-group/auth/authentication";
import {MatDialog} from "@angular/material";
import {LoginComponent} from "../login/login.component";
import {animate, keyframes, query, stagger, style, transition, trigger} from "@angular/animations";
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
  providers: [ProductService],
  animations:[
    trigger('products', [
      transition('* => *', [
        query(':enter', style({opacity: 0}), {optional: true}),

        query(':enter', stagger('200ms', [
          animate('.3s ease-in', keyframes([
            style({opacity: 0, transform: 'translateY(25%)', offset: 0}),
            style({opacity: .5, transform: 'translateY(35px)', offset: .5}),
            style({opacity: 1, transform: 'translateY(0)', offset: 1})
          ]))
        ]), {optional: true})
      ])
    ])
  ]
})
export class ProductListComponent implements OnInit, OnDestroy {

  itemBlockHeight = 400; //VDN динамическая высота блока, изменяется в зависимости от
  // входящей инфо дабы "красиво" отрисовать блок, приеняется для класса product-item, вычисляется в calculateHeightProductItem

  productLoading = false;
  templateId:number;
  _items: Product[] = [];
  count = 0;
  offset = 0;
  limit = GlobalConst.PRODUCT_lIST_LIMIT;

  sort_direction;
  sort_criteria;
  basketCnt: any;
  compareCnt: any;

  private routeParams: Subscription;

  @Input() list_style = false;
  @Output() size = new EventEmitter<number>();

  @Input() set items(items: Product[]) {
    if (!items)
      return;

    this._items = items;
    this.calculateHeightProductItem();
  }

  constructor(private productService: ProductService,
              private commentService: CommentService,
              private productSortService: ProductSortService,
              private basketService: BasketService,
              private compareService: CompareService,
              private route: ActivatedRoute,
              private zhdun: ObservableZhdunService,
              private observableCommonService: ObservableCommonService,
              private favoriteService: FavoriteService,
              private authentication: Authentication,
              private loginDialog: MatDialog) {
    this.routeParams = this.route.params.subscribe(params => {
      this.templateId = params['id'];
      this.offset = 0;
      this.findAll(this.offset, GlobalConst.PRODUCT_lIST_LIMIT);
    });

    observableCommonService.anyAddToBasket$.subscribe(
      eventVal => {
        this._items[this._items.findIndex(p => p.baseInfo.id === eventVal)].baseInfo.isInBasketBox = true;
      });
    observableCommonService.anyDeleteFromBasket$.subscribe(
      eventVal => {
        this._items[this._items.findIndex(p => p.baseInfo.id === eventVal)].baseInfo.isInBasketBox = false;
      });
    observableCommonService.anyAddToCompare$.subscribe(
      eventVal => {
        this._items[this._items.findIndex(p => p.baseInfo.id === eventVal)].baseInfo.isInCompareBox = true;
      });
    observableCommonService.anyDeleteFromCompare$.subscribe(
      eventVal => {
        this._items[this._items.findIndex(p => p.baseInfo.id === eventVal)].baseInfo.isInCompareBox = false;
      });
  }

  calculateHeightProductItem() {
    let height = 400;

    if (this._items.filter(p => p.priceInfo.new_future_price !== null).length > 0) {
      height += 30;
    }

    if (this._items.filter(p => p.priceInfo.exists_sale === true).length > 0) {
      height += 25;
    }

    this.itemBlockHeight = height;
  }

  ngOnInit() {
  }

  findAll(offset: number, limit: number, sort_direction?: string, sort_criteria?: string, filter_string?: string) {
    this.productLoading = true;
    const listSubscription = this.productSortService.findAll(this.templateId, offset, limit, sort_direction, sort_criteria)
      .subscribe((productsList: ProductList) => {
        this._items = productsList.products;
        this.count = productsList.count;
        this.calculateHeightProductItem();
      });

    listSubscription.add(() => {
      this.productLoading = false;
    });
  }

  onChangePage(offset: number) {
    this.offset = offset;
    this.findAll(this.offset, this.limit, this.sort_direction, this.sort_criteria);
  }

  changeSort(sort_direction?, sort_criteria?) {
    this.offset = 0;
    this.sort_direction = sort_direction;
    this.sort_criteria = sort_criteria;

    this.findAll(this.offset, this.limit, this.sort_direction, this.sort_criteria);
  }

  addToBasket(p: ProductEntry) {
    p.isInBasketBox = true;
    const basketProduct: BasketProduct = new BasketProduct(p, 1);
    this.basketService.addToBasket(basketProduct);

    this.basketCnt = this.basketService.getBasketCnt();
    this.observableCommonService.changeCntBasket(this.basketCnt);
  }

  addToCompare(p: Product) {
    if (p.baseInfo.isInCompareBox) {
      return;
    }

    p.baseInfo.isInCompareBox = true;
    const compareProduct: CompareProduct = new CompareProduct(p, 1);
    compareProduct.templateId = p.baseInfo.templateId;
    compareProduct.templateName = p.baseInfo.templateName;

    this.compareService.addToCompare(compareProduct);

    this.compareCnt = this.compareService.getCompareCnt();
    this.observableCommonService.changeCntCompare(this.compareCnt);
  }

  basketInit() {
    this.observableCommonService.pushShowBasket(true);
  }

  ngOnDestroy() {
    this.routeParams.unsubscribe();
  }

  favorite(p: Product) {
    if(!this.authentication.authenticated){
      this.loginDialog.open(LoginComponent, {panelClass: 'loginDialogClass'});
      return false; //VDN - checked is false
    }
    this.favoriteService.addToFavorite(p);
  }
}
