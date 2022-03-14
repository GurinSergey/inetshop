export class AttributeModel {
  public sort: number;
  public name: string;

  constructor(values: Object = {'name': '', 'sort': '0'}) {
    Object.assign(this, values);
  }

}


