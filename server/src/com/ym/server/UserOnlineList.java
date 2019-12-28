package com.ym.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Set;



/**
 * 	在线用户列表
 * 
 * @author 黄龙士
 *
 */

public class UserOnlineList {

	private  UserOnlineList() {		//单例类 - 饿汉实现
		// TODO Auto-generated constructor stub
	}
	
	private static UserOnlineList userOnlineList = new UserOnlineList();
	
	public static UserOnlineList getUserOnlineList() {
		return userOnlineList;
	}
	
	//在线账户记录在字典里 - 方便查找 || 也可以放在linkedlist链表队列里
	private HashMap<String, UserInfo> hashMap = new HashMap<String,UserInfo>();	//前面id后面值
	
	//注册在线用户
	public void regOnline(String uid,String passward,Socket socket,String qqnumber) {
		
		//判断是否已经在线;抢占登陆
		UserInfo userInfo = hashMap.get(uid);
		if(userInfo!=null) {
			try {
				try {
					userInfo.getSocket().getOutputStream().write(4);  	//输出编号4提示
				} catch (Exception e) {
					// TODO: handle exception
				}
				userInfo.getSocket().close();		//抢占前者下线
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		userInfo = new UserInfo();		
		userInfo.setSocket(socket);
		userInfo.setUid(uid);
		userInfo.setPassward(passward);			
		userInfo.setQqnumber(qqnumber);
		hashMap.put(uid, userInfo);	//登记在线	
	}
	
	public void updateOnlineUDP(String uid,String udpip,int udpport) throws NullPointerException{		//后续再进行UDP信息更新
		
		UserInfo userInfo = hashMap.get(uid);
		userInfo.setUdpip(udpip);
		userInfo.setUdpport(udpport);
		
	}
	
	public boolean isUserOnline(String uid) {		//判断用户是否在线
		return hashMap.containsKey(uid);
	}
	
	public UserInfo getOnlineUserInfo(String uid) {		//得到指定在线用户信息
		return hashMap.get(uid);
	}
	
	public void logout(String uid) {		//下线
		hashMap.remove(uid);
	}
	
	public Set<String> getOnlineUserInfos(){	//得到在线用户列表 - 具体数据从数据库调用
		return hashMap.keySet();
	}
	

}
