package com.brass-bullet.dbutils;

import com.brass-bullet.dbutils.xml.DatabaseParser;
import com.brass-bullet.dbutils.xml.Database;
import com.brass-bullet.dbutils.xml.Table;
import com.brass-bullet.dbutils.xml.Field;
import com.brass-bullet.dbutils.xml.Reference;
import com.brass-bullet.dbutils.ConnectionPool;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class IntegrityValidator
{
  private Database database;
  private ConnectionPool pool;
  
  private static int MODIFY=0;
  private static int DELETE=1;
  
  public IntegrityValidator(Database db, ConnectionPool Pool)
  {
    database=db;
    pool=Pool;
  }
  
  public boolean validateModify(Vector querylist, Field target, String oldvalue, String newvalue) throws SQLException
  {
    if ((oldvalue==null)||(oldvalue.equals(newvalue)))
    {
      return true;
    }
    else
    {
      return validate(MODIFY,querylist,target,oldvalue,newvalue);
    }
  }
  
  public boolean validateDelete(Vector querylist, Field target, String oldvalue) throws SQLException
  {
    return validate(DELETE,querylist,target,oldvalue,null);
  }
  
  private boolean validate(int type, Vector querylist, Field target, String oldvalue, String newvalue) throws SQLException
  {
    boolean valid = true;
    if ((type==MODIFY)&&(newvalue==null)&&(!target.getNull()))
    {
      valid=false;
    }
    else
    {
    	Connection dbcon = pool.getConnection();
      if (dbcon!=null)
      {
      	for (int refs=0; refs<target.getReferencedbyCount(); refs++)
      	{
      	  Reference ref = target.getReferencedby(refs);
      	  Field source = ref.getSource();
      	  int reftype = 0;
      	  if (type==DELETE)
      	  {
      	    reftype=ref.getDeleteType();
      	  }
      	  else if (type==MODIFY)
      	  {
      	    reftype=ref.getModifyType();
      	  }
      	  String sourcename=source.getName();
      	  String sourcetable=source.getParent().getName();
      	  switch(reftype)
      	  {
      	    case 1:  ResultSet checkrollback = dbcon.createStatement().executeQuery("SELECT * FROM "+sourcetable+" WHERE "+sourcename+"="+oldvalue+";");
      	             if (checkrollback.next())
      	             {
      	               valid=false;
      	             }
      	             break;
      	    case 2:  if (source.getReferencedbyCount()>0)
      	             {
      	               ResultSet cannull = dbcon.createStatement().executeQuery("SELECT * FROM "+sourcetable+" WHERE "+sourcename+"="+oldvalue+";");
      	               if (cannull.next())
      	               {
      	                 if (!validate(type,querylist,source,oldvalue,null))
      	                 {
      	                   valid=false;
      	                 }
      	               }
      	             }
      	             querylist.addElement("UPDATE "+sourcetable+" SET "+sourcename+"=NULL WHERE "+sourcename+"="+oldvalue+";");
      	             break;
      	    case 3:  if (type==MODIFY)
      	             {
      	               if (source.getReferencedbyCount()>0)
      	               {
      	                 ResultSet modcheck = dbcon.createStatement().executeQuery("SELECT * FROM "+sourcetable+" WHERE "+sourcename+"="+oldvalue+";");
      	                 if (modcheck.next())
      	                 {
      	                   if (!validate(type,querylist,source,oldvalue,newvalue))
      	                   {
      	                     valid=false;
      	                   }
      	                 }
      	               }
      	               querylist.addElement("UPDATE "+sourcetable+" SET "+sourcename+"="+newvalue+" WHERE "+sourcename+"="+oldvalue+";");
      	             }
      	             else if (type==DELETE)
      	             {
      	               int refcount=0;
      	               String query="";
      	               for (int loop=0; loop<source.getParent().getAllFieldCount(); loop++)
      	               {
      	                 if (source.getParent().getAllField(loop).getReferencedbyCount()>0)
      	                 {
      	                   query=query+source.getParent().getAllField(loop).getName()+",";
      	                   refcount++;
      	                 }
      	               }
      	               if (refcount>0)
      	               {
      	                 query=query.substring(0,query.length()-1);
      	                 ResultSet results = dbcon.createStatement().executeQuery("SELECT "+query+" FROM "+sourcetable+" WHERE "+sourcename+"="+oldvalue+";");
      	                 while (results.next())
      	                 {
      	                   int pos=1;
      	                   for (int loop=0; loop<source.getParent().getAllFieldCount(); loop++)
      	                   {
      	                     if (source.getParent().getAllField(loop).getReferencedbyCount()>0)
      	                     {
      	                       if (!validate(DELETE,querylist,source.getParent().getAllField(loop),results.getString(pos),null))
      	                       {
      	                         valid=false;
      	                       }
      	                       pos++;
      	                     }
      	                   }
      	                 }
      	               }
      	               querylist.addElement("DELETE FROM "+sourcetable+" WHERE "+sourcename+"="+oldvalue+";");
      	             }
      	             break;
      	  }
      	}
        pool.releaseConnection(dbcon);
      }
      else
      {
      	return false;
      }
    }
    return valid;
  }
}
