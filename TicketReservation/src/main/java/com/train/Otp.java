package com.train;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.catalina.valves.rewrite.Substitution.StaticElement;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Servlet implementation class Otp
 */
@WebServlet("/Otp")
public class Otp extends HttpServlet {
	static int i;
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		for (int i = 0; i < 3; i++) {
			i = Integer.parseInt(request.getParameter("otp"));
			boolean b = true;
			if (b = (CodeWord.getOtp() == i)) {
//		System.out.println(b);

				Insert obj1 = new Insert();
				Connection con = null;
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Train", "root", "");
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					System.out.println(con);
					obj1.loginuser(con, response, request);
					System.out.println("redirect");
					response.sendRedirect("TrainRegis.html");
				} catch (NoSuchAlgorithmException | SQLException e) {

				}
			} else {
				System.out.println("otp wrong");
				response.sendRedirect("otp.html");
			}
		}
	}
}
