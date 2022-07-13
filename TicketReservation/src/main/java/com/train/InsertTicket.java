package com.train;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

public class InsertTicket extends HttpServlet {

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
				insert(train_id, seats, date, user_id, start, end, request, response);
			} else {
				status = false;
				System.out.println("failed");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void insert(int train_id, int[] arr, String date, Long user_id, String start, String end,

			HttpServletRequest request, HttpServletResponse response)
			throws ClassNotFoundException, SQLException, IOException {
		int k = 0;
		JSONObject object = new JSONObject();
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
		System.out.println(no_seats + "no");
		int[] coach = new int[arr.length];
		for (int j = 0; j < arr.length; j++) {
			System.out.println(arr[j]);
			coach[j] = arr[j] <= (0.6 * no_seats) ? 1 : arr[j] <= (0.6 * no_seats) + (0.25 * no_seats) ? 2 : 3;

		}
		con.setAutoCommit(false);
		stmt.execute("start transaction");
		stmt.execute("set session  transaction isolation level read committed;");
		stmt = con.prepareStatement("select @@transaction_isolation level");
		ResultSet rs2 = stmt.executeQuery();
		while (rs2.next()) {
			System.out.println(rs2.getString(1));
		}
		for (int i = 0; i < arr.length; i++) {
			stmt = con.prepareStatement(
					"insert into transactionseat(seat_id,train_id,date,user_id,coach,fromPlace,toPlace) values(?,?,?,?,?,?,?)");
			stmt.setInt(1, arr[i]);
			stmt.setInt(2, train_id);
			stmt.setString(3, date);
			stmt.setLong(4, user_id);
			stmt.setInt(5, coach[i]);
			stmt.setString(6, start);
			stmt.setString(7, end);
			System.out.println("insert data into transactionseat:" + stmt);

			if (stmt.executeUpdate() == 1) {
				System.out.println("inserted completely");
			}
			String sql = "select * from transactionseat where seat_id=? and train_id=? and date=? and fromPlace=? and toPlace=?";
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, arr[i]);
			stmt.setInt(2, train_id);
			stmt.setString(3, date);
			stmt.setString(4, start);
			stmt.setString(5, end);
			ResultSet rs1 = stmt.executeQuery();
			System.out.println(stmt);
			k = 0;
			while (rs1.next()) {
				System.out.println(rs1.getInt(1) + " " + rs1.getInt(2) + rs1.getInt(3) + " " + rs1.getString(4)
						+ rs1.getLong(5) + " " + rs1.getString(6) + rs1.getString(7) + " " + rs1.getString(8));
				k++;
			}
		}
		if (k == 1) {
			System.out.println("commit");
			con.commit();
			if (updatingTable(train_id, arr, date, user_id, coach, start, end, request, response))
				object.put("soln", true);
			System.out.println(object.toJSONString());
			response.getWriter().write(object.toJSONString());
			con.close();
		} else {
			System.out.println("rollback");
			con.rollback();
			object.put("soln", false);
			System.out.println(object.toJSONString());
			response.getWriter().write(object.toJSONString());
			con.close();

		}

	}

	private boolean updatingTable(int train_id, int[] arr, String date, Long user_id, int[] coach, String start,
			String end, HttpServletRequest request, HttpServletResponse response)
			throws ClassNotFoundException, SQLException {
		String userName = "";
		int no_of_tickets = 0;
		Connection con = Landing.getCon();
		PreparedStatement stmt;
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
		return true;
	}

}
