import {Component, EventEmitter, OnChanges, OnDestroy, OnInit, Output, SimpleChanges} from '@angular/core';
import {FilterEntry, FilterValueEntry} from "../../../models/filter-entry";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs/index";
import {ProductFilterService} from "../../../services/product-filter/product-filter.service";
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthorizeConst} from "../../../globals/AuthorizeConst";

@Component({
  selector: 'app-filter-list',
  templateUrl: './filter-list.component.html',
  styleUrls: ['./filter-list.component.css']
})
export class FilterListComponent implements OnInit, OnDestroy {
  templateId:number;
  filterList:FilterEntry[];
  openAll:boolean = true;
  private routeParams: Subscription;
  isLoadingFilter:boolean = false;

  priceForm: FormGroup;
  minPrice:number = 0;
  minPriceId:number = -1;
  maxPriceId:number = -2;

  @Output() filterClick: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(
    private route: ActivatedRoute,
    private filterService: ProductFilterService,
    private fBuilder: FormBuilder)
  {
    this.priceForm = fBuilder.group({
      'minPrice': ['', [Validators.min(0)]],
      'maxPrice': ['', [(control:AbstractControl) => Validators.min(this.minPrice)(control)]],
    });
  }

  ngOnInit() {
    this.priceForm.get('minPrice').valueChanges.subscribe(val => this.minPrice = val);

    this.routeParams = this.route.params.subscribe(params => {
      this.isLoadingFilter = true;
      this.templateId = params['id'];
      const getFilterSub = this.filterService.getFilter(this.templateId).subscribe(data => this.filterList = data.fields);
      getFilterSub.add(() =>{
        this.isLoadingFilter = false;
        this.findAllCtnByFilter();
      });
    });
  }

  findAllCtnByFilter(){
    this.filterService.findAllCtnByFilter(this.templateId).subscribe((data:any) => {
      this.filterList.map(item => {
        item.params.map(val => {
          let obj = data.filter(c => c.id == val.id);
          let old_val = val.cntByFilterValue;
          val.cntByFilterValue = obj.length > 0? obj[0].cnt : 0;
          if(item.checked && !val.checked)
            val.cntByFilterValue = old_val;
        })
      });
    });
  }

  ngOnDestroy(): void {
    this.routeParams.unsubscribe();
    this.filterService.setFilter([]);
  }

  unCheckedValueFilter(id:number){
    this.filterList.map(item => {
      item.params.filter(child => child.id == id)
        .map(child => child.checked = false)
    });

    if(id == this.minPriceId)
      this.priceForm.controls['minPrice'].setValue('');

    if(id == this.maxPriceId)
      this.priceForm.controls['maxPrice'].setValue('');

    this.checkValueFilter();
    this.searchByFilter();
  }

  checkValueFilter(){
    this.filterList.map(f => f.checked = false);

    this.filterList.filter(f => {
      return f.params.some(c => c.checked);
    }).map(f => f.checked = true);
  }

  private validPriceForm():boolean{
    return this.priceForm.valid && (
      this.priceForm.controls['minPrice'].value > 0 ||
      this.priceForm.controls['maxPrice'].value > 0
    );
  }

  searchByFilter(){
    /*Гавно-код как просто сделать копию объкта, очень желательно переделать*/
    let selectedFilter:FilterEntry[] = JSON.parse(JSON.stringify(this.filterList.filter(f => f.checked))) as FilterEntry[];
    selectedFilter.map(f => {f.params = f.params.filter(param => param.checked);});

    if(this.validPriceForm()){
      let prices = new FilterEntry(0, 'price');
      let value = this.priceForm.controls['minPrice'].value;
      if(value > 0)
        prices.params.push(new FilterValueEntry(this.minPriceId, 'от '.concat(value), value));
      value = this.priceForm.controls['maxPrice'].value
      if(value > 0)
        prices.params.push(new FilterValueEntry(this.maxPriceId, 'до '.concat(value), '-'.concat(value)));
      selectedFilter = selectedFilter.concat(prices);
    }

    this.filterService.setFilter(selectedFilter);
    this.filterClick.emit(true);
    this.findAllCtnByFilter();
  }




  /*
  selectedFiles: FileList;
  currentFileUpload: File;

  selectFile(event){
    const file = event.target.files.item(0);

    if (file.type.match('image.*')) {
      this.selectedFiles = event.target.files;
    } else {
      alert('invalid format!');
    }
  }

  uploadToServer():Observable<HttpEvent<{}>>{
    this.currentFileUpload = this.selectedFiles.item(0);

    let formdata: FormData = new FormData();
    formdata.append('file', this.currentFileUpload );

    const req = new HttpRequest('POST', `${GlobalConst.SERVER_ADDR}product/photo/load/?id=${2}`, formdata, {
      reportProgress: true,
      responseType: 'text'
    });

    return this.http.request(req);
  }

  upload(){
    this.uploadToServer().subscribe(event =>{
      console.log('upload');
    });
  }

  <!--<label class="btn btn-default">-->
    <!--<input type="file" (change)="selectFile($event)">-->
  <!--</label>-->

  <!--<button class="btn btn-success" (click)="upload()">Upload</button>-->

  */
}
