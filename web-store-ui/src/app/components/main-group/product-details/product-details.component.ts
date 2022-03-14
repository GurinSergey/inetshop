import {
  AfterContentInit, AfterViewInit, Component, ElementRef, Inject, OnDestroy, OnInit,
  ViewChild,
} from '@angular/core';
import {HttpRequestsService} from '../../../services/http-requests/http-requests.service';
import {ActivatedRoute, NavigationEnd, NavigationStart, Router} from '@angular/router';
import {ProductService} from '../../../services/product/product.service';
import {BasketService} from '../../../services/basket/basket.service';
import {ProductEntry} from '../../../models/product-entry';
import {ObservableCommonService} from '../../../services/observable/observable-common.service';
import {CompareService} from '../../../services/compare/compare.service';
import {Product} from '../../../models/product';
import {AppLocalStorageService} from "../../common-group/app-local-storage/app-local-storage.service";
import {ComplexWatchStorage} from "../../common-group/app-local-storage/watch-storage.model";
import {Subscription} from "rxjs";
import {GlobalConst} from "../../../globals/GlobalConst";
import {PopupService} from "../../common-group/compose-message/popup.service";
import {PopupStyle} from "../../../globals/popup-style.enum";
import {CommentService} from "../../../services/comment/comment.service";
import {ObservableZhdunService} from "../../../services/observable/observable-zhdun.service";
import {CommentList} from "../../../models/comment-list";
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material";
import {PhotoEntry} from "../../../models/photo-entry";
import {CompareProduct} from "../../../models/compare";
import {BasketProduct} from "../../../models/basket";
import {TransferState, makeStateKey, Meta} from '@angular/platform-browser';
import {FieldList} from "../../../models/field-list";

const DOGS_KEY = makeStateKey<any>('dogs');

@Component({
  selector: 'app-photos-dialog',
  templateUrl: './photos-dialog.html',
  styleUrls: ['./photos-dialog.css', 'product-details.component.css']
})
export class PhotosDialogComponent implements AfterViewInit {
  items: PhotoEntry[] = [];
  slideIndex: number = 1;
  @ViewChild('slides') slides: ElementRef;

  constructor(@Inject(MAT_DIALOG_DATA) public data: { items: PhotoEntry[], currentSlide: number }) {
    this.items = data.items;
    this.slideIndex = data.currentSlide;
  }

  showBigSlides(index: number): void {
    this.slideIndex = index > this.items.length ? 1 : index < 1 ? this.items.length : index;

    let slides = this.slides.nativeElement.getElementsByClassName('mySlides');
    let dots = this.slides.nativeElement.getElementsByClassName('demo');

    for (var i = 0; i < slides.length; i++)
      slides[i].classList.remove('flex', 'open');

    for (var i = 0; i < dots.length; i++)
      dots[i].classList.remove('active');

    slides[this.slideIndex - 1].classList.add('flex', 'open');
    dots[this.slideIndex - 1].classList.add('active');

    this.imageZoom();
  }

  moveLens(event) {
    let p = event.target.parameters;
    event.preventDefault();
    let a = p.img.getBoundingClientRect();

    let position = {
      x: event.pageX - a.left,
      y: event.pageY - a.top
    };

    let x = position.x - (p.lens.offsetWidth / 2);
    let y = position.y - (p.lens.offsetHeight / 2);

    x = (x > p.img.width - p.lens.offsetWidth) ? p.img.width - p.lens.offsetWidth : x < 0 ? 0 : x;
    y = (y > p.img.height - p.lens.offsetHeight) ? p.img.height - p.lens.offsetHeight : y < 0 ? 0 : y;

    p.lens.style.left = x + "px";
    p.lens.style.top = y + "px";

    p.res.style.backgroundPosition = "-" + (x * p.cx) + "px -" + (y * p.cy) + "px";
  }

  imageZoom() {
    let img = this.slides.nativeElement.getElementsByClassName('mySlides open')[0].firstChild as any;
    let res = document.getElementById('resultPhotoLens') as any;
    let lens = document.getElementById('photoLens') as any;

    let cx = res.offsetWidth / lens.offsetWidth;
    let cy = res.offsetHeight / lens.offsetHeight;

    res.style.backgroundImage = "url('" + img.src + "')";
    res.style.backgroundSize = (img.width * cx) + "px " + (img.height * cy) + "px";

    lens.parameters = {
      lens: lens, img: img, res: res, cx: cx, cy: cy
    };
    res.parameters = lens.parameters;

    lens.addEventListener("mousemove", this.moveLens, false);
    res.addEventListener("mousemove", this.moveLens, false);
    lens.addEventListener("touchmove", this.moveLens, false);
    res.addEventListener("touchmove", this.moveLens, false);
  }

  ngAfterViewInit(): void {
    this.imageZoom();
  }
}

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css', '../main-body/main-body.component.css'],
  providers: [ProductService, BasketService/*, SeoService*/]
})
export class ProductDetailsComponent implements OnInit, AfterContentInit, OnDestroy {
  product: Product;
  tabIndex: number = 0;
  slideIndex: number = 1;
  isLoadingComments: boolean = false;
  watchListSub: Subscription;
  basketSub: Subscription;

  prevViewedItems: string = "Ранее просмотренные товары";

  @ViewChild('slides') slides: ElementRef;

  itemsWatch: Product[] = [];
  offset: number = 0;
  limit: number = GlobalConst.PRODUCT_COMMENTS_LIMIT;
  dogs: any;

