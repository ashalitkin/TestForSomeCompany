package com.dataart.test.service;

import com.dataart.test.dto.GroupInfo;
import com.dataart.test.dto.ProductInfo;
import com.dataart.test.dto.ShopInfo;

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
public abstract class DBExtractor {

    private DataSource dataSource;

    public DBExtractor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected <T> List<T> extractList(final Connection connection, final String sql, SQLFunction<ResultSet, T> transformer, SQLConsumer<PreparedStatement> parameterSetter) throws SQLException {
        List<T> list = new ArrayList<T>();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        parameterSetter.accept(preparedStatement);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                T dto = transformer.apply(resultSet);
                list.add(dto);
            }
        }

        return list;
    }

    public <T> ShopInfo load(final Long groupId, final Integer page, final String orderByField) throws SQLException {
        ShopInfo shopInfo = null;
        try (Connection connection = dataSource.getConnection()) {
            shopInfo = _load(connection, groupId, page, orderByField);
        }
        return shopInfo;
    }

    public abstract ShopInfo _load(final Connection connection, final Long groupId, final Integer page, final String orderByField) throws SQLException;

    protected <T> List<T> extractList(final Connection connection, final String sql, SQLFunction<ResultSet, T> transformer) throws SQLException {
        return extractList(connection, sql, transformer, e -> {
        });
    }

}
