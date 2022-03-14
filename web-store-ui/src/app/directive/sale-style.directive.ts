import {Directive, HostBinding, Input} from "@angular/core";

@Directive({
  selector:'[forSale]'
})
export class SaleStyleDirective{
  @HostBinding("style.color") get getColor(){
    return "#FF6968";
  }

  constructor(){}
}
