import {Component, HostBinding, Input, OnInit} from '@angular/core';
import {ActivatedRoute, ActivatedRouteSnapshot, Router} from '@angular/router';
import {trigger, state, style, animate, transition, query, stagger} from '@angular/animations';

@Component({
  templateUrl: './compose-message.component.html',
  // styles: [ ':host { position: relative; bottom: 10%; }' ],
  styleUrls: ['./compose-message.css'],
  animations: [
    trigger('routeAnimation', [
      state('*',
        style({
          opacity: 1,
          transform: 'translateX(0%)'
        })
      ),
      transition(':enter', [
        style({
          opacity: 0,
          transform: 'translateX(-100%)'

        }),
        animate('0.2s ease-in')
      ]),
      transition(':leave', [
        animate('0.5s ease-out', style({
          opacity: 0,
          transform: 'translateY(-100%)'
        }))
      ])
    ])
  ]
})
// popupType;//alert-success,alert-info,alert-danger
export class ComposeMessageComponent implements OnInit {

  @HostBinding('@routeAnimation') routeAnimation = true;
//@HostBinding('style.display')   display = 'block'
// @HostBinding('style.position')  position = 'fixed';


  title: string;
  sending = false;
  timer = 3;
  message = '';
  popupType;//alert-success,alert-info,alert-danger

  ngOnInit(): void {
    this.title = this.activatedRoute.snapshot.paramMap.get('title');
    this.message = this.activatedRoute.snapshot.paramMap.get('message');
    this.popupType = this.activatedRoute.snapshot.paramMap.get('popupType');
    if (this.activatedRoute.snapshot.paramMap.get('timer')) {
      this.timer = parseInt(this.activatedRoute.snapshot.paramMap.get('timer'));
    }

    //если прилетел 0, информационную форму не закрываем
    this.closeByTime(this.timer);

  }


  constructor(private activatedRoute: ActivatedRoute, private router: Router) {

  }


  closeByTime(timer: number) {

    if (this.timer == 0) {
      return;
    }
    this.sending = true;
    // this.details = 'Sending Message...';

    setTimeout(() => {
      this.sending = false;
      this.closePopup();
    }, timer * 1000);
  }

  cancel() {
    this.closePopup();
  }

  closePopup() {
    // Providing a `null` value to the named outlet
    // clears the contents of the named outlet
    this.router.navigate([{outlets: {popup: null}}], {
      relativeTo: this.activatedRoute.parent // <--- PARENT activated route.
    });


  }

}
