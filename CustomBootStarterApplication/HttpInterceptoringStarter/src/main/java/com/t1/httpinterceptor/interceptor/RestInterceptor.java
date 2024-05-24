package com.t1.httpinterceptor.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class RestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", System.currentTimeMillis());
        Map<String, String> headersMap = getHeadersFromRequest(request);

        log.info("""
                
                Request info:
                URI: {}
                Method: {}
                Params: {}
                Headers: {}
                """,
                request.getRequestURI(),
                request.getMethod(),
                request.getQueryString(),
                headersMap);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            log.error("Exception: {} during request {}", ex.getMessage(), request.getRequestURI());
        }
        long startTime = (long) request.getAttribute("startTime");
        long processingTime = System.currentTimeMillis() - startTime;

        log.info("""
                
                Response info:
                Status: {}
                Headers: {}
                Duration, milliseconds: {}
                
                """, response.getStatus(), getHeadersFromResponse(response), processingTime);
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private Map<String, String> getHeadersFromRequest(HttpServletRequest request) {
        Iterator<String> headersIterator = request.getHeaderNames().asIterator();
        List<String> headerNames = StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(headersIterator, Spliterator.ORDERED), false)
                .toList();
        return headerNames.stream().collect(Collectors.toMap(name -> name, request::getHeader));
    }

    private Map<String, String> getHeadersFromResponse(HttpServletResponse response) {
        Collection<String> headerNames = response.getHeaderNames();
        return headerNames.stream().collect(Collectors.toMap(name -> name, response::getHeader));
    }
}
