package com.nutsandbolts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.NamingException;

import com.nutsandbolts.tools.DBConnection;

public class HomePage {

	public static String dateTime;
	public static String connSt;
	public static final String createSQL = "UPDATE login SET time = ? WHERE id = 1";
	//String createSQL = "INSERT INTO login (time) VALUES (?) WHERE id = 1";

	{		
		try {			
			System.out.println("Save the time");
			DBConnection inst = DBConnection.getInstance();
			Connection conn = inst.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(createSQL);

			/*
			 * LocalDateTime localDateTime = LocalDateTime.now(); DateTimeFormatter
			 * dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
			 * String myDate = localDateTime.format(dateTimeFormatter);
			 */
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' hh:mm:ss z");
			Date date = new Date(System.currentTimeMillis());

			pstmt.setString(1, formatter.format(date));			

			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public String dateFromDB() {
		String date = null;
		try {
			System.out.println("Get the time");
			/*
			 * DBConnection inst = DBConnection.getInstance(); Connection conn =
			 * inst.getConnection();
			 */
			String createSQL = "SELECT time from login where id = 1 ";
			DBConnection inst = DBConnection.getInstance();
			Connection conn = inst.getConnection();

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