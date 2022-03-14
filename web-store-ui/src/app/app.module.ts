import {BrowserModule, BrowserTransferStateModule} from '@angular/platform-browser';
import {APP_ID, APP_INITIALIZER, Inject, NgModule, PLATFORM_ID} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ReactiveFormsModule} from '@angular/forms';
import {AppComponent} from './app.component';
import {MessageService} from './services/message/message.service';
import {AppRoutingModule} from './app-routing.module';
import {MainPageComponent} from './components/main-group/main-page/main-page.component';
import {HttpClientModule} from '@angular/common/http';
import {ProductGroupComponent} from './components/main-group/product-group/product-group.component';
import {HttpRequestsService} from './services/http-requests/http-requests.service';
import {HttpWrapperService} from './services/http-wrapper/http-wrapper.service';
import {Authentication, Authentication4Login} from './components/common-group/auth/authentication';
import {LoginComponent} from './components/main-group/login/login.component';
import {RegistryComponent} from './components/main-group/registry/registry.component';
import {ContactsComponent} from './components/main-group/contacts/contacts.component';
import {ComposeMessageComponent} from './components/common-group/compose-message/compose-message.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BasketComponent} from './components/main-group/basket/basket.component';
import {CheckoutComponent} from './components/main-group/checkout/checkout.component';
import {RecoveryPassComponent} from './components/main-group/recovery-pass/recovery-pass.component';
import {ForgotPassComponent} from './components/main-group/recovery-pass/forgot-pass.component';
import {PopupService} from './components/common-group/compose-message/popup.service';
import {AppLocalStorageService} from './components/common-group/app-local-storage/app-local-storage.service';
import {FilterListComponent} from './components/main-group/filter-list/filter-list.component';
import {
  PhotosDialogComponent,
  ProductDetailsComponent
} from './components/main-group/product-details/product-details.component';
import {MainBodyComponent} from './components/main-group/main-body/main-body.component';
import {RoutingStateService} from './routing-state.service';
import {SettingsService} from './settings-service.service';
import {LOCALE_ID} from '@angular/core';
import {registerLocaleData, CommonModule, isPlatformBrowser} from '@angular/common';
import localeRu from '@angular/common/locales/ru';
import {MatFormFieldModule} from '@angular/material/form-field';

import {
  MatIconModule,
  MatInputModule,
  MatRadioModule,
  MatStepperModule,
  MatCheckboxModule,
  MatSelectModule,
  MatDividerModule,
  MatListModule,
  MatDatepickerModule,
  MatNativeDateModule,
  MatTabsModule,
  MatDialogModule,
  MatButtonToggleModule,
  MatCardModule,
  MatGridListModule,
  MatButtonModule,
  MatProgressSpinnerModule,
  MatProgressBarModule,
  MatTooltipModule,
  MatChipsModule,
  MatAutocompleteModule,
  MatTableModule,
  MatPaginatorModule,
  MatSortModule,
  MatSort,
  MatBadgeModule,
  MatToolbarModule,
  MatSlideToggleModule,
  MatSliderModule
} from '@angular/material';

import {MatExpansionModule} from '@angular/material/expansion';
import {CompareComponent} from './components/main-group/compare/compare.component';
import {OrderDetailsService} from './services/order-details/order-details.service';
import {CompareService} from './services/compare/compare.service';
import {BasketService} from './services/basket/basket.service';
import {OrderService} from './services/order/order.service';
import {TextMaskModule} from 'angular2-text-mask';
import {ZhdunComponent} from './components/common-group/zhdun/zhdun.component';
import {ObservableZhdunService} from './services/observable/observable-zhdun.service';
import {ProductService} from './services/product/product.service';
import {MailConfirmComponent} from './components/main-group/registry/mail-confirm/mail-confirm.component';
import {PageNotFoundComponent} from './not-found';
import {ObservableCommonService} from './services/observable/observable-common.service';
import {SearchComponent} from './components/main-group/search/search.component';
import {CdkTableModule} from '@angular/cdk/table';
import {CommentService} from './services/comment/comment.service';
import {VarDirective} from './directive/ngVar.directive';
import {CookieService} from 'ngx-cookie-service';
import {ProductDetailsResolver} from './components/main-group/product-details/product-details.resolver.service';
import {ProductSortService} from './services/product-sort/product-sort.service';
import {ProductFilterService} from './services/product-filter/product-filter.service';
import {TooltipContentComponent} from './components/common-group/tooltip/tooltip.component';
import {TooltipContainerComponent} from './components/common-group/tooltip/tooltip-container.component';
import {TooltipService} from './services/tooltip/tooltip.service';
import {ConfirmationDialogComponent} from './components/common-group/confirmation-dialog/confirmation-dialog.component';
import {CommentComponent} from './components/main-group/comment/comment.component';
import {CategoryService} from './services/category/category.service';
import {NgtUniversalModule} from "@ng-toolkit/universal";
import {SharedModule} from "./shared/shared.module";
import {FavoriteDialogComponent} from "./components/main-group/main-page/main-page.component";
import {FavoriteService} from "./services/favorite/favorite.service";
import {TransferHttpCacheModule} from "@nguniversal/common";
import {RatingComponent} from "./components/common-group/rating/rating.component";
import {CatalogService} from "./services/catalog/catalog.service";
import {CatalogListComponent} from "./components/main-group/catalog/catalog-list.component";
import {SectionListComponent} from "./components/main-group/section/section-list.component";
import {SliderComponent} from "./components/common-group/slider/slider.component";

