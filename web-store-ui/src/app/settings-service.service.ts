import { Injectable } from '@angular/core';

@Injectable()
export class SettingsService {

  /*Angular follows the Unicode LDML convention that uses stable identifiers
  (Unicode locale identifiers) based on the norm BCP47. It is very important that you follow this convention when you define your locale,
   because the Angular i18n tools use this locale id to find the correct corresponding locale data.
   see^ https://github.com/angular/angular/tree/master/packages/common/locales*/
  private _language:string = 'ru-UA';


  constructor() { }


  getLanguage(): string {
    return this._language;
  }
}
