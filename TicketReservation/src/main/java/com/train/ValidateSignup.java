package com.train;

import java.io.IOException;
import java.util.regex.Pattern;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;

public class ValidateSignup extends HttpFilter {
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println("entered");
		 boolean checkMail= Pattern.matches("[a-zA-z0-9]+[@][a-z]+[.][a-z]{2,3}", request.getParameter("email"));
		 boolean checkName = Pattern.matches("[a-zA-z_]*", request.getParameter("name"));
		 boolean checkNumber = Pattern.matches("[0-9]{10}",request.getParameter("phone"));
		 boolean checkPass = Pattern.matches("^(?=.*[0-9])"+ "(?=.*[a-z])(?=.*[A-Z])"+ "(?=.*[@#$%^&+=])"+ "(?=\\S+$).{8,20}$",request.getParameter("pass"));
		 if(checkMail&&checkName&&checkNumber&&checkPass) {
			 System.out.println("valid");		
			 chain.doFilter(request, response);
		 }
	}
}
