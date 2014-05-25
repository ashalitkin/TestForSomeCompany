package com.dataart.test.service;

import com.dataart.test.dto.GroupInfo;
import com.dataart.test.dto.ProductInfo;
import com.dataart.test.dto.ShopInfo;
import com.dataart.test.service.exceptions.NoDataAvailableException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by andrey on 23/05/2014.
 */
public class ShopInfoLoader extends DBExtractor {
    public static final int FIRST_PAGE = 1;
    public static final int PAGE_SIZE = 10;
    Logger logger = Logger.getLogger(DBExtractor.class.getName());

    public static final String GET_GROUP_INFO = "select g.group_id, g.group_name, n.product_count from T_GROUP g\n" +
            "left join \n" +
            "(\n" +
            "\tselect p.group_id group_id, count(*) product_count from t_product p\n" +
            "\tgroup by p.group_id\n" +
            "\thaving count(*) > 0) n\n" +
            "on n.group_id=g.group_id\n" +
            "order by g.group_name;";

    public ShopInfoLoader(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected ShopInfo _load(Connection connection, Long groupId, Integer page, String orderByField) throws SQLException {
        List<GroupInfo> groupInfos = loadGroupInfo(connection);

        final long refinedGroupId = getRefinedGroupId(groupId, groupInfos);
        final int refinedPage = getRefinedPage(page);
        ProductsOrdering ordering = getProductsOrdering(orderByField);

        List<ProductInfo> productInfos = loadProductsInfo(connection, refinedGroupId, refinedPage, ordering.getSql());
        return new ShopInfo(productInfos, refinedPage, groupInfos, ordering, refinedGroupId);
    }


    private ProductsOrdering getProductsOrdering(String orderByField) {
        return orderByField == null ? ProductsOrdering.ORDERED_BY_NAME : ProductsOrdering.valueOf(orderByField);
    }

    private int getRefinedPage(Integer page) {
        return page == null ? FIRST_PAGE : page;
    }

    private long getRefinedGroupId(Long groupId, List<GroupInfo> groupInfos) {
        if (groupId == null) {
            if (groupInfos.size() > 0)
                return groupInfos.get(0).getId();
            else throw new NoDataAvailableException("No Data is available in DB");
        } else {
            return groupId;
        }
    }

    private List<ProductInfo> loadProductsInfo(Connection connection, final long groupId, final int page, final String sql) throws SQLException {
        logger.log(Level.FINE, String.format("loading products for group=%0$d, page=%1$d, sql=%2$s", groupId, page, sql));
        return extractList(connection, sql, rs -> {
            return new ProductInfo(rs.getLong(1), rs.getString(2), rs.getBigDecimal(3));
        }, st -> {
            st.setLong(1, groupId);
            st.setInt(2, (page - 1) * PAGE_SIZE);
            st.setInt(3, PAGE_SIZE);
        });
    }

    private List<GroupInfo> loadGroupInfo(Connection connection) throws SQLException {
        return extractList(connection, GET_GROUP_INFO, rs -> {
            return new GroupInfo(rs.getLong(1), rs.getString(2), rs.getInt(3));
        });
    }


}
