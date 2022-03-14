import {ActivatedRoute, ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Product} from "../../../models/product";
import {Injectable} from "@angular/core";
import {ProductService} from "../../../services/product/product.service";
import {CommentService} from "../../../services/comment/comment.service";
import {ObservableZhdunService} from "../../../services/observable/observable-zhdun.service";
import {makeStateKey, TransferState} from "@angular/platform-browser";
import {Observable} from "rxjs/Rx";

const RESULT_KEY = makeStateKey<any>('ident');

@Injectable()
export class ProductDetailsResolver implements Resolve<Product> {
  result: any;

  constructor(private productService: ProductService,
              private commentService: CommentService,
              private transferState: TransferState,
              private zhdun: ObservableZhdunService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {
    const found = this.transferState.hasKey(RESULT_KEY);
    if (found) {
      const res = Observable.of(this.transferState.get<Product>(RESULT_KEY, null));
      this.transferState.remove(RESULT_KEY);
      return res;
    } else {
      this.transferState.onSerialize(RESULT_KEY, () => this.result);
      this.zhdun.on();
      return this.productService.findOne(route.params['ident']).do(result => this.result = result);
    }
  }
}
