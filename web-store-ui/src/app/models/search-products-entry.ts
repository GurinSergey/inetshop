import {Product} from "./product";
import {ProductEntry} from "./product-entry";

export class SearchProductEntry {
  templateId: number;
  templateName: String;
  products: ProductEntry[];

  constructor(templateId: number, templateName: String, products: ProductEntry[]) {
    this.templateId = templateId;
    this.templateName = templateName;
    this.products = products;
  }
}

export class SearchProductsEntry {
  allTemplatesCnt: number;
  searchProductsCnt: number;
  searchProductsInfo: SearchProductEntry[];

  constructor(allTemplatesCnt: number, searchProductsCnt: number, searchProductsInfo: SearchProductEntry[]) {
    this.allTemplatesCnt = allTemplatesCnt;
    this.searchProductsCnt = searchProductsCnt;
    this.searchProductsInfo = searchProductsInfo;
  }
}
