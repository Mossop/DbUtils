package com.brass-bullet.dbutils.xml;

import java.util.Vector;
import org.xml.sax.HandlerBase;
import org.xml.sax.DocumentHandler;
import org.xml.sax.AttributeList;
import com.sun.xml.parser.Parser;

public class Database extends HandlerBase
{
  private Vector tables;
  private String name;
  private StringBuffer author;
  private StringBuffer comment;
  private DocumentHandler previous;
  private Parser parser;
  
  public Database()
  {
    tables = new Vector(10);
    author=null;
    comment=null;
  }
  
  public Database(DocumentHandler last, Parser thisparser)
  {
    this();
    previous=last;
    parser=thisparser;
    parser.setDocumentHandler(this);
  }
  
  public void startElement(String name, AttributeList attributes)
  {
    if (name.equals("Table"))
    {
      Table newtable = new Table(this,this,parser);
      newtable.setName(attributes.getValue("name"));
      addTable(newtable);
    }
    else if (name.equals("Comment"))
    {
      comment = new StringBuffer();
      TextParser text = new TextParser(comment,this,parser);
    }
    else if (name.equals("Author"))
    {
      author = new StringBuffer();
      TextParser text = new TextParser(author,this,parser);
    }
  }
  
  public void endElement(String name)
  {
    parser.setDocumentHandler(previous);
  }

  public void setName(String Name)
  {
    name=Name;
  }
  
  public String getName()
  {
    return name;
  }
  
  public void setAuthor(String Author)
  {
    author = new StringBuffer(Author);
  }
  
  public String getAuthor()
  {
  	if (author==null)
    {
    	return null;
    }
    else
    {
    	return author.toString();
    }
  }
  
  public void setComment(String Comment)
  {
    comment = new StringBuffer(Comment);
  }
  
  public String getComment()
  {
  	if (comment==null)
    {
    	return null;
    }
    else
    {
    	return comment.toString();
  	}
  }
  
  public int getTableCount()
  {
    return tables.size();
  }
  
  public Table getTable(String name)
  {
    Table test = new Table(this);
    test.setName(name);
    int pos = tables.indexOf(test);
    if (pos>=0)
    {
      return (Table)tables.elementAt(pos);
    }
    else
    {
      return null;
    }
  }
  
  public Table getTable(int pos)
  {
    return (Table)tables.elementAt(pos);
  }
  
  public void addTable(Table table)
  {
    tables.addElement(table);
  }
}
