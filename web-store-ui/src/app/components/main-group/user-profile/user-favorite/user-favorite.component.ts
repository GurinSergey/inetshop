import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {FavoriteService} from "../../../../services/favorite/favorite.service";
import {Product} from "../../../../models/product";

@Component({
  selector: 'app-user-favorite',
  templateUrl: './user-favorite.component.html',
  styleUrls: ['./user-favorite.component.css']
})
export class UserFavoriteComponent implements OnInit{
  favoriteList: Product[];

  constructor(
    private route: ActivatedRoute,
    private favoriteService: FavoriteService){
    this.favoriteService.syncFavoriteList();
    this.favoriteList = this.favoriteService.getFavoriteList();
  }

  ngOnInit(): void {

  }
}
