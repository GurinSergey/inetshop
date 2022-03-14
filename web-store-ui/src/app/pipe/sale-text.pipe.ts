import {Pipe, PipeTransform} from "@angular/core";

@Pipe({
  name: 'sale'
})
export class SaleTextPipe implements PipeTransform {

  transform(value: any, ...args): any {
    return value + ' Специальная цена';
  }
}
