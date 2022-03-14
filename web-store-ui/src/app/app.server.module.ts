import {AppComponent} from './app.component';
import {AppModule} from './app.module';
import {NgModule} from '@angular/core';
import {ServerModule, ServerTransferStateModule} from '@angular/platform-server';
import {ModuleMapLoaderModule} from '@nguniversal/module-map-ngfactory-loader';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {AppStorage} from "./shared/for-storage/universal.inject";
import {UniversalStorage} from "./shared/for-storage/server.storage";
import {Authentication, Authentication4Login} from "./components/common-group/auth/authentication";


@NgModule({
  imports: [
    AppModule,
    NoopAnimationsModule,
    ServerModule,
    ModuleMapLoaderModule,
    ServerTransferStateModule
  ],
  bootstrap: [AppComponent],
  providers: [Authentication,
    Authentication4Login,
    {provide: AppStorage, useClass: UniversalStorage}
  ]
})
export class AppServerModule {
}
