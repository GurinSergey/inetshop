import {Component, OnInit} from '@angular/core';
import {ObservableZhdunService} from '../../../services/observable/observable-zhdun.service';

@Component({
  selector: 'app-zhdun',
  templateUrl: './zhdun.component.html',
  styleUrls: ['./zhdun.component.css']
})
export class ZhdunComponent implements OnInit {
  visible: boolean = false;

  constructor(private zhdunService: ObservableZhdunService) {
    this.zhdunService.zhdunStateVisible$.subscribe(val => {
      this.visible = val;
    });
  }

  ngOnInit() {
  }
}
