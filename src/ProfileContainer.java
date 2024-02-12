import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

import api.Database;

public class ProfileContainer extends JFrame {
   private String username;
   private int jmlh_attempt;
   private int score;
   private int high_score;
   private int last_score;
   private int no_attempt_high_score;

   ProfileContainer(String username) {
      super("Profile");
      this.username = username;
      
      getUserStats(username);
      getAttemptWithHighScore(username);

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
      
      JPanel profilePanel = new JPanel();
      profilePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
      profilePanel.setBackground(Color.BLACK);
      profilePanel.setPreferredSize(new Dimension(350, 50));
      JLabel congratulationsLabel = new JLabel("PROFILE");
      congratulationsLabel.setFont(poppinsFontBold.deriveFont(30f));
      congratulationsLabel.setForeground(Color.WHITE);
      congratulationsLabel.setPreferredSize(new Dimension(350, 50));
      congratulationsLabel.setHorizontalAlignment(JLabel.CENTER);
      congratulationsLabel.setBackground(Color.RED);
      profilePanel.add(congratulationsLabel);
      panelContainer.add(profilePanel);

      JPanel usernamePanel = new JPanel();
      usernamePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
      usernamePanel.setBackground(Color.decode("#4467df"));
      usernamePanel.setPreferredSize(new Dimension(350, 40));
      JLabel usernameLabel = new JLabel("Name : " + username);
      usernameLabel.setFont(poppinsFontBold.deriveFont(20f));
      usernameLabel.setForeground(Color.WHITE);
      usernameLabel.setPreferredSize(new Dimension(350, 50));
      usernameLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
      usernamePanel.add(usernameLabel);
      panelContainer.add(usernamePanel);

      JPanel wpmPanel = new JPanel();
      wpmPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
      wpmPanel.setBackground(Color.decode("#4467df"));
      wpmPanel.setPreferredSize(new Dimension(350, 40));
      JLabel yourWpmLabel = new JLabel("HighScore : " + high_score + " WPM on attempt " + no_attempt_high_score);
      yourWpmLabel.setFont(poppinsFontBold.deriveFont(20f));
      yourWpmLabel.setForeground(Color.WHITE);
      yourWpmLabel.setPreferredSize(new Dimension(350, 50));
      yourWpmLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
      wpmPanel.add(yourWpmLabel);
      panelContainer.add(wpmPanel);

      JPanel attemptPanel = new JPanel();
      attemptPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
      attemptPanel.setBackground(Color.decode("#4467df"));
      attemptPanel.setPreferredSize(new Dimension(350, 40));
      JLabel wpmLabel = new JLabel("Last Score : " + last_score + " WPM");
      wpmLabel.setFont(poppinsFontBold.deriveFont(20f));
      wpmLabel.setForeground(Color.WHITE);
      wpmLabel.setPreferredSize(new Dimension(350, 50));
      wpmLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
      attemptPanel.add(wpmLabel);
      panelContainer.add(attemptPanel);
      
      JPanel lastAttemptPanel = new JPanel();
      lastAttemptPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
      lastAttemptPanel.setBackground(Color.decode("#4467df"));
      lastAttemptPanel.setPreferredSize(new Dimension(350, 40));
      JLabel lastAttemptLabel = new JLabel("Total attempts : " + jmlh_attempt);
      lastAttemptLabel.setFont(poppinsFontBold.deriveFont(20f));
      lastAttemptLabel.setForeground(Color.WHITE);
      lastAttemptLabel.setPreferredSize(new Dimension(350, 50));
      lastAttemptLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
      lastAttemptPanel.add(lastAttemptLabel);
      panelContainer.add(lastAttemptPanel);

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

   public void getUserStats(String username) {
      String query;
      try {
         query = "SELECT id_user, jmlh_attempt FROM user WHERE username = ?";
         PreparedStatement pst = Database.database.prepareStatement(query);
         pst.setString(1, username);
         
         ResultSet rs = pst.executeQuery();
         if (rs.next()) {
            int id_user = rs.getInt("id_user");
            jmlh_attempt = rs.getInt("jmlh_attempt");

            query = "SELECT score FROM score WHERE id_user = ?";
            pst = Database.database.prepareStatement(query);
            pst.setInt(1, id_user);
            rs = pst.executeQuery();
            if (rs.next()) {
               score = rs.getInt("score");

               query = "SELECT MAX(score) as high_score FROM score WHERE id_user = ?";
               pst = Database.database.prepareStatement(query);
               pst.setInt(1, id_user);
               rs = pst.executeQuery();
               if (rs.next()) {
                  high_score = rs.getInt("high_score");

                  query = "SELECT score FROM score WHERE id_user = ? ORDER BY no_attempt DESC LIMIT 1";
                  pst = Database.database.prepareStatement(query);
                  pst.setInt(1, id_user);
                  rs = pst.executeQuery();
                  if (rs.next()) {
                     last_score = rs.getInt("score");
                  }
               }
            }
         }

      } catch (Exception e) {
         System.out.println("Error" + e.getMessage());
      }
   }

   public void getAttemptWithHighScore(String username) {
      String query;
      try {
         query = "SELECT id_user FROM user WHERE username = ?";
         PreparedStatement pst = Database.database.prepareStatement(query);
         pst.setString(1, username);
         ResultSet rs = pst.executeQuery();
         if (rs.next()) {
            int id_user = rs.getInt("id_user");

            query = "SELECT MAX(score) as high_score FROM score WHERE id_user = ?";
            pst = Database.database.prepareStatement(query);
            pst.setInt(1, id_user);
            rs = pst.executeQuery();
            if (rs.next()) {
               int high_scoree = rs.getInt("high_score");

               query = "SELECT no_attempt FROM score WHERE id_user = ? AND score = ?";
               pst = Database.database.prepareStatement(query);
               pst.setInt(1, id_user);
               pst.setInt(2, high_scoree);
               rs = pst.executeQuery();
               if (rs.next()) {
                  no_attempt_high_score = rs.getInt("no_attempt");
               }
            }
         }

      } catch (Exception e) {
         System.out.println("Error" + e.getMessage());
      }
   }

}
