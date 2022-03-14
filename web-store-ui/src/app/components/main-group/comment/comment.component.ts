import { Inject } from '@angular/core';
import { WINDOW } from '@ng-toolkit/universal';
import {
  AfterViewInit, Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild,
  ViewChildren
} from "@angular/core";
import {CommentEntry, CommentEntryBuilder} from "../../../models/comment-entry";
import {PopupStyle} from "../../../globals/popup-style.enum";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthorizeConst} from "../../../globals/AuthorizeConst";
import {Authentication} from "../../common-group/auth/authentication";
import {PopupService} from "../../common-group/compose-message/popup.service";
import {CommentService} from "../../../services/comment/comment.service";
import {ActivatedRoute, Router} from "@angular/router";
import {GlobalConst} from "../../../globals/GlobalConst";
import {Product} from "../../../models/product";

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnInit, AfterViewInit{
  @Input() _product: Product;
  commentForm: FormGroup;
  answerForm: FormGroup;
  navigateToCommentId:number;

  @Input() isLoadingComments: boolean = false;
  deactivate: boolean = false;
  submitAttempt: boolean = false;
  deactivateFormAnswer: boolean = false;
  submitAnswer: boolean = false;

  offset: number = 0;
  limit: number = GlobalConst.PRODUCT_COMMENTS_LIMIT;
  star_titles = GlobalConst.STAR_TITLES;
  @ViewChild('answerMenu') answerMenu: ElementRef;
  @ViewChildren('comments') comments: ElementRef;

  @Output() changePage: EventEmitter<number> = new EventEmitter<number>();

  test:boolean = true;

  constructor(@Inject(WINDOW) private window: Window, 
    private fBuilder: FormBuilder,
    private authentication: Authentication,
    private popupService: PopupService,
    private commentService: CommentService,
    private activateRoute: ActivatedRoute,
    private router: Router
  ){
    this.popupService.setRouter(this.router);
    this.popupService.setActivatedRoute(this.activateRoute);
    this.popupService.setPage('main-page');

    let profile = this.authentication.getProfile;
    this.commentForm = fBuilder.group({
      'userName': [profile.personName, [Validators.required]],
      'email': [profile.userName, [Validators.required, Validators.pattern(AuthorizeConst.emailPattern)]],
      'commentText': ['', Validators.required],
      'rating':['0']
    });

    this.answerForm = fBuilder.group({
      'userName': [profile.personName, [Validators.required]],
      'answerText': ['', Validators.required]
    });

    if(this.activateRoute.snapshot.queryParamMap.get('comment'))
      this.navigateToCommentId = Number(this.activateRoute.snapshot.queryParamMap.get('comment'));
  }
  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.navigateToComment(this.navigateToCommentId);
  }

  navigateToComment(commentId:number = 0){
    let parent = document.getElementById('comment' + commentId);
    if(parent){
      let position = parent.getBoundingClientRect();
      this.window.scroll({top: position.top - (this.window.innerHeight / 2) + position.height, left: 0, behavior: 'smooth' });
    }
  }

  checkFormControls(form: FormGroup) {
    Object.keys(form.controls).forEach(key => {
      let text = form.get(key).value.toString();
      form.controls[key].setValue(text.trim());
    });
    return form.valid;
  }

  submitCommentClick() {
    this.submitAttempt = true;
    if (!this.checkFormControls(this.commentForm))
      return false;

    this.deactivate = !this.deactivate;

    let newComment = new CommentEntryBuilder()
      .setAuthor(this.commentForm.controls['userName'].value)
      .setDescription(this.commentForm.controls['commentText'].value)
      .setStar(this.commentForm.controls['rating'].value)
      .buildEntry();

    const subscription = this.commentService.addNewComment(this._product.baseInfo.id, newComment)
      .subscribe(
        (comment: CommentEntry) => {
          this._product.commentInfo.commentList.unshift(comment);
        }, (data: any) => {
          this.popupService.popupView(`Ошибка`, data, 3, PopupStyle.INFO);
        }, () => {
          this.popupService.popupView(`Комментарий сохранен успешно`, '', 3, PopupStyle.SUCCESS);
          this._product.commentInfo.totalCount++;
          this.submitAttempt = false;
        });

    subscription.add(() => {
      this.deactivate = !this.deactivate;
      this.commentForm.controls['commentText'].setValue(' ');
      this.commentForm.controls['rating'].setValue('');
    });
  }

  submitAnswerClick(commentIndex: number) {
    this.submitAnswer = true;
    if (!this.checkFormControls(this.answerForm))
      return;

    this.deactivateFormAnswer = true;

    let newComment = new CommentEntryBuilder()
      .setAuthor(this.answerForm.controls['userName'].value)
      .setDescription(this.answerForm.controls['answerText'].value)
      .setParentId(this._product.commentInfo.commentList[commentIndex].id)
      .buildEntry();

    const subscription = this.commentService.addNewComment(this._product.baseInfo.id, newComment)
      .subscribe(
        (comment: CommentEntry) => {
          this._product.commentInfo.commentList[commentIndex].children.push(comment);
        }, (data: any) => {
          this.popupService.popupView(`Ошибка`, data, 3, PopupStyle.INFO);
        }, () => {
          this.popupService.popupView(`Комментарий сохранен успешно`, '', 3, PopupStyle.SUCCESS);
          this.answerMenu.nativeElement.classList.remove('show');
          this._product.commentInfo.totalCount++;
          this.submitAnswer = false;
        });

    subscription.add(() => {
      this.deactivateFormAnswer = !this.deactivateFormAnswer;
      this.answerForm.controls['answerText'].setValue(' ');
    });
  }

  onChangePage(offset: number) {
    this.offset = offset;
    this.changePage.emit(this.offset);
  }

}
