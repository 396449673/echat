import Entity.FriendEntity;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

class MyMouseListener implements MouseListener {
    private long longd;
    @Override
    public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();
        if(src == Start.mainWindow.friendTable){
            long tl = e.getWhen()-longd;
            if(tl<500){
                longd = 0;
                int row = Start.mainWindow.friendTable.getSelectedRow();
                if(row == -1){
                    return;
                }
                else{
                    int userId = Start.friendList.getJSONObject(row).toJavaObject(FriendEntity.class).getUserId();;
                    Vector<Object> vector=(Vector<Object>)(Start.friendHashMap.get(userId));
                    Boolean chatOpen = (Boolean) vector.get(1);
                    if(((Vector<String>)(vector.get(0))).equals("")){
                        JOptionPane.showMessageDialog(null, "请检查好友的在线状态！", "警告",JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    else if(chatOpen == false){
                        chatWindow chat = new chatWindow(((Vector<String>)(vector.get(0))).get(0),((Vector<String>)(vector.get(0))).get(1),userId);
                        chatOpen = true;
                        if(!chat.connectSocket()){
                            chat.chatWindows.dispose();
                            chatOpen = false;
                        }

                        vector.set(1,chatOpen);
                        Start.friendHashMap.put(userId,vector);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "窗口已被打开！", "提示",JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
            else{
                longd = e.getWhen();
            }
        }



    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}