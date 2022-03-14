import {Directive, HostBinding, Input} from "@angular/core";

@Directive({
  selector:'[forNewPrice]'
})
export class NewPriceStyleDirective{
  @Input('value') price:number;

  @HostBinding("style.background-color") get getBackgroundColor(){
    return this.price? "#4267b2": "#ebf0ff";
  }

  @HostBinding("style.color") get getColor(){
    return this.price? "#ffffff": "#575757";
  }

  @HostBinding("style.font-weight") get getFontStyle(){
    return "600";
  }

  constructor(){}
}
