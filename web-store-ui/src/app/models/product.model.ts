import {GlobalConst} from "../globals/GlobalConst";
export class ProductBuilder {
  private product: Product;
  constructor() {
    this.product = new Product();
  }

  setFields(id: number, title: string, code: string, producer: string, description: string, template: string, price: number, visible: boolean, isExists: boolean, photos: Photos[]){
    this.product.id = id;
    this.product.title = title;
    this.product.code = code;
    this.product.producer = producer;
    this.product.description = description;
    this.product.template = template;
    this.product.price = price;
    this.product.visible = visible;
    this.product.isExists = isExists;
    this.product.photos = photos;
    return this;
  }

  setAdvancedFields(statistics: any){
    this.product.statistics = statistics;
    return this;
  }
  build(): Product {
    return this.product;
  }
}

export class Product {
  id: number;
  title: string;
  code: string;
  producer: string;
  description: string;
  template: string;
  price: number;
  visible: boolean;
  isExists: boolean;
  photos: Photos[];
  statistics:any = null;


  constructor() {
  }

  getSinglePhoto(){
    if (!this.photos || this.photos.length == 0) {
      return GlobalConst.SERVER_IMG_ADDR + 'images/no-image.png';
    }
    return GlobalConst.SERVER_IMG_ADDR + this.photos[0].path;
  }

}

export class Photos {
  id: number;
  path: string;
  name: string;
  format: string;
}
