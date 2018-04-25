package com.cooksys.twoweek;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DBHandler
{
	public static void saveHistory(String serverIP, String developerName, int port) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/file_sharer", "root", "bondstone");
		
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO client_connections "
                + "VALUES (null, ?, ?, ?)");
                
		stmt.setString(1, serverIP);
		stmt.setString(2, developerName);
		stmt.setInt(3, port);
		
		stmt.executeUpdate();
		
		conn.close();
		stmt.close();
		
	}
	
	public static ArrayList<String> getHistory() throws Exception
	{
		ArrayList<String> values = new ArrayList<String>();
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/file_sharer", "root", "bondstone");
		
		PreparedStatement stmt = conn.prepareStatement("SELECT * "
                + "FROM client_connections");
		
		ResultSet results = stmt.executeQuery();
		
		while(results.next())
		{
			values.add(results.getString(2));
		}
		
		results.close();
		conn.close();
		stmt.close();
		return values;
	}
}
