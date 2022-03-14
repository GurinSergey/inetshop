import {GlobalConst} from "../globals/GlobalConst";

export class ProductEntry {
  id: number;
  latIdent: string;
  title: string;
  code: string;
  price: number;
  url_image: string;
  description: string;
  isInBasketBox: boolean = false;
  isInCompareBox: boolean = false;
  isInTrackBox: boolean = false;
  isFavorite: boolean = false;
  templateId: number;
  templateName: string;

  constructor(id, title, code, price, url_image = GlobalConst.SERVER_IMG_ADDR + 'images/no-image.png') {
    this.id = id;
    this.title = title;
    this.code = code;
    this.price = price;
    this.url_image = url_image;
  }
}
