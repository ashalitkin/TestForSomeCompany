package com.dataart.test.service;

/**
 * Created by andrey on 24/05/2014.
 */
public enum ProductsOrdering {
    ORDERED_BY_NAME("select p.* from t_product p\n" +
            "where p.group_id=?\n" +
            "order by p.product_name\n" +
            "limit ?, ?", "ORDERED_BY_NAME_DESC"),

    ORDERED_BY_NAME_DESC("select p.* from t_product p\n" +
            "where p.group_id=?\n" +
            "order by p.product_name desc\n" +
            "limit ?, ?", "ORDERED_BY_NAME"),



    ORDERED_BY_PRICE("select p.* from t_product p\n" +
            "where p.group_id=?\n" +
            "order by p.product_price\n" +
            "limit ?, ?", "ORDERED_BY_PRICE_DESC"),

    ORDERED_BY_PRICE_DESC("select p.* from t_product p\n" +
                             "where p.group_id=?\n" +
                             "order by p.product_price desc\n" +
                             "limit ?, ?", "ORDERED_BY_PRICE");

    private String sql;

    private String opposite;

    public String getSql() {
        return sql;
    }

    public ProductsOrdering getOpposite(ProductsOrdering currentOrder){
        if (this.equals(currentOrder))
            return ProductsOrdering.valueOf(opposite);
        else return this;
    }

    ProductsOrdering(String sql, String opposite) {
        this.sql = sql;
        this.opposite = opposite;
    }


}
