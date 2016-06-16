import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setBounds(500, 500, 100, 100);
        jf.setTitle("Server");
        jf.setVisible(true);
        SQLHandler.connect();
        MyServer w = new MyServer();
        //SQLHandler.disconnect();
    }
}
