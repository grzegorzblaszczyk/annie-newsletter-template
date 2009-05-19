package gbc.annie.solr;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Node;

public class ResponseExtractor {

  public static final Logger logger = Logger.getLogger(ResponseExtractor.class);

  public static final String XPATH_PREFIX = "//response/result/doc/";

  public static String getValue(Document doc, String productId, String fieldName, FieldType fieldType, boolean isMultiple) throws ResponseExtractorException {

    if (logger.isDebugEnabled()) {
      logger.debug(doc.asXML());
    }

    if (StringUtils.isBlank(fieldName) || StringUtils.isBlank(fieldType.name())) {
      throw new ResponseExtractorException("Field name or field type is blank");
    }
    StringBuffer xpath = new StringBuffer(XPATH_PREFIX + "str[@name=\"id\"][text()=\"" + productId + "\"]/../");
    if (isMultiple) {
      xpath.append(FieldType.ARR.toString().toLowerCase() + "[@name='" + fieldName + "']/" + fieldType.toString().toLowerCase() + "[1]");
    } else {
      xpath.append(fieldType.toString().toLowerCase() + "[@name='" + fieldName + "']");
    }
    Node node = doc.selectSingleNode(xpath.toString());
    if (node == null) {
      throw new ResponseExtractorException("Node " + xpath + " is null");
    }
    String value = node.getText();
    return value;
  }
}
