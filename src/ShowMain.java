import com.alibaba.fastjson.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class ShowMain{
    public JFrame mainWindow ;
    public JButton logoutButton = new JButton("登出");
    public JButton addFriendButton = new JButton("加好友");
    private JLabel nickLable = new JLabel("昵称:");
    public JLabel nickNameLable = new JLabel("");
    public Vector<String> tableHead = new Vector<String>();
    public Vector<Vector<String>> friendTableList = new Vector<Vector<String>>();
    public DefaultTableModel model=new DefaultTableModel(friendTableList,tableHead);
    public JTable friendTable = new JTable(model){
        public boolean isCellEditable(int row,int column){
            return false;
        }
    };
    public chatWindow chatwindow;
    public JButton refreshButton = new JButton("刷新好友");
    JScrollPane tablePanel=new JScrollPane(friendTable);
    private MyActionListener myActionListener = new MyActionListener();
    MyMouseListener myMouseListener = new MyMouseListener();
    private Boolean isDraging = false;
    private int xx=0;
    private int yy=0;
    public ShowMain(){

        tableHead.add("好友列表");
        tableHead.add("状态");
        mainWindow = new JFrame();
        mainWindow.setSize(300,650);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setTitle("main");
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setLayout(null);
        mainWindow.setUndecorated(true);
        mainWindow.add(nickLable);
        nickLable.setBounds(20,20,70,20);
        mainWindow.add(nickNameLable);
        nickNameLable.setBounds(90,20,100,20);
        friendTable.setFont(new Font("宋体", Font.PLAIN, 12));
        mainWindow.add(tablePanel);
        tablePanel.setBounds(20,50,265,540);
        tablePanel.setViewportView(friendTable);
        friendTable.setBounds(0,0,265,540);
        mainWindow.add(refreshButton);
        refreshButton.setBounds(190,20,95,20 );
        mainWindow.add(addFriendButton);
        addFriendButton.setBounds(50,600,95,20 );
        mainWindow.add(logoutButton);
        logoutButton.setBounds(155,600,95,20);
        addFriendButton.addActionListener(myActionListener);
        logoutButton.addActionListener(myActionListener);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JSONObject loginJSON =HttpClient.DoGet(Start.urlServer,"refreshFriend?userId="+Start.loginWindow.userField.getText(),Start.token);
                Start.friendList = loginJSON.getJSONArray("friendList");
                Util.setFriendTable(Start.friendList);
            }
        });


        friendTable.addMouseListener(myMouseListener);
        this.mainWindow.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isDraging = true;
                xx = e.getX();
                yy = e.getY();
            }
            public void mouseReleased(MouseEvent e) {
                isDraging = false;
            }
        });
        this.mainWindow.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDraging) {
                    int left = mainWindow.getLocation().x;
                    int top = mainWindow.getLocation().y;
                    mainWindow.setLocation(left + e.getX() - xx, top + e.getY() - yy);
                }
            }
        });
    }

}

