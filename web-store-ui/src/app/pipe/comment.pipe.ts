import {Pipe, PipeTransform} from '@angular/core';
import {Observable} from "rxjs/Rx";

@Pipe({
  name: 'comment'
})
export class CommentPipe implements PipeTransform {

  transform(value: any, args: string): any {
    if(value){
      let text = value == 1? ' отзыв': value > 1 && value < 5? ' отзыва': ' отзывов';
      return value + text;
    }
  }
}
