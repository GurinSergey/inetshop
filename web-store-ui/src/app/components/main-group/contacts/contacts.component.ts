import {Component, OnInit} from '@angular/core';


@Component({
  selector: 'app-contacts',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.css']
})
export class ContactsComponent implements OnInit {
  visible = false;

  constructor() {}

  ngOnInit() {

    window.top;

    setTimeout(() => {
      this.visible = true;
    }, 3000);
  }

}
