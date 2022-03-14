import {Directive, ElementRef, HostListener, Input, OnDestroy} from "@angular/core";
import {TooltipService} from "../services/tooltip/tooltip.service";

@Directive({
  selector: '[tooltip]'
})
export class TooltipDirective implements OnDestroy{
  @Input() productId:number;
  @Input() latIdent:string;
  @Input() data:any;
  @Input() width:number;
  private id:number;

  constructor(
    private tooltipService: TooltipService,
    private element: ElementRef
  ){}

  @HostListener('click')
  onClick():void{
    this.tooltipService.components = [];

    if(!this.data) this.data = {};
    this.id = Math.random();
    this.tooltipService.components.push(
      { id: this.id,
        width: this.width,
        productId: this.productId,
        latIdend : this.latIdent,
        ref: this.element,
        data: this.data});
  }

  ngOnDestroy(): void {
    this.tooltipService.destroy(this.id);
  }
}
