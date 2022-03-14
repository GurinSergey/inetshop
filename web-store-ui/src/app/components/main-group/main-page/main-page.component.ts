import {LOCAL_STORAGE} from '@ng-toolkit/universal';
import {
  Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild, Inject,
  PLATFORM_ID
} from '@angular/core';
import {Authentication} from '../../common-group/auth/authentication';
import {LoginComponent} from '../login/login.component';
import {HttpRequestsService} from '../../../services/http-requests/http-requests.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {AppLocalStorageService} from '../../common-group/app-local-storage/app-local-storage.service';
import {BasketService} from '../../../services/basket/basket.service';
import {CompareService} from '../../../services/compare/compare.service';
import {ObservableCommonService} from '../../../services/observable/observable-common.service';
import {GlobalConst} from '../../../globals/GlobalConst';
import {RouterConst} from '../../../globals/RouterConst';
import {ResultCode} from '../../../globals/result-code.enum';
import {ProductService} from '../../../services/product/product.service';
import {Subscription} from 'rxjs';
import {FormControl} from '@angular/forms';
import {
  map,
  debounceTime,
  distinctUntilChanged,
  switchMap,
  tap, startWith, catchError
} from 'rxjs/operators';
import {merge, of} from 'rxjs/index';
import {
  MAT_DIALOG_DATA, MatAutocompleteSelectedEvent, MatDialog, MatDialogRef, MatPaginator,
  MatTableDataSource
} from '@angular/material';
import {MainBodyComponent} from '../main-body/main-body.component';
import {ProductFilterService} from '../../../services/product-filter/product-filter.service';
import {AppStorage} from '../../../shared/for-storage/universal.inject';
import {FavoriteService} from '../../../services/favorite/favorite.service';
import {isPlatformBrowser} from '@angular/common';
import {CatalogService} from '../../../services/catalog/catalog.service';
import {CatalogEntry} from '../../../models/catalog-entry';
import {SliderEntry} from '../../../models/slider-entry';
import {Observable} from 'rxjs/Rx';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})

export class MainPageComponent implements OnInit, OnDestroy {
  overlay = false;
  menuOpen = false;
  basketCnt: any;
  compareCnt: any;
  favoriteCnt: number;

  mainSub: Subscription;
  favoriteCntSub: Subscription;

  isLoadingResults = false;
  searchField: FormControl = new FormControl();

  displayedColumns = ['productImage', 'productCode', 'productTitle'];
  dataSource = new MatTableDataSource();

  @ViewChild(LoginComponent) loginComponent: LoginComponent;
  @ViewChild(MainBodyComponent) mainBodyComponent: MainBodyComponent;

  @ViewChild('userMenu') userMenu: ElementRef;
  @ViewChild('search') search: ElementRef;
  @ViewChild('input') input: ElementRef;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  searchPageSize: number = GlobalConst.AUTOCOMPLETE_SEARCH_PAGE_SIZE;
  isShowPaginator = false;

  catalog: Observable<CatalogEntry[]>;

