export enum OrderState {
  NEW = "NEW",
  IN_PROCESS = "IN_PROCESS",
  CONFIRM = "CONFIRM",
  WAIT = "WAIT",
  SEND_ORDER = "SEND_ORDER",
  RECEIVED = "RECEIVED",
  CLOSED ="CLOSED" ,
  REJECTED ="REJECTED" ,
  MANAGER_CHANGE ="MANAGER_CHANGE" ,
  OPER_SHIFT = "OPER_SHIFT"
}


export const orderStateDescription = [
  {code: 'NEW', text: 'Новый Заказ'},
  {code: 'IN_PROCESS', text: 'Обрабатывается менеджером'},
  {code: 'CONFIRM', text: 'Заказ подтвержден'},
  {code: 'WAIT', text: 'Заказ сформирован и ожидает отгрузки'},
  {code: 'SEND_ORDER', text: 'Заказ отправлен'},
  {code: 'RECEIVED', text: 'Товар получен'},
  {code: 'CLOSED', text: 'Заказ закрыт'},
  {code: 'REJECTED', text: 'Заказ отменен'},
  {code: 'MANAGER_CHANGE', text: 'Заказ изменен менеджером'},
  {code: 'OPER_SHIFT', text: 'Смена исполнителя'},
  {code: 'WAIT_REST', text: 'На ожидание'},
  {code: 'MANUAL_PROCESSING', text: 'На ручной обработке'},
  {code: 'CHECK_REST', text: 'Проверка остатков'}
];
