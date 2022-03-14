import {Injectable} from '@angular/core';
import {GlobalConst} from '../../globals/GlobalConst';
import {ResultCode} from '../../globals/result-code.enum';
import {HttpRequestsService} from '../http-requests/http-requests.service';
import {map} from "rxjs/internal/operators";
import {CatalogEntry, TemplateEntry} from "../../models/catalog-entry";

@Injectable()
export class TemplatesService {

  constructor(private httpRequest: HttpRequestsService) {
  }

  getAllTemplates(): any {
    return this.httpRequest.getAllTemplates().pipe(map((data: any) => {
      if (!(data.result === GlobalConst.RESPONSE_RESULT_SUCCESS))
        throw data.payload;
      return null;
    }));
  }

  getFieldsByTemplateId(id: number): any {
    return this.httpRequest.getFieldsByTemplateId(id).pipe(map((data: any) => {
      if ((data.result === GlobalConst.RESPONSE_RESULT_SUCCESS) && (data.resultCode === ResultCode.SUCCESSFULL)) {
        return data.payload;
      }
      return null;
    }));
  }
}
