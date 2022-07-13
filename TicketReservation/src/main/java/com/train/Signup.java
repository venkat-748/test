package com.train;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/Signup")
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HttpServletResponse response;
	HttpServletRequest request;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("signup");
		this.response = response;
		this.request = request;
		String email = request.getParameter("email");
		String password = request.getParameter("pass");
		String uname = request.getParameter("name");
		String phone = request.getParameter("phone");
		System.out.println(phone);

		try {
			if (checking(email, password, uname, phone, response)) {
				System.out.println("checking");
				request.getRequestDispatcher("otp.html").forward(request, response);
			}
		} catch (ClassNotFoundException | NoSuchAlgorithmException | SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean checking(String email, String password, String uname, String dob, HttpServletResponse response)
			throws ClassNotFoundException, SQLException, NoSuchAlgorithmException, IOException, ServletException {
		PreparedStatement stmt;

		Connection con = Landing.getCon();
		stmt = con.prepareStatement("select email from users");
		System.out.println(stmt);
		ResultSet rs = stmt.executeQuery();
		boolean duplicate = false;

		while (rs.next()) {
			if (rs.getString(1).equals(email)) {
				System.out.println(rs.getString(1));
				duplicate = true;
				break;
			} else {
				System.out.println(rs.getString(1));
				System.out.println("sucesss........");
			}
		}
		if (!duplicate) {
			Landing landing = new Landing(email, password, uname, dob);
			System.out.println("no duplicate");
			return true;
		} else {
			PrintWriter out = response.getWriter();
			System.out.println("not correct");
			out.println("already an existing user");
			response.sendRedirect("Signin.html");
		}
		con.close();
		return false;
	}
}