registerLocaleData(localeRu, 'ru-UA');

@NgModule({
  declarations: [
    AppComponent,
    MainPageComponent,
    ProductGroupComponent,
    LoginComponent,
    RegistryComponent,
    ContactsComponent,
    ComposeMessageComponent,
    BasketComponent,
    CheckoutComponent,
    RecoveryPassComponent,
    ForgotPassComponent,
    FilterListComponent,
    ProductDetailsComponent,
    PhotosDialogComponent,
    MainBodyComponent,
    CommentComponent,
    CompareComponent,
    SliderComponent,
    ZhdunComponent,
    MailConfirmComponent,
    SearchComponent,
    PageNotFoundComponent,  SearchComponent,
    VarDirective,
    TooltipContentComponent,
    TooltipContainerComponent,
    ConfirmationDialogComponent,
    FavoriteDialogComponent,
    CatalogListComponent,
    SectionListComponent,
    // RatingComponent
  ],
  imports: [
    BrowserModule.withServerTransition({appId: 'my-app-wtf'}),
    SharedModule.forRoot(),
    BrowserTransferStateModule,
    TransferHttpCacheModule,
    NgtUniversalModule,
 /*   CommonModule,
    FormsModule,*/
    CdkTableModule,
    TextMaskModule,
    ReactiveFormsModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatButtonToggleModule,
    MatSlideToggleModule,
    MatInputModule, MatFormFieldModule, MatRadioModule, MatStepperModule,MatCheckboxModule, MatCardModule, MatTabsModule,
    MatExpansionModule, MatSelectModule, MatDividerModule, MatListModule, MatDatepickerModule, MatNativeDateModule,
    MatGridListModule, MatButtonModule, MatProgressSpinnerModule, MatProgressBarModule, MatTooltipModule, MatChipsModule, MatAutocompleteModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatDialogModule,
    MatBadgeModule,
    MatSliderModule,
    MatToolbarModule

  ],
  providers: [
    MessageService,
    HttpWrapperService,
    HttpRequestsService,
    CookieService,
    Authentication,
    Authentication4Login,
    PopupService,
    AppLocalStorageService,
    RoutingStateService,
    SettingsService,
    OrderService,
    OrderDetailsService,
    BasketService,
    CompareService,
    SliderComponent,
    ProductService,
    ProductSortService,
    ProductFilterService,
    ObservableCommonService,
    ObservableZhdunService,
    CommentService,
    FavoriteService,
    CatalogService,
    {
      provide: LOCALE_ID,
      deps: [SettingsService], /*service handling global settings*/
      useFactory: (settingsService) => settingsService.getLanguage()  /*returns locale string*/
    },
    CompareService,
    ProductDetailsResolver,
    TooltipService,
    CategoryService
  ],
  bootstrap: [AppComponent],
  entryComponents: [PhotosDialogComponent, ConfirmationDialogComponent, FavoriteDialogComponent],
  exports:[]
})
export class AppModule {
  constructor(@Inject(PLATFORM_ID) private platformId: Object,
              @Inject(APP_ID) private appId: string) {
    const platform = isPlatformBrowser(platformId) ?
      'in the browser' : 'on the server';
    console.log(`Running ${platform} with appId=${appId}`);
  }
}

