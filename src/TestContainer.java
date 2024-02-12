import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentListener;

import api.Database;

import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

public class TestContainer extends JFrame {
   private String username = "";
   private Timer timer = new Timer(1);
   private JPanel timerPanel;
   private javax.swing.Timer swingTimer;
   private Thread timerThread;
   private JTextField typeField;
   private JPanel typePanel;
   private boolean isTimerRunning = false;
   private CsvReader csvReader = new CsvReader();
   private List<String> words = new ArrayList<>();
   private List<JLabel> wordLabels = new ArrayList<>();
   private int correctWord = 0;
   private int currentWord = 0;
   private int temp = 0;
   private int wpm = 0;
   private JLabel wpmPanel;
   private int space = 0;

   TestContainer(String username) {
      super("Let's Type");
      this.username = username;
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
      panelContainer.setBackground(Color.WHITE);
      panelContainer.setPreferredSize(new Dimension(948, 400));
      panel.setLayout(new GridBagLayout());
      panel.setBackground(Color.WHITE);
      add(panel);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(1000, 450);
      setLocationRelativeTo(null);

      JPanel headerPanel = new JPanel();
      JPanel headerContainer = new JPanel();
      headerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
      JLabel leaderBoardLabel = new JLabel("Leaderboard");
      leaderBoardLabel.setForeground(Color.decode("#4467df"));
      leaderBoardLabel.setFont(poppinsFontBold.deriveFont(18f));
      leaderBoardLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
      JLabel profileLabel = new JLabel("Profile");
      profileLabel.setForeground(Color.decode("#4467df"));
      profileLabel.setFont(poppinsFontBold.deriveFont(18f));
      
      leaderBoardLabel.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(java.awt.event.MouseEvent e) {
            new LeaderBoard();
         }
      });
      
      profileLabel.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(java.awt.event.MouseEvent e) {
            new ProfileContainer(username);
         }
      });
      
      headerContainer.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
      headerContainer.setOpaque(false);

      headerContainer.add(leaderBoardLabel);
      headerContainer.add(profileLabel);
      headerPanel.add(headerContainer);
      headerPanel.setBackground(Color.WHITE);
      headerPanel.setPreferredSize(new Dimension(940, 90));

      timerPanel = new JPanel();
      timerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
      timerPanel.setBackground(Color.WHITE);
      timerPanel.setPreferredSize(new Dimension(940, 27));

      wpmPanel = new JLabel(wpm + " WPM");
      wpmPanel.setFont(poppinsFontBold.deriveFont(18f));
      wpmPanel.setForeground(Color.decode("#4467df"));
      wpmPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

      swingTimer = new javax.swing.Timer(1000, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (typeField.isEditable()) {
               updateGameState();
            }
         }
      });
      swingTimer.start();

      typePanel = new JPanel();
      this.csvReader.readCSV("src\\data\\aliganteng.csv");
      typePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
      typePanel.setBackground(Color.WHITE);
      typePanel.setPreferredSize(new Dimension(940, 185));

      randomWord();

      this.words = csvReader.getRecords().subList(0, 20);
      for (String word : words) {
         JLabel wordLabel = new JLabel(
               "<html><span style='color:black; font-family:Comic Sans MS; font-size:25px; font-weight: normal;'>"
                     + word + "</span></html>");
         wordLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 10));
         wordLabel.setBackground(Color.decode("#dbd8d2"));
         if (temp == 0) {
            wordLabel.setOpaque(true);
         } else {
            wordLabel.setOpaque(false);
         }
         wordLabels.add(wordLabel);
         typePanel.add(wordLabels.get(temp));
         temp++;
      }

      JPanel typeContainer = new JPanel();
      typeContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
      typeContainer.setBackground(Color.WHITE);
      typeContainer.setPreferredSize(new Dimension(940, 62));
      typeContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

      JLabel restartLabel = new JLabel("Restart");
      restartLabel.setFont(poppinsFontBold.deriveFont(18f));
      restartLabel.setForeground(Color.decode("#4467df"));
      restartLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
      restartLabel.addMouseListener((MouseListener) new MouseAdapter() {
         @Override
         public void mouseClicked(java.awt.event.MouseEvent e) {
            restartGame();
         }
      });

      typeField = new JTextField();
      Border border = BorderFactory.createLineBorder(Color.decode("#4467df"), 2, true);
      typeField.setPreferredSize(new Dimension(810, 50));
      typeField.setFont(poppinsFont.deriveFont(18f));
      typeField.setBorder(border);

      typeField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            updateLabel();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            updateLabel();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            updateLabel();
         }

         private void updateLabel() {
            String userInput = typeField.getText();
            StringBuilder sb = new StringBuilder("<html>");

            for (int i = 0; i < words.get(currentWord).length(); i++) {
               if (i < userInput.length() && words.get(currentWord).charAt(i) == userInput.charAt(i)) {
                  sb.append(
                        "<span style='color:green; font-family:Comic Sans MS; font-size:25px; font-weight: normal;'>")
                        .append(words.get(currentWord).charAt(i)).append("</span>");
               } else if (i < userInput.length()) {
                  sb.append(
                        "<span style='color:red; font-family:Comic Sans MS; font-size:25px; font-weight: normal;'>")
                        .append(words.get(currentWord).charAt(i)).append("</span>");
               } else {
                  sb.append(
                        "<span style='color:black; font-family:Comic Sans MS; font-size:25px; font-weight: normal;'>")
                        .append(words.get(currentWord).charAt(i)).append("</span>");
               }
            }

            sb.append("</html>");
            wordLabels.get(currentWord).setText(sb.toString());
         }
      });

      typeField.addKeyListener(new KeyAdapter() {
         public void keyTyped(KeyEvent e) {
            if (!isTimerRunning) {
               swingTimer.start();
               timer.start();
               timerThread = new Thread(new Runnable() {
                  @Override
                  public void run() {
                     try {
                        for (int i = 0; i < 60; i++) {
                           Thread.sleep(1000);
                           wpm = countWPM(correctWord, i);
                           SwingUtilities.invokeLater(new Runnable() {
                              @Override
                              public void run() {
                                 if (wpm >= 0) {
                                    wpmPanel.setText(wpm + " WPM");
                                 }
                              }
                           });
                        }
                     } catch (InterruptedException e) {
                     }
                  }
               });
               timerThread.start();
               isTimerRunning = true;
            }
         }
      });

      typeField.addKeyListener(new KeyAdapter() {
         @Override
         public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
               String userInput = typeField.getText().trim(); 
               if (userInput.equals(words.get(currentWord))) {
                  correctWord++;
               }

               String incompleteWord = wordLabels.get(currentWord).getText();
               incompleteWord = incompleteWord.replace("color:black", "color:red");
               wordLabels.get(currentWord).setText(incompleteWord);

               wordLabels.get(currentWord).setOpaque(false);
               wordLabels.get(currentWord).setBackground(null);
               currentWord++;
               if (currentWord < wordLabels.size()) {
                  wordLabels.get(currentWord).setOpaque(true); 
               } 
               
               if (currentWord == words.size()) {
                  randomWord();
                  words = csvReader.getRecords().subList(0, 20);
                  for (int i = 0; i < 20; i++) {
                     wordLabels.get(i).setText(
                           "<html><span style='color:black; font-family:Comic Sans MS; font-size:25px; font-weight: normal;'>"
                                 + words.get(i) + "</span></html>");
                     wordLabels.get(i).setBackground(Color.decode("#dbd8d2"));
                     wordLabels.get(i).setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 10));

                     if (i == 0) {
                        wordLabels.get(i).setOpaque(true);
                     } else {
                        wordLabels.get(i).setOpaque(false); 
                     }
                  }
                  currentWord = 0;
               }

               typeField.setText("");
            }
         }
      });

      typeContainer.add(typeField);
      typeContainer.add(restartLabel);

      timerPanel.add(timer.getLabel());
      timerPanel.add(wpmPanel);
      panelContainer.add(headerPanel);
      panelContainer.add(timerPanel);
      panelContainer.add(typePanel);
      panelContainer.add(typeContainer);

      typePanel.revalidate();
      typePanel.repaint();
      panel.add(panelContainer);

      setVisible(true);
   }

   public void updateGameState() {
      if (timer.hasExpired()) {
         updateScoreAndAttempt(this.username, this.wpm);
         WpmResult wpmResult = new WpmResult(this.username, wpm);
         typeField.setText("");
         for (JLabel label : wordLabels) {
            label.setText("");
         }
         
         correctWord = 0;
         currentWord = 0;
         temp = 0;
         wpm = 0;
         swingTimer.stop();
         timer.stopTimer();
         isTimerRunning = false;
         timerThread.interrupt();
         typeField.setEditable(false);
      }
   }

   public void restartGame() {
      wpm = 0;
      correctWord = 0;
      currentWord = 0;
      temp = 0;
      typeField.setText("");
      for (JLabel label : wordLabels) {
         label.setText("");
      }
      randomWord();
      this.words = csvReader.getRecords().subList(0, 20);
      for (int i = 0; i < 20; i++) {
         wordLabels.get(i).setText(
               "<html><span style='color:black; font-family:Comic Sans MS; font-size:25px; font-weight: normal;'>"
                     + words.get(i) + "</span></html>");
         wordLabels.get(i).setBackground(Color.decode("#dbd8d2"));
         wordLabels.get(i).setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 10));
         if (i == 0) {
            wordLabels.get(i).setOpaque(true); 
         } else {
            wordLabels.get(i).setOpaque(false); 
         }
      }

      timer.restart();
      isTimerRunning = false;
      swingTimer.stop();

      timerThread.interrupt();
      typeField.setEditable(true);

      timerPanel.removeAll();
      timerPanel.add(timer.getLabel());
      timerPanel.add(wpmPanel);

      timerPanel.revalidate();
      timerPanel.repaint();

   }

   public void randomWord() {
      Collections.shuffle(csvReader.getRecords());
      this.words = csvReader.getRecords();
   }

   public int countWPM(int correectWords, int seconds) {
      return (int) Math.round((double) correectWords / (seconds / 60.0));
   }

   public void updateScoreAndAttempt(String username, int wpm) {
      String query;
      int jmlh_attempt = 0;
      try {
         query = "SELECT id_user, jmlh_attempt FROM user WHERE username = ?";
         PreparedStatement pst = Database.database.prepareStatement(query);
         pst.setString(1, username);
         ResultSet rs = pst.executeQuery();
         if (rs.next()) {
            int id_user = rs.getInt("id_user");
            jmlh_attempt = rs.getInt("jmlh_attempt");

            query = "UPDATE user SET jmlh_attempt = ? WHERE username = ?";
            pst = Database.database.prepareStatement(query);
            pst.setInt(1, jmlh_attempt + 1);
            pst.setString(2, username);
            pst.executeUpdate();

            query = "INSERT INTO score (id_user, no_attempt, score) VALUES (?, ?, ?)";
            pst = Database.database.prepareStatement(query);
            pst.setInt(1, id_user);
            pst.setInt(2, jmlh_attempt + 1);
            pst.setInt(3, wpm);
            pst.executeUpdate();
         }

      } catch (Exception e) {
         System.out.println("Error" + e.getMessage());
      }
   }
}
