import {CommentEntry} from "./comment-entry";

export class CommentList{
  count:number; //used for pagination
  totalCount:number = 0; //total count
  commentList:CommentEntry[];

  constructor(count:number = 0, comments:CommentEntry[] = []){
    this.count = count;
    this.commentList = comments;
  }
}
