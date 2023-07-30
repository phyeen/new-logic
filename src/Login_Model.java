import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Login_Model extends JFrame {

    /**
     * @param username
     * @param password
     */
    protected void Login_Model1(String username, String password) {
        // Constructor for the model. This is a private constructor to prevent
        // instantiation of this object from outside
        if (username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter Username", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Enter Password", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection obj = DriverManager.getConnection("jdbc:mysql://localhost:3306/fitnessManager", "root", "");
                PreparedStatement stmt = obj.prepareStatement(
                        "SELECT `username`, `password`, `role` FROM `login_credentials_table` WHERE password = ?");
                stmt.setString(1, password);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String dataUsername = rs.getString("username");
                    String dataPassword = rs.getString("password");
                    String dataRole = rs.getString("role");

                    if (username.equals(dataUsername)
                            && (password.equals(dataPassword) && (dataRole.equals("Admin")))) {
                        System.out.println("Login Successful!");
                        JOptionPane.showMessageDialog(null, "Welcome ," + dataRole + " Login Success", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        new ManagerPortal().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Bad Credential, Please Input Right Credentials",
                                "login_credentials ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    System.out.println("ERROR");
                }
            } catch (Exception e) {
                System.out.println(e);
                // TODO: handle exception
            }
        }
    }

}
