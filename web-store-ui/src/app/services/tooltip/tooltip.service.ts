import {Injectable} from "@angular/core";

@Injectable()
export class TooltipService{
  components: any[] = [];

  destroy(id:number){
    const index = this.components.findIndex((item) => item.id === id);
    this.components.splice(index, 1);
  }
}
