package com.brass-bullet.dbutils;

import com.sun.xml.parser.Resolver;
import org.xml.sax.InputSource;
import java.io.IOException;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import com.brass-bullet.dbutils.xml.Database;
import com.brass-bullet.dbutils.xml.Table;
import com.brass-bullet.dbutils.xml.Field;
import com.brass-bullet.dbutils.xml.Reference;
import com.brass-bullet.dbutils.xml.DatabaseParser;

public class DbValidator
{
  private Database database;
  
  public DbValidator()
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
  
  public boolean validate()
  {
    try
    {
    	ConnectionPool pool = new ConnectionPool();
      Connection dbcon = pool.getConnection();
      boolean valid = true;
      for (int tables=0; tables<database.getTableCount(); tables++)
      {
        Table table = database.getTable(tables);
        for (int fields=0; fields<table.getKeyFieldCount(); fields++)
        {
          Field field = table.getKeyField(fields);
          for (int refs=0; refs<field.getReferencesCount(); refs++)
          {
            Reference ref = field.getReferences(refs);
            if (ref.getMustExist())
            {
              Field dest = ref.getDestination();
              ResultSet sources = dbcon.createStatement().executeQuery("SELECT "+field.getName()+" FROM "+table.getName()+";");
              while (sources.next())
              {
                ResultSet check = dbcon.createStatement().executeQuery("SELECT "+dest.getName()+" FROM "+dest.getParent().getName()+" WHERE "+dest.getName()+"='"+sources.getString(1)+"';");
                if (!check.next())
                {
                  valid=false;
                  System.err.println("Error - "+table.getName()+"."+field.getName()+" value "+sources.getString(1)+" does not match any value in "+dest.getParent().getName()+"."+dest.getName());
                }
              }
            }
          }
        }
        for (int fields=0; fields<table.getFieldCount(); fields++)
        {
          Field field = table.getField(fields);
        }
      }
      pool.releaseConnection(dbcon);
      return valid;
    }
    catch (SQLException e)
    {
      System.err.println("Database access error - "+e.getMessage());
      return false;
    }
  }

  public static void main(String args[])
  {
    if (args.length != 1)
    {
      System.err.println("You must specify the xml file to be validated");
    }
    else
    {
      try
      {
        InputSource input = Resolver.createInputSource(new File(args[0]));
        DbValidator validator = new DbValidator();
        if (validator.doParse(input))
        {
          if (validator.validate())
          {
            System.out.println("Database validated successfully");
          }
          else
          {
            System.out.println("Database is invalid");
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
