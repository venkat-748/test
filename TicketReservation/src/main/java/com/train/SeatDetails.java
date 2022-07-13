package com.train;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SeatDetails extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int train_id = Integer.parseInt(request.getParameter("train_id"));
		String date = request.getParameter("date");
		String start = request.getParameter("start");
		String end = request.getParameter("end");

		try {
			connect(train_id, date, start, end, request, response);
		} catch (ClassNotFoundException | SQLException | IOException | InterruptedException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void connect(int train_id, String date, String start, String end, HttpServletRequest request,
			HttpServletResponse response)
			throws ClassNotFoundException, SQLException, IOException, InterruptedException, ParseException {
		JSONObject object = new JSONObject();

		Connection con = Landing.getCon();

		PreparedStatement stmt = con.prepareStatement(
				"select seat_id from seat where train_id=? and Date=?  and fromPlace=? and toPlace=? ");
		stmt.setInt(1, train_id);
		stmt.setString(2, date);
		stmt.setString(3, start);
		stmt.setString(4, end);
		System.out.println(stmt);
		ResultSet rs = stmt.executeQuery();

		ArrayList<Integer> seats = new ArrayList<>();
		PrintWriter outPrintWriter = response.getWriter();
		while (rs.next())
			seats.add(rs.getInt(1));
		int key = 1;
		for (int i : seats)
			object.put(key, i);

		outPrintWriter.write(object.toJSONString());
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