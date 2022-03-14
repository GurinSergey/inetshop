import { Component } from '@angular/core';
import {RoutingStateService} from "./routing-state.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Tolcom';
  k: string;

  constructor(routingState: RoutingStateService) {
    routingState.loadRouting();
  }

}
