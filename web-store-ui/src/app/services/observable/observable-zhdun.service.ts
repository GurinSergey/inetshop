import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable()
export class ObservableZhdunService {

  private zhdunStateVisible = new BehaviorSubject<boolean>(false);
  zhdunStateVisible$ = this.zhdunStateVisible.asObservable();
  private onWait = false;

  constructor() {
  }

  private setStateVisible(state: boolean) {
    this.zhdunStateVisible.next(state);
  }

  on() {
    this.onWait = true;
    setTimeout(() => {
      if (this.onWait) {
        this.zhdunStateVisible.next(this.onWait);
      }
    }, 1000);

  }

  off() {
    this.onWait = false;
    this.zhdunStateVisible.next(false);
  }

}
