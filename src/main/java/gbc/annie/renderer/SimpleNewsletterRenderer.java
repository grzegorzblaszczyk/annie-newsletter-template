package gbc.annie.renderer;

import gbc.annie.Constants;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class SimpleNewsletterRenderer {
	
	public static Logger logger = Logger.getLogger(SimpleNewsletterRenderer.class);

	public String render(List<Map<String, String>> productParamsList) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("header");

		for (Map<String, String> listItem : productParamsList) {
			String value = "\nID: " + listItem.get(Constants.PRODUCT_ID);
			logger.debug(value);
			buffer.append(value);
			
			value = "\nImagePath: " + listItem.get(Constants.IMAGE);
			logger.debug(value);
			buffer.append(value);
		}
		return buffer.toString();
	}
}
