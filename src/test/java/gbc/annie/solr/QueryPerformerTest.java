package gbc.annie.solr;

import static org.junit.Assert.assertEquals;

import gbc.annie.Constants;

import java.net.MalformedURLException;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QueryPerformerTest {

  QueryPerformer qPerformer = null;

  @Before
  public void setUp() throws Exception {
    qPerformer = new QueryPerformer();
    qPerformer.setEndpoint(Constants.SOLR_ENDPOINT);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testPerformQuery() throws MalformedURLException {
    Map<String, String> params = qPerformer.performQuery("1124");
    assertEquals("Jonathan Carroll", params.get("author"));
    assertEquals("Szklana zupa", params.get("title"));
    assertEquals("19.9", params.get("price"));
    assertEquals("/57/93/57937073e2d82e6724193a66c9f26be2.jpg", params.get("image1Path"));
  }

}
