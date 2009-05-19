package gbc.annie.solr;

import static org.junit.Assert.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ResponseExtractorTest {

  public static final String SAMPLE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<response>\n<lst name=\"responseHeader\"><int name=\"status\">0</int><int name=\"QTime\">0</int><lst name=\"params\"><str name=\"fl\">id,longDescription,image1Path</str><str name=\"q\">id:1124</str></lst></lst><result name=\"response\" numFound=\"1\" start=\"0\"><doc><str name=\"id\">1124</str><arr name=\"image1Path\"><str>/57/93/57937073e2d82e6724193a66c9f26be2.jpg</str></arr><arr name=\"longDescription\"><str>Najnowsza powieść Carrolla, kontynuacja Białych jabłek. W książce przeplatają się trzy wątki: opowieść o problemach, które nękają Vincenta Ettricha i jego ukochaną Isabelle przed narodzinami ich syna Anjo; historia ich przyjaciela Simona Hadena, który po śmierci staje się niespodziewanie bardzo ważną dla nich osobą; i opowieść o Johnie Flannerym, wysłanym przez Chaos na Ziemię, by uniemożliwić Anjo zdobycie niezbędnych mu mocy. Wszystkie wątki łączą się i rozwiązują w zaskakującym finale.</str></arr></doc></result>\n</response>";

  private Document doc = null;

  @Before
  public void setUp() throws Exception {
    doc = DocumentHelper.parseText(SAMPLE_XML);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetProperValue() throws DocumentException {
    assertEquals("1124", ResponseExtractor.getValue(doc, "1124", "id", FieldType.STR, false));
  }

  @Test(expected=ResponseExtractorException.class)
  public void testGetValueWithInvalidValue() throws DocumentException {
    assertNull("1124", ResponseExtractor.getValue(doc, "1124", "lala", FieldType.INT, false));
  }

  @Test(expected=NullPointerException.class)
  public void testGetValueWithInvalidType() throws DocumentException {
    assertNull("1124", ResponseExtractor.getValue(doc, "1124", "id", null, false));
  }

  @Test
  public void testGetValueFromMultipleField() throws DocumentException {
    assertEquals("/57/93/57937073e2d82e6724193a66c9f26be2.jpg", ResponseExtractor.getValue(doc, "1124", "image1Path", FieldType.STR, true));
  }

  @Test(expected=NullPointerException.class)
  public void testGetValueWithInvalidIndex() throws DocumentException {
    assertNull("1124", ResponseExtractor.getValue(doc, "1124", "image1Path", null, true));
  }

}
