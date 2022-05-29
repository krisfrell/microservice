package com.krisfrell.filter;

import com.krisfrell.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MetricFilter implements Filter {

    @Autowired
    MetricService metricService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (metricService == null) {
            WebApplicationContext appContext = WebApplicationContextUtils
                    .getRequiredWebApplicationContext(filterConfig.getServletContext());

            metricService = appContext.getBean(MetricService.class);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = ((HttpServletRequest) request);
        final String req = httpRequest.getMethod() + " " + httpRequest.getRequestURI();

        chain.doFilter(request, response);

        final int status = ((HttpServletResponse) response).getStatus();
        metricService.increaseCount(req, status);
    }
}
