import {Component, EventEmitter, Input, Output, OnInit} from "@angular/core";
import {GlobalConst} from "../../../globals/GlobalConst";

@Component({
  selector: 'rating',
  templateUrl: '../rating/rating.component.html',
  styleUrls: ['../rating/rating.component.css']
})
export class RatingComponent implements OnInit{
   _titles:string[];
  _arr:number[];
  _currentVal:number;
  _readonly:boolean;

  @Output() currentChange = new EventEmitter();

  constructor(){
    this.createArr(5);
    this._titles = GlobalConst.STAR_TITLES;
    this._currentVal = 0;
    this._readonly = false;
  }

  @Input() set max(val:number){
    this.createArr(val);
  }

  @Input() get current(){
    return this._currentVal;
  }

  set current(val:number){
    this.setValue(val);
  }

  @Input() set readonly(val:boolean){
    this._readonly = val;
  }

  @Input() set titles(arr:string[]){
    this._titles = arr;
  }

  setValue(val:number){
    if(this._readonly)
      return false;

    this._currentVal = val;
    this.currentChange.emit(this._currentVal);
  }

  private createArr(val:number){
    this._arr = Array.from(new Array(val),(val, index)=> index + 1 );
  }

  ngOnInit(): void {

  }
}
