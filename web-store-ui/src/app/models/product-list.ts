import {Product} from "./product";

export class ProductList {
  products:Product[];
  count:number;

  constructor(products:Product[] = [], count:number = 0){
    this.products = products;
    this.count = count;
  }
}
