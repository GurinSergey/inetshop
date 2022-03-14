import { Inject } from '@angular/core';
import { WINDOW } from '@ng-toolkit/universal';
import {
  AfterContentInit, Component, ElementRef, HostListener, Input,
  OnInit
} from "@angular/core";
import {TooltipService} from "../../../services/tooltip/tooltip.service";
import {ProductService} from "../../../services/product/product.service";
import {Observable} from "rxjs/Rx";
import {forkJoin} from "rxjs/index";
import {FieldEntry, FieldList} from "../../../models/field-list";

@Component({
  selector: 'app-tooltip-content',
  templateUrl: './tooltip.component.html',
  styleUrls: ['./tooltip.component.css']
})
export class TooltipContentComponent implements OnInit, AfterContentInit {
  @Input() ref: ElementRef;
  @Input() id: number;
  @Input() data: any;
  @Input() productId: number;
  @Input() latIdent: string;
  @Input() width: number;

  top: number = 0;
  left: number = 0;
  right_marker: number = 0;

  photoPath: string;
  fields: FieldList;
  details: any[];
  info: any;
  loading: boolean;

  constructor(@Inject(WINDOW) private window: Window, private currentRef: ElementRef,
              private tooltipService: TooltipService,
              private productService: ProductService) {
  }

  destroy() {
    this.tooltipService.destroy(this.id);
  }

  @HostListener('document:click', ['$event'])
  clickHandler(event) {
    if (this.currentRef.nativeElement.children[0].classList.contains('show'))
      this.destroy();
  }

  ngOnInit() {
    this.width = this.width || 350;
    this.loading = true;
    let photosGet = this.productService.getProductPhotos(this.productId);
    let fieldGet = this.productService.getProductFields(this.productId);

    forkJoin(photosGet, fieldGet).subscribe(result => {
      this.photoPath = result[0][0].path;
      this.fields = result[1];
      this.loading = false;
    });

    if (Array.isArray(this.data))
      this.details = this.data;
    else
      this.info = this.data;
  }

  ngAfterContentInit(): void {
    let parent = this.ref.nativeElement.getBoundingClientRect();
    let top = parent.bottom + this.window.scrollY + 8;
    let left = parent.left + this.window.scrollX + parent.width - (this.width + 5);
    let right_marker = 0;

    let border = document.getElementById('tooltipBlockBorder') as any;
    if (border) {
      border = border.getBoundingClientRect().left;
      if (left < border) {
        right_marker = border - left;
        left = border;
      }
    }

    this.top = top;
    this.left = left;
    this.right_marker = right_marker;
  }
}
