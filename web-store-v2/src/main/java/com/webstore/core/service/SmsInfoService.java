package com.webstore.core.service;

import com.webstore.core.entities.enums.OrderState;
import com.webstore.core.entities.json.SmsInfoJson;

import java.util.List;

/**
 * Created by Andoliny on 09.11.2016.
 */
public interface SmsInfoService {
    public SmsInfoJson getSmsInfoJson();
    public boolean updateStatus(int listId, OrderState status);
    public boolean updateStatus(List<Integer> idOrdersJson, OrderState status);
}
