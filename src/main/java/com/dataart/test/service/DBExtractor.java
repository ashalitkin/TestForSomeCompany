package com.dataart.test.service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrey on 24/05/2014.
 */
public class DBExtractor {

    private DataSource dataSource;

    public DBExtractor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected  <T> List<T> extractList(final String sql, SQLFunction<ResultSet, T> transformer, SQLConsumer<PreparedStatement> parameterSetter) throws SQLException {
        List<T> list = new ArrayList<T>();
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            parameterSetter.accept(preparedStatement);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    T dto = transformer.apply(resultSet);
                    list.add(dto);
                }
            }

        }
        return list;
    }

    protected <T> List<T> extractList(final String sql, SQLFunction<ResultSet, T> transformer) throws SQLException {
        return extractList(sql, transformer, e -> {});
    }

}
