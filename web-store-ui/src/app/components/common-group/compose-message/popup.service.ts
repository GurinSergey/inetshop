import {Injectable} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {PopupStyle} from '../../../globals/popup-style.enum';


@Injectable()
export class PopupService {
  private router: Router;
  private activetedRoute: ActivatedRoute;
  private page: string;

  constructor() {
    /*VDN default main-page*/
    this.page = 'main-page';
  }

  setRouter(router: Router): void{
    this.router = router;
  }

  setActivatedRoute(activetedRoute: ActivatedRoute):void{
    this.activetedRoute = activetedRoute;
  }

  setPage(page:string = 'main-page'):void{
    this.page = page;
  }

  popupView(title: string, text: string, timer: number, popupType?: PopupStyle | string){
    this.getPopup(this.router, this.activetedRoute, this.page, title, text, timer, popupType);
  }

//popupType value : 'alert-success' 'alert-info' 'alert-danger'
  getPopup(router: Router, route: ActivatedRoute, mainpage: string, title: string, text: string, timer: number, popupType?: PopupStyle | string) {
    if (!popupType) {
      popupType = PopupStyle.SUCCESS;
    }
    router.navigate([`/${mainpage}/`, {
      outlets: {
        popup: ['compose', {
          title: title,
          message: text,
          timer: timer,
          popupType: popupType
        }]
      }
    }], {relativeTo: route.parent, skipLocationChange: true});
  }

  getPopupAndRedirect(router: Router, route: ActivatedRoute, mainpage: string, returnUrl: string, title: string, text: string, timer: number, popupType?: string | PopupStyle) {
    let previousUrl: string;
    previousUrl = returnUrl;
    if (!popupType) {
      popupType = 'alert-success';
    }
    if (previousUrl == null)
      previousUrl = mainpage;

    if (mainpage === previousUrl.replace('/', '')) {
      router.navigateByUrl(`/${mainpage}/(popup:compose;title=${title};message=${text};popupType=${popupType})`);
    }
    else {
      previousUrl = returnUrl.replace(`/${mainpage}/`, '')
        .replace('(', '')
        .replace(')', '');
      router.navigate([{
        outlets: {
          popup: ['compose', {
            title: title,
            message: text,
            popupType: popupType
          }],
          primary: previousUrl
        }
      }], {relativeTo: route.parent});
    }
  }
}
