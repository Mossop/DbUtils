package com.brass-bullet.dbutils.xml;

import org.xml.sax.HandlerBase;
import org.xml.sax.DocumentHandler;
import org.xml.sax.AttributeList;
import com.sun.xml.parser.Parser;
import java.util.Vector;

public class Field extends HandlerBase
{
  private String name;
  private StringBuffer type;
  private StringBuffer comment;
  private DocumentHandler previous;
  private Parser parser;
  private boolean nullallowed;
  private Table parent;
  private Vector references;
  private Vector referencedby;
  
  public Field(Table root)
  {
    type=null;
    comment=null;
    references = new Vector(10);
    referencedby = new Vector(10);
    nullallowed = true;
    parent=root;
  }
  
  public Field(Table root, DocumentHandler last, Parser thisparser)
  {
    this(root);
    previous=last;
    parser=thisparser;
    parser.setDocumentHandler(this);
  }
  
  public void startElement(String name, AttributeList attributes)
  {
    if (name.equals("Type"))
    {
      type = new StringBuffer();
      TextParser text = new TextParser(type,this,parser);
    }
    else if (name.equals("Comment"))
    {
      comment = new StringBuffer();
      TextParser text = new TextParser(comment,this,parser);
    }
    else if (name.equals("Reference"))
    {
      Reference ref = new Reference(this,this,parser);
      ref.setModifyType(Reference.makeType(attributes.getValue("onModify")));
      ref.setDeleteType(Reference.makeType(attributes.getValue("onDelete")));
      if (attributes.getValue("mustexist").equals("yes"))
      {
        ref.setMustExist(true);
      }
      else
      {
        ref.setMustExist(false);
      }
      addReferences(ref);
    }
  }
  
  public void endElement(String name)
  {
    parser.setDocumentHandler(previous);
  }
  
  public boolean equals(Object test)
  {
    return (((Field)test).getName().equals(getName()));
  }
  
  public void setName(String Name)
  {
    name=Name;
  }
  
  public String getName()
  {
    return name;
  }
  
  public void setType(String Type)
  {
    type = new StringBuffer(Type);
  }
  
  public String getType()
  {
    return type.toString();
  }
  
  public boolean isString()
  {
    boolean string = false;
    if (type.toString().equals("TEXT"))
    {
      string=true;
    }
    if ((type.length()>=7)&&(type.toString().substring(0,7).equals("VARCHAR")))
    {
      string=true;
    }
    return string;
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
  
  public void setNull(boolean Null)
  {
    nullallowed=Null;
  }
  
  public boolean getNull()
  {
    return nullallowed;
  }
  
  public Table getParent()
  {
    return parent;
  }
  
  public int getReferencesCount()
  {
    return references.size();
  }
  
  public Reference getReferences(int pos)
  {
    return (Reference)references.elementAt(pos);
  }
  
  public void addReferences(Reference ref)
  {
    references.addElement(ref);
  }
  
  public int getReferencedbyCount()
  {
    return referencedby.size();
  }
  
  public Reference getReferencedby(int pos)
  {
    return (Reference)referencedby.elementAt(pos);
  }
  
  public void addReferencedby(Reference ref)
  {
    referencedby.addElement(ref);
  }
}