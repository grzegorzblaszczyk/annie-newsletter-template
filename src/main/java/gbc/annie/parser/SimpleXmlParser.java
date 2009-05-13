package gbc.annie.parser;

import java.net.URL;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class SimpleXmlParser implements Parser {

  public static final Logger logger = Logger.getLogger(SimpleXmlParser.class);

  /* (non-Javadoc)
   * @see gbc.annie.parser.Parser#parse(java.net.URL)
   */
  public Document parse(URL url) {
    SAXReader reader = new SAXReader();
    Document document = null;
    try {
      document = reader.read(url);
    } catch (DocumentException e) {
      logger.error(e);
    }
    return document;
  }

}
