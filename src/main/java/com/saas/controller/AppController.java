package com.saas.controller;

import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


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
