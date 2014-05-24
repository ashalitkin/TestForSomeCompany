package com.dataart.test.action;

import com.dataart.test.dto.ShopInfo;
import com.dataart.test.service.ShopInfoLoader;
import com.dataart.test.service.exceptions.ValidationException;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * Created by andrey on 23/05/2014.
 */
@WebServlet(
        name = "products",
        urlPatterns = {"/products"}
)
public class TestFacade extends HttpServlet {
    private static final Logger logger = Logger.getLogger(TestFacade.class.getName());
    public static final String SHOP_FORM_NAME = "shopInfo";
    public static final String VIEW_JSP = "/view.jsp";

    @Resource(name="jdbc/dbtest")
    private DataSource dataSource;

    private ShopInfoLoader shopInfoLoader;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setShopInfoLoader(ShopInfoLoader shopInfoLoader) {
        this.shopInfoLoader = shopInfoLoader;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String group = req.getParameter("group");
        String page = req.getParameter("page");
        String orderBy = req.getParameter("orderBy");
        validate(group, page);
        ShopInfo shopInfo = null;
        try {
            shopInfo = shopInfoLoader.load(
                    group == null ? null : Long.valueOf(group),
                    page == null ? null : Integer.valueOf(page),
                    orderBy);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new ServletException("An error occured, please contact support", e);
        }
        req.setAttribute(SHOP_FORM_NAME, shopInfo);
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(VIEW_JSP);
        requestDispatcher.forward(req, resp);

    }

    private void validate(final String group,final String page) {
        if (group != null && group.matches(".*\\D.*")){
            throw new ValidationException("group is not numeric");
        }


        if (page != null){
            if (page.matches(".*\\D.*"))
                throw new ValidationException("page is not numeric");
            if (page.trim().matches("0*"))
                throw new ValidationException("page can't be less than 1");
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        shopInfoLoader = new ShopInfoLoader(this.dataSource);
    }
}