  constructor(private httpService: HttpRequestsService,
              private productService: ProductService,
              private commentService: CommentService,
              private activateRoute: ActivatedRoute,
              private router: Router,
              private basketService: BasketService,
              private popupService: PopupService,
              private appLocalStorage: AppLocalStorageService,
              private compareService: CompareService,
              private observableCommonService: ObservableCommonService,
              private zhdun: ObservableZhdunService,
              public dialog: MatDialog,
              private state: TransferState,
             /* private seoService: SeoService,*/
              private metaService: Meta) {
    let commentId = Number(this.activateRoute.snapshot.queryParamMap.get('comment'));
    this.tabIndex = Number(this.activateRoute.snapshot.queryParamMap.get('page')) | 0;

    if (commentId) {
      this.commentService.getPageByComment(this.limit, this.activateRoute.snapshot.params['id'], commentId)
        .subscribe((data: number) => {
          this.onChangePage(data);
        });
    }

    this.basketSub = observableCommonService.anyDeleteFromBasket$.subscribe(
      eventVal => {
        if (this.product.baseInfo.id === eventVal) {
          this.product.baseInfo.isInBasketBox = false;
        }
      });

    // Дли Димона, возвращает ComplexWatchStorage
    this.watchListSub = this.appLocalStorage.watchProductList.subscribe((data: ComplexWatchStorage) => {
      if (data.watchHistory) {
        this.itemsWatch = data.watchHistory.productList.map((product: any) => {
          return this.productService.getProductEntry(product);
        }).reverse();
      }
    });
  }

  ngOnInit() {
    this.activateRoute.data
      .subscribe((data: { productDetails: any }) => {
        if (data) {
          this.product = data.productDetails;
          this.selectTabIndex(this.tabIndex);

          /*if (this.state.hasKey(DOGS_KEY)) {
            this.product.fieldInfo = this.state.get(DOGS_KEY, new FieldList);
            this.state.remove(DOGS_KEY);

          } else {*/
            this.productService.getProductFields(this.product.baseInfo.id).subscribe(fieldList => {
              this.product.fieldInfo = fieldList;
              //this.state.set(DOGS_KEY, fieldList);
            });
          //}
          this.metaService.updateTag({
            'title': 'купить у нас '+ this.product.baseInfo.title, 'description': this.product.baseInfo.description,
            'code': this.product.baseInfo.code, 'latname': this.product.baseInfo.latIdent,
          });
          this.zhdun.off();
          window.top;
        }
      });
  }

  ngAfterContentInit() {
    //this.appLocalStorage.customSync();
  }

  navigateSlide(val: number) {
    this.currentSlide(this.slideIndex += val);
  }

  currentSlide(index: number) {
    this.showSlides(this.slideIndex = index);
  }

  showSlides(index: number = 1): void {
    if (!this.product) return;
    this.slideIndex = this.slideIndex > this.product.photosInfo.length ? 1 :
      this.slideIndex < 1 ? this.product.photosInfo.length :
        this.slideIndex;

    let slides = this.slides.nativeElement.getElementsByClassName('mySlides');
    let dots = this.slides.nativeElement.getElementsByClassName('demo');

    for (var i = 0; i < slides.length; i++)
      slides[i].classList.remove('flex');

    for (var i = 0; i < dots.length; i++)
      dots[i].classList.remove('active');

    slides[this.slideIndex - 1].classList.add('flex');
    dots[this.slideIndex - 1].classList.add('active');
  }

  selectTabIndex(index: number) {
    this.tabIndex = index;
    if (index === 2 && this.product.commentInfo.count === 0)
      this.getBatchCommets();
  }

  addToBasket(p: ProductEntry) {
    p.isInBasketBox = true;
    const basketProduct: BasketProduct = new BasketProduct(p, 1);
    this.basketService.addToBasket(basketProduct);

    this.observableCommonService.changeCntBasket(this.basketService.getBasketCnt());
    this.observableCommonService.addToBasket(p.id);
  }

  addToCompare(p: Product) {
    p.baseInfo.isInCompareBox = true;
    const compareProduct: CompareProduct = new CompareProduct(p, 1);
    compareProduct.templateId = p.baseInfo.templateId;
    compareProduct.templateName = p.baseInfo.templateName;

    this.compareService.addToCompare(compareProduct);

    this.observableCommonService.changeCntCompare(this.compareService.getCompareCnt());
    this.observableCommonService.addToCompare(p.baseInfo.id);
  }

  basketInit() {
    this.observableCommonService.pushShowBasket(true);
  }

  ngOnDestroy() {
    this.watchListSub.unsubscribe();
    this.basketSub.unsubscribe();
  }

  getBatchCommets() {
    this.isLoadingComments = true;
    const subscription = this.commentService.getProductComments(this.product.baseInfo.id, this.offset, this.limit)
      .subscribe(
        (list: CommentList) => {
          this.product.commentInfo.count = list.count;
          this.product.commentInfo.commentList = list.commentList;
        },
        (data: any) => {
          this.popupService.popupView(`Ошибка`, data, 3, PopupStyle.INFO);
        });

    subscription.add(() => {
      this.isLoadingComments = false;
    });
  }

  openPhotosDialog() {
    this.dialog.open(PhotosDialogComponent,
      {
        data: {items: this.product.photosInfo, currentSlide: this.slideIndex}
      });
  }

  onChangePage(offset: number) {
    this.offset = offset;
    this.getBatchCommets();
  }
}
