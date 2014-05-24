<%@ page import="com.dataart.test.dto.ShopInfo" %>
<%@ page import="com.dataart.test.dto.GroupInfo" %>
<%@ page import="com.dataart.test.dto.ProductInfo" %>
<%@ page import="com.dataart.test.service.ProductsOrdering" %>
<%@ page import="java.util.HashMap" %>
<html>
<head>
 <link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>
 <%
    ShopInfo shopInfo = (ShopInfo)request.getAttribute("shopInfo");
    Long currentGroup = shopInfo.getCurrentGroup();
    Integer currentPage =  shopInfo.getCurrentPage();
    ProductsOrdering ordering = shopInfo.getProductsOrdering();

    HashMap<Long, Integer> groupCountMap = new HashMap<Long, Integer>();
 %>
 <div class="contentArea">
 <h2>Product Shop</h2>
 <div class="columnLeft menu">
 <%
    for(GroupInfo groupInfo: shopInfo.getGroupList()){
        groupCountMap.put(groupInfo.getId(), groupInfo.getCount());
 %>
    <a href='/products?group=<%=groupInfo.getId()%>&page=1&orderBy=<%=ordering%>' >
      <%=groupInfo.getName()%> (<%=groupInfo.getCount()%>)
    </a>
    <br/>
 <%
    }
 %>
 </div>
    <div class="columnLeft productArea">
    <table>
      <tr class="tableHead">
       <th><a href='/products?group=<%=currentGroup%>&page=1&orderBy=<%=ProductsOrdering.ORDERED_BY_NAME.getOpposite(ordering)%>' >Product Name </a></th>
       <th><a href='/products?group=<%=currentGroup%>&page=1&orderBy=<%=ProductsOrdering.ORDERED_BY_PRICE.getOpposite(ordering)%>' > Product Price </th>
      </tr>
     <%for(ProductInfo productInfo : shopInfo.getProductList()) {%>
       <tr>
        <td><%=productInfo.getName()%></td>
        <td><%=productInfo.getPrice()%></td>
       </tr>
      <%}%>
    </table>
    <%if (currentPage > 1){%>
     <div class="columnLeft">
        <a href='/products?group=<%=currentGroup%>&page=<%=(currentPage-1)%>&orderBy=<%=ordering%>' > << </a>
     </div>
    <%}%>
    <%if (10*currentPage < groupCountMap.get(currentGroup)){%>
      <div class="columnRight">
            <a href='/products?group=<%=currentGroup%>&page=<%=(currentPage+1)%>&orderBy=<%=ordering%>' > >> </a>
       </div>
    <%}%>
  </div>
</div>
</body>
</html>
