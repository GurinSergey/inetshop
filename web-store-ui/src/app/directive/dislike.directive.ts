import {CommentService} from "../services/comment/comment.service";
import {
  AfterContentChecked, AfterContentInit, Directive, ElementRef, HostBinding, HostListener, Input,
  Renderer2
} from "@angular/core";
import {PopupService} from "../components/common-group/compose-message/popup.service";
import {ActivatedRoute, Router} from "@angular/router";
import {PopupStyle} from "../globals/popup-style.enum";
import {CommentEntry} from "../models/comment-entry";
import {Product} from "../models/product";

@Directive({
  selector: '[dislike]'
})
export class DislikeDirective implements AfterContentChecked{
  @Input('data') product: Product;
  @Input('comment') comment: CommentEntry;
  @HostBinding('style.color') color: string;

  constructor(
    private element: ElementRef,
    private commentService: CommentService,
    private activateRoute: ActivatedRoute,
    private router: Router,
    private popupService: PopupService
  ) {
    this.popupService.setRouter(this.router);
    this.popupService.setActivatedRoute(this.activateRoute);
  }

  @HostListener("click") dislike(){
    this.commentService.dislike(this.product.baseInfo.id, this.comment.id)
      .subscribe((data:boolean) => {
        this.comment.vote.negative = data;
        this.comment.note--;
        this.product.ratingInfo.addNegative();
        this.popupService.popupView(`Ваше мнение учтено`, '', 3, PopupStyle.SUCCESS);
      }, (data:any) => {
        this.popupService.popupView(`Внимание`, data, 3, PopupStyle.INFO);
      });
  }

  ngAfterContentChecked(): void {
    if(this.comment.vote){
      this.color = this.comment.vote.negative? '#202326': '#f44336';
      this.element.nativeElement.disabled = this.comment.vote.negative;
    }
  }
}
