package com.brass-bullet.dbutils;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DatabaseMetaData;

public class PoolTest
{
	public static void main(String[] args)
	{
  	try
    {
			ConnectionPool pool = new ConnectionPool(1,false,false);
    	Connection conn = pool.getConnection();
    	System.out.println("Created pool");
    	(new BufferedReader(new InputStreamReader(System.in))).readLine();
      if (conn.isClosed())
      {
      	System.out.println("Connection flagged as closed");
      }
      DatabaseMetaData meta = conn.getMetaData();
      System.out.println("Got Meta Data");
    	Statement stmt = conn.createStatement();
    	System.out.println("Created Statement");
    	ResultSet results = stmt.executeQuery("");
    	System.out.println("Got results");
    }
    catch (Exception e)
    {
    	System.out.println("Exception - "+e.getMessage());
    }
	}
}
