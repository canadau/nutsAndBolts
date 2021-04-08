package com.nutsandbolts.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.NamingException;


public class DBConnection {

	//Database credentials
	static final String USER = "purpleteam";
	static final String PASS = "Capstone2021";
	static final String PORT = "1433";
	static final String HOST = "database-1.cnzqwwqzkfbd.us-east-1.rds.amazonaws.com";
	static final String DATABASE = "nutsandbolts";
	private static DBConnection instance = null;
	// Build connection URL
	static final String connectionURL = "jdbc:sqlserver://" + HOST + ":" + PORT +
			";databaseName=" + DATABASE + ";user=" + USER + ";password=" + PASS + ";";

	private static Connection conn = null;
	
	public static DBConnection getInstance() throws NamingException {
		if (instance == null) {
			instance = new DBConnection();
		}
		return instance;
	}
	
	public Connection getConnection() { 

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