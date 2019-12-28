package com.ym.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Set;



/**
 * 	�����û��б�
 * 
 * @author ����ʿ
 *
 */

public class UserOnlineList {

	private  UserOnlineList() {		//������ - ����ʵ��
		// TODO Auto-generated constructor stub
	}
	
	private static UserOnlineList userOnlineList = new UserOnlineList();
	
	public static UserOnlineList getUserOnlineList() {
		return userOnlineList;
	}
	
	//�����˻���¼���ֵ��� - ������� || Ҳ���Է���linkedlist���������
	private HashMap<String, UserInfo> hashMap = new HashMap<String,UserInfo>();	//ǰ��id����ֵ
	
	//ע�������û�
	public void regOnline(String uid,String passward,Socket socket,String qqnumber) {
		
		//�ж��Ƿ��Ѿ�����;��ռ��½
		UserInfo userInfo = hashMap.get(uid);
		if(userInfo!=null) {
			try {
				try {
					userInfo.getSocket().getOutputStream().write(4);  	//������4��ʾ
				} catch (Exception e) {
					// TODO: handle exception
				}
				userInfo.getSocket().close();		//��ռǰ������
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		userInfo = new UserInfo();		
		userInfo.setSocket(socket);
		userInfo.setUid(uid);
		userInfo.setPassward(passward);			
		userInfo.setQqnumber(qqnumber);
		hashMap.put(uid, userInfo);	//�Ǽ�����	
	}
	
	public void updateOnlineUDP(String uid,String udpip,int udpport) throws NullPointerException{		//�����ٽ���UDP��Ϣ����
		
		UserInfo userInfo = hashMap.get(uid);
		userInfo.setUdpip(udpip);
		userInfo.setUdpport(udpport);
		
	}
	
	public boolean isUserOnline(String uid) {		//�ж��û��Ƿ�����
		return hashMap.containsKey(uid);
	}
	
	public UserInfo getOnlineUserInfo(String uid) {		//�õ�ָ�������û���Ϣ
		return hashMap.get(uid);
	}
	
	public void logout(String uid) {		//����
		hashMap.remove(uid);
	}
	
	public Set<String> getOnlineUserInfos(){	//�õ������û��б� - �������ݴ����ݿ����
		return hashMap.keySet();
	}
	

}
