package com.train;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.simple.JSONObject;
import com.google.gson.JsonObject;
import com.mysql.cj.x.protobuf.MysqlxCrud.Update;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/BookingTickets")
public class BookingTickets extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		SeatDetails tick = new SeatDetails();

		int train_id = Integer.parseInt(request.getParameter("train_id"));
		String tickets = request.getParameter("tickets");
		String date = request.getParameter("date");
		Long user_id = Long.parseLong(request.getParameter("user_id"));
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		String[] arr = tickets.split(",");
		int[] seats = new int[arr.length];

		for (int i = 0; i < arr.length; i++)
			seats[i] = Integer.parseInt(arr[i]);
		boolean status = false;
		try {
			if (DateCheck.dateCheck(date)) {
				status = true;
				System.out.println("success");
				booking(train_id, seats, date, user_id, start, end, request, response);
			} else {
				status = false;
				System.out.println("failed");
			}
		} catch (ClassNotFoundException | SQLException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void booking(int train_id, int[] arr, String date, Long user_id, String start, String end,
			HttpServletRequest request, HttpServletResponse response)
			throws ClassNotFoundException, SQLException, IOException, ParseException {

		String userName = "";
		String tickets = "";
		int no_of_tickets = 0;
		Connection con = Landing.getCon();
		PreparedStatement stmt;

		for (int i = 0; i < arr.length; i++) {

			stmt = con.prepareStatement("insert into seat values(?,?,?,?,?,?)");
			stmt.setInt(1, arr[i]);
			stmt.setInt(2, train_id);
			stmt.setString(3, date);
			stmt.setLong(4, user_id);
			stmt.setString(5, start);
			stmt.setString(6, end);
			System.out.println("insert data into ticketHistory:" + stmt);

			if (stmt.executeUpdate() == 1) {
				System.out.println("inserted completely");
			}
		}

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		System.out.println(dtf.format(now));
		Time time = Time.valueOf(dtf.format(now));

		stmt = con.prepareStatement(
				"insert into ticketHistory(train_id,user_id,time,date,booking_date) values(?,?,?,?,?)");
		stmt.setInt(1, train_id);
		stmt.setLong(2, user_id);
		stmt.setTime(3, time);
		stmt.setString(4, date);
		stmt.setString(5, SeatDetails.getToday());
		if (stmt.executeUpdate() == 1)
			System.out.println("inserted completely");
		System.out.println(stmt);
		stmt = con.prepareStatement("select no_of_tickets,username from users where user_id=?");
		stmt.setLong(1, user_id);
		ResultSet rs = stmt.executeQuery();
		System.out.println(stmt);
		while (rs.next()) {
			userName = rs.getString(2);
			no_of_tickets = rs.getInt(1);
		}
		stmt = con.prepareStatement("update users set no_of_tickets =? where user_id=?");
		stmt.setInt(1, no_of_tickets + arr.length);
		stmt.setLong(2, user_id);
		System.out.println(stmt);
		if (stmt.executeUpdate() == 1) {
			System.out.println("vetri");
		}

		for (int i = 0; i < arr.length; i++) {
			if (i == arr.length - 1)
				tickets += String.valueOf(arr[i]);
			else
				tickets += String.valueOf(arr[i]) + ",";
		}

		JsonObject details = new JsonObject();
		details.addProperty("username", userName);
		details.addProperty("train_id", train_id);
		details.addProperty("tickets", tickets);
		details.addProperty("start", start);
		details.addProperty("end", end);
		details.addProperty("date", date);

		JSONObject object = new JSONObject();
		object.put("ticketDetails", details);
		response.getWriter().write(object.toJSONString());
		System.out.println(object.toString());
		con.close();
	}
}
