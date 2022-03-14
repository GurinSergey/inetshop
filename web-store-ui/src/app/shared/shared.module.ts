import {ModuleWithProviders, NgModule} from '@angular/core';
import {CurrencyLocalPipe} from "../pipe/currency-local.pipe";
import {CommentPipe} from "../pipe/comment.pipe";
import {ProductListComponent} from "../components/main-group/product-list/product-list.component";
import {SaleStyleDirective} from "../directive/sale-style.directive";
import {NoteColorDirective} from "../directive/note-color.directive";
import {ProgressBarComponent} from "../components/common-group/progress-bar/progress-bar.component";
import {TooltipDirective} from "../directive/tooltip.directive";
import {ScrollComponent} from "../components/common-group/scroll/scroll.component";
import {PaginationComponent} from "../components/common-group/pagination/pagination.component";
import {DislikeDirective} from "../directive/dislike.directive";
import {CallbackDirective} from "../directive/callback.directive";
import {NewPriceStyleDirective} from "../directive/new-price-style.directive";
import {SaleTextPipe} from "../pipe/sale-text.pipe";
import {LikeDirective} from "../directive/like.directive";
import {RatingComponent} from "../components/common-group/rating/rating.component";
import {NotePipe} from "../pipe/note.pipe";
import {
  MatAutocompleteModule,
  MatBadgeModule,
  MatButtonModule, MatButtonToggleModule, MatCardModule, MatCheckboxModule, MatChipsModule, MatDatepickerModule,
  MatDialogModule,
  MatDividerModule,
  MatGridListModule,
  MatIconModule,
  MatInputModule, MatListModule,
  MatNativeDateModule,
  MatPaginatorModule, MatProgressBarModule, MatProgressSpinnerModule, MatRadioModule, MatSelectModule, MatSliderModule,
  MatSlideToggleModule,
  MatSortModule, MatStepperModule,
  MatTableModule, MatTabsModule, MatToolbarModule, MatTooltipModule
} from "@angular/material";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatExpansionModule} from "@angular/material/expansion";
import {CommonModule} from "@angular/common";
import {RouterModule} from "@angular/router";


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    ReactiveFormsModule,
    MatIconModule,
    MatButtonToggleModule,
    MatSlideToggleModule,
    MatInputModule, MatFormFieldModule, MatRadioModule, MatStepperModule, MatCheckboxModule, MatCardModule, MatTabsModule,
    MatExpansionModule, MatSelectModule, MatDividerModule, MatListModule, MatDatepickerModule, MatNativeDateModule,
    MatGridListModule, MatButtonModule, MatProgressSpinnerModule, MatProgressBarModule, MatTooltipModule, MatChipsModule, MatAutocompleteModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatDialogModule,
    MatBadgeModule,
    MatSliderModule,
    MatToolbarModule,
  ],
  declarations: [CommentPipe, CurrencyLocalPipe,ProductListComponent, LikeDirective, DislikeDirective, TooltipDirective, NoteColorDirective, SaleStyleDirective, NewPriceStyleDirective, CallbackDirective,
    NotePipe,
    SaleTextPipe,
    RatingComponent, ScrollComponent,
    PaginationComponent,
    ProgressBarComponent],
  exports: [
    CommonModule,
    FormsModule,
    LikeDirective, DislikeDirective, TooltipDirective, NoteColorDirective, SaleStyleDirective, NewPriceStyleDirective, CallbackDirective,
    NotePipe,
    SaleTextPipe,
    RatingComponent, ScrollComponent,
    PaginationComponent,
    ProgressBarComponent, ProductListComponent, CommentPipe, CurrencyLocalPipe],
})
export class SharedModule {
  static forRoot(): ModuleWithProviders {
    return {ngModule: SharedModule, providers: []};
  }
}
