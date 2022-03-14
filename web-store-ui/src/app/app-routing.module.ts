import {NgModule} from '@angular/core';
import {Routes} from '@angular/router';
import {RouterModule} from '@angular/router';
import {RouterConst} from './globals/RouterConst';
import {Authentication, Authentication4Login} from './components/common-group/auth/authentication';
import {LoginComponent} from './components/main-group/login/login.component';
import {MainPageComponent} from './components/main-group/main-page/main-page.component';
import {RegistryComponent} from './components/main-group/registry/registry.component';
import {ContactsComponent} from './components/main-group/contacts/contacts.component';
import {ComposeMessageComponent} from './components/common-group/compose-message/compose-message.component';
import {BasketComponent} from './components/main-group/basket/basket.component';
import {CheckoutComponent} from './components/main-group/checkout/checkout.component';
import {ForgotPassComponent} from './components/main-group/recovery-pass/forgot-pass.component';
import {RecoveryPassComponent} from './components/main-group/recovery-pass/recovery-pass.component';
import {ProductDetailsComponent} from './components/main-group/product-details/product-details.component';
import {MainBodyComponent} from './components/main-group/main-body/main-body.component';
import {CompareComponent} from './components/main-group/compare/compare.component';
import {CheckoutResolver} from './components/main-group/checkout/checkout.resolver.service';
import {UserOrdersResolver} from './components/main-group/user-profile/user-orders/user-orders.resolver.service';
import {MainPageResolver} from './components/main-group/main-page/main-page.resolver.service';
import {MailConfirmComponent} from './components/main-group/registry/mail-confirm/mail-confirm.component';
import {PageNotFoundComponent} from './not-found';
import {SearchResolver} from './components/main-group/search/search.resolver.service';
import {SearchComponent} from './components/main-group/search/search.component';
import {ProductDetailsResolver} from './components/main-group/product-details/product-details.resolver.service';
import {UserCommentsResolver} from './components/main-group/user-profile/user-comments/user-comments.resolver.service';
import {CatalogListComponent} from "./components/main-group/catalog/catalog-list.component";
import {SectionListComponent} from "./components/main-group/section/section-list.component";


const routes: Routes = [
  {path: '', redirectTo: RouterConst.MAINPAGE, pathMatch: 'full'},
  {
    path: RouterConst.MAINPAGE, component: MainPageComponent/*, resolve: {
      productList: MainPageResolver
    }*//*, runGuardsAndResolvers: 'always',*/, children: [
      {path: 'compose', component: ComposeMessageComponent, outlet: 'popup'},
      {path: '', component: MainBodyComponent, children:[
          {path: '', component: CatalogListComponent},
          {path: RouterConst.CATALOG_LIST, component: CatalogListComponent},
          {path: RouterConst.SECTION_LIST, component: SectionListComponent}
        ]},
      {
        path: RouterConst.SEARCH, component: SearchComponent, children: [
          {path: '', redirectTo: '/' + RouterConst.MAINPAGE, pathMatch: 'full'},
          {
            path: ':condition', component: SearchComponent, resolve: {
              searchProductsEntry: SearchResolver
            }
          }]
      },
      {path: RouterConst.LOGIN, component: LoginComponent, canActivate: [Authentication4Login]},
      {path: RouterConst.REGISTRY, component: RegistryComponent},
      {path: RouterConst.CONTACTS, component: ContactsComponent},
      {path: RouterConst.BASKET, component: BasketComponent},
      {
        path: RouterConst.CHECKOUT, component: CheckoutComponent, resolve: {
          basket: CheckoutResolver
        }
      },
      {path: RouterConst.COMPARE, component: CompareComponent},
      {path: RouterConst.FORGOTPASS, component: ForgotPassComponent},
      {path: RouterConst.MAILCONFIRM, component: MailConfirmComponent},
      {path: RouterConst.RECOVERPASS, component: RecoveryPassComponent},
      {
        path: RouterConst.PRODUCTDETAILS, component: ProductDetailsComponent, resolve: {
          productDetails: ProductDetailsResolver
        },data: {
          title: "Your title here",
          metatags: {
            'description': 'Your description',
            'keywords': 'Your keywords',
          }
        }
      },
      { path: RouterConst.USERPROFILE, loadChildren: './components/main-group/user-profile/user-profile.module#UserProfileModule' }
     /* {
        path: RouterConst.USERPROFILE, component: UserProfileComponent, canActivate: [Authentication], children: [
          {path: RouterConst.CHANGEMAIL, component: ChangeMailComponent},
          {path: RouterConst.CHANGEPASS, component: ChangePassComponent},
          {path: RouterConst.USERFAVORITE, component: UserFavoriteComponent},
          {
            path: RouterConst.USERORDERS, component: UserOrdersComponent, resolve: {
              orderBasicList: UserOrdersResolver
            }
          },
          {
            path: RouterConst.USERCOMMENTS, component: UserCommentsComponent, resolve: {
              commentBasicList: UserCommentsResolver
            }
          }]
      }*/
    ]
  },
  {path: '**', component: PageNotFoundComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes, {/*onSameUrlNavigation: 'reload',*/initialNavigation: 'enabled'}/*,{enableTracing: true}/*--debug_only*/)],
  exports: [RouterModule],
  providers: [CheckoutResolver, MainPageResolver, UserOrdersResolver, UserCommentsResolver, SearchResolver]
})
export class AppRoutingModule {
}
