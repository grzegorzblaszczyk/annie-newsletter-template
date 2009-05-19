package gbc.annie;

import gbc.annie.ui.StartPage;

import org.apache.log4j.Logger;

public class Main {

  public static final Logger logger = Logger.getLogger(Main.class);

  private static final String PROGRAM_NAME = "Annie";
  private static final String VERSION = "0.1";

  /**
   *
   */
  private static final long serialVersionUID = 4884136881615528439L;

  /**
   * @param args
   */
  public static void main(String[] args) {
      StartPage startPage = new StartPage();
      startPage.createAndShowGUI(PROGRAM_NAME + " " + VERSION);
  }

}
