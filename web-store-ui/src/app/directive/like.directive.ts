import {
  AfterContentChecked, AfterContentInit, Directive, ElementRef, HostBinding, HostListener,
  Input
} from '@angular/core';
import {CommentService} from "../services/comment/comment.service";
import {ActivatedRoute, Router} from "@angular/router";
import {PopupService} from "../components/common-group/compose-message/popup.service";
import {PopupStyle} from "../globals/popup-style.enum";
import {CommentEntry} from "../models/comment-entry";
import {Product} from "../models/product";

@Directive({
  selector: '[like]'
})
export class LikeDirective implements AfterContentChecked{
  @Input('data') product: Product;
  @Input('comment') comment: CommentEntry;
  @HostBinding('style.color') color: string;

  constructor(
    private element: ElementRef,
    private commentService: CommentService,
    private activateRoute: ActivatedRoute,
    private router: Router,
    private popupService: PopupService)
  {
    this.popupService.setRouter(this.router);
    this.popupService.setActivatedRoute(this.activateRoute);
  }

  @HostListener("click") like(){
    this.commentService.like(this.product.baseInfo.id, this.comment.id)
      .subscribe((data:boolean) => {
        this.comment.vote.positive = data;
        this.comment.note++;
        this.product.ratingInfo.addPositive();
        this.popupService.popupView(`Ваше мнение учтено`, '', 3, PopupStyle.SUCCESS);
      }, (data:any) => {
        this.popupService.popupView(`Внимание`, data, 3, PopupStyle.INFO);
      });
  }

  ngAfterContentChecked(): void {
    if(this.comment.vote){
      this.color = this.comment.vote.positive? '#202326': '#4CAF50';
      this.element.nativeElement.disabled = this.comment.vote.positive;
    }
  }


}
