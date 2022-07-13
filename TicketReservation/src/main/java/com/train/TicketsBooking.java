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
import java.util.Arrays;
import org.json.simple.JSONObject;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/TicketsBooking")
public class TicketsBooking extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("ticketsbooking");
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
			e.printStackTrace();
		}
	}

	public void booking(int train_id, int[] arr, String date, Long user_id, String start, String end,
			HttpServletRequest request, HttpServletResponse response)
			throws ClassNotFoundException, SQLException, IOException, ParseException {
		String userName="";
		int no_of_tickets=0;
		Connection con = Landing.getCon();
		PreparedStatement stmt;
		stmt = con.prepareStatement("select * from train where train_id=? and date=?and fromPlace=? and toPlace=?");
		stmt.setInt(1, train_id);
		stmt.setString(2, date);
		stmt.setString(3, start);
		stmt.setString(4, end);
		ResultSet rs = stmt.executeQuery();
		System.out.println(stmt);
		int no_seats = 0;
		while (rs.next()) {
			Train train = new Train(rs.getInt(2));
			System.out.println(train.getSeat_avail());
			no_seats = train.getSeat_avail();
		} 
		int[] coach = new int[arr.length];
		for (int j = 0; j < arr.length; j++) {
			System.out.println(arr[j]);
			coach[j] = arr[j] <= (0.6 * no_seats) ? 1 : arr[j] <= (0.6 * no_seats) + (0.25 * no_seats) ? 2 : 3;

		}
		
		stmt = con.prepareStatement("select no_of_tickets,username from users where user_id=?");
		stmt.setLong(1, user_id);
		ResultSet rs1 = stmt.executeQuery();
		System.out.println(stmt);
		while (rs1.next()) {
			userName = rs1.getString(2);
			no_of_tickets = rs1.getInt(1);
		}
		stmt = con.prepareStatement("update users set no_of_tickets =? where user_id=?");
		stmt.setInt(1, no_of_tickets + arr.length);
		stmt.setLong(2, user_id);
		System.out.println(stmt);
		if (stmt.executeUpdate() == 1) {
			System.out.println("vetri");
		}
		String tickets = arrayToString(arr);
		String coaches = arrayToString(coach);
		System.out.println(coaches);

		JsonObject details = new JsonObject();
		details.addProperty("username", userName);
		details.addProperty("train_id", train_id);
		details.addProperty("tickets", tickets);
		details.addProperty("coaches", coaches);
		details.addProperty("start", start);
		details.addProperty("end", end);
		details.addProperty("date", date);
		JSONObject object = new JSONObject();
		object.put("ticketDetails", details);
		response.getWriter().write(object.toJSONString());
		System.out.println(object.toString());
		con.close();
	}

	private String arrayToString(int[] arr) {
		String tickets = "";
		for (int i = 0; i < arr.length; i++) {
			if (i == arr.length - 1)
				tickets += String.valueOf(arr[i]);
			else
				tickets += String.valueOf(arr[i]) + ",";
		}
		return tickets;

	}
}
