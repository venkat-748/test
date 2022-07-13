package com.train;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

class CodeWord {
	private static int otp;

	public static int getOtp() {
		return otp;
	}

	public static void setOtp(int otp) {
		CodeWord.otp = otp;
	}
}

public class Landing {
	public static int otp;
	public static String email;
	public static String password;
	public static String uname;
	public static String phone;
	public static long userid;
	public static Connection con;

	public Landing() {
	}

	public Landing(String email, String password, String uname, String phone)
			throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
		this.email = email;
		this.password = password;
		this.uname = uname;
		this.phone = phone;
		System.out.println("Landing" + phone);
		Class.forName("com.mysql.cj.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Train", "root", "");
		this.userid = userId();
		CodeWord.setOtp(otp());
		this.otp = CodeWord.getOtp();
		System.out.println(this.otp);
		System.out.println("sended sucessfully");
	}

	public static String hashPassword(String password) throws NoSuchAlgorithmException {
		String salt = "qwertyuiopoigfdsacvbnm,237890!@#$%^&*QWERTYUIKJHGFDSCVHJITRESXCVBL:";
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] messageDigest = md.digest((password + salt).getBytes());
		StringBuilder builder = new StringBuilder(new BigInteger(1, messageDigest).toString(16));
		return builder.reverse().toString();
	}

	public long userId() {
		Long min = (long) 111111111;
		Long max = (long) 999999999;
		Long user_id = (long) (Math.random() * (max - min + 1) + min);
		System.out.println(user_id);
		return user_id;
	}

	public static Connection getCon() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/Train", "root", "");
	}

	public int otp() {
		Random random = new Random();
		return random.nextInt(1000) + 9999;
	}

	public static boolean signinCheck(String email, String password)
			throws SQLException, NoSuchAlgorithmException, ClassNotFoundException {
		if (con == null) {
			con = getCon();
		}
		PreparedStatement stmt = con.prepareStatement("select username from users where email=? and password=?");
		stmt.setString(1, email);
		stmt.setString(2, hashPassword(password));
		System.out.println(stmt);
		ResultSet rs = stmt.executeQuery();
		System.out.println(stmt);
		return rs.next();
	}



}
