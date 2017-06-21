package com.saas.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AppController {

    @Value("${security.loggedOutUri}")
    private String loggedOutUri;
    
	@GetMapping(value="/logout")
	public String logoutPage (HttpServletRequest request) throws ServletException {

		request.logout();
			
		return loggedOutUri;
	}
}
