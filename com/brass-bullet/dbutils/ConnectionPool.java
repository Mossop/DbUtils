package com.brass-bullet.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

public class ConnectionPool
{
	private boolean allowextra;
  private boolean wait;
  private int connections;
  private int inuse;
  private Vector spare;
  private boolean loaded;

	public ConnectionPool(int Connections, boolean Allowextra, boolean Wait)
  {
    try
    {
      Class.forName("org.gjt.mm.mysql.Driver");
      loaded=true;
  		connections=Connections;
    	allowextra=Allowextra;
    	wait=Wait;
      inuse=0;
    	spare = new Vector(10,2);
      Connection conn;
      for (int loop=0; loop<connections; loop++)
      {
      	conn=createConnection();
        if (conn!=null)
        {
      		spare.addElement(conn);
        }
      }
    }
    catch (ClassNotFoundException e)
    {
      loaded=false;
      connections=0;
      inuse=0;
      allowextra=false;
      wait=false;
      spare = new Vector();
    }
  }

  public ConnectionPool()
  {
  	this(10,true,false);
  }

  public boolean getLoaded()
  {
  	return loaded;
  }

  public synchronized boolean getWait()
  {
  	return wait;
  }

  public synchronized void setWait(boolean Wait)
  {
  	wait=Wait;
  }

  public synchronized boolean getAllowExtra()
  {
  	return allowextra;
  }

  public synchronized void setAllowExtra(boolean AllowExtra)
  {
  	allowextra=AllowExtra;
  }

  public synchronized int getConnections()
  {
  	return connections;
  }

  public synchronized void setConnections(int Connections)
  {
  	if (loaded)
    {
  		connections=Connections;
    	while ((spare.size()>0)&&((spare.size()+inuse)>connections))
    	{
      	try
      	{
   	 			((Connection)spare.elementAt(0)).close();
   	 	  }
   	 	  catch (SQLException e)
				{
      	}
    		spare.removeElementAt(0);
    	}
 	   	int needed = (connections-(spare.size()+inuse));
 	   	Connection conn;
    	for (int loop=0; loop<needed; loop++)
    	{
    		conn = createConnection();
    	  if (conn!=null)
    	  {
    	  	spare.addElement(conn);
    	  }
    	}
    }
  }

  public synchronized int getTotalConnections()
  {
  	return (spare.size()+inuse);
  }

  public synchronized int getInUse()
  {
  	return inuse;
  }

  public synchronized void setInUse(int InUse)
  {
  	inuse=InUse;
  }

  public synchronized Connection getConnection()
  {
  	if (loaded)
    {
  		if (spare.size()>0)
    	{
    		inuse++;
    	  Connection conn = (Connection)spare.elementAt(0);
    	  spare.removeElementAt(0);
    	  return conn;
    	}
    	else if (allowextra)
    	{
    		Connection conn = createConnection();
    	  if (conn!=null)
    	  {
    	  	inuse++;
   	   	}
    	  return conn;
    	}
    	else
    	{
  			return null;
    	}
    }
    else
    {
    	return null;
    }
  }

  public synchronized void releaseConnection(Connection Old)
  {
  	if (Old!=null)
    {
			if ((getInUse()+spare.size())<=getConnections())
    	{
  			spare.addElement(Old);
    	}
      else
      {
      	try
        {
      		Old.close();
        }
        catch (SQLException e)
        {
        }
      }
    	inuse--;
    }
  }

  private Connection createConnection()
  {
  	if (loaded)
    {
  		try
    	{
    		Connection newconn = DriverManager.getConnection("jdbc:mysql://firewall/eetownsd_proj","eetownsd","78Cthulhu20");
    	  return newconn;
    	}
    	catch (SQLException e)
    	{
   		 	System.err.println("(ConnectionPool) Could not create connection - "+e.getMessage());
    		return null;
    	}
    }
    else
    {
    	return null;
    }
  }
}