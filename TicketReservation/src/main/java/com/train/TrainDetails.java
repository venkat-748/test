package com.train;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.simple.JSONObject;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TrainDetails {
	public static int seat_avail=0;
	public String date = "";
	public String start = "";
	public String end = "";
	HttpServletResponse response;
	HttpServletRequest request;

	public TrainDetails(String start, String end, String date, HttpServletRequest request, HttpServletResponse response)
			throws ClassNotFoundException, SQLException, IOException, InterruptedException, ParseException {
		this.date = date;
		this.start = start;
		this.end = end;
		this.request = request;
		this.response = response;

		connect(date, start, end, request, response);
	}

	public static void connect(String date, String start, String end, HttpServletRequest request,
			HttpServletResponse response)
			throws ClassNotFoundException, SQLException, IOException, InterruptedException, ParseException {

		Time time;
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Train", "root", "");

		PreparedStatement stmt = con.prepareStatement("select * from train where fromPlace=? and toPlace=? and Date=?");
		stmt.setString(1, start);
		stmt.setString(2, end);
		stmt.setString(3, date);

		JSONObject total = new JSONObject();
		System.out.println(stmt);
		ResultSet rs = stmt.executeQuery();

		PrintWriter outPrintWriter = response.getWriter();
		Integer i = 1;
		while (rs.next()) {
			time = rs.getTime(6);
			if (!DateCheck.timeCheck(time)) {
				continue;
			}
seat_avail = rs.getInt(2);
System.out.println(seat_avail);
			String train_id = String.valueOf(rs.getInt(1));
			String seat = String.valueOf(rs.getInt(2));
			String price = String.valueOf(rs.getInt(3));
			String Traveldate = String.valueOf(rs.getDate(4));
			String travel = String.valueOf(rs.getTime(5));
			String arriveTime = String.valueOf(rs.getTime(7));
			String from = String.valueOf(rs.getString(8));
			String to = String.valueOf(rs.getString(9));
			JsonObject obj = new JsonObject();

			String time2 = String.valueOf(time);
			obj.addProperty("train_id", train_id);
			obj.addProperty("seat_avail", seat);
			obj.addProperty("price", price);
			obj.addProperty("Date", Traveldate);
			obj.addProperty("Travel", travel);
			obj.addProperty("Start_time", time2);
			obj.addProperty("Arrive", arriveTime);
			obj.addProperty("fromPlace", from);
			obj.addProperty("toPlace", to);
			total.put("train" + i, obj);
			i++;

		}

		outPrintWriter.write(total.toJSONString());
		outPrintWriter.flush();
		outPrintWriter.close();
		con.close();
	}

	public static String getToday() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		return String.valueOf(dtf.format(now));

	}
}
