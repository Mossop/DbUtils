package com.brassbullet.dbutils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DbSession
{
	protected Connection connection;
	protected DbHandler handler;
	protected String username;
	protected String password;
	
	public DbSession(DbHandler handler, String username, String password) throws SQLException
	{
		this.handler=handler;
		this.username=username;
		this.password=password;
		connection=createJdbcConnection(username,password);
	}
	
	protected Connection createJdbcConnection(String user, String password) throws SQLException
	{
		System.err.println("jdbc:"+handler.getSpec().getType()+":"+handler.getSpec().getUrl());
		return DriverManager.getConnection("jdbc:"+handler.getSpec().getType()+":"+handler.getSpec().getUrl(),user,password);
	}

	public Connection getConnection()
	{
		return connection;
	}
	
	public void close() throws SQLException
	{
		connection.close();
	}
}
