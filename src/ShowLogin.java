import javax.swing.*;

public class ShowLogin{
    public JFrame loginWindow = null;
    public JTextField userField = new JTextField();
    public JPasswordField passwordField = new JPasswordField();
    private JLabel userLabel = new JLabel("userId：");
    private JLabel passwordLabel = new JLabel("password：");
    public JButton enterButton = new JButton("enter");
    public JButton closeButton = new JButton("close");
    public JButton registButton = new JButton("regist");
    private MyActionListener myActionListener = new MyActionListener();

    public ShowLogin(){

        loginWindow = new JFrame();
        loginWindow.setSize(365,160);
        loginWindow.setLocationRelativeTo(null);
        loginWindow.setTitle("login");
        loginWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginWindow.setLayout(null);
        //组件
        loginWindow.add(userLabel);
        userLabel.setBounds(20,20,70,20);
        loginWindow.add(userField);
        userField.setBounds(95,21,235,20);
        loginWindow.add(passwordLabel);
        passwordLabel.setBounds(20,50,70,20);
        loginWindow.add(passwordField);
        passwordField.setBounds(95,51,235,20);
        loginWindow.add(registButton);
        registButton.setBounds(20,80,100,20);
        loginWindow.add(enterButton);
        enterButton.setBounds(125,80,100,20);
        loginWindow.add(closeButton);
        closeButton.setBounds(230,80,100,20);
        //---loginWindow.setUndecorated(true);
        loginWindow.setVisible(true);


        registButton.addActionListener(myActionListener);
        enterButton.addActionListener(myActionListener);
        closeButton.addActionListener(myActionListener);
    }

}
