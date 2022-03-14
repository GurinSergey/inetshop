import {Product} from './product';

export class CompareProduct extends Product {
  cnt: number;
  templateId: number;
  templateName: string;

  constructor(p: Product, cnt: number) {
    super(p.baseInfo);

    this.priceInfo = p.priceInfo;
    this.photosInfo = p.photosInfo;

    this.baseInfo.isInCompareBox = p.baseInfo.isInCompareBox;
    this.baseInfo.latIdent = p.baseInfo.latIdent;
    this.cnt = cnt;
  }
}

export class CompareEntry {
  key: number;
  value: CompareProduct[];

  constructor(key: number, value: CompareProduct[] = []) {
    this.key = key;
    this.value = value;
  }
}

export class Compare {
  productList: CompareEntry[];

  constructor(productList: CompareEntry[] = []) {
    this.productList = productList;
  }
}

export class CompareBoxEntry {
  fieldName: string;
  fieldValues: String[];
  allValuesAreTheSame: Boolean = false;

  constructor(fieldName: string, fieldValues: String[], allValuesAreTheSame: Boolean) {
    this.fieldName = fieldName;
    this.fieldValues = fieldValues;
    this.allValuesAreTheSame = allValuesAreTheSame;
  }
}

export class CompareBox {
  item: CompareBoxEntry[];

  constructor(box: CompareBoxEntry[] = []) {
    this.item = box;
  }
}
