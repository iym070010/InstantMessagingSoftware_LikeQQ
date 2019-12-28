package com.qq.view.server;

import java.util.HashMap;
import java.util.LinkedList;

import com.qq.view.Chat;
import com.qq.view.FaceJPanel;
import com.qq.view.Msg;
import com.qq.view.util.Config;

import net.sf.json.JSONObject;

/**
 * 	��Ϣջ - ��洢���е���Ϣ
 * @author ����ʿ
 *
 */

public class MessageStack {		//�����߶���һ�� ����ʽ����ʵ��
	
	private MessageStack() {}
	
	private static MessageStack messageStack = new MessageStack();
	
	public static MessageStack getMessageStack() {
		return messageStack;
	}
	
	public static HashMap<String, LinkedList<Msg>> hashMap = new HashMap();
	
	// �����Ǹ�˭����Ϣ���洢����
	public void addMessage(String json) {
		// {"msg":"123456","code":"1574426820174","toUID":"10002","myUID":"10001","type":"msg"}
		
		JSONObject jsonObject = JSONObject.fromObject(json);
		String toUID = jsonObject.getString("toUID");
		String myUID = jsonObject.getString("myUID");
		String msg = jsonObject.getString("msg");
		String code = jsonObject.getString("code");
		String type = jsonObject.getString("type");		
		
		// ����Ϣ������Msg��
		Msg msgObj = new Msg(); 
		msgObj.setCode(code);
		msgObj.setMsg(msg);
		msgObj.setMyUID(myUID);
		msgObj.setToUID(toUID);
		msgObj.setType(type);
		
		try {
			Chat chat = Config.chatTab.get(myUID);
			if (chat.isVisible()) {
				chat.addMessage(msgObj);
			}else {
				throw new Exception();
			}
		} catch (Exception e) {
			
			
			FaceJPanel faceJPanel = Config.list.get(myUID);		//������û��ʱ����
			faceJPanel.addMessage(msgObj);
			
			//Msg�б�
//			LinkedList<Msg> list = hashMap.get(myUID);	//���Ҵ��ڴ�ʱ����
//			if (list == null) {
//				list = new LinkedList<Msg>();
//			}
//			
//			list.add(msgObj);
//			hashMap.put(myUID, list);
			
		}
	}
	
}
