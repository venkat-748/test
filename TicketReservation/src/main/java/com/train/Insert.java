package com.train;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Insert {
	boolean loginuser(Connection con, HttpServletResponse response, HttpServletRequest request)
			throws SQLException, NoSuchAlgorithmException, IOException {
		PreparedStatement stmt = con
				.prepareStatement("insert into users(user_id,Phone,username,email,password) values(?,?,?,?,?)");
		stmt.setLong(1, Landing.userid);
		stmt.setString(3, Landing.uname);
		stmt.setString(4, Landing.email);
		System.out.println("Insert" + Landing.userid);
		System.out.println("Insert" + Landing.uname);
		System.out.println("Insert" + Landing.phone);
		stmt.setLong(2, Long.parseLong(Landing.phone));
		stmt.setString(5, Landing.hashPassword(Landing.password));
		System.out.println(stmt);
		if (stmt.executeUpdate() == 1) {
			Cookie ck = new Cookie("user_id", (String.valueOf(Landing.userid)));
			ck.setMaxAge(24 * 60 * 60 * 7);
			response.addCookie(ck);
			return true;
		} else {
			System.out.println("bad");
			return false;

		}
	}
}
