package gbc.annie.solr;

import gbc.annie.parser.Parser;
import gbc.annie.parser.SimpleXmlParser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;

public class QueryPerformer {

  public static final Logger logger = Logger.getLogger(QueryPerformer.class);

  private String endpoint;

  public Map<String,String> performQuery(String productId) throws MalformedURLException {
    URL url = new URL(endpoint + "?q=id:" + productId + "&fl=author,basePrice,director,displayName,goldPrice,id,longDescription,image1Path,movieCast,originalDisplayName,originalSubTitle,originalTitie,performer,productDisplayNames,salePrice,subtitle,title");

    Parser parser = new SimpleXmlParser();
    Document document = parser.parse(url);

    Map<String,String> params = new HashMap<String, String>();

    params.put("id", ResponseExtractor.getValue(document, "id", FieldType.STR, false));
    params.put("title", getFirstNonEmptyValue(document,
                                              Field.TITLE,
                                              Field.SUB_TITLE,
                                              Field.ORIGINAL_TITLE,
                                              Field.ORIGINAL_SUB_TITLE,
                                              Field.DISLAY_NAME,
                                              Field.PRODUCT_DISLAY_NAMES));

    params.put("author", getFirstNonEmptyValue(document,
                                                Field.AUTHOR,
                                                Field.DIRECTOR,
                                                Field.MOVIE_CAST,
                                                Field.PERFORMER));

    params.put("image1Path", ResponseExtractor.getValue(document, "image1Path", FieldType.STR, true));

    params.put("price", getFirstNonEmptyValue(document,
                                              Field.SALE_PRICE,
                                              Field.BASE_PRICE,
                                              Field.GOLD_PRICE));

    if (logger.isDebugEnabled()) {
      for (String key : params.keySet()) {
        logger.info(key + ": " + params.get(key));
      }
    }
    return params;
  }

  public static String getFirstNonEmptyValue(final Document document, final Field... fields) {
    for (Field field : fields) {
      String value = "";
      try {
        value = ResponseExtractor.getValue(document, field.getFieldName(), field.getType(), field.isMultiple());
      } catch (ResponseExtractorException e) {
        // nothing to do
      }

      if (StringUtils.isNotBlank(value)) {
        return value;
      }
    }
    return "";
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getEndpoint() {
    return endpoint;
  }

}
