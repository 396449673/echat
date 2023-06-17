import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class chatWindow implements Runnable{
    public JFrame chatWindows = null;
    public JTextArea messageTextArea = new JTextArea();
    public JTextArea tabInTextArea = new JTextArea();
    public JButton enterButton = new JButton("发送");
    private JLabel nickNameLable = new JLabel();
    public JButton closeButton = new JButton("关闭");
    private String ipAdress;
    public DataInputStream in;
    public DataOutputStream out;
    private JScrollPane messageJScrollPane = new JScrollPane();
    private JScrollPane tapinJScrollPane = new JScrollPane();
    ServerSocket serverSocket =Start.serverSocket;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Thread serverThread;
    Socket socket=new Socket();
    Thread thread = new Thread(this);
    private Boolean noend = true;
    private int userId;
    public chatWindow(){
        chatWindows = new JFrame();
        chatWindows.setSize(570,625);
        chatWindows.setLocationRelativeTo(null);
        chatWindows.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        chatWindows.setLayout(null);
        chatWindows.add(nickNameLable);
        nickNameLable.setBounds(225,10,100,20);
        chatWindows.add(messageJScrollPane);
        messageJScrollPane.setBounds(10,30,530,300);
        messageJScrollPane.setViewportView(messageTextArea);
        chatWindows.add(tapinJScrollPane);
        tapinJScrollPane.setBounds(10,340,530,140);
        tapinJScrollPane.setViewportView(tabInTextArea);
        chatWindows.add(closeButton);
        closeButton.setBounds(330,490,90,20);
        chatWindows.add(enterButton);
        enterButton.setBounds(430,490,90,20);
        chatWindows.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                try{
                    socket.close();

                    noend = false;
                    chatWindows.dispose();
                    Vector<Object> vector=(Vector<Object>)(Start.friendHashMap.get(userId));
                    vector.set(1,false);
                    Start.friendHashMap.put(userId,vector);
                }
                catch(Exception e2){

                }
            }
        });
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket=serverSocket.accept();
                    InetAddress inetAddress = socket.getInetAddress();
                    out = new DataOutputStream(socket.getOutputStream());
                    in = new DataInputStream(socket.getInputStream());
                    String s=in.readUTF();
                    String[] s1 = s.split(":");
                    int userId1 = Integer.parseInt(s1[1]);
                    userId =userId1;
                    Vector<Object> friendInformation =(Vector<Object> ) Start.friendHashMap.get(userId);
                    nickNameLable.setText(((Vector<String>)friendInformation.get(0)).get(0));
                    setIp(((Vector<String>)friendInformation.get(0)).get(1));
                    friendInformation.set(1, true);
                    Start.friendHashMap.put(userId,friendInformation);
                    chatWindows.setVisible(true);
                    chatWindow chatwindow = new chatWindow();
                    Start.mainWindow.chatwindow = chatwindow;
                    thread.start();
                }catch (Exception e){
                    messageTextArea.append(df.format(new Date())+"  自己: \n链接失败，请检查好友在线状态\n");
                }

            }
        });

        serverThread.start();

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatWindows.setVisible(false);
                try{
                    socket.close();

                    noend = false;
                    chatWindows.dispose();
                    Vector<Object> vector=(Vector<Object>)(Start.friendHashMap.get(userId));
                    vector.set(1,false);
                    Start.friendHashMap.put(userId,vector);
                }
                catch(Exception e2){

                }
            }
        });
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s =null;
                s=tabInTextArea.getText();
                if(s.equals("")){
                    JOptionPane.showMessageDialog(null, "请输入内容！", "提示",JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                tabInTextArea.setText("");
                try{
                    out.writeUTF(df.format(new Date())+"  对方:\n"+s);
                    messageTextArea.append(df.format(new Date())+"  自己:\n"+s+"\n");
                }catch(IOException ex){
                    messageTextArea.append(df.format(new Date())+"  自己:  \n链接失败，请检查好友在线状态\n");
                }
            }
        });

    }
    public chatWindow(String title,String ipAdress,int userId){
        chatWindows = new JFrame();
        chatWindows.setSize(570,600);
        chatWindows.setLocationRelativeTo(null);
        chatWindows.setTitle(title);
        chatWindows.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        chatWindows.setLayout(null);
        chatWindows.add(nickNameLable);
        nickNameLable.setBounds(225,10,100,20);
        chatWindows.add(messageJScrollPane);
        messageJScrollPane.setBounds(10,30,530,300);
        messageJScrollPane.setViewportView(messageTextArea);
        chatWindows.add(tapinJScrollPane);
        tapinJScrollPane.setBounds(10,340,530,140);
        tapinJScrollPane.setViewportView(tabInTextArea);
        chatWindows.add(closeButton);
        closeButton.setBounds(330,490,90,20);
        chatWindows.add(enterButton);
        enterButton.setBounds(430,490,90,20);
        chatWindows.setVisible(true);
        this.ipAdress = ipAdress;
        nickNameLable.setText(title);
        this.userId =userId;
        chatWindows.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                try{
                    socket.close();

                    noend = false;
                    chatWindows.dispose();
                    Vector<Object> vector=(Vector<Object>)(Start.friendHashMap.get(userId));
                    vector.set(1,false);
                    Start.friendHashMap.put(userId,vector);
                }
                catch(Exception e2){

                }
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    socket.close();

                    noend = false;
                    chatWindows.dispose();

                    Vector<Object> vector=(Vector<Object>)(Start.friendHashMap.get(userId));
                    vector.set(1,false);
                    Start.friendHashMap.put(userId,vector);
                }
                catch(Exception e2){

                }
                chatWindows.setVisible(false);
            }
        });
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s =null;
                s=tabInTextArea.getText();
                if(s.equals("")){
                    JOptionPane.showMessageDialog(null, "请输入内容！", "提示",JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                tabInTextArea.setText("");
                try{
                    out.writeUTF(df.format(new Date())+"  对方:\n"+s);
                    messageTextArea.append(df.format(new Date())+"  自己:\n"+s+"\n");
                }catch(IOException ex){
                    messageTextArea.append(df.format(new Date())+"  自己: \n发送失败，请检查好友在线状态及网络\n");
                }
            }
        });
        thread =new Thread(this);
    }


    public Boolean getIp(String ipAdress){
        return this.ipAdress==ipAdress?true:false;
    }
    public void setIp(String ipAdress){
        this.ipAdress=ipAdress;
    }

    public Boolean connectSocket(){
        String[] ip = this.ipAdress.split(":");
        try {
            InetAddress address = InetAddress.getByName(ip[0]);
            InetSocketAddress socketAddress = new InetSocketAddress(address,Integer.parseInt(ip[1]));
            socket.connect(socketAddress);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("ssss:"+Start.loginWindow.userField.getText()+":ssss");
            if(!(thread.isAlive())){
                thread = new Thread(this);
            }

            thread.start();
        }catch (Exception e){
            messageTextArea.append(df.format(new Date())+"  自己: \n连接失败，请检查好友在线状态及网络\n");
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        String s = null;
        while(noend){
            try{
                s=in.readUTF();
                messageTextArea.append(s+"\n");
            }catch(IOException e){
                messageTextArea.append(df.format(new Date())+"   自己: \n连接失败，请检查好友在线状态及网络\n");
                break;
            }
        }
    }
}
