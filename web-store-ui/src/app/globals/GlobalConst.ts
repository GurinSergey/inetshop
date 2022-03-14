export class GlobalConst {
  static readonly SERVER_ADDR = 'http://localhost:8778/';
  static readonly SERVER_IMG_ADDR = 'http://localhost:8065/assets/';
 // static readonly SERVER_IMG_ADDR = 'http://localhost:8778/';
  static readonly WEBSOCKET_ADDR = 'ws://localhost:8778/commonNotifications/websocket';
  static TOKEN: string;
  static readonly COOKIES_TOKEN = 'tolcom_token';
  static readonly Domain ='localhost';
  static readonly RESPONSE_RESULT_SUCCESS = 'OK';
  static readonly RESPONSE_RESULT_ERROR = 'SERVICE_UNAVAILABLE';
  static readonly RESPONSE_RESULT_UNAUTHORIZED = 'UNAUTHORIZED';
  static readonly INTERNAL_SERVER_ERROR = '500';
  static readonly PHONE_MASK = ['(', /[0-9]/, /\d/, /\d/, ')', ' ', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/];

  static readonly PRODUCT_lIST_LIMIT = 15;
  static readonly PRODUCT_COMMENTS_LIMIT = 10;
  static readonly PERSONAL_AREA_LIMIT = 15;

  static readonly MAX_PRODUCTS_TO_COMPARE = 5;

  static readonly SLIDER_PRODUCT_LIMIT = 5;

  static readonly AUTOCOMPLETE_SEARCH_PAGE_SIZE = 10;

  static readonly SEARCH_TEMPLATES_LIMIT = 5;
  static readonly SEARCH_PRODUCTS_LIMIT = 10;

  static readonly FILTER_VALUES_LIMIT = 5;

  static readonly STAR_TITLES = ['Плохо', 'Так себе', 'Нормально', 'Не лучшее', 'Агонь'];
}
