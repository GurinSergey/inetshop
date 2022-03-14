import {ProductEntry} from './product-entry';
import {FieldEntry, FieldList} from './field-list';
import {Observable} from 'rxjs';
import {RatingEntry} from "./rating-entry";
import {CommentList} from "./comment-list";
import {PriceEntry} from "./price-entry";
import {ProducerEntry} from "./producer-entry";
import {PhotoEntry} from "./photo-entry";

export class Product {
  baseInfo: ProductEntry;
  producerInfo: ProducerEntry;
  fieldInfo: FieldList;
  commentInfo: CommentList;
  ratingInfo: RatingEntry;
  priceInfo: PriceEntry;
  photosInfo: PhotoEntry[];

  constructor(baseInfo: ProductEntry = null) {
    this.baseInfo = baseInfo;
    this.commentInfo = new CommentList();
    this.fieldInfo = new FieldList();
    this.ratingInfo = new RatingEntry();
    this.priceInfo = new PriceEntry();
    this.producerInfo = new ProducerEntry();
    this.photosInfo = [];
  }
}