  constructor(@Inject(LOCAL_STORAGE) private localStorage: any,
              public authentication: Authentication,
              public httpRequest: HttpRequestsService,
              @Inject(AppStorage) private cookies: Storage,
              @Inject(PLATFORM_ID) private platformId: Object,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private appLocalStorage: AppLocalStorageService,
              private basketService: BasketService,
              private compareService: CompareService,
              private productService: ProductService,
              private productFilterService: ProductFilterService,
              private observableCommonService: ObservableCommonService,
              private favoriteDialog: MatDialog,
              private favoriteService: FavoriteService,
              private catalogService: CatalogService) {
    this.mainSub = observableCommonService.anyCntBasket$.subscribe(
      eventVal => {
        this.changeBasketCnt(eventVal);
      });

    this.mainSub = observableCommonService.anyCntCompare$.subscribe(
      eventVal => {
        this.changeCompareCnt(eventVal);
      });

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.favoriteDialog.closeAll();
      }
    });

    this.favoriteCntSub = this.favoriteService.count.subscribe(val => this.favoriteCnt = val);
  }

  @HostListener('document:click', ['$event'])
  clickHandler(event) {
    if (this.userMenu && !this.userMenu.nativeElement.classList.contains('show')) {
      this.menuOpen = false;
    }
  }

  syncCatalogStruct() {
    this.catalog = this.catalogService.getAllCatalogs();
  }

  ngOnInit() {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }
    if (!this.localStorage.getItem('profile')) {
      this.logout();
    }
    this.initGlobalCnt();
    this.autoCompleteSearch();
    this.syncCatalogStruct();
  }

  private autoCompleteSearch() {
    this.searchField.valueChanges.pipe(debounceTime(400)).subscribe(search_cond => {
      merge(search_cond)
        .pipe(
          startWith({}),
          switchMap(() => {
            this.isLoadingResults = true;

            return this.productService.getAutoCompleteProductForSearch(search_cond);
          }),
          map(data => {
            setTimeout(() => {
              this.isLoadingResults = false;
            }, 500);

            return data;
          }),
          catchError(() => {
            setTimeout(() => {
              this.isLoadingResults = false;
            }, 500);

            return of([]);
          }))
        .subscribe(data => {
          this.dataSource.data = data;
          this.isShowPaginator = data.length > this.searchPageSize;
          this.dataSource.paginator = this.paginator;
        });
    });
  }

  findData() {
    const searchCondition = this.input.nativeElement.value;
    this.searchField.reset();

    if (searchCondition.trim() === '') {
      return;
    }

    this.router.navigate([RouterConst.SEARCH, searchCondition], {relativeTo: this.activatedRoute});
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.searchField.reset();

    const productId = event.option.value.id;
    if (productId !== '' && productId !== undefined) {
      this.router.navigate(['/' + RouterConst.MAINPAGE + '/product/' + productId]);
    }
  }

  openUserMenu() {
    this.menuOpen = !this.menuOpen;
  }

  logout() {
    console.log('пробуем удалить печеньки');
    this.localStorage.removeItem('initials');
    this.localStorage.removeItem('profile');

    this.httpRequest.logout().subscribe((data: any) => {
      console.log('this is logout', data);
      this.handleData_logout(data);
    }, (error: any) => {
      this.handleError_logout(error);
    });

    /*VDN Clear favorite's storage*/
    this.favoriteService.clearStorage();
  }

  private handleData_logout(data: any) {
    if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) || (data.resultCode === ResultCode.SUCCESSFULL)) {
      this.removeToken(GlobalConst.COOKIES_TOKEN);

    }
    if (data.result === GlobalConst.RESPONSE_RESULT_UNAUTHORIZED) {
      this.handleError_logout(data.result);
    }
    if (data.result === GlobalConst.INTERNAL_SERVER_ERROR || data.status === 500) {
      this.handleError_logout('Internal Server Error');
    }
  }

  private handleError_logout(error: any) {
    console.log('login error', error);
    this.removeToken(GlobalConst.COOKIES_TOKEN);
  }

  private removeToken(token: string) {
    console.log(this.cookies.getItem(token));
    this.cookies.removeItem(token);
    //  this.cookies.clear();
    GlobalConst.TOKEN = undefined;
  }

  private getPopup(details: string, message: string) {
    this.router.navigate([{
      outlets: {
        popup: ['compose', {
          details: details,
          message: message
        }]
      }
    }], {relativeTo: this.activatedRoute});
  }

  initBasket() {
    this.observableCommonService.pushShowBasket(true);
  }

  initMenu() {
    this.loginComponent.resetForm();
  }

  private changeBasketCnt(cnt: number) {
    this.basketCnt = (cnt === 0 ? null : cnt);
  }

  private changeCompareCnt(cnt: number) {
    this.compareCnt = (cnt === 0 ? null : cnt);
  }

  private initGlobalCnt() {
    this.changeBasketCnt(this.basketService.getBasketCnt());
    this.changeCompareCnt(this.compareService.getCompareCnt());
  }

  // LAO а отписываться не надо?
  ngOnDestroy() {
    this.mainSub.unsubscribe();
    this.favoriteCntSub.unsubscribe();
  }

  openDialogFavorite() {
    if (!this.authentication.authenticated) {
      this.favoriteDialog.open(FavoriteDialogComponent, {
        width: '770px',
        data: {auth: this.authentication}
      });
    } else {
      this.router.navigate([RouterConst.MAINPAGE + '/' + RouterConst.USERPROFILE + '/' + RouterConst.USERFAVORITE]);
    }
  }
}

/**********************************************************************************************************************/
@Component({
  selector: 'app-favorite-dialog',
  templateUrl: './favorite-dialog.html',
  styleUrls: ['./favorite-dialog.css']
})
export class FavoriteDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              public dialogRef: MatDialogRef<FavoriteDialogComponent>) {
  }
}
