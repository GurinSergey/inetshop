import {UserCommentsResolver} from "./user-comments/user-comments.resolver.service";
import {UserFavoriteComponent} from "./user-favorite/user-favorite.component";
import {ChangeMailComponent} from "../change-mail/change-mail.component";
import {ChangePassComponent} from "../recovery-pass/change-pass.component";
import {UserProfileComponent} from "./user-profile.component";
import {Authentication} from "../../common-group/auth/authentication";
import {UserCommentsComponent} from "./user-comments/user-comments.component";
import {UserOrdersResolver} from "./user-orders/user-orders.resolver.service";
import {RouterConst} from "../../../globals/RouterConst";
import {UserOrdersComponent} from "./user-orders/user-orders.component";
import {RouterModule, Routes} from "@angular/router";

const routes: Routes = [
  {
    path: '', component: UserProfileComponent, canActivate: [Authentication], children: [
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
  }
];

export const UserProfileRoutes = RouterModule.forChild(routes);
