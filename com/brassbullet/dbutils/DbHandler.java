package com.brassbullet.dbutils;

import com.brassbullet.dbutils.xml.Database;
import javax.xml.bind.UnmarshalException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

public abstract class DbHandler
{
	protected Database dbspec;
	private List sessions;
		
	public static DbHandler newInstance(URL location) throws UnmarshalException, IOException
	{
    return newInstance(Database.unmarshalDatabase(location));
	}
	
	public static DbHandler newInstance(String filename) throws UnmarshalException, FileNotFoundException
	{
    return newInstance(Database.unmarshalDatabase(filename));
	}
	
	public static DbHandler newInstance(File file) throws UnmarshalException, FileNotFoundException
	{
    return newInstance(Database.unmarshalDatabase(file));
	}
	
  public static DbHandler newInstance(InputStream in) throws UnmarshalException
  {
    return newInstance(Database.unmarshalDatabase(in));
  }

	public static DbHandler newInstance(Database spec) throws IllegalArgumentException
	{
		try
		{
			Class handler = Class.forName(DbHandler.class.getPackage().getName()+"."+spec.getType()+".Handler");
			DbHandler newdb = (DbHandler)handler.newInstance();
			newdb.initialise(spec);
			return newdb;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException("Database type not supported or specification error.");
		}
	}

	protected DbSession createCustomSession(String user, String password) throws SQLException
	{
		return new DbSession(this,user,password);
	}
	

	protected void initialise(Database spec)
	{
		dbspec=spec;
		sessions = new LinkedList();
	}
	
	public Database getSpec()
	{
		return dbspec;
	}
	
	public abstract void initialiseDatabase(String user, String password) throws SQLException;
	
	public DbSession createSession(String user, String password) throws SQLException
	{
		DbSession session = createCustomSession(user,password);
		sessions.add(session);
		return session;
	}
	
	public void releaseSession(DbSession session)
	{
		try
		{
			sessions.remove(session);
			session.close();
		}
		catch (SQLException e)
		{
			System.err.println("Error closing session - "+e.getMessage());
		}
	}
}
