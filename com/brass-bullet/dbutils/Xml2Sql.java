package com.brass-bullet.dbutils;

import com.sun.xml.parser.Resolver;
import org.xml.sax.InputSource;
import java.io.IOException;
import java.io.File;
import com.brass-bullet.dbutils.xml.Database;
import com.brass-bullet.dbutils.xml.Table;
import com.brass-bullet.dbutils.xml.Field;
import com.brass-bullet.dbutils.xml.DatabaseParser;
import java.sql.Connection;
import java.sql.SQLException;

public class Xml2Sql
{
  private Database database;
  
  public Xml2Sql()
  {
    super();
  }
  
  public boolean doParse(InputSource input)
  {
    DatabaseParser parser = new DatabaseParser();
    try
    {
      database=parser.parse(input);
      return (!(database==null));
    }
    catch (Exception e)
    {
      System.err.println("Exception during parse - "+e.getMessage());
      return false;
    }
  }
  
  public void display() throws SQLException
  {
  	ConnectionPool pool = new ConnectionPool();
    Connection con = pool.getConnection();
		if (con!=null)
    {
    	for (int tables=0; tables<database.getTableCount(); tables++)
    	{
    	  Table table = database.getTable(tables);
        System.out.println("Creating "+table.getName()+" table");
    	  con.createStatement().executeUpdate("DROP TABLE IF EXISTS "+table.getName()+";");
    	  String buffer = "CREATE TABLE "+table.getName()+" (";
    	  for (int fields=0; fields<table.getKeyFieldCount(); fields++)
    	  {
    	    Field field = table.getKeyField(fields);
    	    buffer=buffer+field.getName()+" "+field.getType()+" NOT NULL,";
    	  }
    	  for (int fields=0; fields<table.getFieldCount(); fields++)
    	  {
    	    Field field = table.getField(fields);
    	    buffer=buffer+field.getName()+" "+field.getType();
    	    if (!field.getNull())
    	    {
    	      buffer=buffer+" NOT NULL";
    	    }
    	    buffer=buffer+",";
    	  }
    	  buffer=buffer+"PRIMARY KEY (";
    	  for (int fields=0; fields<table.getKeyFieldCount(); fields++)
    	  {
    	    Field field = table.getKeyField(fields);
    	    if (fields>0)
    	    {
    	      buffer=buffer+",";
    	    }
    	    buffer=buffer+field.getName();
    	  }
    	  buffer=buffer+"));";
    	  con.createStatement().executeUpdate(buffer);
    	}
    	pool.releaseConnection(con);
    }
    else
    {
    	System.out.println("Could not get a database connection");
    }
  }
  
  public static void main(String args[])
  {
    if (args.length != 1)
    {
      System.err.println("You must specify the xml file to be converted");
    }
    else
    {
      try
      {
        InputSource input = Resolver.createInputSource(new File(args[0]));
        Xml2Sql convertor = new Xml2Sql();
        if (convertor.doParse(input))
        {
          try
          {
            convertor.display();
          }
          catch (Exception e)
          {
            System.out.println("Error - "+e.getMessage());
          }
        }
        else
        {
          System.err.println("Parse unsuccessfull");
        }
      }
      catch (Exception e)
      {
      }
    }
  }
}
