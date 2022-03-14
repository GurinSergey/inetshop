import {Injectable} from '@angular/core';
import {ActivatedRoute, ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot} from '@angular/router';
import {ObservableZhdunService} from '../../../../services/observable/observable-zhdun.service';
import {CommentService} from '../../../../services/comment/comment.service';
import {PopupService} from '../../../common-group/compose-message/popup.service';
import {map} from "rxjs/internal/operators";
import {GlobalConst} from "../../../../globals/GlobalConst";
import {CommentBasicList} from "../../../../models/comment-basic";

@Injectable()
export class UserCommentsResolver implements Resolve<[CommentBasicList]> {
  commentBasicList: CommentBasicList;

  constructor(private commentService: CommentService,
              private router: Router,
              private route: ActivatedRoute,
              private popupService: PopupService,
              private zhdun: ObservableZhdunService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {
    this.zhdun.on();
    return this.commentService.getUserComments(0, GlobalConst.PERSONAL_AREA_LIMIT).pipe(map(data => {
      this.commentBasicList = data;

      this.zhdun.off();
      return this.commentBasicList;
    }));
  }
}
