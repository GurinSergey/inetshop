import {Component} from "@angular/core";
import {TooltipService} from "../../../services/tooltip/tooltip.service";

@Component({
  selector: 'app-tooltip-container',
  template: `
    <div class="tooltip-container">
      <app-tooltip-content *ngFor="let tooltip of myService.components"
                           [ref]="tooltip.ref"
                           [id]="tooltip.id"
                           [data]="tooltip.data"
                           [width]="tooltip.width"
                           [productId]="tooltip.productId"
                           [latIdent]="tooltip.latIdent">
      </app-tooltip-content>
    </div>
  `
})
export class TooltipContainerComponent {
  myService;

  constructor(private tooltipService: TooltipService) {
    this.myService = tooltipService;
  }
}
