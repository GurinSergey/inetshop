import {GlobalConst} from "../globals/GlobalConst";

export class Filter{
  name:string;
  fields:FilterEntry[];

  constructor(name: string, fields: FilterEntry[]) {
    this.name = name;
    this.fields = fields;
  }
}

export class FilterEntry{
  id:number;
  name:string;
  checked:boolean;
  cntForView:number;
  params:FilterValueEntry[];

  constructor(id:number = null, name:string = null, params:FilterValueEntry[] = [], checked:boolean = false){
    this.id = id;
    this.name = name;
    this.checked = checked;
    this.params = params;
    this.cntForView = GlobalConst.FILTER_VALUES_LIMIT;
  }
}

export class FilterValueEntry{
  id:number;
  name:string;
  value:string;
  checked:boolean = false;
  isCntLoading:boolean = false;
  cntByFilterValue:number = 0;

  constructor(id:number = null, name:string = null, value:string = null){
    this.id = id;
    this.name = name;
    this.value = value;
  }


}
