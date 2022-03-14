import {Injectable} from '@angular/core';
import {CommentEntry, CommentEntryBuilder, VoteEntry} from "../../models/comment-entry";
import {Observable, of, throwError} from 'rxjs';
import {HttpRequestsService} from "../http-requests/http-requests.service";
import {GlobalConst} from "../../globals/GlobalConst";
import {catchError, map} from "rxjs/internal/operators";
import {HttpErrorResponse} from "@angular/common/http";
import {CommentList} from "../../models/comment-list";
import {CommentBasicList} from "../../models/comment-basic";

@Injectable()
export class CommentService {

  constructor(private httpService: HttpRequestsService) {
  }

  getVoteEntry(data, commentId): VoteEntry {
    if (!data)
      return null;

    let vote = new VoteEntry();
    data.filter(c => c.commentId === commentId).map(item => {
      if (!vote.positive) vote.positive = item.positive;
      if (!vote.negative) vote.negative = !item.positive;
    });
    return vote;
  }

  getCommentEntry(data, notes: any = null): CommentEntry {
    const children: CommentEntry[] = [];
    data.children.forEach(item => {
      children.push(this.getCommentEntry(item));
    });

    return new CommentEntryBuilder()
      .setId(data.id)
      .setAuthor(data.author)
      .setCreateDate(data.date)
      .setDescription(data.text)
      .setChildren(children)
      .setNote(data.note)
      .setVotes(this.getVoteEntry(notes, data.id))
      .setStar(data.starRating)
      .buildEntry();
  }

  getCountComment(id: number): Observable<number> {
    return this.httpService.getCountComments(id).pipe(map((data: any) => {
      return data.payload;
    }));
  }

  getProductComments(id: number, offset: number = 0, limit: number = 10): Observable<CommentList> {
    return this.httpService.getProductComments(id, offset, limit).pipe(
      map((data: any) => {
        if (data.result !== GlobalConst.RESPONSE_RESULT_SUCCESS)
          throw (data.payload);
        let notes = data.payload.notes;
        return new CommentList(
          data.payload.count,
          data.payload.comments.map((item: any) => {
            return this.getCommentEntry(item, notes);
          }));
      }),
      catchError(error => {
        throw ("Что-то пошло не так:(");
      }));
  }

  addNewComment(product_id: number, comment: CommentEntry): Observable<any> {
    return this.httpService.postNewComment(product_id, comment).pipe(map((data: any) => {
      if (data.result !== GlobalConst.RESPONSE_RESULT_SUCCESS)
        throw (data.payload);

      return new CommentEntryBuilder()
        .setAuthor(data.payload.author)
        .setCreateDate(data.payload.date)
        .setParentId(data.payload.parent_id)
        .setId(data.payload.id)
        .setDescription(data.payload.text)
        .setNote(data.payload.note)
        .setStar(data.payload.starRating)
        .buildEntry();
    }));
  }

  like(product_id: number, comment_id: number): Observable<any> {
    return this.httpService.likeComment(product_id, comment_id).pipe(
      map((data: any) => {
        if (data.result !== GlobalConst.RESPONSE_RESULT_SUCCESS)
          throw (data.payload);
        return true;
      }),
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 403)
          throw ("Только зарегистрированные пользователи могут оценивать комментарии");
        throw(error);
      })
    );
  }

  dislike(product_id: number, comment_id: number): Observable<any> {
    return this.httpService.dislikeComment(product_id, comment_id).pipe(
      map((data: any) => {
        if (data.result !== GlobalConst.RESPONSE_RESULT_SUCCESS)
          throw (data.payload);
        return true;
      }),
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 403)
          throw ("Только зарегистрированные пользователи могут оценивать комментарии");
        throw(error);
      })
    );
  }

  getPageByComment(limit:number = 10, productId:number, commentId:number): Observable<number>{
    return this.httpService.getPageCommentsByCommentId(limit, productId, commentId).pipe(
      map((data:any)=>{
        if (data.result !== GlobalConst.RESPONSE_RESULT_SUCCESS)
          throw (data.payload);
        return data.payload;
      })
    );
  }

  getUserComments(offset: number = 0, limit: number = 10): Observable<CommentBasicList> {
    return this.httpService.getUserComments(offset, limit).pipe(
      map((data: any) => {
        const cnt = data.payload.count;
        const results = data.payload.results;
        return {
          count: cnt, commentsBasic: results.map((commentBasic: any) => {
            return commentBasic;
          })
        };
      }),
      catchError(error => {
        throw (error);
      })
    );
  }
}
