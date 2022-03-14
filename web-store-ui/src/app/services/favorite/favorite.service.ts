import {Inject, Injectable, PLATFORM_ID} from "@angular/core";
import {HttpRequestsService} from "../http-requests/http-requests.service";
import {BehaviorSubject} from "rxjs/index";
import {AppLocalStorageService} from "../../components/common-group/app-local-storage/app-local-storage.service";
import {Product} from "../../models/product";
import {isPlatformBrowser} from "@angular/common";
import {map} from "rxjs/internal/operators";
import {GlobalConst} from "../../globals/GlobalConst";
import {ProductService} from "../product/product.service";

@Injectable()
export class FavoriteService{
  private readonly complexFavoriteStorageName = 'ComplexFavoriteStorage';

  private productList: Product[] = [];
  count: BehaviorSubject<number> = new BehaviorSubject<number>(0);

  constructor(
    private httpRequest: HttpRequestsService,
    @Inject(PLATFORM_ID) private platformId: Object,
    private httpRequestsService: HttpRequestsService,
    private productService: ProductService
  ) {
    this.syncFavoriteList();
  }

  syncFavoriteList(){
    if (!isPlatformBrowser(this.platformId))
      return;

    this.httpRequestsService.synchronizeFavorite()
      .pipe(
        map((data:any) => {
          if (data.result !== GlobalConst.RESPONSE_RESULT_SUCCESS)
            throw data.payload;
          this.productList = [];
          data.payload.map((item:any) => this.productList.push(this.productService.getProductEntry(item.product)))
        }))
      .subscribe( (data:any) => {
        this.count.next(this.productList.length);
    });
  }

  isExistsInFavoriteBox(id:number):boolean{
    return (this.productList.filter(p => {return p.baseInfo.id === id}).length !== 0);
  }

  getFavoriteList():Product[]{
    return this.productList;
  }

  addToFavorite(product:Product){
    if(this.isExistsInFavoriteBox(product.baseInfo.id))
      return;

    product.baseInfo.isFavorite = true;
    this.httpRequestsService.addToFavorite(product.baseInfo.id).pipe(
      map((data:any) => {
        if (data.result !== GlobalConst.RESPONSE_RESULT_SUCCESS)
          throw data.payload;
        data.map(item => this.productList.push(this.productService.getProductEntry(item.product)))
      }))
      .subscribe( (data:any) => {
        this.count.next(this.productList.length);
      });
  }

  deleteFromFavorite(product: Product){
  }

  clearStorage(){

  }
}
