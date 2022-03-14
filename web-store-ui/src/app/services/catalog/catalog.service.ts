import {Injectable} from '@angular/core';
import {HttpRequestsService} from '../http-requests/http-requests.service';
import {map} from "rxjs/internal/operators";
import {Observable} from 'rxjs';
import {CatalogEntry, TemplateEntry} from "../../models/catalog-entry";
import {GlobalConst} from "../../globals/GlobalConst";
import {BehaviorSubject} from "rxjs/index";

@Injectable()
export class CatalogService {
  private obsCatalogName:BehaviorSubject<string>;
  private obsCatalogStruct:BehaviorSubject<CatalogEntry[]>;

  constructor(private httpRequest: HttpRequestsService) {
    this.obsCatalogStruct = new BehaviorSubject<CatalogEntry[]>([]);
    this.obsCatalogName = new BehaviorSubject<string>("");
  }

  setCatalogName(value:string){
    this.obsCatalogName.next(value);
  }

  get catalogName(){
    return this.obsCatalogName.asObservable();
  }

  get catalog(){
    return this.obsCatalogStruct.asObservable();
  }

  getCatalogEntry(item:any){
    let catalog = new CatalogEntry(item.id, item.title);
    if(item.listItem)
      catalog.children = this.getCatalogStruct(item.listItem);
    if(item.template)
      catalog.template = new TemplateEntry(item.template.id, item.template.name);
    catalog.photo_path = GlobalConst.SERVER_IMG_ADDR + item.photoPath;

    return catalog;
  }

  getCatalogStruct(data:any):CatalogEntry[]{
    let result = [];
    data.map(item => {
      result.push(this.getCatalogEntry(item));
    });
    return result;
  }

  getAllCatalogs():Observable<CatalogEntry[]>{
    return this.httpRequest.getAllCatalogs().pipe(
      map((data:any) => {
        if (!(data.result === GlobalConst.RESPONSE_RESULT_SUCCESS))
          throw data.payload;
        let currentCatalogStruct = this.getCatalogStruct(data.payload);
        this.obsCatalogStruct.next(currentCatalogStruct);
        return currentCatalogStruct;
      })
    );
  }

  getChildrenCatalog(catalogId:number):Observable<CatalogEntry>{
    return this.httpRequest.getChildrenCatalog(catalogId).pipe(
      map((data:any) => {
        if (!(data.result === GlobalConst.RESPONSE_RESULT_SUCCESS))
          throw data.payload;
        return this.getCatalogEntry(data.payload);
      })
    );
  }
}
