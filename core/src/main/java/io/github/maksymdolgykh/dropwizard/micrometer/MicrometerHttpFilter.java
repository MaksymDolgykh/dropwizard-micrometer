package io.github.maksymdolgykh.dropwizard.micrometer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MicrometerHttpFilter implements Filter {


    protected FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // start the timer add let request to pass the filter
        long startTime = System.nanoTime();
        chain.doFilter(request, response);

        // record time after response
        double elapsed = (System.nanoTime() - startTime) / 1E9;
        String requestPath = ((HttpServletRequest) request).getPathInfo();

        String responseStatus = String.valueOf(((HttpServletResponse) response).getStatus());
        String requestMethod = ((HttpServletRequest) request).getMethod();
        MicrometerBundle.httpRequests.labels(requestMethod, responseStatus, requestPath).observe(elapsed);
    }

    @Override
    public void destroy() {
    }
}
