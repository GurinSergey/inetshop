import {Injectable} from '@angular/core';
import {HttpWrapperService} from '../http-wrapper/http-wrapper.service';
import {GlobalConst} from '../../globals/GlobalConst';
import {User} from '../../models/user.model';
import {HttpHeaders, HttpParams} from '@angular/common/http';
import {Order} from '../../models/order.model';
import {OrderState} from '../../globals/order-state.enum';

import {CommentEntry} from "../../models/comment-entry";
import {Product} from "../../models/product.model";

import {UserOrder} from "../../models/user-order";
import {FilterEntry} from "../../models/filter-entry";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};


@Injectable()
export class HttpRequestsService {

  constructor(private httpWrapper: HttpWrapperService) {
  }

  /*Product*/
  getProducts(templateId: number, offset: number, limit: number, sort_direction: string, sort_property: string, filter: string) {
    let url = `${GlobalConst.SERVER_ADDR}${templateId}/product/?&offset=${offset}&limit=${limit}&sort_direction=${sort_direction}&sort_property=${sort_property}${filter}`;
    return this.httpWrapper.get(url);
  }


  getOneProduct(latIdent: string) {
    return this.httpWrapper.getJSessions(`${GlobalConst.SERVER_ADDR}product/${latIdent}`);
  }

  /*End*/
  getProductFields(id: number) {
    return this.httpWrapper.get(`${GlobalConst.SERVER_ADDR}product/${id}/fields`);
  }

  getProductPhotos(id: number) {
    return this.httpWrapper.get(`${GlobalConst.SERVER_ADDR}product/${id}/photos`);
  }

  /*VDN Comments*/
  getProductComments(id: number, offset: number, limit: number) {
    return this.httpWrapper.get(`${GlobalConst.SERVER_ADDR}product/${id}/comments/?offset=${offset}&limit=${limit}`);
  }

  getCountComments(id_product: number) {
    return this.httpWrapper.get(`${GlobalConst.SERVER_ADDR}product/${id_product}/comments/count`);
  }

  postNewComment(id_product: number, comment: CommentEntry) {
    return this.httpWrapper.postJSessions(`${GlobalConst.SERVER_ADDR}product/${id_product}/comment/add`, comment);
  }

  likeComment(id_product: number, id_comment: number) {
    return this.httpWrapper.postJSessions(`${GlobalConst.SERVER_ADDR}product/${id_product}/comment/${id_comment}/like`, null);
  }

  dislikeComment(id_product: number, id_comment: number) {
    return this.httpWrapper.postJSessions(`${GlobalConst.SERVER_ADDR}product/${id_product}/comment/${id_comment}/dislike`, null);
  }

  getPageCommentsByCommentId(limit: number, productId: number, commentId: number) {
    return this.httpWrapper.get(`${GlobalConst.SERVER_ADDR}comments/getOffsetByCommentId/?limit=${limit}&productId=${productId}&commentId=${commentId}`);
  }
  /*End Comments*/
  getAllProductGroups() {
    return this.httpWrapper.get(GlobalConst.SERVER_ADDR + 'admin/getAllPG');
  }

  /*Filter*/
  getFilterListByTemplateId(id: number) {
    const url = `${GlobalConst.SERVER_ADDR}templates/getTemplateFieldsById?id=${id}`;
    return this.httpWrapper.get(url);
  }

  findCntByFilter(templateId:number, filter:string){
    return this.httpWrapper.get(`${GlobalConst.SERVER_ADDR}product/getCntFilterValue?id=${templateId}${filter}`);
  }
  /*End Filter*/

  /*Catalog*/
  getAllCatalogs(){
    return this.httpWrapper.get(`${GlobalConst.SERVER_ADDR}catalog/getAllCatalogs`);
  }

  getCatalogsByParentIsNull(){
    return this.httpWrapper.get(`${GlobalConst.SERVER_ADDR}catalog/getParentCatalogs`);
  }

  getChildrenCatalog(catalogId:number){
    return this.httpWrapper.get(`${GlobalConst.SERVER_ADDR}catalog/getCatalogs?id=${catalogId}`);
  }
  /*End Catalog*/

  /*Favorite*/
  addToFavorite(product_id: number){
    return this.httpWrapper.postJSessions(GlobalConst.SERVER_ADDR + 'sessionStorage/addToFavoriteList', product_id);
  }

  synchronizeFavorite() {
    return this.httpWrapper.get(GlobalConst.SERVER_ADDR + 'sessionStorage/getFavoriteList');
  }
  /*End Favorite*/

