import {Component, OnDestroy, OnInit} from '@angular/core';
import {GlobalConst} from "../../../globals/GlobalConst";
import {ProductService} from "../../../services/product/product.service";
import {SearchProductEntry, SearchProductsEntry} from "../../../models/search-products-entry";

import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit, OnDestroy {
  items: SearchProductEntry[];

  templates_count = 0;
  templates_offset = 0;
  templates_limit = GlobalConst.SEARCH_TEMPLATES_LIMIT;
  products_limit = GlobalConst.SEARCH_PRODUCTS_LIMIT;

  filter_cond = '';
  find_products_cnt = 0;
  search_info = '';

  isLoadingResults: boolean = false;

  constructor(private route: ActivatedRoute,
              private productService: ProductService) {
  }

  ngOnInit() {
    this.route.firstChild.data
      .subscribe((data: { searchProductsEntry: SearchProductsEntry }) => {
        this.items = [];
        if (data.searchProductsEntry) {
          this.templates_count = data.searchProductsEntry.allTemplatesCnt;
          this.find_products_cnt = data.searchProductsEntry.searchProductsCnt;
          this.items = data.searchProductsEntry.searchProductsInfo;
        }
      });

    this.filter_cond = this.route.snapshot.firstChild.params['condition'];
    this.getSearchInfo();
  }

  private getSearchInfo() {
    const caseInfo = (this.find_products_cnt === 1) ? ' товар' : (this.find_products_cnt >= 5 ? ' товаров' : ' товара');

    let cntInfo = 'Найдено ' + this.find_products_cnt + caseInfo;
    if (this.find_products_cnt === 0) {
      cntInfo = 'Ничего не найдено';
    }

    this.search_info = '<h5>' + cntInfo + '<br>' + 'Результаты поиска по запросу "' + this.filter_cond + '"</h5>';
  }

  private findAll(offset: number, limit: number) {
    this.isLoadingResults = true;

    const listSubscription = this.productService.searchProducts(offset, limit, this.products_limit, this.filter_cond)
      .subscribe((searchProductsEntry: SearchProductsEntry) => {
        this.templates_count = searchProductsEntry.allTemplatesCnt;
        this.find_products_cnt = searchProductsEntry.searchProductsCnt;
        this.items = searchProductsEntry.searchProductsInfo;
      });

    listSubscription.add(() => {
      this.isLoadingResults = false;
    });
  }

  onChangePage(offset: number) {
    this.templates_offset = offset;
    this.findAll(this.templates_offset, this.templates_limit);
  }

  ngOnDestroy() {
  }
}
