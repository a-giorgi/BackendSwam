package it.cagnesgiorgi.swam.elaborato2020.businessLogic.auth;

import it.cagnesgiorgi.swam.elaborato2020.Configurations;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityRequestFilter implements ContainerRequestFilter {
    @Inject
    private MySecurityContext securityContext;

    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        //creating security context

        securityContext.setContainerRequestContext(containerRequestContext);

        //firstly I check the origin
        if(!Configurations.frontendDomain.equals("*")) {
            String originHeader = containerRequestContext.getHeaderString(("Origin"));
            if (originHeader == null) {
                containerRequestContext.abortWith(
                        Response
                                .status(Response.Status.UNAUTHORIZED)
                                .build()
                );
            } else if (!originHeader.equals(Configurations.frontendDomain)) {
                containerRequestContext.abortWith(
                        Response
                                .status(Response.Status.UNAUTHORIZED)
                                .build()
                );
            }
        }

        //retrieving header
        String authorizationHeader = containerRequestContext.getHeaderString((HttpHeaders.AUTHORIZATION));

        //if no auth header is set
        if(authorizationHeader == null || authorizationHeader.isEmpty()){
            //no principal is set, so isUserInRole will always return false
            securityContext.setPrincipalUsername(null);

            /*containerRequestContext.abortWith(
                    Response
                         .status(Response.Status.UNAUTHORIZED)
                         .build()
            );*/

        }else if(authorizationHeader.startsWith("Bearer")|| authorizationHeader.startsWith("BEARER")){
            String token = authorizationHeader.substring("BEARER".length()).trim();
            securityContext.setPrincipalUsername(TokenFactory.getUserMailFrom(token));
        }


        //now I can install the securityContext
        containerRequestContext.setSecurityContext(securityContext);
    }
}
