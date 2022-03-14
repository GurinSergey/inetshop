import {
  Component, ElementRef, OnInit, ViewChild, Input, OnChanges, SimpleChanges, DoCheck, AfterViewInit, HostListener
} from '@angular/core';
import {SliderEntry} from '../../../models/slider-entry';

declare var $: any;

@Component({
  selector: 'app-slider',
  templateUrl: './slider.component.html',
  styleUrls: ['./slider.component.css']
})
export class SliderComponent implements OnInit, AfterViewInit, OnChanges {
  _items: SliderEntry[] = [];
  isLoading = true;

  interval = 3500;

  @Input() title: string;

  @Input() set items(items: SliderEntry[]) {
    if (items.length === 0) {
      return;
    }

    this._items = items;

    this.isLoading = false;
  }

  constructor() {
  }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
  }

  ngAfterViewInit() {
    $('#bestSellingCarousel').carousel({
      interval: this.interval
    });
  }

  cook() {
    $('#bestSellingCarousel .carousel-item').each(function () {
      var next = $(this).next();
      if (!next.length) {
        next = $(this).siblings(':first');
      }
      next.children(':first-child').clone().appendTo($(this));

      for (var i = 0; i < 2; i++) {
        next = next.next();
        if (!next.length) {
          next = $(this).siblings(':first');
        }

        next.children(':first-child').clone().appendTo($(this));
      }
    });
  }
}
