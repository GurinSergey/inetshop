import {Injectable} from '@angular/core';
import {Compare, CompareBox, CompareBoxEntry, CompareEntry, CompareProduct} from '../../models/compare';
import {AppLocalStorageService} from '../../components/common-group/app-local-storage/app-local-storage.service';
import {ComplexCompareStorage} from '../../components/common-group/app-local-storage/compare-storage.model';
import {Observable} from 'rxjs/index';
import {HttpRequestsService} from '../http-requests/http-requests.service';
import {catchError, map} from 'rxjs/internal/operators';

@Injectable()
export class CompareService {
  compare: Compare;
  private productList: CompareProduct[] = [];

  constructor(private appLocalStorage: AppLocalStorageService,
              private httpRequestsService: HttpRequestsService) {
    this.compare = new Compare();
  }

  getCompareData(): Compare {
    if (this.appLocalStorage.exist('ComplexCompareStorage')) {
      this.compare.productList = this.appLocalStorage.get('ComplexCompareStorage').compare.productList;
    }

    return this.compare;
  }

  getCompareListByTemplateId(templateID: number): CompareProduct[] {
    const index = Array.from(this.getCompareData().productList.values()).findIndex(value => value.key === templateID);

    return this.compare.productList[index].value;
  }

  addToCompare(compareProduct: CompareProduct): Compare {
    this.productList = [];

    this.compare = this.getCompareData();

    if (this.isExistsProduct(compareProduct)) {
      return this.compare;
    }

    this.productList.push(compareProduct);

    this.pushCompareProductToCompareMap(compareProduct);

    this.appLocalStorage.set('ComplexCompareStorage', new ComplexCompareStorage(this.compare, 'client -> addToCompare',
      this.getAllProductsCnt(this.compare)));

    return this.compare;
  }

  private pushCompareProductToCompareMap(compareProduct: CompareProduct) {
    let f = false;
    this.compare.productList.forEach(values => {
      if (values.key === compareProduct.templateId) {
        values.value.push(compareProduct);
        f = true;
      }
    });

    if (!f) {
      const p = new CompareEntry(compareProduct.templateId, this.productList);

      this.compare.productList.push(p);
    }
  }

  deleteFromCompare(templateID: number, productId: number): Compare {
    if (templateID === null) {
      alert('deleteFromCompare -> \'templateID\' is empty');
      return;
    }

    if (productId === null) {
      alert('deleteFromCompare -> \'productId\' is empty');
      return;
    }

    this.compare = this.getCompareData();
    Array.from(this.compare.productList.values()).forEach(values => {
      if (values.key === templateID) {
        if (values.value.length === 1) {
          this.deleteFromCompareByTemplateId(templateID);
        } else {
          const index = values.value.findIndex(p => p.baseInfo.id === productId);
          values.value.splice(index, 1);
        }
      }
    });

    this.appLocalStorage.set('ComplexCompareStorage', new ComplexCompareStorage(this.compare, 'client -> deleteFromCompare',
      this.getAllProductsCnt(this.compare)));

    return this.compare;
  }

  private deleteFromCompareByTemplateId(templateID: number): Compare {
    if (templateID === null) {
      alert('deleteFromCompareByTemplateId -> \'templateID\' is empty');
      return;
    }
    const index = Array.from(this.compare.productList.values()).findIndex(values => values.key === templateID);
    this.compare.productList.splice(index, 1);

    return this.compare;
  }

  removeCompare() {
    this.productList = [];
    this.appLocalStorage.set('ComplexCompareStorage', new ComplexCompareStorage(new Compare(), 'client -> removeCompare', 0));
  }

  existsInCompareBox(product_id: number) {
    return this.getCompareData().productList.some(item => {
      return item.value.filter(p => p.baseInfo.id === product_id).length > 0;
    });
  }

  private isExistsProduct(product: CompareProduct): boolean {
    if (product === null) {
      alert('isExistsProduct -> \'product\' is empty');
      return;
    }

    this.compare = this.getCompareData();
    if (this.compare.productList.length === 0) {
      return false;
    }

    let ret = false;
    this.compare.productList.forEach(values => {
        values.value.forEach(p => {
          if (p.baseInfo.id === product.baseInfo.id) {
            ret = true;
          }
        });
      }
    );
    return ret;
  }

  getCompareCnt(): number {
    let total = 0;

    this.compare = this.getCompareData();

    this.compare.productList.forEach(values => {
        values.value.forEach(p => {
          total += parseFloat(p.cnt.toString());
        });
      }
    );

    return total;
  }

  private getAllProductsCnt(compare: Compare): number {
    let total = 0;
    compare.productList.forEach(values => {
      total += values.value.length;
    });

    return total;
  }

  private isAllValuesAreTheSame(list: String[]): Boolean {
    const dictinct: String[] = list.filter((v, i, a) => a.indexOf(v) === i);
    return dictinct.length === 1;
  }

  getCompareBox(productsId: any): Observable<CompareBox> {
    return this.httpRequestsService.getCompareBox(productsId).pipe(
      map((data: any) => {
        const result: CompareBox = new CompareBox();

        Object.entries(data.payload).forEach((d: any) => {
          result.item.push(new CompareBoxEntry(d[0], d[1], this.isAllValuesAreTheSame(d[1])));
        });

        return result;
      }),
      catchError(error => {
        throw (error);
      })
    );
  }
}
