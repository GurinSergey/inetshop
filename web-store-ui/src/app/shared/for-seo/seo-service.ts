import { Injectable } from '@angular/core';
import { Title, Meta } from "@angular/platform-browser";
import { Router, NavigationEnd } from "@angular/router";
import 'rxjs/add/operator/filter';

@Injectable()
export class SeoService {
  public constructor(
    private titleService: Title,
    private metaService: Meta,
    private router: Router,
  ) { }

  public addSeoData(): void {
    this.router.events.filter((event: any) => event instanceof NavigationEnd).subscribe(() => {
      let root = this.router.routerState.snapshot.root;
      while (root) {
        if (root.children && root.children.length) {
          root = root.children[0];
        } else if (root.data && root.data['title']) {
          this.titleService.setTitle(root.data['title']);
          let tags = root.data["metatags"];
          for (let tag in tags) {
            this.metaService.updateTag({ property: tag, content: tags[tag] });
            this.metaService.updateTag({ name: tag, content: tags[tag] });
          }
          return;
        } else {
          return;
        }
      }
    });
  }
}
