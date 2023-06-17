import javax.swing.*;

public class ShowRegist {
    public JFrame registWindow = null;
    private JLabel userIdLable = new JLabel("userId:");
    private JLabel cikiNameLable = new JLabel("nikiName:");
    private JLabel passwordLable = new JLabel("password:");
    private JLabel passwordRefrainLable = new JLabel("password:");
    public JButton registButton = new JButton("regist");
    public JButton cancelButton = new JButton("cancel");
    public JButton checkButton = new JButton("check");
    public JTextField userIdField = new JTextField();
    public JTextField nikeNameField = new JTextField();
    public JPasswordField passwordField = new JPasswordField();
    public JPasswordField passwordRefrainField = new JPasswordField();
    private MyActionListener myActionListener = new MyActionListener();
    public ShowRegist(){
        registWindow = new JFrame();
        registWindow.setSize(390,250);
        registWindow.setLocationRelativeTo(null);
        registWindow.setTitle("regist");
        registWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        registWindow.setLayout(null);

        registWindow.add(userIdLable);
        userIdLable.setBounds(20,20,75,20);
        registWindow.add(userIdField);
        userIdField.setBounds(90,21,160,20);
        registWindow.add(checkButton);
        checkButton.setBounds(270,20,75,20);
        registWindow.add(cikiNameLable);
        cikiNameLable.setBounds(20,50,75 ,20);
        registWindow.add(nikeNameField);
        nikeNameField.setBounds(90,51,255,20);
        registWindow.add(passwordLable);
        passwordLable.setBounds(20,80,75,20);
        registWindow.add(passwordField);
        passwordField.setBounds(90,81,255,20);
        registWindow.add(passwordRefrainLable);
        passwordRefrainLable.setBounds(20,110,75,20);
        registWindow.add(passwordRefrainField);
        passwordRefrainField.setBounds(90,111,255,20);
        registWindow.add(registButton);
        registButton.setBounds(80,150,100,20);
        registWindow.add(cancelButton);
        cancelButton.setBounds(190,150,100,20);

        checkButton.addActionListener(myActionListener);
        registButton.addActionListener(myActionListener);
        cancelButton.addActionListener(myActionListener);

    }
}
