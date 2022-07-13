package com.train;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class seatCheck
 */
@WebServlet("/SeatCheck")
public class SeatCheck extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("enter to the ticket");
		int train_id = Integer.parseInt(request.getParameter("train_id"));
		String date = request.getParameter("date");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		System.out.println(start + " " + end);

		try {
			connect(train_id, date, start, end, request, response);
		} catch (ClassNotFoundException | SQLException | IOException | InterruptedException | ParseException e) {
			e.printStackTrace();
		}

	}

	public static void connect(int train_id2, String date, String start, String end, HttpServletRequest request,
			HttpServletResponse response)
			throws ClassNotFoundException, SQLException, IOException, InterruptedException, ParseException {
		JSONObject seats = new JSONObject();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Train", "root", "");

		PreparedStatement stmt = con.prepareStatement(
				"select seat_id from transactionseat where train_id=? and Date=?  and fromPlace=? and toPlace=? ");
		stmt.setInt(1, train_id2);
		stmt.setString(2, date);
		stmt.setString(3, start);
		stmt.setString(4, end);
		System.out.println(stmt);
		ResultSet rs = stmt.executeQuery();
		PrintWriter outPrintWriter = response.getWriter();
		int k = 1;
		while (rs.next()) {
			System.out.println(rs.getInt(1));
			seats.put(String.valueOf(k), rs.getInt(1));
			k++;
		}

		outPrintWriter.write(seats.toJSONString());
		outPrintWriter.flush();
		outPrintWriter.close();

	}

	public static String getToday() {
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now2 = LocalDateTime.now();
		return String.valueOf(dtf2.format(now2));

	}
}
