package com.dataart.test.action;

import com.dataart.test.dto.ShopInfo;
import com.dataart.test.service.ShopInfoLoader;
import com.dataart.test.service.exceptions.ValidationException;
import com.sun.deploy.net.HttpRequest;
import org.junit.Before;
import org.junit.Test;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
/**
 * Example of the test.
 * I haven't written other tests as it's not a real application but a test exercise.
 */
public class TestFacadeTest {


    @Test
    public void testDoGet_EverythingCorrectWithoutParameters() throws Exception{
        TestBuilder.checkThat().forRequest()
                .and().havingShopInfoLoaderReturnValue()
                .afterInvocation().attributeIsSet("shopInfo", ShopInfo.class).requestIsForwarded();

    }

    @Test
    public void testDoGet_EverythingCorrectWithProperParameters() throws Exception{
        TestBuilder.checkThat().forRequest()
                .withParameter("group", "1").withParameter("page","1").withParameter("orderBy","SomeOrder")
                .and().havingShopInfoLoaderReturnValue()
                .afterInvocation().attributeIsSet("shopInfo", ShopInfo.class).requestIsForwarded();

    }

    @Test(expected = ValidationException.class)
    public void testDoGet_exceptionIsThrownWhenPageIsNotNumeric() throws Exception{
        TestBuilder.checkThat().forRequest()
                .withParameter("group", "1").withParameter("page","1we")
                .and().havingShopInfoLoaderReturnValue()
                .afterInvocation().attributeIsSet("shopInfo", ShopInfo.class).requestIsForwarded();

    }

    @Test(expected = ValidationException.class)
    public void testDoGet_exceptionIsThrownWhenPageLessThen1() throws Exception{
        TestBuilder.checkThat().forRequest()
                .withParameter("group", "1").withParameter("page","0")
                .and().havingShopInfoLoaderReturnValue()
                .afterInvocation().attributeIsSet("shopInfo", ShopInfo.class).requestIsForwarded();

    }

    @Test(expected = ValidationException.class)
    public void testDoGet_exceptionIsThrownWhenGroupIsNotNumeric() throws Exception{
        TestBuilder.checkThat().forRequest()
                .withParameter("group", "1ee").withParameter("page","1")
                .and().havingShopInfoLoaderReturnValue()
                .afterInvocation().attributeIsSet("shopInfo", ShopInfo.class).requestIsForwarded();

    }

    static class TestBuilder{
        public static TestBuilder checkThat(){
            return new TestBuilder();
        }

        private TestFacade testFacade;
        private HttpServletRequest request;
        private HttpServletResponse response;
        private ShopInfoLoader shopInfoLoader;
        private RequestDispatcher requestDispatcher;

        public void setRequest(HttpServletRequest request) {
            this.request = request;
        }

        public void setResponse(HttpServletResponse response) {
            this.response = response;
        }

        TestBuilder() {
            testFacade = spy(new TestFacade());
            response = mock(HttpServletResponse.class);
            shopInfoLoader = mock(ShopInfoLoader.class);
            testFacade.setShopInfoLoader(shopInfoLoader);
            requestDispatcher = mock(RequestDispatcher.class);
            ServletContext ctx = mock(ServletContext.class);
            when(ctx.getRequestDispatcher(any(String.class))).thenReturn(requestDispatcher);
            doReturn(ctx).when(testFacade).getServletContext();

        }

        public RequestBuilder forRequest(){
            return new RequestBuilder(this);
        }

        public TestBuilder havingShopInfoLoaderReturnValue() throws SQLException {
            when(shopInfoLoader.load(any(Long.class),any(Integer.class), any(String.class))).thenReturn(new ShopInfo());
            return this;
        }

        public TestBuilder afterInvocation() throws ServletException, IOException {
            testFacade.doGet(request, response);
            return this;
        }

        public TestBuilder attributeIsSet(String name, Class clazz){
            verify(request, times(1)).setAttribute(eq(name), any(clazz));
            return this;
        }

        public TestBuilder requestIsForwarded() throws ServletException, IOException {
            verify(requestDispatcher, times(1)).forward(any(HttpServletRequest.class), any(HttpServletResponse.class));
            return this;
        }


    }

    static class RequestBuilder {
        private HttpServletRequest request = mock(HttpServletRequest.class);
        private TestBuilder parent;

        RequestBuilder(TestBuilder parent) {
            this.parent = parent;
        }

        public RequestBuilder withParameter(String name, String value){
            when(request.getParameter(name)).thenReturn(value);
            return this;
        }

        public TestBuilder and(){
            parent.setRequest(request);
            return parent;
        }
    }
}
