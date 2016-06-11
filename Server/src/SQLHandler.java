import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;

/**
 * Created by FlameXander on 07.06.2016.
 */
public class SQLHandler {
    private static Connection conn;
    private static PreparedStatement stmt;

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:ClientsDB.db");
        } catch (Exception c) {
            System.out.println("Connection Error");
        }
    }

    public static void disconnect() {
        try {
            conn.close();
        } catch (Exception c) {
            System.out.println("Connection Error");
        }
    }

    public static String getNickByLoginPassword(String login, String password) {
        String w = null;
        try {
            stmt = conn.prepareStatement("SELECT Nickname FROM main WHERE Login = ? AND Password = ?;");
            stmt.setString(1, login);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
                w = rs.getString("Nickname");
        } catch (SQLException e) {
            System.out.println("SQL Query Error");
        }
        return w;
    }
}
