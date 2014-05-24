package com.dataart.test.service;

import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * Created by andrey on 24/05/2014.
 */


@FunctionalInterface
public interface SQLFunction<T,R> {
    R apply(T t) throws SQLException;
}
