package com.saas.security;


import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import com.saas.security.CustomAccessDeniedHandler;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class ResourceSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    /**
     * Registers the KeycloakAuthenticationProvider with the authentication manager.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
    	
       	//By default, Spring Security REQUIRES roles defined in Keycloak to be prepended with ROLE_ 
    	//so using a SimpleAuthorityMapper will negate this confusing convention. 
    	 
    	keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Autowired
    public KeycloakClientRequestFactory keycloakClientRequestFactory;
       
    @Bean()
    @Scope("prototype")
    public KeycloakRestTemplate keycloakRestTemplate() {
    	return new KeycloakRestTemplate(keycloakClientRequestFactory); 
    }
        
    
    @Bean
    public org.keycloak.adapters.KeycloakConfigResolver KeycloakConfigResolver(){
       return new KeycloakSpringBootConfigResolver();
    } 
    
    /**
     * Defines the session authentication strategy.
     */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }
    
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        super.configure(http);
        
        http.csrf().disable();
        
        http
        	.authorizeRequests()
        	.antMatchers("/index.html", "/sso/login/**", "/logout", "/loggedout.html").permitAll()
        	.antMatchers("/greeting/help", "/services/**").permitAll()
        	.antMatchers("/greeting/profile*").hasRole("developer")  
        	.anyRequest().authenticated()
        	.and()
        	.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }
    
    
}
