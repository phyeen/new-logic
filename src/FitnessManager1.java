import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.mysql.cj.jdbc.DatabaseMetaData;

public class FitnessManager1 extends JFrame {
    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final String serverAddress = "localhost"; // String for saving the name of the database

        final int[] serverPorts = { 3306, 88 };
        for (int port : serverPorts) {
            try {
                Socket socket = new Socket();
                InetSocketAddress serverSocketAddress = new InetSocketAddress(serverAddress, port);
                int timeout = 5000; // 5 seconds to check availability
                socket.connect(serverSocketAddress, timeout);
                if (port == 3306) {
                    JOptionPane.showMessageDialog(null,
                            "XAMPP MySQL Server is ONLINE!! @ port " + port + ".",
                            "XAMPP MySQL Server", JOptionPane.INFORMATION_MESSAGE);
                } else if (port == 88) {
                    JOptionPane.showMessageDialog(null,
                            "XAMPP Apache Server is ONLINE!! @ port " + port + ".",
                            "XAMPP Apache Server", JOptionPane.INFORMATION_MESSAGE);
                    validating_database_status();
                }
                socket.close();
            } catch (Exception e) {
                if (port == 3306) {
                    JOptionPane.showMessageDialog(null,
                            "PLEASE CHECK YOUR SERVER CONNECTION: SERVER XAMPP MySQL FAILED",
                            "XAMPP MySQL Server", JOptionPane.ERROR_MESSAGE);
                } else if (port == 88) {
                    JOptionPane.showMessageDialog(null,
                            "PLEASE CHECK YOUR SERVER CONNECTION: SERVER XAMPP Apache FAILED",
                            "XAMPP Apache Server", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "PLEASE CHECK YOUR SERVER CONNECTION",
                            "Server Connection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    protected static void validating_database_status() {
        try {
            int port = 3306;
            final String database = "fitnessManager";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:" + port, "root", "");
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?");
            statement.setString(1, database);
            ResultSet resultSet = statement.executeQuery();
            boolean databaseExists = resultSet.next();
            if (databaseExists) {
                JOptionPane.showMessageDialog(null,
                        "Database " + database + " exists on the XAMPP MySQL Server running on port: " + port + ".",
                        "XAMPP MySQL Server", JOptionPane.INFORMATION_MESSAGE);
                availability_of_login_table();
            } else {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "");
                PreparedStatement statement1 = connection1.prepareStatement("CREATE DATABASE " + database);
                statement1.executeUpdate();
                JOptionPane.showMessageDialog(null, "Database " + database + " created successfully.",
                        "XAMPP MySQL Server", JOptionPane.INFORMATION_MESSAGE);
                availability_of_login_table();
                connection1.close();
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    protected static void availability_of_login_table() {
        final String table_name = "login_credentials_table";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fitnessManager", "root",
                    "");
            DatabaseMetaData metaData = (DatabaseMetaData) connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, table_name, null);
            boolean tableExists = resultSet.next();
            if (tableExists) {
                JOptionPane.showMessageDialog(null, "Table " + table_name + " exists in the fitnessManager"
                        + " database on the XAMPP MySQL Server running on port: 3306.", "XAMPP MySQL Server",
                        JOptionPane.INFORMATION_MESSAGE);
                availability_of_employee_table();
            } else {
                login_table_creation();
            }
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    protected static void login_table_creation() {
        final String table_name = "login_credentials_table";
        final String col1 = "username";
        final String col2 = "password";
        final String col3 = "role";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fitnessManager", "root",
                    "");
            PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE " + table_name + " (" + col1 + " VARCHAR(255), " + col2 + " VARCHAR(255), " + col3
                            + " VARCHAR(255)) ");
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Table " + table_name + " created successfully.", "XAMPP MySQL Server",
                    JOptionPane.INFORMATION_MESSAGE);
            statement.close();
            connection.close();
            insertion_login_table_creation();
        } catch (SQLException e) {
            System.out.println("An error occurred while creating the login_credentials_table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected static void insertion_login_table_creation() {
        final String table_name = "login_credentials_table";
        final String parameter1 = "F_admin123";
        final String parameter2 = "F_pass_code";
        final String parameter3 = "Admin";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fitnessManager", "root",
                    "");
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO " + table_name + " (username, password, role) VALUES (?, ?, ?)");
            statement.setString(1, parameter1);
            statement.setString(2, parameter2);
            statement.setString(3, parameter3);
            int i = statement.executeUpdate();
            if (i == 1) {
                JOptionPane.showMessageDialog(null, "Table " + table_name + " , Insertion successfully.",
                        "XAMPP MySQL Server",
                        JOptionPane.INFORMATION_MESSAGE);
                availability_of_employee_table();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Insertion Of Credentials WAS NOT SUCCESSFUL",
                        "login_credentials_table ERROR", JOptionPane.ERROR_MESSAGE);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("An error occurred while creating the login_credentials_table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected static void availability_of_employee_table() {
        final String table_name = "employee_credentials_table";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fitnessManager", "root",
                    "");
            DatabaseMetaData metaData = (DatabaseMetaData) connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, table_name, null);
            boolean tableExists = resultSet.next();
            if (tableExists) {
                JOptionPane.showMessageDialog(null, "Table " + table_name + " exists in the fitnessManager"
                        + " database on the XAMPP MySQL Server running on port: 3306.", "XAMPP MySQL Server",
                        JOptionPane.INFORMATION_MESSAGE);
                new Home().setVisible(true);
            } else {
                employee_creation();
            }
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    protected static void employee_creation() {
        final String table_name = "employee_credentials_table";
        final String col1 = "Name";
        final String col2 = "Age";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fitnessManager", "root",
                    "");
            PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE " + table_name + " (" + col1 + " VARCHAR(255), " + col2 + " INT) ");
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Table " + table_name + " created successfully.", "XAMPP MySQL Server",
                    JOptionPane.INFORMATION_MESSAGE);
            statement.close();
            connection.close();
            insertion_employee_credentials_table();
        } catch (SQLException e) {
            System.out.println("An error occurred while creating the employee_credentials_table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected static void insertion_employee_credentials_table() {
        final String table_name = "employee_credentials_table";
        String[] names = { "Jacob", "Amanda", "John" };
        int[] ages = { 27, 31, 22 };
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fitnessManager", "root",
                    "");
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO " + table_name + " (Name, Age) VALUES (?, ?)");

            for (int ii = 0; ii < names.length; ii++) {
                statement.setString(1, names[ii]);
                statement.setInt(2, ages[ii]);
                int result = statement.executeUpdate();
                if (result == 1) {
                    JOptionPane.showMessageDialog(null,
                            "Assistant " + names[ii] + " With Age " + ages[ii] + " , Insertion successfully.",
                            "XAMPP MySQL Server",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Insertion Of " + names[ii] + " WAS NOT SUCCESSFUL",
                            "employee_credentials_table ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
            new Home().setVisible(true);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("An error occurred while creating the employee_credentials_table: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