  // Orders
  addOrder(order: UserOrder) {
    return this.httpWrapper.postJSessions(GlobalConst.SERVER_ADDR + 'orders/addOrder', order);
  }

  getAllOrders() {
    return this.httpWrapper.get(GlobalConst.SERVER_ADDR + 'orders/getAll');
  }

  getUserOrders(offset: number = 0, limit: number = 10) {
    const url = `${GlobalConst.SERVER_ADDR}orders/getAllByCurrentUser?offset=${offset}&limit=${limit}`;

    return this.httpWrapper.get(url);
  }

  getOrderDetailsById_Admin(id: number | string) {
    const url = `${GlobalConst.SERVER_ADDR}orderDetails/adminGetOrderDetailsById?id=${id}`;
    return this.httpWrapper.get(url);
  }

  getOrderDetailsById_User(id: number | string) {
    const url = `${GlobalConst.SERVER_ADDR}orderDetails/userGetOrderDetailsById?id=${id}`;
    return this.httpWrapper.get(url);
  }

  getOrderStatesById(id: number | string) {
    const url = `${GlobalConst.SERVER_ADDR}orderDetails/getOrderStatesById?id=${id}`;
    return this.httpWrapper.get(url);
  }


  getAllReserveByOrderDetailId(id: number | string) {
    const url = `${GlobalConst.SERVER_ADDR}orderDetails/getAllReserveByOrderDetailId?id=${id}`;
    return this.httpWrapper.getJSessions(url);
  }


  getProtocolById(id: number | string) {
    const url = `${GlobalConst.SERVER_ADDR}orders/getProtocolById?id=${id}`;
    return this.httpWrapper.getJSessions(url);
  }

