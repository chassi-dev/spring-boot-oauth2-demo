
package com.saas.controller;

import java.security.Principal;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.saas.controller.Greeting;
import com.saas.security.KeycloakOAuthUser;


@RestController
public class ApiRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiRestController.class);

    @Autowired
    KeycloakOAuthUser oAuthUser;
    
    @Autowired
    private Environment env;
    
    private static final String template = "Hello, %s!";

    private final AtomicLong counter = new AtomicLong();

    //Various functions for getting auth token and refresh token from spring security objects
    private AccessToken getAccessToken() {
    	
    	KeycloakAuthenticationToken authToken = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    	KeycloakPrincipal kcPrincipal =  (KeycloakPrincipal) authToken.getPrincipal();
    	
        KeycloakSecurityContext ctxt = kcPrincipal.getKeycloakSecurityContext();
        return  (AccessToken) ctxt.getToken();
        
    }
    
   
   private String getAccessTokenString() {
    	
    	KeycloakAuthenticationToken authToken = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    	KeycloakPrincipal kcPrincipal =  (KeycloakPrincipal) authToken.getPrincipal();
    	
        KeycloakSecurityContext ctxt = kcPrincipal.getKeycloakSecurityContext();
        return  ctxt.getIdTokenString();
        
    }
 
   private String getRefreshTokenString() {
   	
   	KeycloakAuthenticationToken authToken = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
   	KeycloakPrincipal kcPrincipal =  (KeycloakPrincipal) authToken.getPrincipal();
   	
   	RefreshableKeycloakSecurityContext ctxt = (RefreshableKeycloakSecurityContext) kcPrincipal.getKeycloakSecurityContext();
       return ctxt.getRefreshToken();
       
   }

   
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
    	
        AccessToken token = getAccessToken();
        
        String usersName = token.getName();
        
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, usersName));
    }
    
    @RequestMapping("/greeting/help")
    public String help(HttpSession httpSession, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
   
    	return String.format("Usage: http://%s:%d/greeting[/profile]?<name=value>\n", httpRequest.getServerName(),  httpRequest.getServerPort());
    }
    
    //In case if you want to see Profile of user then you this 
    @RequestMapping(value = "/greeting/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public KeycloakOAuthUser user(Principal principal) {
        oAuthUser.setPrincipal(principal);

        return oAuthUser;
    }

}
