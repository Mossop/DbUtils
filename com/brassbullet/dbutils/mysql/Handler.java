package com.brassbullet.dbutils.mysql;

import com.brassbullet.dbutils.DbHandler;
import com.brassbullet.dbutils.DbSession;
import com.brassbullet.dbutils.xml.Database;
import com.brassbullet.dbutils.xml.Table;
import com.brassbullet.dbutils.xml.Field;
import java.sql.Connection;
import java.sql.SQLException;

public class Handler extends DbHandler
{
	public Handler()
	{
		try
		{
			Class.forName("org.gjt.mm.mysql.Driver");
		}
		catch (ClassNotFoundException e)
		{
			System.err.println("Could not load MySql driver");
		}
	}
	
	protected DbSession createCustomSession(String user, String password) throws SQLException
	{
		return new Session(this,user,password);
	}

	public void initialiseDatabase(String username, String password) throws SQLException
	{
		DbSession session = createSession(username, password);
		Connection con = session.getConnection();
    for (int tables=0; tables<dbspec.getTables().size(); tables++)
    {
      Table table = (Table)dbspec.getTables().get(tables);
      System.out.println("Creating "+table.getName()+" table");
      con.createStatement().executeUpdate("DROP TABLE IF EXISTS "+table.getName()+";");
      String buffer = "CREATE TABLE "+table.getName()+" (";
      for (int fields=0; fields<table.getPrimaryKey().getFields().size(); fields++)
      {
        Field field = (Field)table.getPrimaryKey().getFields().get(fields);
        buffer=buffer+field.getName()+" "+field.getType()+" NOT NULL,";
      }
      for (int fields=0; fields<table.getFields().size(); fields++)
      {
        Field field = (Field)table.getFields().get(fields);
        buffer=buffer+field.getName()+" "+field.getType();
        if (!field.getAllowNull())
        {
          buffer=buffer+" NOT NULL";
        }
        buffer=buffer+",";
      }
      buffer=buffer+"PRIMARY KEY (";
      for (int fields=0; fields<table.getPrimaryKey().getFields().size(); fields++)
      {
        Field field = (Field)table.getPrimaryKey().getFields().get(fields);
        if (fields>0)
        {
          buffer=buffer+",";
        }
        buffer=buffer+field.getName();
      }
      buffer=buffer+"));";
      System.out.println(buffer);
      con.createStatement().executeUpdate(buffer);
    }
    releaseSession(session);
	}
}
