export class VoteEntry{
  positive:boolean;
  negative:boolean;

  constructor(positive:boolean = false, negative:boolean = false){
    this.positive = positive;
    this.negative = negative;
  }
}

export class CommentEntry{
  id:number;
  author:string;
  date:string;
  text:string;
  children:CommentEntry[] = [];
  parent_id:number;
  note:number;
  vote:VoteEntry = new VoteEntry();
  rating_star:number;

  constructor(){}
}

export class CommentEntryBuilder{
  private entry:CommentEntry;

  constructor(){
    this.entry = new CommentEntry();
  }

  setId(id:number):CommentEntryBuilder{
    this.entry.id = id;
    return this;
  }

  setAuthor(author:string):CommentEntryBuilder{
    this.entry.author = author;
    return this;
  }

  setCreateDate(date:string):CommentEntryBuilder{
    this.entry.date = date;
    return this;
  }

  setDescription(text:string):CommentEntryBuilder{
    this.entry.text = text;
    return this;
  }

  setChildren(children:CommentEntry[]):CommentEntryBuilder{
    this.entry.children = children;
    return this;
  }

  setParentId(parent_id):CommentEntryBuilder{
    this.entry.parent_id = parent_id;
    return this;
  }

  setNote(note):CommentEntryBuilder{
    this.entry.note = note;
    return this;
  }

  setVotes(vote:VoteEntry):CommentEntryBuilder{
    this.entry.vote = vote;
    return this;
  }

  setStar(rating_star:number = 0):CommentEntryBuilder{
    this.entry.rating_star = rating_star;
    return this;
  }

  buildEntry():CommentEntry{
    return this.entry;
  }

}
