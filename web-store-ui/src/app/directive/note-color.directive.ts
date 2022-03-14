import {AfterContentChecked, Directive, HostBinding, Input} from "@angular/core";

@Directive({
  selector:'[note-color]'
})
export class NoteColorDirective implements AfterContentChecked{
  @Input('note') note:number;
  @HostBinding('style.color') color: string;

  constructor(){}

  ngAfterContentChecked(){
    if(this.note > 0)
      this.color = '#4CAF50';
    else if(this.note < 0)
      this.color = '#f44336';
    else
      this.color = 'transparent';
  }
}
