package com.brassbullet.dbutils.xml;

import com.brassbullet.dbutils.xml.base.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.net.URL;
import java.io.InputStream;
import javax.xml.bind.UnmarshalException;
import javax.xml.marshal.XMLScanner;
import javax.xml.bind.Dispatcher;

public class Database extends DatabaseBase
{
  public static Dispatcher newDispatcher()
  {
    Dispatcher d = DatabaseBase.newDispatcher();
    d.register(DatabaseBase.class, Database.class);
    d.register(FieldBase.class, Field.class);
    d.register(PrimaryKeyBase.class, PrimaryKey.class);
    d.register(ReferenceBase.class, Reference.class);
    d.register(TableBase.class, Table.class);
    return d;
  }

	public static Database unmarshalDatabase(String filename) throws UnmarshalException, FileNotFoundException
	{
		return unmarshalDatabase(new FileInputStream(filename));
	}
	
	public static Database unmarshalDatabase(URL location) throws UnmarshalException, IOException
	{
		return unmarshalDatabase(location.openStream());
	}
	
	public static Database unmarshalDatabase(File file) throws UnmarshalException, FileNotFoundException
	{
		return unmarshalDatabase(new FileInputStream(file));
	}
	
  public static Database unmarshalDatabase(InputStream in) throws UnmarshalException
  {
    return unmarshalDatabase(XMLScanner.open(in));
  }

  public static Database unmarshalDatabase(XMLScanner xs) throws UnmarshalException
  {
    return unmarshalDatabase(xs, newDispatcher());
  }

  public static Database unmarshalDatabase(XMLScanner xs, Dispatcher d) throws UnmarshalException
  {
    return (Database)d.unmarshal(xs);
  }

	public Table getTable(String name)
	{
		int loop=0;
	  while ((loop<getTables().size())&&(!((Table)getTables().get(loop)).getName().equals(name)))
	  {
	  	loop++;
	  }
		if (loop<getTables().size())
		{
			return (Table)getTables().get(loop);
		}
		else
		{
			return null;
		}
	}
}
