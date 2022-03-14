import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {MatExpansionPanel} from '@angular/material';
import {OrderService} from "../../../../services/order/order.service";
import {OrderDetailsService} from "../../../../services/order-details/order-details.service";
import {OrderBasic, OrderBasicList} from "../../../../models/order-basic";
import {GlobalConst} from "../../../../globals/GlobalConst";


@Component({
  selector: 'app-user-orders',
  templateUrl: './user-orders.component.html',
  styleUrls: ['./user-orders.component.css'],
  providers: [OrderService, OrderDetailsService]
})

export class UserOrdersComponent implements OnInit {
  items: OrderBasic[];

  itemDetails = [];
  itemHistStates = [];

  count = 0;
  offset = 0;
  limit = GlobalConst.PERSONAL_AREA_LIMIT;

  isLoadingOrders: boolean = false;
  isLoadingResults: boolean = false;

  server_url = GlobalConst.SERVER_IMG_ADDR;

  constructor(private orderService: OrderService,
              private orderDetailsService: OrderDetailsService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.data
      .subscribe((data: { orderBasicList: OrderBasicList }) => {
        this.items = [];
        if (data.orderBasicList) {
          this.items = data.orderBasicList.ordersBasic;
          this.count = data.orderBasicList.count;
        }
      });
  }

  private findAll(offset: number, limit: number) {
    this.isLoadingOrders = true;

    const listSubscription = this.orderService.getUserOrders(offset, limit)
      .subscribe((orderBasicList: OrderBasicList) => {
        this.items = orderBasicList.ordersBasic;
        this.count = orderBasicList.count;
      });

    listSubscription.add(() => {
      this.isLoadingOrders = false;
    });
  }

  onChangePage(offset: number) {
    this.offset = offset;
    this.findAll(this.offset, this.limit);
  }

  getDetails(id: number) {
    this.getOrderDetails(id);
    this.getOrderHistStates(id);
  }

  private getOrderDetails(id: number) {
    this.orderDetailsService.findDetailsByOrderId_User(id).subscribe((data: any) => {
      this.itemDetails = data.orderDetails;

      this.isLoadingResults = false;
      document.getElementById('loading' + id).style.display = 'none';
    });
  }

  private getOrderHistStates(id: number) {
    this.orderDetailsService.getAllStatesByOrderId(id).subscribe((data: any) => {
      this.itemHistStates = data;
    });
  }

  onExpand(matExpansionPanel: MatExpansionPanel, id: number) {
    this.toggle(id);

    this.isLoadingResults = true;
    document.getElementById('loading' + id).style.display = 'block';

    this.getDetails(id);
  }

  onCollapse(id: number) {
    this.itemDetails = [];
    this.itemHistStates = [];

    this.toggle(id);
  }

  private toggle(id: number) {
    const index = this.items.findIndex(item => item.orderId === id);
    this.items[index].flag = !this.items[index].flag;
  }
}
