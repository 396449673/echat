import Entity.AddEntity;
import Entity.CheckUser;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Start.loginWindow.registButton){
            Start.showRegistWindow.registWindow.setVisible(true);
        }
        else if(e.getSource()==Start.loginWindow.closeButton){
            System.exit(0);
        }
        else if(e.getSource()==Start.loginWindow.enterButton){
            String userId = Start.loginWindow.userField.getText();
            String password = String.valueOf(Start.loginWindow.passwordField.getPassword());
            if(userId.trim().equals("") ){
                JOptionPane.showMessageDialog(null, "请输入用户名！", "警告",JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(password.trim().equals("") ){
                JOptionPane.showMessageDialog(null, "请输入密码！", "警告",JOptionPane.WARNING_MESSAGE);
                return;
            }
            try{
                Integer.parseInt(userId);
            }catch(NumberFormatException numberFormatException){
                Start.loginWindow.passwordField.setText("");
                JOptionPane.showMessageDialog(null, "用户名不合法！", "警告",JOptionPane.WARNING_MESSAGE);
            }
            String ipAddress = Util.getIp()+":"+Integer.toString(Start.port);
            JSONObject loginJSON =HttpClient.DoGet(Start.urlServer,"login?userId="+userId+"&password="+Util.encrypt(password)+"&ipAddress="+ipAddress,"");
            if(loginJSON.getBoolean("loginState")){

                Start.userId =Integer.parseInt(Start.loginWindow.userField.getText());
                Start.mainWindow.nickNameLable.setText(loginJSON.getString("nickName"));
                JOptionPane.showMessageDialog(null, "登陆成功！", "提示",JOptionPane.PLAIN_MESSAGE);
                Start.token=loginJSON.getString("token");
                Start.friendList = loginJSON.getJSONArray("friendList");
                Util.setFriendTable(Start.friendList);
                for(int row = 0;row<Start.friendList.size();row++){
                    Start.mainWindow.friendTable.isCellEditable(row,0);
                    Start.mainWindow.friendTable.isCellEditable(row,1);
                }
                Start.mainWindow.chatwindow = new chatWindow();
                Start.loginWindow.loginWindow.setVisible(false);
                Start.mainWindow.mainWindow.setVisible(true);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(true){
                            JSONObject getaddJSON =HttpClient.DoGet(Start.urlServer,"getAddFriend?userId="+Start.userId,Start.token);
                            JSONArray addList = getaddJSON.getJSONArray("friendList");
                            for(int n=0;n<addList.size();n++){
                                AddEntity fL = addList.getJSONObject(n).toJavaObject(AddEntity.class);
                                if(fL.getIsAdd() == 1)
                                    JOptionPane.showMessageDialog(null, Integer.toString(fL.getUserId2())+"已同意您的好友申请！", "好友同意申请",JOptionPane.PLAIN_MESSAGE);
                                else if(fL.getIsDiss() == 1)
                                    JOptionPane.showMessageDialog(null, Integer.toString(fL.getUserId2())+"拒绝了您的好友申请！", "好友同意申请",JOptionPane.PLAIN_MESSAGE);

                            }
                            JSONObject addJSON =HttpClient.DoGet(Start.urlServer,"getAdd?userId="+Start.userId,Start.token);
                            JSONArray addJSONJSONArray = addJSON.getJSONArray("friendList");
                            for(int n=0;n<addJSONJSONArray.size();n++){
                                AddEntity fL = addJSONJSONArray.getJSONObject(n).toJavaObject(AddEntity.class);
                                if(fL.getIsAdd() == 0 &&fL.getIsDiss()==0){
                                    int select = JOptionPane.showConfirmDialog(null,"是否同意"+Integer.toString(fL.getUserId1())+"的好友请求","好友请求",JOptionPane.YES_NO_OPTION );
                                    if(select==JOptionPane.YES_OPTION) {
                                        JSONObject agreeJSON =HttpClient.DoGet(Start.urlServer,"agreeAdd?userId="+Start.userId+"&userId2="+Integer.toString(fL.getUserId1())+"&select=1",Start.token);
                                        if(!agreeJSON.getBoolean("code")){
                                            JOptionPane.showMessageDialog(null, "操作失败", "提示",JOptionPane.WARNING_MESSAGE);
                                        }
                                    }
                                    else if(select==JOptionPane.NO_OPTION) {
                                        JSONObject agreeJSON =HttpClient.DoGet(Start.urlServer,"agreeAdd?userId="+Start.userId+"&userId2="+Integer.toString(fL.getUserId1())+"&select=0",Start.token);
                                        if(!agreeJSON.getBoolean("code")){
                                            JOptionPane.showMessageDialog(null, "操作失败", "提示",JOptionPane.WARNING_MESSAGE);
                                        }
                                    }
                                }
                            }

                            JSONObject refreshJSON =HttpClient.DoGet(Start.urlServer,"refreshFriend?userId="+Start.loginWindow.userField.getText(),Start.token);
                            Start.friendList = refreshJSON.getJSONArray("friendList");
                            Util.setFriendTable(Start.friendList);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                    }
                });
                thread.start();
            }
            else{
                JOptionPane.showMessageDialog(null, loginJSON.getString("message"), "登录失败",JOptionPane.WARNING_MESSAGE);
            }
        }
        else if(e.getSource()==Start.showRegistWindow.checkButton){
            try{
                Integer.parseInt(Start.showRegistWindow.userIdField.getText());
            }catch(NumberFormatException numberFormatException){
                Start.loginWindow.userField.setText("");
                JOptionPane.showMessageDialog(null, "用户名不合法！", "警告",JOptionPane.WARNING_MESSAGE);
            }
            if(Start.showRegistWindow.userIdField.getText().trim().equals("") ){
                JOptionPane.showMessageDialog(null, "该用户名不合法！", "警告",JOptionPane.WARNING_MESSAGE);
                return;
            }
            CheckUser checkUser = Util.checkUserId(Start.showRegistWindow.userIdField.getText());
            if(checkUser.getNickName() != null){
                Start.showRegistWindow.userIdField.setText("");
                JOptionPane.showMessageDialog(null, "该用户名已存在！", "警告",JOptionPane.WARNING_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "该用户名可用！", "提示",JOptionPane.PLAIN_MESSAGE);
            }
        }
        else if(e.getSource()==Start.showRegistWindow.registButton){
            String userId = Start.showRegistWindow.userIdField.getText();
            String password = String.valueOf(Start.showRegistWindow.passwordField.getPassword());
            String nickName = Start.showRegistWindow.nikeNameField.getText();
            if(userId.trim().equals("")){
                JOptionPane.showMessageDialog(null, "该用户名不合法！", "警告",JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(nickName.trim().equals("")){
                JOptionPane.showMessageDialog(null, "该昵称不合法！", "警告",JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(password.trim().equals("")){
                JOptionPane.showMessageDialog(null, "该密码不合法！", "警告",JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(!password.trim().equals(String.valueOf(Start.showRegistWindow.passwordRefrainField.getPassword()).trim()) ){
                JOptionPane.showMessageDialog(null, "两次密码不同！", "警告",JOptionPane.WARNING_MESSAGE);
                return;
            }

            JSONObject registJSON = HttpClient.DoGet(Start.urlServer,"regist?userId="+userId+"&password="+Util.encrypt(password)+"&nickName="+nickName,"");
            Boolean state = registJSON.getBoolean("loginState");
            if(state){
                Start.showRegistWindow.userIdField.setText("");
                Start.showRegistWindow.nikeNameField.setText("");
                Start.showRegistWindow.passwordField.setText("");
                Start.showRegistWindow.passwordRefrainField.setText("");
                Start.showRegistWindow.registWindow.setVisible(false);
                Start.loginWindow.userField.setText(userId);
                Start.loginWindow.passwordField.setText(password);
            }
            else{
                if(registJSON.getString("message") .equals("账号已存在！")){
                    Start.showRegistWindow.userIdField.setText("");
                    JOptionPane.showMessageDialog(null, "该用户名已存在！", "警告",JOptionPane.WARNING_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null, "注册失败，请检查网络并重试！", "警告",JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        else if(e.getSource()==Start.showRegistWindow.cancelButton){
            Start.showRegistWindow.userIdField.setText("");
            Start.showRegistWindow.nikeNameField.setText("");
            Start.showRegistWindow.passwordField.setText("");
            Start.showRegistWindow.passwordRefrainField.setText("");
            Start.showRegistWindow.registWindow.setVisible(false);
        }
        else if(e.getSource()== Start.mainWindow.logoutButton){
            JSONObject logoutJSON =HttpClient.DoGet(Start.urlServer,"logout?userId="+Start.loginWindow.userField.getText(),Start.token);
            if(!logoutJSON.getBoolean("state")){
                JOptionPane.showMessageDialog(null, "登出失败，请重新再试！", "警告",JOptionPane.WARNING_MESSAGE);
            }
            else{
                System.exit(0);
            }
        }
        else if(e.getSource()== Start.mainWindow.addFriendButton){
            String userIdI = JOptionPane.showInputDialog(null,"请输入用户名","加好友",JOptionPane.PLAIN_MESSAGE);
            int userId=0;
            try{
                userId=Integer.parseInt(userIdI);
            }catch (Exception event){
                JOptionPane.showMessageDialog(null, "请输入格式规范的用户名！", "警告",JOptionPane.WARNING_MESSAGE);
            }
            JSONObject checkJSON =HttpClient.DoGet(Start.urlServer,"checkUserId?userId="+userId,Start.token);
            if(checkJSON.getString("message").equals("查询成功")){
                int select = JOptionPane.showConfirmDialog(null,"确认向"+checkJSON.getString("nickName")+"的好友请求","好友请求",JOptionPane.YES_NO_OPTION );
                if(select==JOptionPane.YES_OPTION) {
                    JSONObject agreeJSON =HttpClient.DoGet(Start.urlServer,"addFriend?userId1="+Start.userId+"&userId2="+userId,Start.token);
                    if(!agreeJSON.getBoolean("code")){
                        JOptionPane.showMessageDialog(null, "操作失败", "提示",JOptionPane.WARNING_MESSAGE);
                    }
                }
                else if(select==JOptionPane.NO_OPTION) {

                }
            }
            else{
                JOptionPane.showMessageDialog(null, "用户不存在！", "警告",JOptionPane.WARNING_MESSAGE);

            }

        }

    }
}


