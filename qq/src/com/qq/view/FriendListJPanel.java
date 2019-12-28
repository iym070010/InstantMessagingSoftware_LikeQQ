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
	
	public void onlineUpdate() {	//���ߺ��Ѹ���
		//�����б�
		String OnlineList = Config.friend_online;
		
		if (Config.friend_online.length() == 0) {		// û�к���
			return;
		}
		
		String[] uids = OnlineList.split(",");	//���ߺ���
		Set<String> keys = Config.list.keySet();	//���к���
		
		for(String key:keys) {		//�Ƚ����к�����Ϊ����
			Config.list.get(key).setOnline(false);
		}
		
		if(!OnlineList.equals("notFound") && !OnlineList.trim().equals("")) {		//���û���������ø������ߺ���
			for(String uid:uids) {		//�ٽ����ߺ�����Ϊ����
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
			faceJPanel.flashImage();	//ˢ��ͷ��
			this.add(faceJPanel);
		} 
		
		this.setPreferredSize(new Dimension(0,60*Config.list.size()));
		this.updateUI();
		
		Config.friendListJPanel = this;
	}
	
	
	
	public void listUpdate() {		//�����б����
		//�����б�
		String FriendList = Config.friend_json_data;	
		
		JSONArray jsonArray = JSONArray.fromObject(FriendList);	//����json
		
		if(Config.list.size() == 0) {	//��һ�μ����б�
			//{"uid":"10002","img":"def","netname":"С����","info":"����С����"}
			
			for (int i = 0; i < jsonArray.size(); i++) {	
				JSONObject jsonObject = (JSONObject)jsonArray.get(i);
				
				Config.list.put(jsonObject.getString("uid"), new FaceJPanel(jsonObject.getString("uid"), 
						jsonObject.getString("netname"), jsonObject.getString("info"), 
						jsonObject.getString("img"),jsonObject.getString("qqnumber")));
			}
		}else {		//�Ѿ����ع��б���
			
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
