import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Denis on 03.06.2016.
 */
public class ClientHandler implements Runnable {
    private MyServer owner;
    private Socket s;
    private DataOutputStream out;
    private DataInputStream in;
    private String name;
    private int authTimer;

    public int getAuthTimer() {return authTimer;}

    public void setAuthTimer(int authTimer){this.authTimer = authTimer;}

    public String getName(){return name;}

    public ClientHandler(Socket s, MyServer owner) {
        try {
            this.s = s;
            this.owner = owner;
            out = new DataOutputStream(s.getOutputStream());
            in = new DataInputStream(s.getInputStream());
            name = "";
            authTimer = 0;
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String w = in.readUTF();
                if (w != null) {
                    String[] n = w.split("\t");
                    if (n.length == 3) {
                        String t = SQLHandler.getNickByLoginPassword(n[1], n[2]);
                        if (!owner.isNicknameUsed(t) && t != null) {
                            owner.broadcastMsg(t + " connected to the chatroom");
                            name = t;
                            sendMsg("zxcvb");
                            break;
                        } else {
                            if (t == null)
                                sendMsg("Auth Error: No such account");
                            if (owner.isNicknameUsed(t))
                                sendMsg("Auth Error: Account is busy");
                        }
                    }
                }
                Thread.sleep(100);
            }
            while (true) {
                String w = in.readUTF();
                if (w != null) {
                    owner.broadcastMsg(name + ": " + w);
                    System.out.println(name + ": " + w);
                    if (w.equalsIgnoreCase("END")) break;
                }
                Thread.sleep(100);
            }


        } catch (IOException e) {
            System.out.println("IOException");
        } catch (InterruptedException e) {
            System.out.println("Thread sleep error");
        } finally {
            close();
        }
    }

    public void close(){
        try {
            System.out.println("Client disconnected");
            owner.remove(this);
            s.close();
            if (!name.isEmpty())
                owner.broadcastMsg(name+ " disconnected from the chatroom");
        } catch (IOException e) {
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
        }
    }
}
