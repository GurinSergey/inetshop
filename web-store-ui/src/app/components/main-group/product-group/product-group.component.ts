import { Component, OnInit } from '@angular/core';
import {ProductGroup} from '../../../models/product-group.model';
import {HttpRequestsService} from '../../../services/http-requests/http-requests.service';

@Component({
  selector: 'app-product-group',
  templateUrl: './product-group.component.html',
  styleUrls: ['./product-group.component.css']
})
export class ProductGroupComponent implements OnInit {
  productGroupList: ProductGroup[];

  constructor(private httpRequest: HttpRequestsService) { }

  ngOnInit() {
  }

  reloadData() {
    this.httpRequest.getAllProductGroups().subscribe((data: any) => {
      this.productGroupList = data;
    });
  }
}
