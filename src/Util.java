import Entity.CheckUser;
import Entity.FriendEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Vector;

public class Util {
    public JSONObject getUserIdObject(int userId){
        JSONObject jsonObject = new JSONObject();

        return jsonObject;
    }

    public static String encrypt(String dataStr) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes("UTF8"));
            byte s[] = m.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getIp() {
        InetAddress ia = null;
        String localip = "";
        try {
            ia = ia.getLocalHost();
            localip = ia.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localip;
    }

    public static CheckUser checkUserId(String userId){
        JSONObject checkUserIdResult = HttpClient.DoGet(Start.urlServer,"checkUserId?userId="+Start.showRegistWindow.userIdField.getText(),"");
        CheckUser checkUser = new CheckUser();
        checkUser.setUserId(userId);
        checkUser.setNickName(checkUserIdResult.getString("nickName"));
        return checkUser;
    }

    public static void setFriendTable(JSONArray friendList){
        Start.mainWindow.friendTableList.clear();
        HashMap friendHashMap1 = new HashMap<Integer,Vector<Object>>();

        for(int n=0;n<friendList.size();n++){
            FriendEntity fL = friendList.getJSONObject(n).toJavaObject(FriendEntity.class);
            Vector<String> information = new Vector<String>();
            information.add(fL.getNickName());
            information.add(fL.getIpAddress()==null?"":fL.getIpAddress());
            Vector<Object> friend = new Vector<Object>();
            Boolean chatOpen=false;
            if(Start.friendHashMap.get(fL.getUserId())!=null){
                Vector<Object> friendInformation = (Vector<Object>) Start.friendHashMap.get(fL.getUserId());
                chatOpen = (Boolean) friendInformation.get(1);
            }
            friend.add(information);
            friend.add(chatOpen);
            friendHashMap1.put(fL.getUserId(),friend);
            Vector<String> friendInformation = new Vector<String>();
            friendInformation.add(fL.getNickName());
            friendInformation.add((fL.getIpAddress()==null||fL.getIpAddress().equals(""))?"离线":"在线");
            Start.mainWindow.friendTableList.add(friendInformation);
        }
        Start.friendHashMap.clear();
        Start.friendHashMap = friendHashMap1;
        Start.mainWindow.model=new DefaultTableModel(Start.mainWindow.friendTableList,Start.mainWindow.tableHead);
        Start.mainWindow.friendTable.setModel(Start.mainWindow.model);
        TableColumn column = Start.mainWindow.friendTable.getColumnModel().getColumn(1);
        column.setPreferredWidth(70);
        column.setMaxWidth(70);
        column.setMinWidth(70);
    }
}
