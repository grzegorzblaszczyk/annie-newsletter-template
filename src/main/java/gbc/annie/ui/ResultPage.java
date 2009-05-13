package gbc.annie.ui;

import java.awt.Dimension;
import java.awt.Panel;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class ResultPage extends JFrame {

  private static final int RESULT_WINDOW_WIDTH = 800;
  private static final int RESULT_WINDOW_HEIGHT = 600;

  /**
   *
   */
  private static final long serialVersionUID = 6038866453844420864L;
  private Panel panel;

  public ResultPage() {
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setupWindowName();
    setupMainWindowLocationOnScreen();
    panel = new Panel();
    add(panel);
  }

  private void setupMainWindowLocationOnScreen() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(screenSize.width / 2 - RESULT_WINDOW_WIDTH / 2 + 100, screenSize.height / 2 - RESULT_WINDOW_HEIGHT / 2  + 100);
    setVisible(true);
    setSize(800, 600);
  }

  private void setupWindowName() {
    this.setTitle("Wyniki");
  }

  public void setPanel(Panel panel) {
    this.panel = panel;
  }

  public Panel getPanel() {
    return panel;
  }

}
