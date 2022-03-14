import { WINDOW } from '@ng-toolkit/universal';
import {Component, OnInit, ViewEncapsulation, Inject} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {PopupService} from '../../common-group/compose-message/popup.service';
import {CompareService} from '../../../services/compare/compare.service';
import {ProductService} from '../../../services/product/product.service';
import {ObservableCommonService} from '../../../services/observable/observable-common.service';
import {GlobalConst} from '../../../globals/GlobalConst';
import {Compare, CompareBox, CompareProduct} from "../../../models/compare";

@Component({
  selector: 'app-compare',
  templateUrl: './compare.component.html',
  styleUrls: ['./compare.component.css',
    '../product-list/product-list.component.css'],
  providers: [CompareService, ProductService]
})
export class CompareComponent implements OnInit {
  btnVisible = false;
  isLoading = false;

  items: Compare;
  totalCnt = 0;

  compareProducts: CompareProduct[] = [];
  compareBox: CompareBox;

  switchTitle = 'Все характеристики';
  showOnlyUnique = false;

  constructor(@Inject(WINDOW) private window: Window, private compareService: CompareService,
              private router: Router,
              private route: ActivatedRoute,
              private popupService: PopupService,
              private productService: ProductService,
              private observableCommonService: ObservableCommonService) {
  }

  ngOnInit() {
    this.getItems();
    this.getCompareCnt();
  }

  onChange(event) {
    this.showOnlyUnique = event.checked;
    this.switchTitle = event.checked ? 'Только отличия' : 'Все характеристики';
  }

  getCompareCnt() {
    this.totalCnt = this.compareService.getCompareCnt();
    this.observableCommonService.changeCntCompare(this.totalCnt);
  }

  getItems() {
    this.items = this.compareService.getCompareData();
  }

  changeVisible() {
    this.btnVisible = !this.btnVisible;
  }

  deleteItem(templateID: number, productId: number) {
    this.items = this.compareService.deleteFromCompare(templateID, productId);
    this.getItems();
    this.getCompareCnt();
    this.observableCommonService.deleteFromCompare(productId);
  }

  compare(templateId: number, templateName: string, arrSize: number) {
    this.compareProducts = [];
    this.compareBox = null;

    if (arrSize < 2) {
      this.popupService.getPopup(this.router, this.route, 'main-page',
        'Внимание!', 'Недостаточно товаров для сравнения в каталоге ' + templateName.bold(), 3, 'alert-info');
      return;
    }

    if (arrSize > GlobalConst.MAX_PRODUCTS_TO_COMPARE) {
      this.popupService.getPopup(this.router, this.route, 'main-page',
        'Внимание!', 'Максимальное количество товаров доступных для сравнения ' + GlobalConst.MAX_PRODUCTS_TO_COMPARE, GlobalConst.MAX_PRODUCTS_TO_COMPARE, 'alert-info');
    }

    this.isLoading = true;
    this.compareProducts = this.compareService.getCompareListByTemplateId(templateId).slice(0, GlobalConst.MAX_PRODUCTS_TO_COMPARE);

    const productsId = [];
    this.compareProducts.forEach(p => {
      productsId.push(p.baseInfo.id);
    });

    this.changeVisible();

    this.compareService.getCompareBox(productsId).subscribe(compareBox => {
      this.compareBox = compareBox;

      this.isLoading = false;
    });

    this.window.scroll(0, 0);
  }
}
