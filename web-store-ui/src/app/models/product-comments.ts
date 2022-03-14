import {CommentEntry} from "./comment-entry";
import {Observable} from "rxjs";

export class ProductComments {
  allCount:number;
  positiveCount:number;
  negativeCount:number;

  comments:CommentEntry[] = [];

  constructor(){}
}
