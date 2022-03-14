import {ResultCode} from "../globals/result-code.enum";

export class Response{
  result:string;
  resultCode:ResultCode;
  payload:any;

  constructor() {
  }
}
