package com.brass-bullet.dbutils.xml;

import java.util.Vector;
import org.xml.sax.HandlerBase;
import org.xml.sax.DocumentHandler;
import org.xml.sax.AttributeList;
import com.sun.xml.parser.Parser;

public class Table extends HandlerBase
{
  private Vector fields;
  private Vector keyfields;
  private String name;
  private StringBuffer comment;
  private DocumentHandler previous;
  private Parser parser;
  private Database parent;
  private boolean inkey;

  public Table(Database root)
  {
    fields = new Vector(10);
    keyfields = new Vector(10);
    comment=null;
    parent=root;
    inkey=false;
  }
  
  public Table(Database root, DocumentHandler last, Parser thisparser)
  {
    this(root);
    previous=last;
    parser=thisparser;
    parser.setDocumentHandler(this);
  }
  
  public void startElement(String name, AttributeList attributes)
  {
    if (name.equals("PrimaryKey"))
    {
			inkey=true;
    }
    else if (name.equals("Field"))
    {
      Field newfield = new Field(this,this,parser);
      newfield.setName(attributes.getValue("name"));
      if (inkey)
      {
      	newfield.setNull(false);
        addKeyField(newfield);
      }
      else
      {
      	if (attributes.getValue("null").equals("yes"))
      	{
      	  newfield.setNull(true);
      	}
      	else
      	{
      	  newfield.setNull(false);
      	}
      	addField(newfield);
      }
    }
    else if (name.equals("Comment"))
    {
    	comment = new StringBuffer();
      TextParser text = new TextParser(comment,this,parser);
    }
  }
  
  public void endElement(String name)
  {
  	if (name.equals("PrimaryKey"))
    {
    	inkey=false;
    }
    else
    {
    	parser.setDocumentHandler(previous);
    }
  }
  
  public boolean equals(Object test)
  {
    return (((Table)test).getName().equals(getName()));
  }
  
  public void setName(String Name)
  {
    name=Name;
  }
  
  public String getName()
  {
    return name;
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
  
  public Database getParent()
  {
    return parent;
  }
  
  public int getFieldCount()
  {
    return fields.size();
  }
  
  public Field getField(int pos)
  {
    return (Field)fields.elementAt(pos);
  }
  
  public void addField(Field field)
  {
    fields.addElement(field);
  }

  public int getAllFieldCount()
  {
    return keyfields.size()+fields.size();
  }
  
  public Field getAllField(int pos)
  {
    if (pos>=keyfields.size())
    {
      return getField(pos-keyfields.size());
    }
    else
    {
      return getKeyField(pos);
    }
  }
  
  public Field getField(String name)
  {
    Field test = new Field(this);
    test.setName(name);
    int pos = fields.indexOf(test);
    if (pos>=0)
    {
      return (Field)fields.elementAt(pos);
    }
    else
    {
      return null;
    }
  }
  
  public Field getKeyField(String name)
  {
    Field test = new Field(this);
    test.setName(name);
    int pos = keyfields.indexOf(test);
    if (pos>=0)
    {
      return (Field)keyfields.elementAt(pos);
    }
    else
    {
      return null;
    }
  }
  
  public int getKeyFieldCount()
  {
    return keyfields.size();
  }
  
  public Field getAllField(String name)
  {
    Field test = new Field(this);
    test.setName(name);
    int pos = fields.indexOf(test);
    if (pos>=0)
    {
      return (Field)fields.elementAt(pos);
    }
    else
    {
      pos = keyfields.indexOf(test);
      if (pos>=0)
      {
        return (Field)keyfields.elementAt(pos);
      }
      else
      {
        return null;
      }
    }
  }
  
  public Field getKeyField(int pos)
  {
    return (Field)keyfields.elementAt(pos);
  }
  
  public void addKeyField(Field field)
  {
    keyfields.addElement(field);
  }
}
