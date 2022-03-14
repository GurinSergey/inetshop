import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import {GlobalConst} from "../../globals/GlobalConst";

@Injectable()
export class CookieStorage implements Storage {
  [index: number]: string;
  [key: string]: any;
  length: number;

  constructor(private cookieService: CookieService) {
  }

  public clear(): void {
    this.cookieService.deleteAll('/',GlobalConst.Domain);
  }

  public getItem(key: string): string {
    return this.cookieService.get(key);
  }

  public key(index: number): string {
    return this.cookieService.getAll().propertyIsEnumerable[index];
  }

  public removeItem(key: string): void {
    console.log('removeItem browser');
    console.log(key);
    this.cookieService.delete(key,'/',GlobalConst.Domain);
    this.cookieService.set(key, '', new Date("Thu, 01 Jan 1970 00:00:01 GMT"));
  }

  public setItem(key: string, data: string): void {
    //this.cookieService.set(key, data);
    this.cookieService.set(key, data,10,'/',GlobalConst.Domain);
  }
}
