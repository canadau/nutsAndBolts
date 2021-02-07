package com.nutsandbolts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.nutsandbolts.tools.DBConnection;

public class HomePage {

	public static String dateTime;
	public static String connSt;
	public static final String createSQL = "UPDATE login SET time = ? WHERE id = 1";
	//String createSQL = "INSERT INTO login (time) VALUES (?) WHERE id = 1";

	{		
		try {			

			Connection conn = DBConnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(createSQL);

			/*
			 * LocalDateTime localDateTime = LocalDateTime.now(); DateTimeFormatter
			 * dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
			 * String myDate = localDateTime.format(dateTimeFormatter);
			 */
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
			Date date = new Date(System.currentTimeMillis());

			pstmt.setString(1, formatter.format(date));			

			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public String dateFromDB() {
		String date = null;
		try {
			/*
			 * DBConnection inst = DBConnection.getInstance(); Connection conn =
			 * inst.getConnection();
			 */
			String createSQL = "SELECT time from login where id = 1 ";
			Connection conn = DBConnection.getConnection();

			Statement statement = conn.createStatement();			

			ResultSet rSet = statement.executeQuery(createSQL);
			while (rSet.next()) {
				date = rSet.getString("time");				
			}			

		} catch (Exception e) {
			e.getMessage();
		}
		return date;
	}
	

}