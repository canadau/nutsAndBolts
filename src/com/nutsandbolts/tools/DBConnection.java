package com.nutsandbolts.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DBConnection {

	//Database credentials
	static final String USER = "purpleteam";
	static final String PASS = "Capstone2021";
	static final String PORT = "1433";
	static final String HOST = "nutsandbolts-1.cxvligjgnozh.us-east-1.rds.amazonaws.com";
	static final String DATABASE = "nutsandbolts";

	// Build connection URL
	static final String connectionURL = "jdbc:sqlserver://" + HOST + ":" + PORT +
			";databaseName=" + DATABASE + ";user=" + USER + ";password=" + PASS + ";";

	private static Connection conn = null;

	public static Connection getConnection() { 

		try {

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(connectionURL);

		} catch (SQLException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return conn;

	}

	public static void close(Connection con) {
		try {
			con.close();
		} catch (Exception ex) {

		}
	}

	public static void closePreparedStmt(PreparedStatement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
			/* ignore */ }
		}
	}

}