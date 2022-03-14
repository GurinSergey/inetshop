import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {CatalogEntry} from "../../../models/catalog-entry";
import {CatalogService} from "../../../services/catalog/catalog.service";
import {animate, keyframes, query, stagger, style, transition, trigger} from "@angular/animations";

@Component({
  selector: 'app-catalog-list',
  templateUrl: './catalog-list.component.html',
  styleUrls: ['./catalog-list.component.css'],
  animations:[
    trigger('catalogs', [
      transition('* => *', [
        query(':enter', style({opacity: 0}), {optional: true}),

        query(':enter', stagger('200ms', [
          animate('.3s ease-in', keyframes([
            style({opacity: 0, transform: 'translateY(25%)', offset: 0}),
            style({opacity: .5, transform: 'translateY(35px)', offset: .5}),
            style({opacity: 1, transform: 'translateY(0)', offset: 1})
          ]))
        ]), {optional: true})
      ])
    ])
  ]
})

export class CatalogListComponent implements OnInit{
  catalog:CatalogEntry;
  catalogLoading:boolean = false;

  constructor(
    private route: ActivatedRoute,
    private rootRoute: Router,
    private catalogService: CatalogService
  ){
    this.catalog = new CatalogEntry();
  }

  navigateToCatalog(catalog: CatalogEntry){
    let url;
    this.catalogService.setCatalogName(catalog.name);
    if(catalog.children.length == 0 && catalog.template)
      url = this.rootRoute.createUrlTree(['/main-page/section', catalog.template.id]);
    else
      url = this.rootRoute.createUrlTree(['/main-page/catalog', catalog.id]);
    this.rootRoute.navigateByUrl(url);
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if(!params['id']){
        this.catalogService.catalog.subscribe(data => this.catalog.children = data);
        return;
      }

      this.catalogLoading = true;
      this.catalogService.getChildrenCatalog(params['id'])
        .subscribe(data => {this.catalog = data})
        .add(() => {
          this.catalogLoading = false;
          if(this.catalog.children.length == 0 && this.catalog.template)
            this.navigateToCatalog(this.catalog);
        });
    });
  }
}
