export class RatingEntry{
  allCommentNotes:number;
  positiveCommentNotes:number;
  negativeCommentNotes:number;

  ratingStar:number;

  constructor(cntPositive:number = 0, cntNegative:number = 0, star = 0){
    this.positiveCommentNotes = cntPositive;
    this.negativeCommentNotes = cntNegative;
    this.allCommentNotes = cntPositive + cntNegative;
    this.ratingStar = star;
  }

  addPositive(){
    this.positiveCommentNotes++;
    this.allCommentNotes = this.positiveCommentNotes + this.negativeCommentNotes;
  }

  addNegative(){
    this.negativeCommentNotes++;
    this.allCommentNotes = this.positiveCommentNotes + this.negativeCommentNotes;
  }
}
