import {Product} from "../../../models/product";



export class ComplexWatchStorage {
  watchHistory: WatchHistory;
  note: string;
  count: number ;

  constructor(watchHistory: WatchHistory, note: string, count: number) {
    this.watchHistory = watchHistory;
    this.note = note;
    this.count = count;
  }
}

export class WatchHistory{
  productList:Product[];
}

