import {GroupModel} from './group.model';

export class CategoryModel {
  public id: number = 0;
  public parent_id: number = 0;
  public title: string = '';
  public description: string = '';
  public enabled: boolean = false;
  public groups: GroupModel[] = [];

  constructor(values: Object = {}) {
    Object.assign(this, values);
  }
}
