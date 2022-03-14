import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'currencyLocal'
})
export class CurrencyLocalPipe implements PipeTransform {

  transform(value: any, args: string): any {
    if (!value) return value;
    let currency = args === undefined ? 'UAH' : args;

    return value.toLocaleString(
      'uk-UA',
      {
        style: 'currency',
        currency: currency,
        minimumFractionDigits: 0
      });
  }

}
