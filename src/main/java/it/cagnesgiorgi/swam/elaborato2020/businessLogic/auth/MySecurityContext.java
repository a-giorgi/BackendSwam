package it.cagnesgiorgi.swam.elaborato2020.businessLogic.auth;


import it.cagnesgiorgi.swam.elaborato2020.DAO.UserDAO;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.User;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;


public class MySecurityContext implements SecurityContext {

    @Inject
    private UserDAO userDAO;
    private String principalUsername;
    private ContainerRequestContext containerRequestContext;

    public void setPrincipalUsername(String principalUsername) {
        this.principalUsername = principalUsername;
    }

    public void setContainerRequestContext(ContainerRequestContext containerRequestContext) {
        this.containerRequestContext = containerRequestContext;
    }

    public MySecurityContext() {

    }

    @Override
    public Principal getUserPrincipal() {
        return new Principal() {
            @Override
            public String getName() {
                return principalUsername;
            }
        };
    }

    @Override
    public boolean isUserInRole(String role) {
        if(principalUsername == null){
            return false;
        }
        User user = userDAO.getUserByEmail(principalUsername);
        if (user == null) {
            return false;
        }
        return user.hasRole(role);
    }

    @Override
    public boolean isSecure() {
        return containerRequestContext.getSecurityContext().isSecure();
    }

    @Override
    public String getAuthenticationScheme() {
        return "BEARER";
    }
}
