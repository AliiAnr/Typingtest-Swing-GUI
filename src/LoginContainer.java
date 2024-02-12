import api.Database;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class LoginContainer extends JPanel {

   JTextField userNameField = new JTextField();
   JPasswordField passwordField = new JPasswordField();

   LoginContainer() {
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
      JLabel registerLabel = new JLabel("Login");
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
      passwordPanel.setPreferredSize(new Dimension(400, 100));

      JLabel passwordLabel = new JLabel("Password");
      passwordLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 14f));
      passwordField.setFont(poppinsFont.deriveFont(Font.BOLD, 12f));
      passwordField.setPreferredSize(new Dimension(390, 30));
      passwordPanel.add(passwordLabel);
      passwordPanel.add(passwordField);

      JPanel loginButtonPanel = new JPanel();
      loginButtonPanel.setLayout(new FlowLayout());
      loginButtonPanel.setOpaque(false);
      loginButtonPanel.setPreferredSize(new Dimension(400, 50));

      JButton loginButton = new JButton("Login");
      loginButton.setFont(poppinsFont.deriveFont(Font.BOLD, 14f));
      loginButton.setPreferredSize(new Dimension(390, 40));
      loginButton.setBackground(Color.decode("#3465ff"));
      loginButton.setForeground(Color.WHITE);
      loginButtonPanel.add(loginButton);

      loginButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            LoginBtnActionPerformed(e);
         }
      });

      JPanel accountPanel = new JPanel();
      accountPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
      accountPanel.setOpaque(false);
      accountPanel.setPreferredSize(new Dimension(400, 50));

      JLabel accountLabel = new JLabel("Not have an account?");
      accountLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 13f));
      accountLabel.setForeground(Color.decode("#8c8c8c"));

      JButton registerButton = new JButton("Register Here");
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
      add(loginButtonPanel);
      add(accountPanel);

      registerButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            removeAll();
            add(new RegisterContainer());
            revalidate();
            repaint();
         }
      });
   }

   private void LoginBtnActionPerformed(java.awt.event.ActionEvent evt) {
      String username, passwordDB, query, passDB = null;

      int notFound = 0;

      try {
         if ("".equals(userNameField.getText())) {
            JOptionPane.showMessageDialog(new JFrame(), "Username tidak boleh kosong", "Error",
                  JOptionPane.WARNING_MESSAGE);
         } else if ("".equals(new String(passwordField.getPassword()))) {
            JOptionPane.showMessageDialog(new JFrame(), "Password tidak boleh kosong", "Error",
                  JOptionPane.WARNING_MESSAGE);
         } else {
            username = userNameField.getText();
            passwordDB = new String(passwordField.getPassword());

            query = "SELECT * FROM user WHERE username = ? AND password = ?";
            PreparedStatement pst = Database.database.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, passwordDB);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
               passDB = rs.getString("password");
               notFound = 1;
            }
            if (notFound == 1 && Objects.equals(passwordDB, passDB)) {
               JOptionPane.showMessageDialog(new JFrame(), "Login Berhasil", "Success",
                     JOptionPane.INFORMATION_MESSAGE);
               new TestContainer(username);
               SwingUtilities.getWindowAncestor(LoginContainer.this).dispose();
            } else {
               JOptionPane.showMessageDialog(new JFrame(), "Username atau Password salah", "Error",
                     JOptionPane.ERROR_MESSAGE);
            }
         }

      } catch (Exception e) {
         System.out.println("Error" + e.getMessage());
      }
   }

}
