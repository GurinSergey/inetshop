export class PriceEntry{
  current_price:number;
  new_future_price:number;
  sale_price:number;
  exists_sale:boolean;

  constructor(current_price:number = null, new_future_price:number = null, sale_price:number = null){
    this.current_price = current_price;
    this.new_future_price = new_future_price;
    this.sale_price = sale_price;

    this.exists_sale = !!(sale_price) || false;
  }
}
