package gbc.annie.ui;

import gbc.annie.renderer.SimpleNewsletterRenderer;
import gbc.annie.solr.QueryPerformer;
import gbc.annie.solr.ResponseExtractorException;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class StartPage implements ActionListener {

  public static final Logger logger = Logger.getLogger(StartPage.class);

  private JTextField newsletterIdTextField;
  private JTextField dateToSendTextField;
  private JTextField speciallyRecommendedIdTextField;
  private JTextArea speciallyRecommendedTextArea;

  private JTextField categoryName1TextField;
  private JTextField productIds1TextField;

  private JTextField categoryName2TextField;
  private JTextField productIds2TextField;

  private JTextArea footerTextArea;

  private Configuration config;

  public StartPage() throws ConfigurationException {
    config = new PropertiesConfiguration("annie.properties");
  }

  public void createAndShowGUI(String programNameAndVersion) {
    JFrame frame = new JFrame(programNameAndVersion);
    frame.setBounds(0, 0, 800, 600);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    Container contentPane = frame.getContentPane();
    contentPane.setBounds(0,0,800,600);
    contentPane.add(buildPanel());

    packAndShowOnScreenCenter(frame);
  }

  protected final void packAndShowOnScreenCenter(JFrame frame) {
    frame.pack();
    locateOnOpticalScreenCenter(frame);
    frame.setVisible(true);
  }

  protected final void locateOnOpticalScreenCenter(Component component) {
    Dimension paneSize = component.getSize();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    component.setBounds((screenSize.width - paneSize.width) / 2, (screenSize.height - paneSize.height) / 2, 800, 600);
  }

  private void initComponents() {
    newsletterIdTextField = new JTextField(10);
    newsletterIdTextField.setToolTipText("Identyfikator newslettera do systemu zewnetrznego");

    dateToSendTextField = new JTextField("YYYY-MM-DD");
    dateToSendTextField.setToolTipText("Data planowanego wyslania newslettera");

    speciallyRecommendedTextArea = new JTextArea(8,120);
    speciallyRecommendedTextArea.setToolTipText("Dodatkowa tresc dla produktu szczegolnie polecanego...");

    speciallyRecommendedIdTextField = new JTextField(10);

    categoryName1TextField = new JTextField("Ksiazki");
    productIds1TextField = new JTextField();

    categoryName2TextField = new JTextField("Filmy");
    productIds2TextField = new JTextField();

    footerTextArea = new JTextArea(8,120);
    footerTextArea.setText("powered by foobar");
    footerTextArea.setToolTipText("Stopka newslettera...");
  }

  public JComponent buildPanel() {
    initComponents();

    FormLayout layout = new FormLayout("right:[35dlu,pref], 3dlu, 70dlu, 7dlu, right:[40dlu,pref], 3dlu, 75dlu:grow",
                                       "pref, pref, pref, pref, pref, pref, pref, pref, pref, pref, pref, pref, pref, fill:pref:grow, pref");

    DefaultFormBuilder builder = new DefaultFormBuilder(layout/*, new FormDebugPanel()*/);
    builder.setDefaultDialogBorder();

    builder.appendSeparator("Ustawienia globalne");

    builder.append("Data wysyłki:", dateToSendTextField, false);
    builder.append("Newsletter ID:", newsletterIdTextField, true);
    builder.appendRow(builder.getLineGapSpec());
    builder.nextLine();

    builder.appendSeparator("Szczegolnie polecamy");
    builder.append("ID produktu szczegolnie polecanego:", speciallyRecommendedIdTextField, true);
    builder.append(new JScrollPane(speciallyRecommendedTextArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), 7);
    builder.appendRow(builder.getLineGapSpec());
    builder.nextLine();

    builder.appendSeparator("Kategoria");
    builder.append("Nazwa kategorii:", categoryName1TextField, false);
    builder.append("ID produktów:", productIds1TextField, true);
    builder.appendRow(builder.getLineGapSpec());
    builder.nextLine();

    builder.appendSeparator("Kategoria");
    builder.append("Nazwa kategorii:", categoryName2TextField, false);
    builder.append("ID produktów:", productIds2TextField, true);
    builder.appendRow(builder.getLineGapSpec());
    builder.nextLine();

    builder.appendSeparator("Stopka");
    builder.appendRow(builder.getLineGapSpec());
    builder.append(new JScrollPane(footerTextArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), 7);

    JButton submit = new JButton("Generuj");
    submit.setName("generate");
    submit.addActionListener(this);
    builder.nextLine();
    builder.append(submit, 7);

    return builder.getPanel();
  }

  public void actionPerformed(ActionEvent ae) {

    logger.info("source: " + ae.getSource());

    try {
      ResultPage page = prepareResultPage();
      page.setVisible(true);
    } catch (ConfigurationException e) {
      logger.error(e);
    }

  }

  private ResultPage prepareResultPage() throws ConfigurationException {
    ResultPage resultPage = new ResultPage();
    String solrEndpoint = (String)config.getProperty("solrEndpoint");
    List<Map<String, String>> productParamsList = performProductQuery(speciallyRecommendedIdTextField.getText(), solrEndpoint);
    renderOutput(resultPage, productParamsList);
    return resultPage;
  }

  private List<Map<String, String>> performProductQuery(String input, String solrEndpoint) {
    List<Map<String, String>> productParamsList = new ArrayList<Map<String, String>>();
    try {

      StringTokenizer tokenizer = new StringTokenizer(input, ",");
      while (tokenizer.hasMoreTokens()) {
        String productId = tokenizer.nextToken();
        QueryPerformer qPerformer = new QueryPerformer();
        qPerformer.setEndpoint(solrEndpoint);
        try {
          Map<String, String> productParams = qPerformer.performQuery(productId);
          productParamsList.add(productParams);
        } catch (ResponseExtractorException e) {
          logger.error("Problem with querying for product: " + productId + ": " + e);
        }
      }

    } catch (MalformedURLException e) {
      logger.error(e);
    }
    return productParamsList;
  }

  private void renderOutput(ResultPage resultPage, List<Map<String, String>> productParamsList) {
	  SimpleNewsletterRenderer renderer = new SimpleNewsletterRenderer();
	  String newsletter = renderer.render(productParamsList);

	  JTextArea textArea = new JTextArea(30,70);
	  textArea.setText(newsletter);
	  textArea.setToolTipText("Newsletter");
	  resultPage.getPanel().add(new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
  }

}
