import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-progress-bar',
  templateUrl: './progress-bar.component.html',
  styleUrls: ['./progress-bar.component.css']
})
export class ProgressBarComponent implements OnInit {
  procces:number = -25;
  width:number = 25;
  visible:boolean = true;

  constructor() { }

  init(){
    this.procces = -25;
    this.width = 25;
  }

  toogleVisible(){
    this.visible = !this.visible;
    this.init();
  }

  move(){
    setTimeout(() => {
      this.procces >= 100?this.procces = -this.width:this.procces += 0.5;
      this.move();
    }, 1000/60);
  }

  ngOnInit() {
    this.move();
  }

}