  updateOrder(data: Order) {
    console.log('updateOrder');
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'admin/updateOrder', data);
  }

  updateStateOrder(order: Order, state: OrderState) {
    let body = new HttpParams();
    body = body.set('orderId', order.orderId.toString());
    body = body.set('newState', state.toString());
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'orders/updateStateOrder', body);
  }

  updateStateOrderMass(orders: any, state: OrderState) {
    let body = new HttpParams();
    body = body.set('orderId', orders.toString());
    body = body.set('newState', state.toString());
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'orders/updateStateOrderMass', body);
  }

  // Login
  getAllUsers() {
    return this.httpWrapper.get(GlobalConst.SERVER_ADDR + 'users/getAll');
  }

  getUser(id: number | string) {
    const url = `${GlobalConst.SERVER_ADDR}users/getById?id=${id}`;
    return this.httpWrapper.get(url);
  }

  getAllTemplates() {
    return this.httpWrapper.get(GlobalConst.SERVER_ADDR + 'templates/getAllTemplates');
  }

  getFieldsByTemplateId(id: number) {
    const url = `${GlobalConst.SERVER_ADDR}templates/getFieldsByTemplateId?id=${id}`;
    return this.httpWrapper.get(url);
  }

  login(data: any) {
    return this.httpWrapper.post(GlobalConst.SERVER_ADDR + 'login', data);
  }

  // Logout
  logout() {
    return this.httpWrapper.getJSessions(GlobalConst.SERVER_ADDR + 'logoutMe');
  }


  // Registry
  registry(data: any) {
    return this.httpWrapper.post(GlobalConst.SERVER_ADDR + 'registry', data);
  }

  // change email
  changeEmail(data: any) {
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'users/changeEmail', data);
  }

  checkEmail(email: any) {
    let body = new HttpParams();
    body = body.set('email', email);
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'users/checkEmail', body);
  }

  // forgot pass
  forgotPass(email: string) {
    return this.httpWrapper.post(GlobalConst.SERVER_ADDR + 'sendpass', email);
  }

  // change pass
  changePass(data: any) {
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'changepass', data);
  }

  checkPass(pass: any) {
    let body = new HttpParams();
    body = body.set('pass', pass);
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'users/checkPass', body);
  }

  updateUser(data: User) {
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'users/updateUser', data);
  }


  updateUserProfile(data: User) {
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'users/updateUserProfile', data);
  }

  sendConfirmEmail() {
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'users/sendConfirmEmail', '');
  }


  getProfile() {
    return this.httpWrapper.get(GlobalConst.SERVER_ADDR + 'users/profile');
  }

  synchronizeBasket(data: any) {
    return this.httpWrapper.postJSessions(GlobalConst.SERVER_ADDR + 'sessionStorage/synchronizeBasket', data);
  }

  synchronizeWatch(data: any) {
    return this.httpWrapper.postJSessions(GlobalConst.SERVER_ADDR + 'sessionStorage/synchronizeWatchHistory', data);
  }

  addToBasket(code: any) {
    let body = new HttpParams();
    body = body.set('code', code);
    return this.httpWrapper.postJSessions(GlobalConst.SERVER_ADDR + 'basket/addToBasket', body);
  }

  addToWatch(code: any) {
    let body = new HttpParams();
    body = body.set('code', code);
    return this.httpWrapper.postJSessions(GlobalConst.SERVER_ADDR + 'sessionStorage/addToWatchHistory', body);
  }

  mailConfirm(guid: any) {
    const url = `${GlobalConst.SERVER_ADDR}confirm_mail?guid=${guid}`;
    return this.httpWrapper.get(url);
  }


  getAllContractor() {
    return this.httpWrapper.get(GlobalConst.SERVER_ADDR + 'warehouse/getAllContractor');
  }


  getAllWarehouse() {
    return this.httpWrapper.get(GlobalConst.SERVER_ADDR + 'warehouse/getAllWarehouse');
  }

  getAllInvoiceByWarehouse(warehouseId: any) {
    const url = `${GlobalConst.SERVER_ADDR}warehouse/getAllInvoiceByWarehouse?id=${warehouseId}`;
    return this.httpWrapper.get(url);
  }

  getCurrentProductRestAll(date: any, warehouseId: any) {
    let body = new HttpParams();
    body = body.set('restDate', date.toString());
    body = body.set('warehouseId', warehouseId.toString());
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'warehouse/getCurrentProductRestAll', body);
  }

  getInvoicePartByArrayId(invoices: any) {
    let body = new HttpParams();
    body = body.set('invoices', invoices.toString());
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'warehouse/getInvoicePartByArrayId', body);
  }

  getInvoicePartById(invoice: any) {
    let body = new HttpParams();
    body = body.set('invoice', invoice.toString());
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'warehouse/getInvoicePartById', body);
  }

  calcInitialSumById(invoice: any) {
    let body = new HttpParams();
    body = body.set('invoice', invoice.toString());
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'warehouse/calcInitialSumById', body);
  }


  getInvoice(id: number | string) {
    const url = `${GlobalConst.SERVER_ADDR}warehouse/getInvoice?id=${id}`;
    return this.httpWrapper.get(url);
  }


  checkInPurchaseInvoiceById(invoice: any) {
    let body = new HttpParams();
    body = body.set('invoice', invoice.toString());
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'warehouse/checkInPurchaseInvoiceById', body);
  }


  getAutoCompleteProduct(data: Product) {
    console.log('getAutoCompleteProduct');
    console.log(data);
    return this.httpWrapper.postJWT(GlobalConst.SERVER_ADDR + 'warehouse/getAutoCompleteProduct', data);
  }

  getAutoCompleteProductForSearch(data: String) {
    return this.httpWrapper.get(`${GlobalConst.SERVER_ADDR}product/getAutoCompleteProductForSearch?match=${data}`);
  }

  getUserComments(offset: number = 0, limit: number = 10) {
    const url = `${GlobalConst.SERVER_ADDR}comments/getAllByCurrentUser?offset=${offset}&limit=${limit}`;

    return this.httpWrapper.get(url);
  }

  searchProducts(templates_offset: number = 0, templates_limit: number = 5, products_limit: number = 10, filter_string: string = '') {
    const url = `${GlobalConst.SERVER_ADDR}search/?templates_offset=${templates_offset}&templates_limit=${templates_limit}&products_limit=${products_limit}&filter_string=${filter_string}`;

    return this.httpWrapper.get(url);
  }

  getCompareBox(productsId: any) {
    let param = new HttpParams();
    param = param.set('productsId', productsId.toString());

    return this.httpWrapper.getWithParams(GlobalConst.SERVER_ADDR + 'compare/', param);
  }

  getAllNovaCity() {
    return this.httpWrapper.get(GlobalConst.SERVER_ADDR + 'checkout/getAllNova');
  }

  getAllByCity(cityRef: any) {
    let param = new HttpParams();
    param = param.set('cityRef', cityRef.toString());
    return this.httpWrapper.getWithParams(GlobalConst.SERVER_ADDR + 'checkout/getAllByCityRef', param);
  }

  getTheBestSellingProducts(limit: number = 10) {
    const url = `${GlobalConst.SERVER_ADDR}statistics/getTheBestSellingProducts?limit=${limit}`;

    return this.httpWrapper.get(url);
  }
}
