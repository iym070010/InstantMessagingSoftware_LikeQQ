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
	
	//��������ַ
//	public static final String IP = "10.177.215.78";		//У԰�� - ÿ���������ӵ�������ip��ַ
	public static final String IP = "192.168.31.38";		//����wifi - ÿ���������ӵ�������ip��ַ
	
	//�˿ں�
	public static final int LOGIN_PORT = 4001;	//��½�˿�
	public static final int REG_PORT = 4002; 	//ע��˿�
	public static final int Msg_PORT = 4004; 	//ע��˿�
	
	//�û���&�������Ϣ�Ĵ�
	public static String username;
	public static String password;
	public static String friend_json_data;		//�����б�json��Ϣ
	public static String personality_json_data;	//��������json��Ϣ
	public static String friend_list_data; 		//�����б��json��Ϣ - ��uid
	public static String friend_online;			//���ߺ���json��Ϣ
	
	public static FriendListJPanel friendListJPanel;	//����ʵʱ���º����б�
	
	public static DatagramSocket datagramSocket_client = null;	//UDP���ͽ���������
	
	public static Hashtable<String, FaceJPanel> list = new Hashtable<String, FaceJPanel>();	//�Լ������к������
	
	//��������json��Ϣ�õ������б�
	public static  void jiexi_friend_json_data(String friend_json_data) {
		Config.friend_json_data = friend_json_data;	
		JSONArray json = JSONArray.fromObject(friend_json_data);
		StringBuffer stringBuffer = new StringBuffer();	//������ߺ���id
		for(int i = 0;i<json.size();i++) {
				JSONObject jsonObject = (JSONObject)json.get(i);
				stringBuffer.append(jsonObject.getString("uid"));
				stringBuffer.append(",");		
		}
		friend_list_data = stringBuffer.toString();
	}
	
	//���촰�Ǽ�
	public static Hashtable<String, Chat> chatTab = new Hashtable<String, Chat>(); 
	
	//��ʾ�����
	public static void showChat(String uid,String netname,String info,String img,boolean isOnline,String qqnumber,Vector<Msg> msgs) {
		if (chatTab.get(uid) == null) {
			Chat chat = new Chat(uid, netname, info, img, isOnline,qqnumber,msgs);
			chatTab.put(uid, chat);
		}else {
			chatTab.get(uid).setAlwaysOnTop(true);
			chatTab.get(uid).setVisible(true);
		}
		chatTab.get(uid).onlineUpdate(isOnline);	//ˢ������״̬
	}
	//�ر������
	public static void closeChat(String uid) {
		chatTab.remove(uid);
	}
}
