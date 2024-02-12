import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;

import api.Database;

public class LeaderBoard extends JFrame {

   LeaderBoard() {

      super("Leaderboard");

      List<UserScore> leaderBoard = getLeaderboard();

      leaderBoard.sort(new Comparator<UserScore>() {
         @Override
         public int compare(UserScore u1, UserScore u2) {
            return Integer.compare(u2.getHighScore(), u1.getHighScore());
         }
      });

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
      
      JScrollPane scrollPane = new JScrollPane(panelContainer);
      scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setBounds(50, 30, 300, 50);

      JPanel profilePanel = new JPanel();
      profilePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
      profilePanel.setBackground(Color.BLACK);
      profilePanel.setPreferredSize(new Dimension(350, 50));
      profilePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));
      JLabel congratulationsLabel = new JLabel("LEADERBOARD");
      congratulationsLabel.setFont(poppinsFontBold.deriveFont(30f));
      congratulationsLabel.setForeground(Color.WHITE);
      congratulationsLabel.setPreferredSize(new Dimension(350, 50));
      congratulationsLabel.setHorizontalAlignment(JLabel.CENTER);
      congratulationsLabel.setBackground(Color.RED);
      profilePanel.add(congratulationsLabel);
      panelContainer.add(profilePanel);

      for (int i = 0; i < leaderBoard.size(); i++) {
         JPanel userBoard = new JPanel();
         userBoard.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
         userBoard.setBackground(Color.decode("#4467df"));
         userBoard.setPreferredSize(new Dimension(350, 40));
         JLabel userLabel = new JLabel(
               (i + 1) + ". " + leaderBoard.get(i).getUsername() + " " + leaderBoard.get(i).getHighScore() + " WPM");
         userLabel.setFont(poppinsFontBold.deriveFont(20f));
         userLabel.setForeground(Color.WHITE);
         userLabel.setPreferredSize(new Dimension(350, 50));
         userLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
         userBoard.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
         userBoard.add(userLabel);
         panelContainer.add(userBoard);
      }

      panel.setLayout(new GridBagLayout());
      panel.setBackground(Color.WHITE);
      panel.setPreferredSize(new Dimension(400, 400));
      panel.add(scrollPane);
      panel.add(panelContainer);
      panel.setBackground(Color.decode("#4467df"));

      add(panel);
      this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setSize(400, 400);
      setLocationRelativeTo(null);
      setVisible(true);

   }

   public List<UserScore> getLeaderboard() {
      List<UserScore> leaderboard = new ArrayList<>();
      String query;
      try {
         query = "SELECT username, id_user FROM user";
         PreparedStatement pst = Database.database.prepareStatement(query);
         ResultSet rs = pst.executeQuery();
         while (rs.next()) {
            String username = rs.getString("username");
            int id_user = rs.getInt("id_user");

            query = "SELECT MAX(score) as high_score FROM score WHERE id_user = ?";
            pst = Database.database.prepareStatement(query);
            pst.setInt(1, id_user);
            ResultSet rs2 = pst.executeQuery();
            if (rs2.next()) {
               int high_score = rs2.getInt("high_score");
               leaderboard.add(new UserScore(username, high_score));
            }
         }

      } catch (Exception e) {
         System.out.println("Error" + e.getMessage());
      }
      return leaderboard;
   }

}
