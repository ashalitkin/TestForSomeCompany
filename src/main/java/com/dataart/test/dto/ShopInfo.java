package com.dataart.test.dto;

import com.dataart.test.service.ProductsOrdering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by andrey on 23/05/2014.
 */
public class ShopInfo {
    private List<ProductInfo> productList;
    private List<GroupInfo> groupList;

    private Integer currentPage;
    private ProductsOrdering productsOrdering;
    private Long currentGroup;

    public ShopInfo(List<ProductInfo> productList, Integer currentPage, List<GroupInfo> groupList, ProductsOrdering productsOrdering, Long currentGroup) {
        this.productList = productList;
        this.currentPage = currentPage;
        this.groupList = groupList;
        this.productsOrdering = productsOrdering;
        this.currentGroup = currentGroup;
    }

    public ShopInfo() {
    }

    public Long getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(Long currentGroup) {
        this.currentGroup = currentGroup;
    }

    public ProductsOrdering getProductsOrdering() {
        return productsOrdering;
    }

    public void setProductsOrdering(ProductsOrdering productsOrdering) {
        this.productsOrdering = productsOrdering;
    }

    public List<ProductInfo> getProductList() {
        if (productList == null) productList = new ArrayList<ProductInfo>();
        return productList;
    }

    public void setProductList(List<ProductInfo> productList) {
        this.productList = productList;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Collection<GroupInfo> getGroupList() {
        if (groupList == null) groupList = new ArrayList<GroupInfo>();
        return groupList;
    }

    public void setGroupList(List<GroupInfo> groupList) {
        this.groupList = groupList;
    }
}
