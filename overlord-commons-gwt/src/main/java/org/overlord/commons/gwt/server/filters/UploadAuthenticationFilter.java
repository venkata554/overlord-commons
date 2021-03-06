/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.overlord.commons.gwt.server.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * {@link Filter} that is used to protect an upload servlet.
 */
public class UploadAuthenticationFilter implements Filter {

    /**
     * C'tor
     */
    public UploadAuthenticationFilter() {
    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpreq = (HttpServletRequest) request;
        if (httpreq.getRemoteUser() == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("{");
            builder.append(" \"exception\" : \"true\",");
            builder.append(" \"exception-message\" : \"Error 401: Not authenticated.\",");
            builder.append(" \"exception-stack\" : \"\"");
            builder.append("}");
            byte [] jsonData = builder.toString().getBytes("UTF-8"); //$NON-NLS-1$
            response.setContentType("text/html; charset=UTF8"); //$NON-NLS-1$
            response.setContentLength(jsonData.length);
            response.getOutputStream().write(jsonData);
            response.getOutputStream().flush();
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
    }

}
