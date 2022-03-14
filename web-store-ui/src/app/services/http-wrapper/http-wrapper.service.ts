import {Inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {GlobalConst} from '../../globals/GlobalConst';
import {AppStorage} from "../../shared/for-storage/universal.inject";




@Injectable()
export class HttpWrapperService {

  constructor(private http: HttpClient, @Inject(AppStorage) private cookies: Storage) {
  }

  getWithoutHeaders(url: string) {
    return this.http.get(url);
  }

  get(url: string) {
    return this.http.get(url, {headers: this.getHeaders()});
  }

  getJSessions(url: string) {
    return this.http.get(url, {headers: this.getHeaders(), withCredentials: true});
  }

  getWithParams(url: string, params: HttpParams) {
    return this.http.get(url, {headers: this.getHeaders(), params: params});
  }

  post(url: string, body: any) {
    return this.http.post(url, body);
  }

  postJWT(url: string, body: any) {
    return this.http.post(url, body, {headers: this.getHeaders()});
  }

  postJSessions(url: string, body: any) {
    return this.http.post(url, body, {headers: this.getHeaders(), withCredentials: true});
  }

  getHeaders() {
    let token = GlobalConst.TOKEN;
    if (token === undefined) {
      token = this.cookies.getItem(GlobalConst.COOKIES_TOKEN);
    }
    let headers = new HttpHeaders();
    headers = headers.set('X-AUTH-TOKEN', token ? token : '');
    headers = headers.set('Cross-Origin', 'true');
    return headers;
  }
}
