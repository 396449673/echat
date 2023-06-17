import com.alibaba.fastjson.JSONArray;

import javax.swing.*;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Vector;

public class Start {
    //final static String urlServer ="http://op.shanghaojia.icu:8092/";
    final static String urlServer ="http://127.0.0.1:8080/";
    static ShowRegist showRegistWindow = new ShowRegist();
    static ShowLogin loginWindow = new ShowLogin();
    static int userId;
    static ServerSocket serverSocket =  null;
    static ShowMain mainWindow = new ShowMain();
    static HashMap friendHashMap = new HashMap<Integer,Vector<Object>>();
    public static int port ;
    static String token ="";
    static JSONArray friendList;
    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(0);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "没有可用的空闲端口！", "警告",JOptionPane.WARNING_MESSAGE);
        }
        port = serverSocket.getLocalPort();

    }
}
