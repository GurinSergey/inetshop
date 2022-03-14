import {Injectable, ViewChild} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {ProductList} from '../../../models/product-list';
import {ObservableZhdunService} from '../../../services/observable/observable-zhdun.service';
import {GlobalConst} from "../../../globals/GlobalConst";
import {map} from "rxjs/internal/operators";
import {ProductFilterService} from "../../../services/product-filter/product-filter.service";
import {HttpRequestsService} from "../../../services/http-requests/http-requests.service";
import {ProductService} from "../../../services/product/product.service";
import {SearchProductsEntry} from "../../../models/search-products-entry";

@Injectable()
export class SearchResolver implements Resolve<ProductList> {
  searchProductsEntry: SearchProductsEntry;

  constructor(private productService: ProductService,
              private zhdun: ObservableZhdunService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {
    this.zhdun.on();

    const filter_str = route.paramMap.get('condition');

    return this.productService.searchProducts(0, GlobalConst.SEARCH_TEMPLATES_LIMIT, GlobalConst.SEARCH_PRODUCTS_LIMIT, filter_str).pipe(map(data => {
      this.searchProductsEntry = data;

      this.zhdun.off();
      return this.searchProductsEntry;
    }));
  }
}
