package com.t1.httpinterceptor.interceptor;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class RestInterceptorTest {

    @Test
    void testPreHandle() throws Exception {

        RestInterceptor interceptor = new RestInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = new Object();

        boolean result = interceptor.preHandle(request, response, handler);

        assertTrue(result);
        assertNotNull(request.getAttribute("startTime"));
    }
}
