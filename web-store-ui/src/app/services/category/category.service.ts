import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {CategoryModel} from '../../models/category.model';


const PROTOCOL = 'http';
const PORT = 3000;

@Injectable()
export class CategoryService {
  baseUrl: string;

  constructor(private httpClient: HttpClient) {
    this.baseUrl = `${PROTOCOL}://${location.hostname}:${PORT}/`;
  }

  getCategories(): Observable<CategoryModel[]> {
    return this.httpClient.get<CategoryModel[]>(`${this.baseUrl}category/`);
  }

  getCategory(id: number): Observable<CategoryModel> {

    return this.httpClient.get<CategoryModel>(`${this.baseUrl}category/${id}`);
  }

  getParentCategories(): Observable<CategoryModel[]> {
    return this.getCategories().map(c => c.filter(cc => cc.parent_id = cc.id));
  }

  saveCategory(c: CategoryModel): Observable<CategoryModel> {
    if (c.id === 0 || c.id === null) return this.httpClient.post<CategoryModel>(`${this.baseUrl}category/`, c);
    else return this.httpClient.put<CategoryModel>(`${this.baseUrl}category/${c.id}/`, c);
  }

  deleteCategory(id: number): Observable<CategoryModel> {
    return this.httpClient.delete<CategoryModel>(`${this.baseUrl}category/` + id);
  }


}
