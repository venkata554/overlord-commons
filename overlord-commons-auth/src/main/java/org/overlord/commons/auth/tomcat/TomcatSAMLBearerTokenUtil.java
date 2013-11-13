/*
 * Copyright 2013 JBoss Inc
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
package org.overlord.commons.auth.tomcat;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.realm.GenericPrincipal;
import org.overlord.commons.auth.util.SAMLBearerTokenUtil;

/**
 * Class used to create SAML bearer tokens used when calling REST service
 * endpoints protected by SAML.
 *
 * @author eric.wittmann@redhat.com
 */
public class TomcatSAMLBearerTokenUtil {

    /**
     * Creates a SAML Assertion that can be used as a bearer token when invoking REST
     * services.  The REST service must be configured to accept SAML Assertion bearer
     * tokens (in Tomcat this means protecting the REST services with {@link SAMLBearerTokenAuthenticator}).
     * @param request the inbound servlet request
     * @param issuerName the issuer name (typically the context of the calling web app)
     * @param forService the web context of the REST service being invoked
     */
    public static String createSAMLAssertion(HttpServletRequest request, String issuerName, String forService) {
        try {
            Principal principal = request.getUserPrincipal();
            if (principal instanceof GenericPrincipal) {
                GenericPrincipal gp = (GenericPrincipal) principal;
                String[] gpRoles = gp.getRoles();
                Set<String> roles = new HashSet<String>(gpRoles.length);
                for (String role : gpRoles) {
                    roles.add(role);
                }
                return SAMLBearerTokenUtil.createSAMLAssertion(principal, roles, issuerName, forService);
            }
            throw new Exception("Unexpected/unsupported principal type: " + principal.getClass());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
