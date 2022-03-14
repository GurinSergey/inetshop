import { WINDOW } from '@ng-toolkit/universal';
import { Component, OnInit, Input, OnChanges, Output, EventEmitter , Inject} from '@angular/core';
import {Observable} from "rxjs/index";
import "rxjs-compat/add/observable/range";
import "rxjs-compat/add/operator/map";
import "rxjs-compat/add/operator/filter";
import "rxjs-compat/add/operator/toArray";
import { range } from "rxjs";


@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnInit, OnChanges {
  @Input() offset: number = 0;
  @Input() limit: number = 1;
  @Input() size: number = 1;
  @Input() range: number = 2;

  @Output() changePage: EventEmitter<number> = new EventEmitter<number>();

  currentPage: number;
  totalPages: number;
  pages: Observable<number[]>;

  constructor(@Inject(WINDOW) private window: Window) { }

  ngOnInit() {
    this.getPages(this.offset, this.limit, this.size);
  }

  ngOnChanges(){
    this.getPages(this.offset, this.limit, this.size);
  }

  isCurrentPage(page:number){
    return page === this.currentPage;
  }

  selectPage(page:number, event){
    this.cancelEvent(event);

    if(this.isCurrentPage(page))
      return;

    if(this.isValidPageNumber(page, this.totalPages)){
      this.changePage.emit(page - 1);
      this.window.scroll({top: 0, left: 0, behavior: 'smooth' });
    }
  }

  isValidPageNumber(page: number, totalPages: number): boolean {
    return page > 0 && page <= totalPages;
  }

  getPages(offset: number, limit: number, size: number){
    this.currentPage = this.getCurrentPage(offset, limit);
    this.totalPages = this.getTotalPages(limit, size);

    this.pages = range(-this.range, this.range * 2 + 1)
      .map(offset => this.currentPage + offset)
      .filter(page => this.isValidPageNumber(page, this.totalPages))
      .toArray();
  }

  getCurrentPage(offset: number, limit: number): number {
    return offset + 1;
  }

  getTotalPages(limit: number, size: number): number {
    return Math.ceil(Math.max(size, 1) / Math.max(limit, 1));
  }

  cancelEvent(event) {
    event.preventDefault();
  }

}
