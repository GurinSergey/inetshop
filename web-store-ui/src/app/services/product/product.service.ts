import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

import {HttpRequestsService} from '../http-requests/http-requests.service';
import {ProductList} from '../../models/product-list';
import {ProductEntry} from '../../models/product-entry';
import {Product} from '../../models/product';
import {GlobalConst} from '../../globals/GlobalConst';
import {BasketService} from '../basket/basket.service';
import {CompareService} from '../compare/compare.service';
import {ResultCode} from '../../globals/result-code.enum';
import {Router} from '@angular/router';
import {FieldEntry, FieldList} from '../../models/field-list';
import {AppLocalStorageService} from '../../components/common-group/app-local-storage/app-local-storage.service';
import {map} from "rxjs/internal/operators";
import {RatingEntry} from "../../models/rating-entry";
import {PriceEntry} from "../../models/price-entry";
import {ProducerEntry} from "../../models/producer-entry";
import {PhotoEntry} from "../../models/photo-entry";
import {ProductBuilder} from "../../models/product.model";
import {SearchProductsEntry} from "../../models/search-products-entry";
import {FilterEntry} from "../../models/filter-entry";
import {SliderEntry} from "../../models/slider-entry";

@Injectable()
export class ProductService {
  product: Product;

  constructor(private httpRequestsService: HttpRequestsService,
              private basketService: BasketService,
              private compareService: CompareService,
              //private favoriteService: FavoriteService,
              private appLocalStorage: AppLocalStorageService,
              private router: Router) {
  }

  getProducerEntry(data) {
    if (!data) return null;
    return {
      id: data.id, name: data.name, country: data.country, path_flag: data.pathFlag
    } as ProducerEntry;
  }

  getPhotos(data): PhotoEntry[] {
    if (!data) return null;
    return data.map((item: any) => {
      return new PhotoEntry(item.id, GlobalConst.SERVER_IMG_ADDR + item.path);
    });
  }

  getSale(data): PriceEntry {
    if (!data.sale)
      return {
        current_price: data.price,
        new_future_price: 2995,
        sale_price: 1450,
        exists_sale: false
      } as PriceEntry;
  }

  getRating(data): RatingEntry {
    if (!data)
      return new RatingEntry();
    return new RatingEntry(
      data.comments_statistics.positiveCnt,
      data.comments_statistics.negativeCnt,
      data.totalRating);
  }

  getEntry(data): ProductEntry {
    const imagePath = data.photos[0] ? GlobalConst.SERVER_IMG_ADDR + data.photos[0].path : null;
    const newProduct = new ProductEntry(data.id, data.title, data.code, data.price, imagePath);
    newProduct.latIdent = data.latIdent;
    newProduct.description = data.description;
    newProduct.templateId = data.template.id;
    newProduct.templateName = data.template.name;

    newProduct.isInBasketBox = this.basketService.isExistsProduct(newProduct.id);
    newProduct.isInCompareBox = this.compareService.existsInCompareBox(newProduct.id);
    //newProduct.isFavorite = this.favoriteService.isExistsInFavoriteBox(newProduct.id);
    return newProduct;
  }

  getProductEntry(data: any) {
    let product = new Product(this.getEntry(data));
    product.priceInfo = this.getSale(data);
    product.ratingInfo = this.getRating(data.statistics);
    product.producerInfo = this.getProducerEntry(data.producer);
    product.photosInfo = this.getPhotos(data.photos);
    product.commentInfo.totalCount = data.statistics.totalComments;

    return product;
  }

  getProductPhotos(id: number): Observable<PhotoEntry[]> {
    return this.httpRequestsService.getProductPhotos(id).pipe(
      map((data: any) => {
        if (data.result !== GlobalConst.RESPONSE_RESULT_SUCCESS)
          throw "Ошибка";
        return this.getPhotos(data.payload);
      })
    );
  }

  getProductFields(id: number): Observable<FieldList> {
    return this.httpRequestsService.getProductFields(id).pipe(
      map((data: any) => {
        const fieldList: FieldList = new FieldList();
        data.payload.map(productField => fieldList.list.push(new FieldEntry(productField.id, productField.name, productField.data.value)));
        return fieldList;
      }));
  }

  findAll(templateId: number, offset: number = 0, limit: number = 25, sort_direction: string = 'DESC', sort_property: string = 'id', filter: string = ''): Observable<ProductList> {
    return this.httpRequestsService.getProducts(templateId, offset, limit, sort_direction, sort_property, filter).pipe(
      map((data: any) => {
        if (data.result !== GlobalConst.RESPONSE_RESULT_SUCCESS)
          throw "Ошибка";

        const cnt = data.payload.count;
        const results = data.payload.results;
        return {
          count: cnt, products: results.map((r: any) => {
            return this.getProductEntry(r);
          })
        };
      }));
  }

  searchProducts(templates_offset: number = 0, templates_limit: number = GlobalConst.SEARCH_TEMPLATES_LIMIT, products_limit: number = GlobalConst.SEARCH_PRODUCTS_LIMIT, filter_string: string = ''): Observable<SearchProductsEntry> {
    return this.httpRequestsService.searchProducts(templates_offset, templates_limit, products_limit, filter_string).pipe(
      map((data: any) => {
        const allTemplatesCnt = data.payload.allTemplatesCnt;
        const searchProductsCnt = data.payload.searchProductsCnt;
        const results = data.payload.results;
        return {
          allTemplatesCnt: allTemplatesCnt,
          searchProductsCnt: searchProductsCnt,
          searchProductsInfo: results.map((sp: any) => {
            return {
              templateId: sp.templateId,
              templateName: sp.templateName,
              products: sp.results.map((p: any) => {
                return this.getProductEntry(p);
              })
            };
          })
        };
      }));
  }

  findOne(latIdent: string): Observable<Product> {
    return this.httpRequestsService.getOneProduct(latIdent).pipe(map((data: any) => {
      if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) && (data.resultCode === ResultCode.SUCCESSFULL)) {
        this.appLocalStorage.customSync(); // LAO обновим инфу по истории просмотров
        return this.getProductEntry(data.payload);
      } else {
        this.router.navigate(['/']);
        return null;
      }
    }));
  }

  addToWatched(id: number) {
    this.httpRequestsService.addToWatch(id);
  }

  getAutoCompleteProductForSearch(match: String): Observable<Product[]> {
    return this.httpRequestsService.getAutoCompleteProductForSearch(match).pipe(map((data: any) => {
      if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) && (
          (data.resultCode === ResultCode.SUCCESSFULL))) {
        const productList = data.payload.map((data: any) => {
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
            data.photos).build();
        });

        return productList;
      }

      this.router.navigate(['/']);
      return null;
    }));
  }

  getTheBestSellingProducts(limit: number): Observable<SliderEntry[]> {
    return this.httpRequestsService.getTheBestSellingProducts(limit).pipe(map((data: any) => {
      if (data.result !== GlobalConst.RESPONSE_RESULT_SUCCESS) {
        throw "Ошибка";
      }

      const sliderList = data.payload.map((d: any) => {
        return new SliderEntry(d.latIdent, d.title, d.description, d.code, GlobalConst.SERVER_IMG_ADDR + d.url_image);
      });

      return sliderList;
    }));
  }
}
