import {CommonModule} from "@angular/common";
import {UserProfileRoutes} from "./user-profile.routing";
import {UserProfileComponent} from "./user-profile.component";
import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {
  MatAutocompleteModule, MatBadgeModule, MatButtonModule, MatButtonToggleModule, MatCardModule, MatCheckboxModule,
  MatChipsModule,
  MatDatepickerModule,
  MatDialogModule,
  MatDividerModule,
  MatGridListModule, MatIconModule, MatInputModule, MatListModule, MatNativeDateModule, MatPaginatorModule,
  MatProgressBarModule,
  MatProgressSpinnerModule,
  MatRadioModule,
  MatSelectModule, MatSliderModule, MatSlideToggleModule, MatSortModule,
  MatStepperModule, MatTableModule, MatTabsModule,
  MatToolbarModule, MatTooltipModule
} from "@angular/material";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatFormFieldModule} from "@angular/material/form-field";
import {ChangePassComponent} from "../recovery-pass/change-pass.component";
import {ChangeMailComponent} from "../change-mail/change-mail.component";
import {UserFavoriteComponent} from "./user-favorite/user-favorite.component";
import {UserOrdersComponent} from "./user-orders/user-orders.component";
import {UserCommentsComponent} from "./user-comments/user-comments.component";
import {SharedModule} from "../../../shared/shared.module";


@NgModule({
  imports: [
  /*  CommonModule,
    FormsModule,*/
    ReactiveFormsModule,
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
    MatToolbarModule,
    UserProfileRoutes,
    SharedModule.forRoot()
  ],
  declarations: [UserProfileComponent,
    ChangePassComponent,
    ChangeMailComponent,
    UserFavoriteComponent,
    UserOrdersComponent,
    UserCommentsComponent]
})
export class UserProfileModule { }
