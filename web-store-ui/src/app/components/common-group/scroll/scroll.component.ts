import {
  Component, OnInit, Input, ViewChild, ElementRef, AfterContentChecked
} from '@angular/core';

@Component({
  selector: 'app-scroll',
  templateUrl: './scroll.component.html',
  styleUrls: ['./scroll.component.css']
})
export class ScrollComponent implements OnInit, AfterContentChecked {
  @Input() title: string;
  @ViewChild('scrollMenu') scrollMenu: ElementRef;

  leftArrowVisible = false;
  rightArrowVisible = false;

  scrollWidth = 0;
  clientWidth = 0;
  scrollLeft = 0;

  constructor() {
  }

  setParams() {
    this.scrollWidth = this.scrollMenu.nativeElement.scrollWidth;
    this.clientWidth = this.scrollMenu.nativeElement.clientWidth;
    this.scrollLeft = this.scrollMenu.nativeElement.scrollLeft;
  }

  ngOnInit() {
  }

  ngAfterContentChecked() {
    this.scrolling();
  }

  scrolling() {
    this.setParams();

    this.rightArrowVisible = Math.round(this.scrollLeft) < Math.round(this.scrollWidth - this.clientWidth);
    this.leftArrowVisible = this.scrollLeft > 0;
  }

  moveRight(event) {
    for (let i = 1; i <= 50; i++) {
      setTimeout(function (ref) {
        return function () {
          ref.scrollLeft += 10;
        };
      }(this.scrollMenu.nativeElement), 1000 * i / 60);
    }
  }

  moveLeft(event) {
    for (let i = 1; i <= 50; i++) {
      setTimeout(function (ref) {
        return function () {
          ref.scrollLeft -= 10;
        };
      }(this.scrollMenu.nativeElement), 1000 * i / 60);
    }
  }
}
