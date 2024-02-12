import api.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterContainer extends JPanel {

   private JTextField userNameField = new JTextField();
   private JPasswordField passwordField = new JPasswordField();
   private JPasswordField confirmPasswordField = new JPasswordField();

   RegisterContainer() {
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

      setBackground(Color.WHITE);
      setPreferredSize(new Dimension(400, 500));
      setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
      JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      JLabel registerLabel = new JLabel("Register");
      registerLabel.setForeground(Color.decode("#4467df"));
      registerLabel.setFont(poppinsFontBold.deriveFont(30f));
      registerPanel.add(registerLabel);
      registerPanel.setBackground(Color.WHITE);
      registerPanel.setPreferredSize(new Dimension(400, 50));

      JPanel detailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      detailPanel.setOpaque(false);
      JLabel detailLabel = new JLabel("Please, enter your detail");
      detailLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 12f));
      detailPanel.add(detailLabel);
      detailPanel.setPreferredSize(new Dimension(400, 30));

      JPanel userNamePanel = new JPanel();
      userNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      userNamePanel.setOpaque(false);
      userNamePanel.setPreferredSize(new Dimension(400, 60));
      JLabel userNameLabel = new JLabel("Username");
      userNameLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 14f));
      userNameLabel.setPreferredSize(new Dimension(400, 20));

      userNameField.setFont(poppinsFont.deriveFont(Font.BOLD, 12f));
      userNameField.setPreferredSize(new Dimension(390, 30));
      userNamePanel.add(userNameLabel);
      userNamePanel.add(userNameField);

      JPanel passwordPanel = new JPanel();
      passwordPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      passwordPanel.setOpaque(false);
      passwordPanel.setPreferredSize(new Dimension(400, 62));

      JLabel passwordLabel = new JLabel("Password");
      passwordLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 14f));
      passwordField.setFont(poppinsFont.deriveFont(Font.BOLD, 12f));
      passwordField.setPreferredSize(new Dimension(390, 30));
      passwordPanel.add(passwordLabel);
      passwordPanel.add(passwordField);

      JPanel confirmPasswordPanel = new JPanel();
      confirmPasswordPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      confirmPasswordPanel.setOpaque(false);
      confirmPasswordPanel.setPreferredSize(new Dimension(400, 100));

      JLabel confirmPasswordLabel = new JLabel("Confirm Password");
      confirmPasswordLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 14f));
      confirmPasswordField.setFont(poppinsFont.deriveFont(Font.BOLD, 12f));
      confirmPasswordField.setPreferredSize(new Dimension(390, 30));
      confirmPasswordPanel.add(confirmPasswordLabel);
      confirmPasswordPanel.add(confirmPasswordField);

      JPanel loginButtonPanel = new JPanel();
      loginButtonPanel.setLayout(new FlowLayout());
      loginButtonPanel.setOpaque(false);
      loginButtonPanel.setPreferredSize(new Dimension(400, 50));

      JButton loginButton = new JButton("Register");
      loginButton.setFont(poppinsFont.deriveFont(Font.BOLD, 14f));
      loginButton.setPreferredSize(new Dimension(390, 40));
      loginButton.setBackground(Color.decode("#3465ff"));
      loginButton.setForeground(Color.WHITE);
      loginButtonPanel.add(loginButton);

      loginButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            RegisterBtnActionPerformed(e);
         }
      });

      JPanel accountPanel = new JPanel();
      accountPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
      accountPanel.setOpaque(false);
      accountPanel.setPreferredSize(new Dimension(400, 50));

      JLabel accountLabel = new JLabel("Already have an account?");
      accountLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 13f));
      accountLabel.setForeground(Color.decode("#8c8c8c"));

      JButton registerButton = new JButton("Login here");
      registerButton.setFont(poppinsFont.deriveFont(Font.BOLD, 13f));
      registerButton.setPreferredSize(new Dimension(150, 20));
      registerButton.setOpaque(false);
      registerButton.setContentAreaFilled(false);
      registerButton.setBorderPainted(false);
      registerButton.setForeground(Color.decode("#3465ff"));
      accountPanel.add(accountLabel);
      accountPanel.add(registerButton);

      add(registerPanel);
      add(detailPanel);
      add(userNamePanel);
      add(passwordPanel);
      add(confirmPasswordPanel);
      add(loginButtonPanel);
      add(accountPanel);

      registerButton.addActionListener(e -> {
         removeAll();
         add(new LoginContainer());
         revalidate();
         repaint();
      });

   }

   private void RegisterBtnActionPerformed(java.awt.event.ActionEvent evt) {
      String username, password, query;
      try {
         if ("".equals(userNameField.getText())) {
            JOptionPane.showMessageDialog(new JFrame(), "Username tidak boleh kosong", "Warning",
                  JOptionPane.WARNING_MESSAGE);
         } else if ("".equals(new String(passwordField.getPassword()))) {
            JOptionPane.showMessageDialog(new JFrame(), "Password tidak boleh kosong", "Error",
                  JOptionPane.WARNING_MESSAGE);
         } else if ("".equals(new String(confirmPasswordField.getPassword()))) {
            JOptionPane.showMessageDialog(new JFrame(), "Confirm Password tidak boleh kosong", "Warning",
                  JOptionPane.WARNING_MESSAGE);
         } else if (!new String(passwordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))) {
            JOptionPane.showMessageDialog(new JFrame(), "Password tidak sama", "Warning", JOptionPane.WARNING_MESSAGE);
         } else {
            username = userNameField.getText();
            password = new String(passwordField.getPassword());

            query = "SELECT * FROM user WHERE username = ?";
            PreparedStatement pst = Database.database.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
               JOptionPane.showMessageDialog(new JFrame(), "Username sudah ada", "Warning",
                     JOptionPane.WARNING_MESSAGE);
            } else {
               query = "INSERT INTO user (username, password, jmlh_attempt) VALUES (?, ?, ?)";
               pst = Database.database.prepareStatement(query);
               pst.setString(1, username);
               pst.setString(2, password);
               pst.setInt(3, 0);
               pst.executeUpdate();
               JOptionPane.showMessageDialog(new JFrame(), "Register Berhasil", "Success",
                     JOptionPane.INFORMATION_MESSAGE);
            }
         }
      } catch (Exception e) {
         System.out.println("Error" + e.getMessage());
      }
   }

}