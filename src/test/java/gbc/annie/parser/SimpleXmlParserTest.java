package gbc.annie.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import gbc.annie.Constants;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleXmlParserTest {

  public static final Logger logger = Logger.getLogger(SimpleXmlParserTest.class);

  public static URL TEST_URL = null;
  static {
    try {
      Configuration config = null;
      try {
        config = new PropertiesConfiguration("annie.properties");
      } catch (ConfigurationException e) {
        logger.error(e);
      }
      String endPoint = (String)config.getProperty("solrEndpoint");
      TEST_URL = new URL(endPoint + "?q=id:1124&fl=id,longDescription,image1Path");
    } catch (MalformedURLException e) {
      logger.error(e);
    }
  }

  private Parser parser = new SimpleXmlParser();

  @Before
  public void setUp() throws Exception {

  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testParse() {
    Document doc = parser.parse(TEST_URL);
    assertNotNull(doc);
    assertNotNull(doc.getRootElement());
    assertEquals("1124", doc.selectSingleNode("//response/result/doc/str[@name='id']").getText());
  }

}
