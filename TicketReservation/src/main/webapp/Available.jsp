
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.io.PrintWriter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    %>
    <%@page import="java.sql.PreparedStatement"%>
    <%@page  import ="java.sql.Connection"%>
 <%@page  import ="java.sql.DriverManager"%>
 <%@page  import =" java.sql.PreparedStatement"%>
 <%@page  import =" java.sql.ResultSet"%>
 <%@page  import ="java.sql.SQLException" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/Train","root","");
		PreparedStatement stmt = con.prepareStatement("select * from train order by date");
  ResultSet rs = stmt.executeQuery();
  PrintWriter outPrintWriter = response.getWriter();

  outPrintWriter.print(
			"<h1 style ='text-align:center'>Train  Information</h1><br><table border=\"1px\" style='text-align: center;margin:auto;border-collapse: collapse'>");
 outPrintWriter.print(
			"<tr><td>train_id</td><td>seat_avail </td><td>Date</td><td>Travel</td><td>Start_time</td><td>Arrive</td><td>Price</td><td>FromPlace</td><td>ToPlace</td></tr>");
	while (rs.next()) {
		outPrintWriter.print("<tr><td>" + rs.getInt(1) + "</td><td>" + rs.getInt(2) + "</td><td> " + rs.getInt(3)
				+ "</td><td> " + rs.getDate(4) + "</td><td> " + rs.getTime(5) + "</td><td> " + rs.getTime(6)
				+ "</td><td> " + rs.getTime(7) + "</td><td> " + rs.getString(8) + "</td><td> "+ rs.getString(9)+ "</td></tr>");

	}
	outPrintWriter.print("</table>");
	outPrintWriter.println("<a style ='margin-left:50vw;font-size:20px'href='TrainRegis.html'>Book</a>");
			
		
		
		%>
</body>
</html>