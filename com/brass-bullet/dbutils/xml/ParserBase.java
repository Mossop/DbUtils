package com.brass-bullet.dbutils.xml;

import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.sun.xml.parser.Parser;
import com.sun.xml.parser.ValidatingParser;
import java.io.IOException;

public abstract class ParserBase extends HandlerBase
{
  protected Parser parser;
  
  public ParserBase()
  {
    super();
  }

  protected Parser getParser()
  {
    return new ValidatingParser(true);
  }
  
  protected boolean doParse(InputSource input) throws SAXException, IOException
  {
    parser = getParser();
    parser.setDocumentHandler(this);
    parser.parse(input);
    return true;
  }
}
