package com.brass-bullet.dbutils.xml;

import org.xml.sax.HandlerBase;
import org.xml.sax.DocumentHandler;
import org.xml.sax.AttributeList;
import org.xml.sax.SAXException;
import com.sun.xml.parser.Parser;

public class Reference extends HandlerBase
{
  private Parser parser;
  private DocumentHandler previous;
  private Field source;
  private Field dest;
  private StringBuffer table;
  private StringBuffer field;
  private boolean mustexist;
  private int deletetype;
  private int modifytype;
  
  public static int REF_IGNORE = 0;
  public static int REF_CASCADE = 3;
  public static int REF_ROLLBACK = 1;
  public static int REF_NULL = 2;
  
  public Reference(Field sourcefield)
  {
    source=sourcefield;
    table=null;
    field=null;
  }
  
  public Reference(Field sourcefield, DocumentHandler last, Parser thisparser)
  {
    this(sourcefield);
    previous=last;
    parser=thisparser;
    parser.setDocumentHandler(this);
  }
  
  public static int makeType(String type)
  {
    if (type.equals("cascade"))
    {
      return Reference.REF_CASCADE;
    }
    else if (type.equals("rollback"))
    {
      return Reference.REF_ROLLBACK;
    }
    else
    {
      return Reference.REF_NULL;
    }
  }
  
  public void startElement(String name, AttributeList attributes)
  {
    if (name.equals("TableRef"))
    {
      table = new StringBuffer();
      TextParser text = new TextParser(table,this,parser);
    }
    else if (name.equals("FieldRef"))
    {
      field = new StringBuffer();
      TextParser text = new TextParser(field,this,parser);
    }
  }
  
  public void endElement(String name) throws SAXException
  {
    parser.setDocumentHandler(previous);
    dest = null;
    Table tableref = source.getParent().getParent().getTable(table.toString());
    if (tableref!=null)
    {
      dest=tableref.getAllField(field.toString());
    }
    if (dest==null)
    {
      SAXException error = new SAXException("Reference error in "+source.getParent().getName()+"."+source.getName()+" referencing "+table.toString()+"."+field.toString());
      throw error;
    }
    else
    {
      dest.addReferencedby(this);
    }
  }
  
  public void setModifyType(int newtype)
  {
    modifytype=newtype;
  }
  
  public int getModifyType()
  {
    return modifytype;
  }
  
  public void setDeleteType(int newtype)
  {
    deletetype=newtype;
  }
  
  public int getDeleteType()
  {
    return deletetype;
  }
  
  public void setMustExist(boolean exist)
  {
    mustexist=exist;
  }
  
  public boolean getMustExist()
  {
    return mustexist;
  }
  
  public void setDestination(Field newdest)
  {
    dest=newdest;
  }
  
  public Field getDestination()
  {
    return dest;
  }

  public void setSource(Field newsource)
  {
    source=newsource;
  }
  
  public Field getSource()
  {
    return source;
  }
}
