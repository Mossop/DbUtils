package com.brass-bullet.dbutils.xml;

import org.xml.sax.HandlerBase;
import org.xml.sax.DocumentHandler;
import com.sun.xml.parser.Parser;

public class TextParser extends HandlerBase
{
  private StringBuffer buffer;
  private DocumentHandler previous;
  private Parser parser;
  
  public TextParser(StringBuffer base, DocumentHandler last, Parser thisparser)
  {
    buffer=base;
    previous=last;
    parser=thisparser;
    parser.setDocumentHandler(this);
  }
  
  public void characters(char[] ch, int start, int length)
  {
    buffer.append(ch,start,length);
  }
  
  public void endElement(String name)
  {
    parser.setDocumentHandler(previous);
  }
}
