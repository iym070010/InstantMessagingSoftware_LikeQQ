package com.qq.view.util;

import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import com.qq.view.Chat;
import com.qq.view.FaceJPanel;
import com.qq.view.FriendListJPanel;
import com.qq.view.MainMenu;
import com.qq.view.Msg;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Config {
	
	//服务器地址
//	public static final String IP = "10.177.215.78";		//校园网 - 每次重新连接得重新配ip地址
	public static final String IP = "192.168.31.38";		//宿舍wifi - 每次重新连接得重新配ip地址
	
	//端口号
	public static final int LOGIN_PORT = 4001;	//登陆端口
	public static final int REG_PORT = 4002; 	//注册端口
	public static final int Msg_PORT = 4004; 	//注册端口
	
	//用户名&密码等信息寄存
	public static String username;
	public static String password;
	public static String friend_json_data;		//好友列表json信息
	public static String personality_json_data;	//个人资料json信息
	public static String friend_list_data; 		//好友列表非json信息 - 仅uid
	public static String friend_online;			//在线好友json信息
	
	public static FriendListJPanel friendListJPanel;	//方便实时更新好友列表
	
	public static DatagramSocket datagramSocket_client = null;	//UDP发送接收心跳端
	
	public static Hashtable<String, FaceJPanel> list = new Hashtable<String, FaceJPanel>();	//自己的所有好友面板
	
	//解析好友json信息得到好友列表
	public static  void jiexi_friend_json_data(String friend_json_data) {
		Config.friend_json_data = friend_json_data;	
		JSONArray json = JSONArray.fromObject(friend_json_data);
		StringBuffer stringBuffer = new StringBuffer();	//存放在线好友id
		for(int i = 0;i<json.size();i++) {
				JSONObject jsonObject = (JSONObject)json.get(i);
				stringBuffer.append(jsonObject.getString("uid"));
				stringBuffer.append(",");		
		}
		friend_list_data = stringBuffer.toString();
	}
	
	//聊天窗登记
	public static Hashtable<String, Chat> chatTab = new Hashtable<String, Chat>(); 
	
	//显示聊天框
	public static void showChat(String uid,String netname,String info,String img,boolean isOnline,String qqnumber,Vector<Msg> msgs) {
		if (chatTab.get(uid) == null) {
			Chat chat = new Chat(uid, netname, info, img, isOnline,qqnumber,msgs);
			chatTab.put(uid, chat);
		}else {
			chatTab.get(uid).setAlwaysOnTop(true);
			chatTab.get(uid).setVisible(true);
		}
		chatTab.get(uid).onlineUpdate(isOnline);	//刷新在线状态
	}
	//关闭聊天框
	public static void closeChat(String uid) {
		chatTab.remove(uid);
	}
}
