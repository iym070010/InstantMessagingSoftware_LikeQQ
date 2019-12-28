package com.qq.view;

import javax.swing.JPanel;

import com.qq.view.util.Config;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.Icon;

public class FriendListJPanel extends JPanel {
	
	/**
	 * Create the panel.
	 */
	public FriendListJPanel() {
		super();
		setLayout(null);

		this.listUpdate();
	}
	
	public void onlineUpdate() {	//在线好友更新
		//在线列表
		String OnlineList = Config.friend_online;
		
		if (Config.friend_online.length() == 0) {		// 没有好友
			return;
		}
		
		String[] uids = OnlineList.split(",");	//在线好友
		Set<String> keys = Config.list.keySet();	//所有好友
		
		for(String key:keys) {		//先将所有好友置为离线
			Config.list.get(key).setOnline(false);
		}
		
		if(!OnlineList.equals("notFound") && !OnlineList.trim().equals("")) {		//如果没人在线则不用更新在线好友
			for(String uid:uids) {		//再将在线好友置为在线
				if (!uid.trim().equals("")) {
					FaceJPanel faceJPanel = (FaceJPanel)Config.list.get(uid);
					faceJPanel.setOnline(true);
				}
			}
		}
		
		Collection<FaceJPanel> faceJPanels = Config.list.values();
		ArrayList<FaceJPanel> tempList = new ArrayList(faceJPanels);
		Collections.sort(tempList);
		
		this.removeAll();
		int i=0;
		
		for (FaceJPanel faceJPanel : tempList) {
			faceJPanel.setBounds(0,i++*60,560,60);
			faceJPanel.flashImage();	//刷新头像
			this.add(faceJPanel);
		} 
		
		this.setPreferredSize(new Dimension(0,60*Config.list.size()));
		this.updateUI();
		
		Config.friendListJPanel = this;
	}
	
	
	
	public void listUpdate() {		//好友列表更新
		//好友列表
		String FriendList = Config.friend_json_data;	
		
		JSONArray jsonArray = JSONArray.fromObject(FriendList);	//解析json
		
		if(Config.list.size() == 0) {	//第一次加载列表
			//{"uid":"10002","img":"def","netname":"小龙人","info":"我是小龙人"}
			
			for (int i = 0; i < jsonArray.size(); i++) {	
				JSONObject jsonObject = (JSONObject)jsonArray.get(i);
				
				Config.list.put(jsonObject.getString("uid"), new FaceJPanel(jsonObject.getString("uid"), 
						jsonObject.getString("netname"), jsonObject.getString("info"), 
						jsonObject.getString("img"),jsonObject.getString("qqnumber")));
			}
		}else {		//已经加载过列表了
			
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = (JSONObject)jsonArray.get(i);
				String uid = jsonObject.getString("uid");
				FaceJPanel faceJPanel = (FaceJPanel)Config.list.get(uid);
				if (faceJPanel != null) {
					faceJPanel.setName(jsonObject.getString("netname"));
					faceJPanel.setInfo(jsonObject.getString("info"));
					faceJPanel.setImage(jsonObject.getString("img"));
				}else {
					Config.list.put(uid, new FaceJPanel(uid, jsonObject.getString("netname"), 
							jsonObject.getString("info"), jsonObject.getString("img"),jsonObject.getString("qqnumber")));
				}
			}
			
		}

		this.onlineUpdate();
			
	}
	
}
