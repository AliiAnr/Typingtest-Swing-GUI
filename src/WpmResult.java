import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;


public class WpmResult extends JFrame {
   private String username;
   private int wpm;

   WpmResult(String username, int wpm) {
      super("WPM Result");
      this.username = username;
      this.wpm = wpm;


      Font poppinsFont = null;
      Font poppinsFontBold = null;
      try {
         poppinsFont = Font.createFont(Font.TRUETYPE_FONT,
               new File("src\\font\\Poppins-Regular.ttf"));
         poppinsFontBold = Font.createFont(Font.TRUETYPE_FONT,
               new File("src\\font\\Poppins-Bold.ttf"));
      } catch (FontFormatException | IOException e) {
         e.printStackTrace();
      }

      JPanel panel = new JPanel();
      
      JPanel panelContainer = new JPanel();
      panelContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
      panelContainer.setBackground(Color.decode("#4467df"));
      panelContainer.setPreferredSize(new Dimension(350, 300));
      JLabel congratulationsLabel = new JLabel("Congratulations !!!");
      congratulationsLabel.setFont(poppinsFontBold.deriveFont(30f));
      congratulationsLabel.setForeground(Color.WHITE);
      congratulationsLabel.setPreferredSize(new Dimension(350, 50));
      congratulationsLabel.setHorizontalAlignment(JLabel.CENTER);
      panelContainer.add(congratulationsLabel);
      
      JLabel usernameLabel = new JLabel(username);
      usernameLabel.setFont(poppinsFontBold.deriveFont(30f));
      usernameLabel.setForeground(Color.WHITE);
      usernameLabel.setPreferredSize(new Dimension(350, 50));
      usernameLabel.setHorizontalAlignment(JLabel.CENTER);
      panelContainer.add(usernameLabel);
      
      JLabel yourWpmLabel = new JLabel("You Reach");
      yourWpmLabel.setFont(poppinsFontBold.deriveFont(20f));
      yourWpmLabel.setForeground(Color.WHITE);
      yourWpmLabel.setPreferredSize(new Dimension(350, 50));
      yourWpmLabel.setHorizontalAlignment(JLabel.CENTER);
      panelContainer.add(yourWpmLabel);
      
      JLabel wpmLabel = new JLabel(String.valueOf(wpm) + " Wpm");
      wpmLabel.setFont(poppinsFontBold.deriveFont(50f));
      wpmLabel.setForeground(Color.WHITE);
      wpmLabel.setPreferredSize(new Dimension(350, 50));
      wpmLabel.setHorizontalAlignment(JLabel.CENTER);
      panelContainer.add(wpmLabel);
      
      panel.setLayout(new GridBagLayout());
      panel.setBackground(Color.WHITE);
      panel.setPreferredSize(new Dimension(400, 400));
      panel.add(panelContainer);
      panel.setBackground(Color.decode("#4467df"));
      
      add(panel);
      this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setSize(400, 400);
      setLocationRelativeTo(null);
      setVisible(true);

   }

}
