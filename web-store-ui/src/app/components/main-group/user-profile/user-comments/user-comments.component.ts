import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CommentBasic} from "../../../../models/comment-basic";
import {CommentBasicList} from "../../../../models/comment-basic";
import {CommentService} from "../../../../services/comment/comment.service";
import {GlobalConst} from "../../../../globals/GlobalConst";

@Component({
  selector: 'app-user-comments',
  templateUrl: './user-comments.component.html',
  styleUrls: ['./user-comments.component.css']
})
export class UserCommentsComponent implements OnInit {
  items: CommentBasic[];

  server_url = GlobalConst.SERVER_IMG_ADDR;

  count = 0;
  offset = 0;
  limit = GlobalConst.PERSONAL_AREA_LIMIT;

  isLoadingComments: boolean = false;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private commentService: CommentService) {
  }

  ngOnInit() {
    this.route.data
      .subscribe((data: { commentBasicList: CommentBasicList }) => {
        this.items = [];
        console.log(data);
        if (data.commentBasicList) {
          this.items = data.commentBasicList.commentsBasic;
          console.log(this.items);
          this.count = data.commentBasicList.count;
        }
      });
  }

  private findAll(offset: number, limit: number) {
    this.isLoadingComments = true;

    const listSubscription = this.commentService.getUserComments(offset, limit)
      .subscribe((commentBasicList: CommentBasicList) => {
        this.items = commentBasicList.commentsBasic;
        this.count = commentBasicList.count;
      });

    listSubscription.add(() => {
      this.isLoadingComments = false;
    });
  }

  onChangePage(offset: number) {
    this.offset = offset;
    this.findAll(this.offset, this.limit);
  }

  onCollapse(id: number) {
    this.toggle(id);
  }

  onExpand(id: number) {
    this.toggle(id);
  }

  private toggle(commentId: number) {
    const index = this.items.findIndex(item => item.commentId === commentId);
    this.items[index].flag = !this.items[index].flag;
  }

}
