package gbc.annie;

import gbc.annie.solr.QueryPerformer;
import gbc.annie.ui.ResultPage;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

public class Main extends JFrame implements ActionListener {

  public static final Logger logger = Logger.getLogger(Main.class);

  private static final String PROGRAM_NAME = "Annie";
  private static final String VERSION = "0.1";

  private static final int MAIN_WINDOW_WIDTH = 800;
  private static final int MAIN_WINDOW_HEIGHT = 600;

  /**
   *
   */
  private static final long serialVersionUID = 4884136881615528439L;

  private JTextField productIdsTextField;

  private JTextField newsletterIdTextField;

  private JTextField speciallyRecommendedIdTextField;

  private Component sectionNameTextField;

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

    JLabel label = new JLabel("Newsletter ID:");
    panel.add(label);
    newsletterIdTextField = new JTextField(10);
    panel.add(newsletterIdTextField);

    panel.add(new JLabel());
    panel.add(new JLabel());

    // -----------------------------------------

    label = new JLabel("Szczegolnie polecamy:");
    panel.add(label);
    speciallyRecommendedIdTextField = new JTextField(10);
    panel.add(speciallyRecommendedIdTextField);

    panel.add(new JLabel());
    panel.add(new JLabel());

    // -----------------------------------------

    label = new JLabel("Nazwa sekcji:");
    panel.add(label);

    sectionNameTextField = new JTextField(20);
    sectionNameTextField.setSize(20, 1);
    panel.add(sectionNameTextField);

    label = new JLabel("ID produktow:");
    panel.add(label);

    productIdsTextField = new JTextField(20);
    productIdsTextField.setSize(20, 1);
    panel.add(productIdsTextField);

    // ------------------------------------------

    JButton submit = new JButton("Odczytaj");
    submit.setName("generate");
    submit.addActionListener(this);

    panel.add(submit);
    add(panel);
    setVisible(true);
  }

  public void actionPerformed(ActionEvent ae) {

    logger.info("source: " + ae.getSource());

    ResultPage page = prepareResultPage();
    page.setVisible(true);

  }

  private ResultPage prepareResultPage() {
    ResultPage resultPage = new ResultPage();

    List<Map<String, String>> productParamsList = performProductQueries();
    renderOutput(resultPage, productParamsList);

    return resultPage;
  }

  private List<Map<String, String>> performProductQueries() {
    List<Map<String, String>> productParamsList = new ArrayList<Map<String, String>>();
    try {

      StringTokenizer tokenizer = new StringTokenizer(productIdsTextField.getText(), ",");
      while (tokenizer.hasMoreTokens()) {
        String productId = tokenizer.nextToken();
        QueryPerformer qPerformer = new QueryPerformer();
        qPerformer.setEndpoint(Constants.SOLR_ENDPOINT);
        Map<String, String> productParams = qPerformer.performQuery(productId);
        productParamsList.add(productParams);
      }

    } catch (MalformedURLException e) {
      logger.error(e);
    }
    return productParamsList;
  }

  private void renderOutput(ResultPage resultPage, List<Map<String, String>> productParamsList) {
    for (Map<String, String> listItem : productParamsList) {
      JLabel label = new JLabel("ID: " + listItem.get(Constants.PRODUCT_ID));
      resultPage.getPanel().add(label);

      label = new JLabel("Image1Path: " + listItem.get(Constants.IMAGE));
      resultPage.getPanel().add(label);
    }
  }

  private void setupMainWindowLocationOnScreen() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(screenSize.width / 2 - MAIN_WINDOW_WIDTH / 2, screenSize.height / 2 - MAIN_WINDOW_HEIGHT / 2);
    setVisible(true);
    setSize(800, 600);
    setLayout(new GridLayout(7, 4));
  }

  private void setupWindowName() {
    this.setTitle(PROGRAM_NAME + " " + VERSION);
  }

}
