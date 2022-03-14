import {Injectable} from '@angular/core';
import {ProductFilterService} from '../product-filter/product-filter.service';
import {ProductList} from '../../models/product-list';
import {Observable} from 'rxjs/index';

@Injectable()
export class ProductSortService {
  constructor(private productFilterService: ProductFilterService) {
  }

  findAll(templateId:number, offset: number, limit: number, sort_direction: string, sort_property: string): Observable<ProductList> {
    return this.productFilterService.findAll(templateId, offset, limit, sort_direction, sort_property);
  }
}
