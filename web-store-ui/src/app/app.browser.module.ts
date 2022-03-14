import {AppComponent} from './app.component';
import {AppModule} from './app.module';
import {NgModule} from '@angular/core';
import {BrowserModule, BrowserTransferStateModule} from '@angular/platform-browser';
//import {AppStorage} from "./shared/for-storage/universal.inject";
//import {CookieStorage} from "./components/common-group/for-storage/browser.storage";
import {REQUEST} from "@nguniversal/express-engine/tokens";
import {AppStorage} from "./shared/for-storage/universal.inject";
import {CookieStorage} from "./shared/for-storage/browser.storage";
import {TransferHttpCacheModule} from "@nguniversal/common";
import {ServiceWorkerModule} from "@angular/service-worker";
import {HttpClientModule} from "@angular/common/http";
import {CommonModule} from "@angular/common";

export function getRequest(): any {
  return {headers: {cookie: document.cookie}};
}
@NgModule({
  bootstrap: [AppComponent],
  imports: [
    BrowserModule.withServerTransition({appId: 'my-app-wtf'}),
    HttpClientModule,
    TransferHttpCacheModule,
    BrowserTransferStateModule,
    AppModule,
    ServiceWorkerModule.register('/ngsw-worker.js', { enabled: false })

  ],
  providers: [
    { provide: AppStorage, useClass: CookieStorage },
    {
      // The server provides these in main.server
      provide: REQUEST, useFactory: (getRequest)
    },

    { provide: 'ORIGIN_URL', useValue: location.origin }
  ]
})
export class AppBrowserModule {
}
