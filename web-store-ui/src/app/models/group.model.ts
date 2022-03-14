import {AttributeModel} from './attribute.model';

export class GroupModel {
  public id: number = 0;
  public sort: number = 0;
  public name: string = '';
  public attributes: AttributeModel[] = [];

  constructor(values: Object = {}) {
    Object.assign(this, values);
  }
}
