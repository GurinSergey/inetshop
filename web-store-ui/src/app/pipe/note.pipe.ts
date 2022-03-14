import {Pipe, PipeTransform} from "@angular/core";

@Pipe({
  name: 'noteStyle'
})
export class NotePipe implements PipeTransform {

  transform(value: any, ...args): any {
    return value > 0 ? "+"+value : value;
  }
}
