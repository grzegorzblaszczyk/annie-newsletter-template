package gbc.annie;

import gbc.annie.parser.Parser;
import gbc.annie.parser.SimpleXmlParser;
import gbc.annie.solr.FieldType;
import gbc.annie.solr.ResponseExtractor;
import gbc.annie.ui.ResultPage;

import java.awt.Dimension;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.dom4j.Document;

public class Main extends JFrame implements ActionListener {

  private static final String SOLR_ENDPOINT = "http://10.9.1.21:9999/solr/select";

  public static final Logger logger = Logger.getLogger(Main.class);

  private static final String PROGRAM_NAME = "Annie";
  private static final String VERSION = "0.1";

  private static final int MAIN_WINDOW_WIDTH = 800;
  private static final int MAIN_WINDOW_HEIGHT = 600;

  /**
   *
   */
  private static final long serialVersionUID = 4884136881615528439L;

  private JTextField productIdTextField;

  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      new Main();
    } catch (MalformedURLException e) {
      logger.error(e);
    }
  }

  public Main() throws MalformedURLException {
    setupWindowName();
    setupMainWindowLocationOnScreen();

    Panel panel = new Panel();

    JLabel label = new JLabel("ID produktu:");
    panel.add(label);

    productIdTextField = new JTextField(20);
    productIdTextField.setSize(20, 1);
    panel.add(productIdTextField);

    JButton submit = new JButton("Odczytaj");
    submit.addActionListener(this);

    panel.add(submit);

    add(panel);

    setVisible(true);
  }

  public void actionPerformed(ActionEvent ae) {
    try {
      Map<String,String> params = performQuery(productIdTextField.getText());

      ResultPage page = new ResultPage();
      page.setVisible(true);

      JLabel label = new JLabel("ID: " + params.get("id"));
      page.getPanel().add(label);

      label = new JLabel("Image1Path: " + params.get("image1Path"));
      page.getPanel().add(label);

    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      logger.error(e);
    }

  }

  private Map<String,String> performQuery(String productId) throws MalformedURLException {
    URL url = new URL(SOLR_ENDPOINT + "?q=id:" + productId + "&fl=id,longDescription,image1Path");

    Parser parser = new SimpleXmlParser();
    Document document = parser.parse(url);

    Map<String,String> params = new HashMap<String, String>();

    params.put("id", ResponseExtractor.getValue(document, "id", FieldType.STR, false));
    params.put("image1Path", ResponseExtractor.getValue(document, "image1Path", FieldType.STR, true));

    if (logger.isDebugEnabled()) {
      for (String key : params.keySet()) {
        logger.info(key + ": " + params.get(key));
      }
    }
    return params;
  }

  private void setupMainWindowLocationOnScreen() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(screenSize.width / 2 - MAIN_WINDOW_WIDTH / 2, screenSize.height / 2 - MAIN_WINDOW_HEIGHT / 2);
    setVisible(true);
    setSize(800, 600);
  }

  private void setupWindowName() {
    this.setTitle(PROGRAM_NAME + " " + VERSION);
  }

}
