package com.brass-bullet.dbutils.xml;

import org.xml.sax.InputSource;
import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import java.io.IOException;

public class DatabaseParser extends ParserBase
{
  private Database database;

  public DatabaseParser()
  {
    super();
    database = null;
  }
  
  public void startElement(String name, AttributeList attributes)
  {
    if (name.equals("Database"))
    {
      database = new Database(this,parser);
      database.setName(attributes.getValue("name"));
    }
  }

  public Database parse(InputSource input) throws SAXException, IOException
  {
    if (doParse(input))
    {
      return database;
    }
    else
    {
      return null;
    }
  }
}
