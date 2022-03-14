package com.webstore.domain.enums;

/**
 * Created by ALisimenko on 27.02.2018.
 * table order_history_state
 */
public enum OrderState {
    NEW(0, "Новый Заказ"),
    IN_PROCESS(1, "Обрабатывается менеджером"),
    CONFIRM(2, "Заказ подтвержден"),
    WAIT(3, "Заказ сформирован и ожидает отгрузки"),
    SEND_ORDER(4, "Заказ отправлен"),
    RECEIVED(5, "Товар получен"),
    CLOSED(6, "Заказ закрыт"),
    REJECTED(7, "Заказ отменен"),
    MANAGER_CHANGE(8, "Заказ изменен менеджером"),
    OPER_SHIFT(9, "Смена исполнителя"),
    WAIT_REST(10, "На ожидание"),
    MANUAL_PROCESSING(11, "На ручной обработке"),
    CHECK_REST(11, "Проверка остатков");


    int index;
    String value;

    private OrderState(int index, String value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }
}