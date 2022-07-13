package com.train;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * Servlet implementation class Signin
 */
public class Signin extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Landing landing =new Landing();
		String email =request.getParameter("email");
		String password = request.getParameter("pass");
		
		try {
			boolean condition=Landing.signinCheck(email,password);
			System.out.println(condition);
			if(condition) {
			      response.sendRedirect("TrainRegis.html");
						}
			else {
				response.sendRedirect("Signin.html");
			}
			
		} catch (NoSuchAlgorithmException | SQLException e) {
		     e.printStackTrace();
		} catch (ClassNotFoundException e) {
			  e.printStackTrace();
		}
	}

}
