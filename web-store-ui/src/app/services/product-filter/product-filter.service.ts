import {Injectable} from '@angular/core';
import {ProductService} from '../product/product.service';
import {ProductList} from '../../models/product-list';
import {Observable, BehaviorSubject} from 'rxjs';
import {Filter, FilterEntry, FilterValueEntry} from "../../models/filter-entry";
import {GlobalConst} from "../../globals/GlobalConst";
import {ResultCode} from "../../globals/result-code.enum";
import {map} from "rxjs/internal/operators";
import {HttpRequestsService} from "../http-requests/http-requests.service";

@Injectable()
export class ProductFilterService {
  private filter:FilterEntry[];
  private filterValueChips:FilterValueEntry[];
  private obsFilterValueChips:BehaviorSubject<FilterValueEntry[]>;

  constructor(private httpRequest: HttpRequestsService, private productService: ProductService) {
    this.filterValueChips = [];
    this.obsFilterValueChips = new BehaviorSubject<FilterValueEntry[]>([]);
  }

  setFilter(filter:FilterEntry[]) {
    this.filter = filter;

    this.filterValueChips = [];
    this.filter.map(item => {this.filterValueChips = this.filterValueChips.concat(item.params);});
    this.obsFilterValueChips.next(Object.assign([], this.filterValueChips));
  }

  get valueChips(){
    return this.obsFilterValueChips.asObservable();
  }

  getFilterEntry(data:any):FilterEntry{
    let filter = new FilterEntry();
    filter.id = data.id;
    filter.name = data.name;

    data.templateFieldsValues.map(item => {
      filter.params.push(new FilterValueEntry(item.id, item.value));
    });

    return filter;
  }

  private createFilterString():string{
    if(!this.filter)
      return;
    return '&filter='.concat(this.filter.map(filter => {
      if(filter.name == 'price')
        return filter.name.concat('=', filter.params.map(p => p.value).join(""));
      return filter.id.toString().concat('=', filter.params.map(value => value.id).toString());
    }).join(';'));
  }

  findAll(templateId:number, offset: number, limit: number, sort_direction: string = 'DESC', sort_property: string = 'id'): Observable<ProductList> {
    let filterString = this.createFilterString();
    return this.productService.findAll(templateId, offset, limit, sort_direction, sort_property, filterString);
  }

  findAllCtnByFilter(templateId:number):Observable<any>{
    return this.httpRequest.findCntByFilter(templateId, this.createFilterString() || '').pipe(
      map((data:any) => {
        if (!(data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) && (data.resultCode === ResultCode.SUCCESSFULL))
          throw (data.payload);
        return data.payload;
      })
    );
  }


  getFilter(templateId:number):Observable<Filter>{
    return this.httpRequest.getFilterListByTemplateId(templateId).pipe(
      map((data:any) => {
        if (!(data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) && (data.resultCode === ResultCode.SUCCESSFULL))
          throw (data.payload);

        const result:FilterEntry[] = [];
        data.payload.map(item => result.push(this.getFilterEntry(item)));

        return new Filter(data.payload.name, result);
      })
    );
  }
}
