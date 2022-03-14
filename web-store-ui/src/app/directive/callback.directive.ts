import {Directive, Input, OnDestroy} from "@angular/core";

@Directive({
  selector:'[method]'
})
export class CallbackDirective implements OnDestroy{
  private called:boolean = false;
  @Input('method') method: Function;
  @Input('clause')
  set condition(value:any){
    if(!value || value == false) return;

    if(!this.called && this.method){
      this.method();
      this.called = true;
    }
  }

  constructor(){}

  ngOnDestroy(): void {
    this.called = false;
  }
}
