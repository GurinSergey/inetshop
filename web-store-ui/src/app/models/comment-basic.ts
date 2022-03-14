export class CommentBasic {
  commentId: number;
  existsChild: boolean;
  text: String;
  date: Date;
  productId: number;
  latIdent: string;
  productPhotoUrl: String;
  productTitle: String;
  productCode: String;
  userId: number;
  author: String;
  flag: Boolean = false;

  constructor() {
  }
}

export class CommentBasicBuilder {
  private entry: CommentBasic;

  constructor() {
    this.entry = new CommentBasic();
  }

  setCommentId(commentId: number): CommentBasicBuilder {
    this.entry.commentId = commentId;
    return this;
  }

  setExistsChild(existsChild: boolean): CommentBasicBuilder {
    this.entry.existsChild = existsChild;
    return this;
  }

  setText(text: string): CommentBasicBuilder {
    this.entry.text = text;
    return this;
  }

  setDate(date: Date): CommentBasicBuilder {
    this.entry.date = date;
    return this;
  }

  setProductId(productId: number): CommentBasicBuilder {
    this.entry.productId = productId;
    return this;
  }

  setLatIdent(latIdent: string): CommentBasicBuilder {
    this.entry.latIdent = latIdent;
    return this;
  }

  setProductPhotoUrl(productPhotoUrl: String): CommentBasicBuilder {
    this.entry.productPhotoUrl = productPhotoUrl;
    return this;
  }

  setProductTitle(productTitle: String): CommentBasicBuilder {
    this.entry.productTitle = productTitle;
    return this;
  }

  setProductCode(productCode: String): CommentBasicBuilder {
    this.entry.productCode = productCode;
    return this;
  }

  setUserId(userId: number): CommentBasicBuilder {
    this.entry.userId = userId;
    return this;
  }

  setAuthor(author: String): CommentBasicBuilder {
    this.entry.author = author;
    return this;
  }

  setFlag(flag: Boolean): CommentBasicBuilder {
    this.entry.flag = flag;
    return this;
  }

  buildEntry(): CommentBasic {
    return this.entry;
  }
}

export class CommentBasicList {
  commentsBasic: CommentBasic[];
  count: number;

  constructor(commentsBasic: CommentBasic[], count: number) {
    this.commentsBasic = commentsBasic;
    this.count = count;
  }
}
