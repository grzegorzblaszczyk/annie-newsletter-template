package gbc.annie.parser;

import java.net.URL;

import org.dom4j.Document;

public interface Parser {

  public abstract Document parse(URL url);

}