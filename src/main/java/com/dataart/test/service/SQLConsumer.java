package com.dataart.test.service;

import java.sql.SQLException;

/**
 * Created by andrey on 24/05/2014.
 */


@FunctionalInterface
public interface SQLConsumer<T> {
    void accept(T t) throws SQLException;
}
