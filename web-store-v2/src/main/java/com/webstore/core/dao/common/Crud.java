package com.webstore.core.dao.common;

/**
 * Created by DVaschenko on 22.07.2016.
 */
public interface Crud<T> {
    T add(T item);
    T find(T item);
    T find(int id);
    boolean delete(T item);
    boolean delete(int id);
    T update(T item);
}
